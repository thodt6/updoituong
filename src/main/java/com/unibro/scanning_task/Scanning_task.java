/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.scanning_task;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author THOND
 */
public class Scanning_task extends BaseScanning_task implements Converter {

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
        Scanning_taskDAO dao = new Scanning_taskDAO();
        Scanning_task ret = dao.getObjectByKey(id);
        return ret;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value.equals("")) {
            return "";
        }
        return String.valueOf(((Scanning_task) value).getTaskid());
    }

    public String getPercentage() {
        int start = Integer.parseInt(this.getStart_position());
        int stop = Integer.parseInt(this.getStop_position());
        int current = Integer.parseInt(this.getCurrent_pos());
        int percent = (current - start) * 100 / (stop - start);
        return String.valueOf(percent);
    }
}
