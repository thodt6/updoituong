/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.mobile_range;

import com.unibro.utils.Global;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author THOND
 */
public class Mobile_range extends BaseMobile_range implements Converter {

    public static final String UNDELIVERY_TASK = "U";
    public static final String READY_TASK = "R";
    public static final String DOING_TASK = "D";
    public static final String COMPLETED_TASK = "C";
    public static final String REDO_TASK = "T";

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
        Mobile_rangeDAO dao = new Mobile_rangeDAO();
        Mobile_range ret = dao.getObjectByKey(id);
        return ret;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value.equals("")) {
            return "";
        }
        return String.valueOf(((Mobile_range) value).getRange_id());
    }

    public void createTask() {
        if (this.getState().equals(Mobile_range.UNDELIVERY_TASK)) {
            Mobile_rangeDAO dao = new Mobile_rangeDAO();
            if (dao.createTask(this) > 0) {
                this.setState(READY_TASK);
                dao.edit(this);
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, Global.getResourceLanguage("general.operationSuccess"), "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, Global.getResourceLanguage("general.operationFail"), "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
    }
}
