package com.unibro.uid_upload;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.unibro.sysuser.UserSessionBean;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Nguyen Duc Tho
 */
@ManagedBean
@ViewScoped
public class Uid_uploadLazyService implements Serializable {

    private LazyDataModel<Uid_upload> lazyModel;
    private Uid_upload selectedObject = new Uid_upload();
    private Uid_upload[] selectedObjects;
    private Uid_upload newObject = new Uid_upload();
    private String selectedId;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public Uid_uploadLazyService() {
        this.loadObjects();
    }

    public void initSelectedObject() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            Uid_uploadDAO dao = new Uid_uploadDAO();
            this.selectedObject = dao.getObjectByKey(selectedId);
        }
    }

    public final void loadObjects() {
        this.lazyModel = new Uid_uploadLazyModel();
    }

    public void setLazyModel(LazyDataModel<Uid_upload> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public LazyDataModel<Uid_upload> getLazyModel() {
        return lazyModel;
    }

    public void setNewObject(Uid_upload newObject) {
        this.newObject = newObject;
    }

    public Uid_upload getNewObject() {
        return newObject;
    }

    public void setSelectedObject(Uid_upload selectedObject) {
        this.selectedObject = selectedObject;
    }

    public Uid_upload getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObjects(Uid_upload[] selectedObjects) {
        this.selectedObjects = selectedObjects;
    }

    public Uid_upload[] getSelectedObjects() {
        return selectedObjects;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    public String getSelectedId() {
        return selectedId;
    }

    public void createObject() {
        if (this.getNewObject() != null) {
            Uid_uploadDAO dao = new Uid_uploadDAO();
            Uid_upload result = dao.create(getNewObject());
            if (result != null) {
                this.newObject = result;
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, Global.getResourceLanguage("general.operationSuccess"), "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, Global.getResourceLanguage("general.operationFail"), "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
    }

    public void editSelected() {
        if (this.selectedObject != null) {
            Uid_uploadDAO dao = new Uid_uploadDAO();
            if (dao.edit(this.selectedObject) != null) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, Global.getResourceLanguage("general.operationSuccess"), "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, Global.getResourceLanguage("general.operationFail"), "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
    }

    public void deleteObject() {
        if (this.selectedObject != null) {
            Uid_uploadDAO dao = new Uid_uploadDAO();
            dao.delete(selectedObject);
            this.loadObjects();
        }
    }

    public void deleteObjects() {
        if (this.selectedObjects != null) {
            Uid_uploadDAO dao = new Uid_uploadDAO();
            for (Uid_upload selectedObject1 : this.selectedObjects) {
                dao.delete(selectedObject1);
            }
            this.loadObjects();
        }
    }

    public void rowEdit(RowEditEvent event) {
        Uid_upload sf = (Uid_upload) event.getObject();
        if (sf != null) {
            this.selectedObject = sf;
            this.editSelected();
        }
    }

    public List<Uid_upload> completeObject(String query) {
        Uid_uploadDAO dao = new Uid_uploadDAO();
        if (query == null || query.equals("")) {
            return dao.load(0, -1, "null", 0, new ArrayList());
        } else {
            RequestFilter filter = new RequestFilter();
            //replace name by the query field
            filter.setName("name");
            filter.setType(RequestFilter.CONTAIN);
            filter.setValue(query);
            List filter_list = new ArrayList();
            filter_list.add(filter);
            return dao.load(0, -1, "null", 0, filter_list);
        }
    }

    private String convertString(String str){
        str=str.replaceAll("\\u0000", "");
        for(int i=0;i<10;i++){
            if(str.startsWith(String.valueOf(i))){
                return str;
            }
        }
        int index=-1;
        char[] charArray = new char[] {'0','1','2','3','4','5','6','7','8','9'};
        boolean quit=false;
        for(int i=0;i<str.length();i++){
            for(int j=0;j<charArray.length;j++){
                if(str.charAt(i) == charArray[j]){
                   index=i;
                   quit=true;
                   break;
                }
            }
            if(quit){
                break;
            }
        }
        if(index>0){
            return str.substring(index);
        }
        return "";
        
    }
    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile f = event.getFile();

        File folder = new File(Global.getConfigValue("FILE_PRIVATE_PATH") + "/" + UserSessionBean.getUserSession().getUser().getUserid() + "/input");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String filename = f.getFileName();
        String file_prefix = Global.getPrefixFileName(filename);
        String tail = Global.getTailFile(filename);
        filename = file_prefix + "_" + System.currentTimeMillis() + "." + tail;

        File saveFile = new File(folder.getAbsolutePath() + "/" + filename);

        try {
            FileOutputStream fos = new FileOutputStream(saveFile);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            InputStream inputStream = f.getInputstream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                line=this.convertString(line.trim());
                if (!line.equals("")) {
                    count++;
                    bw.write(line.trim());
                    bw.newLine();
                    logger.info("Line:" + line);
                }
            }
            bw.close();
            fos.close();
            Uid_upload upload = new Uid_upload();
            upload.setDownload_count(0);
            upload.setFilename(filename);
            upload.setHas_converted(0);
            upload.setTotal_uid(count);
            upload.setUpdate_id(UserSessionBean.getUserSession().getUser().getUserid());
            upload.setUseable_uid(0);
            upload = Uid_upload.createObject(upload);
            if (upload != null) {
                this.deliverConvertTask(upload);
            }
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, Global.getResourceLanguage("general.success"), "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            inputStream.close();
            bw.close();
            this.loadObjects();
        } catch (IOException ex) {
            logger.error(ex);
        } catch (TimeoutException ex) {
            logger.error(ex);
        }
    }

    public void deliverConvertTask(Uid_upload upload) throws java.io.IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Global.getConfigValue("APP.RABBITMQ.HOST"));
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String TASK_QUEUE_NAME = Global.getConfigValue("APP.RABBITMQ.CONVERT.QUEUE");
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

        String message = upload.toJsonStr();

        channel.basicPublish("", TASK_QUEUE_NAME,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes());
        logger.info(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }

}
