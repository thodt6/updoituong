/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.group;

import com.unibro.access_list.Access_list;
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
public class Group extends BaseGroup implements Converter {

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
        GroupDAO dao = new GroupDAO();
        Group ret = dao.getObjectByKey(id);
        return ret;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value.equals("")) {
            return "";
        }
        return String.valueOf(((Group) value).getGroupid());
    }

    public boolean checkAccessPage(String uri) {
        Access_list acl = Access_list.getAccessListByGroup(Global.getConfigValue("app.applicationid_list") + "." + uri, this.getGroupid(), Global.getConfigValue("app.applicationid_list"));
        if (acl == null) {
            return false;
        }
        return acl.getPermission() == 1;
    }

    public static List<Group> loadAllGroups() {
        GroupDAO dao = new GroupDAO();
        String application_id = Global.getConfigValue("app.applicationid_list");
        RequestFilter rf = new RequestFilter();
        rf.setFunction("");
        rf.setName("application_id");
        rf.setRequired(true);
        rf.setType(RequestFilter.EQUAL);
        rf.setValue(application_id);
        List arr = new ArrayList();
        arr.add(rf);
        return dao.load(0, -1, "groupid", 1, arr);
    }
}
