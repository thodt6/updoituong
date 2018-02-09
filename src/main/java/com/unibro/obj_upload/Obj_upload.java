/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.obj_upload;

import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.CustomAudience;
import com.unibro.client.Client;
import com.unibro.client_transaction.Client_transaction;
import com.unibro.sysuser.UserSessionBean;
import com.unibro.utils.Global;
import java.util.Calendar;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author THOND
 */
public class Obj_upload extends BaseObj_upload implements Converter {

    public static int RECEIVED_FILE_STATUS = 0;
    public static int WAITING_FB_STATUS = 1;
    public static int READY_RETURN_STATUS = 2;
    public static int RETURNED_STATUS = 3;

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
        Obj_uploadDAO dao = new Obj_uploadDAO();
        Obj_upload ret = dao.getObjectByKey(id);
        return ret;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value.equals("")) {
            return "";
        }
        return String.valueOf(((Obj_upload) value).getObj_id());
    }

    public static Obj_upload loadObject(String obj_id) {
        Obj_uploadDAO dao = new Obj_uploadDAO();
        return dao.getObjectByKey(obj_id);
    }

    public static Obj_upload createObject(Obj_upload object) {
        Obj_uploadDAO dao = new Obj_uploadDAO();
        return dao.create(object);
    }

    public static Obj_upload updateObject(Obj_upload object) {
        Obj_uploadDAO dao = new Obj_uploadDAO();
        return dao.edit(object);
    }

    public String getAudienceResult() {
        try {
            APIContext context = new APIContext(UserSessionBean.getUserSession().getUser().getLogin_token());
            CustomAudience customAudience = new CustomAudience(this.getObj_id(), context).get().requestIdField().requestNameField().requestDescriptionField().
                    requestApproximateCountField().requestOperationStatusField().requestDataSourceField().requestDeliveryStatusField().execute();

            return customAudience.toString();
        } catch (APIException ex) {
            return "";
        }
    }
    
    public void returnUID() {
        if (this!= null) {
            if (this.getStatus() == Obj_upload.READY_RETURN_STATUS) {
                try {
                    APIContext context = new APIContext(UserSessionBean.getUserSession().getUser().getLogin_token());
                    CustomAudience customAudience = new CustomAudience(this.getObj_id(), context).get().requestIdField().requestNameField().requestDescriptionField().
                            requestApproximateCountField().requestOperationStatusField().requestDataSourceField().requestDeliveryStatusField().execute();
                    if (customAudience.getFieldDeliveryStatus().getFieldCode() == 200) {
                        if (this.getReal_uid() > customAudience.getFieldApproximateCount()) {
                            //Has changed
                            Client c = Client.loadClient(this.getUid());
                            //Create new transaction for get status of facebook
                            Client_transaction trans = new Client_transaction();
                            trans.setObj_id(getObj_id());
                            trans.setReason("Re caculated number uid in facebook");
                            trans.setStatus(Client_transaction.FB_FILTER_STATUS);
                            trans.setUid(getUid());
                            trans.setAmount(getReal_uid() - customAudience.getFieldApproximateCount().intValue());
                            trans.setClient_id(c.getUid());
                            Client_transaction.createObject(trans);
                            //Update for upload File
                            this.setReal_uid(customAudience.getFieldApproximateCount().intValue());
                            this.setReturn_uid(this.getReturn_uid() + trans.getAmount());
                            this.setStatus(Obj_upload.RETURNED_STATUS);
                            Obj_upload.updateObject(this);

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
                }
            }
        }else{
//            logger.info("Upload null");
        }
    }

    public boolean getEnableReturnUid() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime().after(this.getReturn_uid_time());
    }

}
