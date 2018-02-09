/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.access_list;

import com.unibro.utils.Global;
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
public class Access_list extends BaseAccess_list implements Converter {

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
        Access_listDAO dao = new Access_listDAO();
        Access_list ret = dao.getObjectByKey(id);
        return ret;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value.equals("")) {
            return "";
        }
        return String.valueOf(((Access_list) value).getAccess_id());
    }

    public static Access_list create(Access_list access_list) {
        Access_listDAO dao = new Access_listDAO();
        return dao.create(access_list);
    }

    public static Access_list update(Access_list access_list) {
        Access_listDAO dao = new Access_listDAO();
        return dao.edit(access_list);
    }

    public static Access_list getAccessListByGroup(String prop_id, int principle_id, String application_id) {
        Access_listDAO dao = new Access_listDAO();

        RequestFilter rf2 = new RequestFilter();
        rf2.setName("prop_id");
        rf2.setRequired(true);
        rf2.setType(RequestFilter.EQUAL);
        rf2.setValue(prop_id);
        RequestFilter rf3 = new RequestFilter();
        rf3.setName("principle_type");
        rf3.setRequired(true);
        rf3.setType(RequestFilter.EQUAL);
        rf3.setValue("GROUP");
        RequestFilter rf4 = new RequestFilter();
        rf4.setName("principle_id");
        rf4.setRequired(true);
        rf4.setType(RequestFilter.EQUAL);
        rf4.setValue(principle_id);

        RequestFilter rf5 = new RequestFilter();
        rf5.setName("application_id");
        rf5.setRequired(true);
        rf5.setType(RequestFilter.EQUAL);
        rf5.setValue(Global.getConfigValue("app.applicationid_list"));
        List<RequestFilter> filter = new ArrayList();

        filter.add(rf2);
        filter.add(rf3);
        filter.add(rf4);
        filter.add(rf5);

        List<Access_list> ret = dao.load(0, 0, "access_id", 1, filter);
        if (ret != null && !ret.isEmpty()) {
            return ret.get(0);
        }
        return null;
    }

    public static List<Access_list> getAccessList(String prop_id, String application_id,int permission) {
        Access_listDAO dao = new Access_listDAO();

        RequestFilter rf1 = new RequestFilter();
        rf1.setName("permission");
        rf1.setRequired(true);
        rf1.setType(RequestFilter.EQUAL);
        rf1.setValue(permission);
        
        RequestFilter rf2 = new RequestFilter();
        rf2.setName("prop_id");
        rf2.setRequired(true);
        rf2.setType(RequestFilter.EQUAL);
        rf2.setValue(prop_id);
        
        RequestFilter rf3 = new RequestFilter();
        rf3.setName("principle_type");
        rf3.setRequired(true);
        rf3.setType(RequestFilter.EQUAL);
        rf3.setValue("GROUP");

        RequestFilter rf5 = new RequestFilter();
        rf5.setName("application_id");
        rf5.setRequired(true);
        rf5.setType(RequestFilter.EQUAL);
        rf5.setValue(Global.getConfigValue("app.applicationid_list"));
        List<RequestFilter> filter = new ArrayList();

        filter.add(rf1);
        filter.add(rf2);
        filter.add(rf3);
        filter.add(rf5);

        List<Access_list> ret = dao.load(0, 0, "access_id", 1, filter);        
        return ret;
    }
}
