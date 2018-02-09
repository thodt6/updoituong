/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.client;

import com.unibro.sysuser.UserSessionBean;
import com.unibro.utils.Global;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author THOND
 */
public class Client extends BaseClient implements Converter {

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
        ClientDAO dao = new ClientDAO();
        Client ret = dao.getObjectByKey(id);
        return ret;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value.equals("")) {
            return "";
        }
        return String.valueOf(((Client) value).getUid());
    }

    public static Client loadClient(String uid) {
        ClientDAO dao = new ClientDAO();
        Client c = dao.getObjectByKey(uid);
        if (c == null) {
            if (UserSessionBean.getUserSession().getUser().getOauth_provider().equals("facebook")) {
                c = new Client();
                c.setBalance(Integer.valueOf(Global.getConfigValue("app.num_uid_promotion")));
                c.setFull_name(UserSessionBean.getUserSession().getUser().getFullname());
                c.setStatus(1);
                c.setUid(uid);
                c.setUser_code(System.currentTimeMillis() + "");
                c = dao.create(c);
            }
        }
        return c;
    }

    public static Client updateObject(Client object) {
        ClientDAO dao = new ClientDAO();
        return dao.edit(object);
    }

//    private transient Ad_accountService adService=new Ad_accountService();
//
//    /**
//     * @return the adService
//     */
//    public Ad_accountService getAdService() {
//        return adService;
//    }
//
//    /**
//     * @param adService the adService to set
//     */
//    public void setAdService(Ad_accountService adService) {
//        this.adService = adService;
//    }
}
