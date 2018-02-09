/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.ad_account;

import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.APINodeList;
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.CustomAudience;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.unibro.client.Client;
import com.unibro.client_transaction.Client_transaction;
import com.unibro.obj_upload.Obj_upload;
import com.unibro.obj_upload.Obj_uploadDAO;
import com.unibro.sysuser.UserSessionBean;
import com.unibro.utils.ApiClient;
import com.unibro.utils.Global;
import com.unibro.utils.RequestFilter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author THOND
 */
@ManagedBean
@ViewScoped
public class Ad_accountService implements Converter {

    public static int RECEIVED_FILE_STATUS = 0;
    public static int WAITING_FB_STATUS = 1;
    public static int READY_RETURN_STATUS = 2;
    public static int RETURNED_STATUS = 3;

    private Client customer;
    private List<Ad_account> objects;
    private String selectedAd_id;
    private String selectedObj_id;
    private List<CustomAudience> audiences;

    private String obj_name = "";
    private String obj_description = "";

    private List<Obj_upload> uploads;

    private Obj_upload upload;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void initCustomer() throws APIException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            this.customer = Client.loadClient(UserSessionBean.getUserSession().getUser().getUid());
//            logger.info("Customer JSON:" +  customer.toJsonStr());
            loadObjects();
            loadUploads();
        }
    }

    public void loadObjects() throws APIException {
        this.setObjects((List<Ad_account>) new ArrayList());
        APIContext context = new APIContext(UserSessionBean.getUserSession().getUser().getLogin_token());
        JsonObject obj = this.queryFbAdAccount();
        if (obj != null) {
            if (obj.has("data")) {
                JsonArray arr = obj.get("data").getAsJsonArray();
                if (arr != null && arr.size() > 0) {
                    for (int i = 0; i < arr.size(); i++) {
                        JsonObject item = arr.get(i).getAsJsonObject();
//                        logger.info("Item:" + item.toString());
                        String account_id = item.get("account_id").getAsString();
                        AdAccount ad_acc = new AdAccount(account_id, context).get().requestNameField().execute();
                        Ad_account acc = new Ad_account(item.get("account_id").getAsString(), item.get("id").getAsString(),
                                ad_acc.getFieldName());
                        this.getObjects().add(acc);
                    }
                }
            }
        }
    }

    public void loadAudiences() {
        String url = "https://www.facebook.com/ads/manage/customaudiences/tos/?act=" + this.getSelectedAd_id();
        logger.info(this.getSelectedAd_id());
        if (this.getSelectedAd_id() != null && !this.selectedAd_id.equals("")) {
            APIContext context = new APIContext(UserSessionBean.getUserSession().getUser().getLogin_token());
            try {
                logger.info("Start load audiences");
                APINodeList<CustomAudience> list = new AdAccount(this.getSelectedAd_id(), context).getCustomAudiences().execute();
                if (!list.isEmpty()) {
                    this.audiences = new ArrayList();
                    for (CustomAudience a : list) {
                        CustomAudience b = new CustomAudience(a.getFieldId(), context).get().requestIdField().requestNameField().requestDescriptionField().
                                requestApproximateCountField().requestOperationStatusField().requestDataSourceField().requestDeliveryStatusField().execute();
                        logger.info("Count:" + b.getFieldApproximateCount() + ":" + b.getFieldOperationStatus() + ":" + b.getFieldDataSource().getFieldCreationParams() + ":" + b.getFieldDeliveryStatus());
                        this.audiences.add(b);
                    }
                }

            } catch (APIException ex) {
                logger.error(ex);
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Load doi tuong loi " + ex.toString() + ".Login to " + url + "", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
    }

    public void createObject() {
//        String url = "https://www.facebook.com/ads/manage/customaudiences/tos/?act=" + this.getSelectedAd_id();
        if (this.selectedAd_id != null && !this.selectedAd_id.equals("")) {
            try {
                APIContext context = new APIContext(UserSessionBean.getUserSession().getUser().getLogin_token());
                CustomAudience customAudience = new AdAccount(this.getSelectedAd_id(), context).createCustomAudience()
                        .setName(this.obj_name)
                        .setSubtype(CustomAudience.EnumSubtype.VALUE_CUSTOM)
                        .setDescription(this.obj_description)
                        .execute();
                if (customAudience != null) {
                    this.obj_name = "";
                    this.obj_description = "";
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, Global.getResourceLanguage("obj.object.create_success"), "");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }
            } catch (APIException ex) {
//                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tao doi tuong loi " + ex.toString() + ".Login to " + url + "", "");
//                FacesContext.getCurrentInstance().addMessage(null, msg);
                this.processException(ex, this.getSelectedAd_id());
            }
        }
    }

    private void processException(APIException ex, String acc_id) {;
        JsonObject obj = ex.getRawResponseAsJsonObject();
        if (obj.get("error").getAsJsonObject().get("type").getAsString().equals("OAuthException")) {
//            String url = "https://business.facebook.com/ads/manage/customaudiences/tos/?act=" + acc_id;
            String text_msg = Global.getResourceLanguage("obj.object.create_first_error");
            text_msg = text_msg.replace("{act_id}", acc_id);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, text_msg, "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, Global.getResourceLanguage("obj.object.create_fail"), "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

//    public void loadAudiences() {
//        String g = "https://graph.facebook.com/v2.11/act_" + this.selectedAd_id + "/customaudiences";
//        ApiClient client = new ApiClient(g, "Bearer " + UserSessionBean.getUserSession().getUser().getLogin_token());
//        JsonObject obj = client.executeGetQuery(null);
//        logger.info(obj.toString());        
//    }
    private JsonObject queryFbAdAccount() {
        String g = "https://graph.facebook.com/v2.11/" + UserSessionBean.getUserSession().getUser().getUid() + "/adaccounts";
        ApiClient client = new ApiClient(g, "Bearer " + UserSessionBean.getUserSession().getUser().getLogin_token());
        JsonObject obj = client.executeGetQuery(null);
        return obj;
    }

    public Object getAsObject(FacesContext fc, UIComponent uic, String submittedValue) {
        if (submittedValue.trim().equals("")) {
            return null;
        }
        String id;
        try {
            id = String.valueOf(submittedValue);
        } catch (Exception ex) {
            id = null;
        }
        if (this.getObjects() != null) {
            for (Ad_account acc : getObjects()) {
                logger.info("Acc ID:" + acc.getAccount_id());
                if (acc.getId().equals(id)) {
                    return acc;
                }
            }
        }
        return null;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value.equals("")) {
            return "";
        }
        return String.valueOf(((Ad_account) value).getId());
    }

    /**
     * @return the objects
     */
    public List<Ad_account> getObjects() {
        return objects;
    }

    /**
     * @param objects the objects to set
     */
    public void setObjects(List<Ad_account> objects) {
        this.objects = objects;
    }

    /**
     * @return the audiences
     */
    public List<CustomAudience> getAudiences() {
        return audiences;
    }

    /**
     * @param audiences the audiences to set
     */
    public void setAudiences(List<CustomAudience> audiences) {
        this.audiences = audiences;
    }

    /**
     * @return the selectedAd_id
     */
    public String getSelectedAd_id() {
        return selectedAd_id;
    }

    /**
     * @param selectedAd_id the selectedAd_id to set
     */
    public void setSelectedAd_id(String selectedAd_id) {
        this.selectedAd_id = selectedAd_id;
    }

    /**
     * @return the selectedObj_id
     */
    public String getSelectedObj_id() {
        return selectedObj_id;
    }

    /**
     * @param selectedObj_id the selectedObj_id to set
     */
    public void setSelectedObj_id(String selectedObj_id) {
        this.selectedObj_id = selectedObj_id;
    }

    /**
     * @return the obj_name
     */
    public String getObj_name() {
        return obj_name;
    }

    /**
     * @param obj_name the obj_name to set
     */
    public void setObj_name(String obj_name) {
        this.obj_name = obj_name;
    }

    /**
     * @return the obj_description
     */
    public String getObj_description() {
        return obj_description;
    }

    /**
     * @param obj_description the obj_description to set
     */
    public void setObj_description(String obj_description) {
        this.obj_description = obj_description;
    }

    /**
     * @return the uploads
     */
    public List<Obj_upload> getUploads() {
        return uploads;
    }

    /**
     * @param uploads the uploads to set
     */
    public void setUploads(List<Obj_upload> uploads) {
        this.uploads = uploads;
    }

    /**
     * @return the upload
     */
    public Obj_upload getUpload() {
        return upload;
    }

    /**
     * @param upload the upload to set
     */
    public void setUpload(Obj_upload upload) {
        this.upload = upload;
    }

    private String convertString(String str) {
        str = str.replaceAll("\\u0000", "");
        for (int i = 0; i < 10; i++) {
            if (str.startsWith(String.valueOf(i))) {
                return str;
            }
        }
        int index = -1;
        char[] charArray = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean quit = false;
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < charArray.length; j++) {
                if (str.charAt(i) == charArray[j]) {
                    index = i;
                    quit = true;
                    break;
                }
            }
            if (quit) {
                break;
            }
        }
        if (index > 0) {
            return str.substring(index);
        }
        return "";

    }

    public void handleFileUpload(FileUploadEvent event) throws TimeoutException {
        if (this.selectedObj_id == null || this.selectedObj_id.equals("")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, Global.getResourceLanguage("obj.obj_upload.general.selectObject.validateMsg"), "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        Obj_upload obj_upload = Obj_upload.loadObject(this.selectedObj_id);
        if (obj_upload != null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, Global.getResourceLanguage("obj.obj_upload.general.selectObject.validateUploadMsg") + " " + obj_upload.getFile_name(), "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        UploadedFile f = event.getFile();
        File folder = new File(Global.getConfigValue("FILE_PRIVATE_PATH") + "/" + this.customer.getUid() + "/" + this.selectedObj_id);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String filename = f.getFileName();
//        String file_prefix = Global.getPrefixFileName(filename);
//        String tail = Global.getTailFile(filename);
//        filename = file_prefix + "_" + System.currentTimeMillis() + "." + tail;

        File saveFile = new File(folder.getAbsolutePath() + "/" + filename);

        try {
            FileOutputStream fos = new FileOutputStream(saveFile);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            InputStream inputStream = f.getInputstream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            int count = 0;
            int current_num_uid = this.getCustomer().getBalance();
            int num_uid = current_num_uid;
            String last_line = "";
            while ((line = reader.readLine()) != null) {
                line = this.convertString(line);
                if (!line.equals("")) {
                    last_line = line;
                    if (num_uid <= 0) {
                        //Stop import here
                        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, Global.getResourceLanguage("obj.obj_upload.over_uid"), "");
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                        this.getCustomer().setBalance(num_uid);
                        break;
                    } else {
                        count++;
                        bw.write(line);
                        bw.newLine();
                        num_uid = current_num_uid - count;
                        this.getCustomer().setBalance(num_uid);
                    }
                }
            }
            this.setUpload(new Obj_upload());
            this.getUpload().setStatus(0);
            this.getUpload().setFile_name(saveFile.getName());
            this.getUpload().setLast_uid(last_line);
            this.getUpload().setNum_uid(count);
            this.getUpload().setObj_id(this.selectedObj_id);
            this.getUpload().setReal_uid(count);
            this.getUpload().setReturn_uid(0);
            this.getUpload().setUid(this.getCustomer().getUid());
            this.setUpload(Obj_upload.createObject(getUpload()));

            Client.updateObject(this.getCustomer());

            Client_transaction trans = new Client_transaction();
            trans.setAmount(-count);
            trans.setReason("Import UID but not processing");
            trans.setStatus(1);
            trans.setUid(this.getCustomer().getUid());
            trans.setObj_id(this.selectedObj_id);
            trans.setClient_id(this.customer.getUid());

            Client_transaction.createObject(trans);

            this.loadUploads();

            this.deliverConvertTask(this.getUpload());

            inputStream.close();
            bw.close();
        } catch (IOException ex) {
            logger.error(ex);
        }
    }
    private List<Obj_upload> list_uploads;

    /**
     * @return the list_uploads
     */
    public List<Obj_upload> getList_uploads() {
        return list_uploads;
    }

    /**
     * @param list_uploads the list_uploads to set
     */
    public void setList_uploads(List<Obj_upload> list_uploads) {
        this.list_uploads = list_uploads;
    }

    private boolean uploadStatus = false;

    private boolean checkUploadState() {
        if (this.customer == null) {
            return false;
        }
        if (this.customer.getBalance() <= 0) {
            return false;
        }
        if (this.selectedAd_id == null || this.selectedAd_id.equals("")) {
            return false;
        }
        if (this.selectedObj_id == null || this.selectedObj_id.equals("")) {
            return false;
        }
        Obj_upload obj_upload = Obj_upload.loadObject(this.selectedObj_id);
        return obj_upload == null;
    }

    public void loadUploads() {
        logger.info("Load Uploads");
        RequestFilter filter = new RequestFilter();
        filter.setName("uid");
        filter.setRequired(true);
        filter.setType(RequestFilter.EQUAL);
        filter.setValue(this.getCustomer().getUid());
        List list = new ArrayList();
        list.add(filter);

        Obj_uploadDAO dao = new Obj_uploadDAO();
        this.list_uploads = dao.load(0, 0, "created_time", 1, list);
        this.uploadStatus = this.checkUploadState();
    }

    /**
     * @return the customer
     */
    public Client getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Client customer) {
        this.customer = customer;
    }

    public void returnUID() {
        logger.info("Start Return UID:" + this.upload.toJsonStr());
        if (this.getUpload() != null) {
            if (this.getUpload().getEnableReturnUid() && this.getUpload().getStatus()==Obj_upload.WAITING_FB_STATUS) {
                try {
                    logger.info("Upload:" + this.upload.toJsonStr());
                    APIContext context = new APIContext(UserSessionBean.getUserSession().getUser().getLogin_token());
                    CustomAudience customAudience = new CustomAudience(getUpload().getObj_id(), context).get().requestIdField().requestNameField().requestDescriptionField().
                            requestApproximateCountField().requestOperationStatusField().requestDataSourceField().requestDeliveryStatusField().execute();
                    if (customAudience.getFieldDeliveryStatus().getFieldCode() == 200) {
                        if (upload.getReal_uid() > customAudience.getFieldApproximateCount()) {
                            //Has changed
                            Client c = Client.loadClient(upload.getUid());
                            //Create new transaction for get status of facebook
                            Client_transaction trans = new Client_transaction();
                            trans.setObj_id(upload.getObj_id());
                            trans.setReason("Re caculated number uid in facebook");
                            trans.setStatus(Client_transaction.FB_FILTER_STATUS);
                            trans.setUid(upload.getUid());
                            trans.setAmount(upload.getReal_uid() - customAudience.getFieldApproximateCount().intValue());
                            trans.setClient_id(this.customer.getUid());
                            Client_transaction.createObject(trans);
                            //Update for upload File
                            upload.setReal_uid(customAudience.getFieldApproximateCount().intValue());
                            upload.setReturn_uid(upload.getReturn_uid() + trans.getAmount());
                            upload.setStatus(Obj_upload.RETURNED_STATUS);
                            Obj_upload.updateObject(upload);

                            //Update client 
                            c.setBalance(c.getBalance() + trans.getAmount());
                            c.setLast_return_time(new java.util.Date());
                            c.setLast_return_uid(trans.getAmount());
                            Client.updateObject(c);
                        } else {
                            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, Global.getResourceLanguage("obj.obj_upload.aready_modified"), "");
                            FacesContext.getCurrentInstance().addMessage(null, msg);
                        }
                    } else {
                        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, Global.getResourceLanguage("obj.obj_upload.facebook_not_finished"), "");
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                    }
                } catch (APIException ex) {
                    logger.error(ex);
                }
            }
        } else {
            logger.info("Upload null");
        }
    }

    public void deliverConvertTask(Obj_upload upload) throws java.io.IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Global.getConfigValue("APP.RABBITMQ.HOST"));
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String TASK_QUEUE_NAME = Global.getConfigValue("APP.RABBITMQ.FBCONVERT.QUEUE");
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

        String message = upload.toJsonStr();

        channel.basicPublish("", TASK_QUEUE_NAME,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes());
        logger.info(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }

    /**
     * @return the uploadStatus
     */
    public boolean isUploadStatus() {
        return uploadStatus;
    }

    /**
     * @param uploadStatus the uploadStatus to set
     */
    public void setUploadStatus(boolean uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

}
