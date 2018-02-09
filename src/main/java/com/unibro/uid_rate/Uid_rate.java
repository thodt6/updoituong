/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.uid_rate;

import com.unibro.utils.RequestFilter;
import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author THOND
 */
public class Uid_rate extends BaseUid_rate implements Converter {

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
        Uid_rateDAO dao = new Uid_rateDAO();
        Uid_rate ret = dao.getObjectByKey(id);
        return ret;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value.equals("")) {
            return "";
        }
        return String.valueOf(((Uid_rate) value).getRate_id());
    }
    
    public static Uid_rate getCurrentRate(){
        RequestFilter filter=new RequestFilter();
        filter.setFunction("");
        filter.setName("active");
        filter.setRequired(true);
        filter.setType(RequestFilter.EQUAL);
        filter.setValue(1);
        List l=new ArrayList();
        l.add(filter);
        Uid_rateDAO dao=new Uid_rateDAO();
        List<Uid_rate> list=dao.load(0, 1, "rate_id", 0, l);
        if(list!=null&&!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }
}
