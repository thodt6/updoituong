/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.facebook_group;

import com.google.gson.JsonObject;
import com.unibro.utils.ApiClient;
import com.unibro.utils.Global;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.activation.MimetypesFileTypeMap;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author THOND
 */
public class Facebook_group extends BaseFacebook_group implements Converter {

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Facebook_group)) {
            return false;
        }
        Facebook_group compareObj = (Facebook_group) obj;
        return (compareObj.getUniqueKey().equals(this.getUniqueKey()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.getUniqueKey() != null ? this.getUniqueKey().hashCode() : 0);
        return hash;
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
        Facebook_groupDAO dao = new Facebook_groupDAO();
        Facebook_group ret = dao.getObjectByKey(id);
        return ret;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value.equals("")) {
            return "";
        }
        return String.valueOf(((Facebook_group) value).getGroupid());
    }

    public StreamedContent getDownloadCsvFile() throws FileNotFoundException {
        if (this.getState().equals("C")) {
            String filepath = this.getUid_file() + ".xlsx";
            File f = new File(filepath);
            InputStream stream = new FileInputStream(f);
            DefaultStreamedContent ret = new DefaultStreamedContent(stream, new MimetypesFileTypeMap().getContentType(f), f.getName());
            return ret;
        }
        return null;
    }

    public StreamedContent getDownloadTxtFile() throws FileNotFoundException {
        if (this.getState().equals("C")) {
            String filepath = this.getUid_file() + ".txt";
            File f = new File(filepath);
            InputStream stream = new FileInputStream(f);
            DefaultStreamedContent ret = new DefaultStreamedContent(stream, new MimetypesFileTypeMap().getContentType(f), f.getName());
            return ret;
        }
        return null;
    }

    public void extracInfoFromFacebook() {
        String url = "https://graph.facebook.com/#groupid#/?access_token=#token#";
        url = url.replace("#groupid#", this.getGroupid());
        url = url.replace("#token#", Global.getConfigValue("app.default_token"));
        ApiClient client = new ApiClient(url, "");
        JsonObject obj = client.executeGetQuery(null);
        if (obj.has("description")) {
            this.setDescription(obj.get("description").getAsString());
        }
        if (obj.has("email")) {
            this.setEmail(obj.get("email").getAsString());
        }
        if (obj.has("icon")) {
            this.setIcon(obj.get("icon").getAsString());
        }
        if (obj.has("name")) {
            this.setName(obj.get("name").getAsString());
        }
        if (obj.has("owner")) {
            this.setOwner_id(obj.get("owner").getAsJsonObject().get("id").getAsString());
        }
        if (obj.has("privacy")) {
            this.setPrivacy(obj.get("privacy").getAsString());
        }
        if (obj.has("updated_time")) {
            this.setUpdated_time(obj.get("updated_time").getAsString());
        }
    }

    public void editMe() {
        Facebook_groupDAO dao = new Facebook_groupDAO();
        dao.edit(this);
    }

    public void updateFbInfo() {
        this.extracInfoFromFacebook();
        this.editMe();
    }
}
