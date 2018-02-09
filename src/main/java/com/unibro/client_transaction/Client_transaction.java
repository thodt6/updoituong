/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.client_transaction;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author THOND
 */
public class Client_transaction extends BaseClient_transaction implements Converter {
    
    public static int CUSTOMER_UPLOAD_STATUS = 0;
    public static int CUSTOMER_FILTER_STATUS = 1;
    public static int FB_FILTER_STATUS = 2;

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
        Client_transactionDAO dao = new Client_transactionDAO();
        Client_transaction ret = dao.getObjectByKey(id);
        return ret;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value.equals("")) {
            return "";
        }
        return String.valueOf(((Client_transaction) value).getId());
    }

    public static Client_transaction createObject(Client_transaction object) {
        Client_transactionDAO dao = new Client_transactionDAO();
        return dao.create(object);
    }
}
