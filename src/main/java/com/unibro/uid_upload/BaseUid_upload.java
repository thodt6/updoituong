/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.uid_upload;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.unibro.utils.Global;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author THOND
 */
public class BaseUid_upload implements Serializable {
    
    private Long upload_id = Long.valueOf(0);
    private String filename = "";
    private Integer update_id = 0;
    private java.util.Date update_time = new java.util.Date();
    private Integer has_converted = 0;
    private Integer total_uid = 0;
    private Integer useable_uid = 0;
    private Integer download_count = 0;
    
    public void setUpload_id(Long upload_id) {
        this.upload_id = upload_id;
    }

    public Long getUpload_id() {
        return this.upload_id;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setUpdate_id(Integer update_id) {
        this.update_id = update_id;
    }

    public Integer getUpdate_id() {
        return this.update_id;
    }

    public void setUpdate_time(java.util.Date update_time) {
        this.update_time = update_time;
    }

    public java.util.Date getUpdate_time() {
        return this.update_time;
    }

    public void setHas_converted(Integer has_converted) {
        this.has_converted = has_converted;
    }

    public Integer getHas_converted() {
        return this.has_converted;
    }

    public void setTotal_uid(Integer total_uid) {
        this.total_uid = total_uid;
    }

    public Integer getTotal_uid() {
        return this.total_uid;
    }

    public void setUseable_uid(Integer useable_uid) {
        this.useable_uid = useable_uid;
    }

    public Integer getUseable_uid() {
        return this.useable_uid;
    }

    public void setDownload_count(Integer download_count) {
        this.download_count = download_count;
    }

    public Integer getDownload_count() {
        return this.download_count;
    }
    
    public String toJsonStr() {
        Gson gson = Global.getGsonObject();
        return gson.toJson(this);
    }
    
    public JsonObject toJson() {
        Gson gson = Global.getGsonObject();
        return gson.toJsonTree(this).getAsJsonObject();
    }
    
    public static String toJsonArrayString(List<Uid_upload> list) {
        Gson gson = Global.getGsonObject();
        return gson.toJson(list);
    }
    
    public static JsonArray toJsonArray(List<Uid_upload> list) {
        Gson gson = Global.getGsonObject();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<Uid_upload>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
    }
    
    public static Uid_upload getObjectFromJsonString(String jsonObj) {
        try {
            Gson gson = Global.getGsonObject();
            Uid_upload obj;
            obj = gson.fromJson(jsonObj, Uid_upload.class);
            return obj;
        } catch (JsonSyntaxException ex) {
            //this.getLogger.error("Error:": + ex);
            return null;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BaseUid_upload)) {
            return false;
        }
        BaseUid_upload compareObj = (BaseUid_upload) obj;
        return (compareObj.getUpload_id().equals(this.getUpload_id()));
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.getUpload_id() != null ? this.getUpload_id().hashCode() : 0);
        return hash;
    }
    
}
