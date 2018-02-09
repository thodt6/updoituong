/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.access_list;

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
public class BaseAccess_list implements Serializable {

    private Integer access_id = 0;
    private String func_id ;
    private String prop_id ;
    private String principle_type = "";
    private Integer principle_id = 0;
    private Integer permission = 0;
    private java.util.Date update_time = new java.util.Date();
    private Integer update_id = 0;
    private String application_id = "";

    public void setAccess_id(Integer access_id) {
        this.access_id = access_id;
    }

    public Integer getAccess_id() {
        return this.access_id;
    }

    public void setFunc_id(String func_id) {
        this.func_id = func_id;
    }

    public String getFunc_id() {
        return this.func_id;
    }

    public void setProp_id(String prop_id) {
        this.prop_id = prop_id;
    }

    public String getProp_id() {
        return this.prop_id;
    }

    public void setPrinciple_type(String principle_type) {
        this.principle_type = principle_type;
    }

    public String getPrinciple_type() {
        return this.principle_type;
    }

    public void setPrinciple_id(Integer principle_id) {
        this.principle_id = principle_id;
    }

    public Integer getPrinciple_id() {
        return this.principle_id;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    public Integer getPermission() {
        return this.permission;
    }

    public void setUpdate_time(java.util.Date update_time) {
        this.update_time = update_time;
    }

    public java.util.Date getUpdate_time() {
        return this.update_time;
    }

    public void setUpdate_id(Integer update_id) {
        this.update_id = update_id;
    }

    public Integer getUpdate_id() {
        return this.update_id;
    }

    public String toJsonStr() {
        Gson gson = Global.getGsonObject();
        return gson.toJson(this);
    }

    public JsonObject toJson() {
        Gson gson = Global.getGsonObject();
        return gson.toJsonTree(this).getAsJsonObject();
    }

    public static String toJsonArrayString(List<Access_list> list) {
        Gson gson = Global.getGsonObject();
        return gson.toJson(list);
    }

    public static JsonArray toJsonArray(List<Access_list> list) {
        Gson gson = Global.getGsonObject();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<Access_list>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
    }

    public static Access_list getObjectFromJsonString(String jsonObj) {
        try {
            Gson gson = Global.getGsonObject();
            Access_list obj;
            obj = gson.fromJson(jsonObj, Access_list.class);
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
        if (!(obj instanceof BaseAccess_list)) {
            return false;
        }
        BaseAccess_list compareObj = (BaseAccess_list) obj;
        return (compareObj.getAccess_id().equals(this.getAccess_id()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.getAccess_id() != null ? this.getAccess_id().hashCode() : 0);
        return hash;
    }

    /**
     * @return the application_id
     */
    public String getApplication_id() {
        return application_id;
    }

    /**
     * @param application_id the application_id to set
     */
    public void setApplication_id(String application_id) {
        this.application_id = application_id;
    }

}
