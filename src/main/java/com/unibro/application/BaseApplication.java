/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.application;

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
public class BaseApplication implements Serializable {

    private String appid = Global.getRandomString();
    private String name = "";
    private String description = "";
    private Integer update_id = 0;
    private java.util.Date update_time = new java.util.Date();

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppid() {
        return this.appid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
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

    public String toJsonStr() {
        Gson gson = Global.getGsonObject();
        return gson.toJson(this);
    }

    public JsonObject toJson() {
        Gson gson = Global.getGsonObject();
        return gson.toJsonTree(this).getAsJsonObject();
    }

    public static String toJsonArrayString(List<Application> list) {
        Gson gson = Global.getGsonObject();
        return gson.toJson(list);
    }

    public static JsonArray toJsonArray(List<Application> list) {
        Gson gson = Global.getGsonObject();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<Application>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
    }

    public static Application getObjectFromJsonString(String jsonObj) {
        try {
            Gson gson = Global.getGsonObject();
            Application obj;
            obj = gson.fromJson(jsonObj, Application.class);
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
        if (!(obj instanceof BaseApplication)) {
            return false;
        }
        BaseApplication compareObj = (BaseApplication) obj;
        return (compareObj.getAppid().equals(this.getAppid()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.getAppid() != null ? this.getAppid().hashCode() : 0);
        return hash;
    }

}
