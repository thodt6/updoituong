/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.obj_upload;

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
public class BaseObj_upload implements Serializable {

    private String obj_id = "";
    private Integer num_uid = 0;
    private Integer real_uid = 0;
    private String last_uid = "";
    private String file_name = "";
    private java.util.Date created_time = new java.util.Date();
    private Integer return_uid = 0;
    private Integer status = 0;
    private String uid = "";
    private java.util.Date return_uid_time = new java.util.Date();

    public void setObj_id(String obj_id) {
        this.obj_id = obj_id;
    }

    public String getObj_id() {
        return this.obj_id;
    }

    public void setNum_uid(Integer num_uid) {
        this.num_uid = num_uid;
    }

    public Integer getNum_uid() {
        return this.num_uid;
    }

    public void setReal_uid(Integer real_uid) {
        this.real_uid = real_uid;
    }

    public Integer getReal_uid() {
        return this.real_uid;
    }

    public void setLast_uid(String last_uid) {
        this.last_uid = last_uid;
    }

    public String getLast_uid() {
        return this.last_uid;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_name() {
        return this.file_name;
    }

    public void setCreated_time(java.util.Date created_time) {
        this.created_time = created_time;
    }

    public java.util.Date getCreated_time() {
        return this.created_time;
    }

    public void setReturn_uid(Integer return_uid) {
        this.return_uid = return_uid;
    }

    public Integer getReturn_uid() {
        return this.return_uid;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return this.uid;
    }

    public String toJsonStr() {
        Gson gson = Global.getGsonObject();
        return gson.toJson(this);
    }

    public JsonObject toJson() {
        Gson gson = Global.getGsonObject();
        return gson.toJsonTree(this).getAsJsonObject();
    }

    public static String toJsonArrayString(List<Obj_upload> list) {
        Gson gson = Global.getGsonObject();
        return gson.toJson(list);
    }

    public static JsonArray toJsonArray(List<Obj_upload> list) {
        Gson gson = Global.getGsonObject();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<Obj_upload>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
    }

    public static Obj_upload getObjectFromJsonString(String jsonObj) {
        try {
            Gson gson = Global.getGsonObject();
            Obj_upload obj;
            obj = gson.fromJson(jsonObj, Obj_upload.class);
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
        if (!(obj instanceof BaseObj_upload)) {
            return false;
        }
        BaseObj_upload compareObj = (BaseObj_upload) obj;
        return (compareObj.getObj_id().equals(this.getObj_id()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.getObj_id() != null ? this.getObj_id().hashCode() : 0);
        return hash;
    }

    /**
     * @return the return_uid_time
     */
    public java.util.Date getReturn_uid_time() {
        return return_uid_time;
    }

    /**
     * @param return_uid_time the return_uid_time to set
     */
    public void setReturn_uid_time(java.util.Date return_uid_time) {
        this.return_uid_time = return_uid_time;
    }

}
