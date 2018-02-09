/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.uid_upload;

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
public class Uid_upload extends BaseUid_upload implements Converter {

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
        Uid_uploadDAO dao = new Uid_uploadDAO();
        Uid_upload ret = dao.getObjectByKey(id);
        return ret;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value.equals("")) {
            return "";
        }
        return String.valueOf(((Uid_upload) value).getUpload_id());
    }

    public static Uid_upload createObject(Uid_upload object) {
        Uid_uploadDAO dao = new Uid_uploadDAO();
        return dao.create(object);
    }

    public static Uid_upload updateObject(Uid_upload object) {
        Uid_uploadDAO dao = new Uid_uploadDAO();
        return dao.edit(object);
    }

    public StreamedContent getDownloadFile() throws FileNotFoundException {
        if (this.getHas_converted() == 1) {
            String filepath = Global.getConfigValue("FILE_PRIVATE_PATH") + "/" + this.getUpdate_id() + "/output/" + Global.getPrefixFileName(this.getFilename()) + ".csv";
            File f = new File(filepath);
            InputStream stream = new FileInputStream(f);
            DefaultStreamedContent ret= new DefaultStreamedContent(stream, new MimetypesFileTypeMap().getContentType(f),f.getName());
            this.setDownload_count(this.getDownload_count()+1);
            Uid_upload.updateObject(this);
            return ret;
        }
        return null;        
    }
}
