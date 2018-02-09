/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.appclient;

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
public class BaseAppclient implements Serializable {

    private String clientid = Global.getRandomString();
    private String name = "";
    private String ipaddress = "";
    private java.util.Date online_time = new java.util.Date();
    private java.util.Date created_time = new java.util.Date();
    private java.util.Date last_request_time = new java.util.Date();
    private String last_request_event = "";

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getClientid() {
        return this.clientid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getIpaddress() {
        return this.ipaddress;
    }

    public void setOnline_time(java.util.Date online_time) {
        this.online_time = online_time;
    }

    public java.util.Date getOnline_time() {
        return this.online_time;
    }

    public void setCreated_time(java.util.Date created_time) {
        this.created_time = created_time;
    }

    public java.util.Date getCreated_time() {
        return this.created_time;
    }

    public void setLast_request_time(java.util.Date last_request_time) {
        this.last_request_time = last_request_time;
    }

    public java.util.Date getLast_request_time() {
        return this.last_request_time;
    }

    public void setLast_request_event(String last_request_event) {
        this.last_request_event = last_request_event;
    }

    public String getLast_request_event() {
        return this.last_request_event;
    }

    public String toJsonStr() {
        Gson gson = Global.getGsonObject();
        return gson.toJson(this);
    }

    public JsonObject toJson() {
        Gson gson = Global.getGsonObject();
        return gson.toJsonTree(this).getAsJsonObject();
    }

    public static String toJsonArrayString(List<Appclient> list) {
        Gson gson = Global.getGsonObject();
        return gson.toJson(list);
    }

    public static JsonArray toJsonArray(List<Appclient> list) {
        Gson gson = Global.getGsonObject();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<Appclient>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
    }

    public static Appclient getObjectFromJsonString(String jsonObj) {
        try {
            Gson gson = Global.getGsonObject();
            Appclient obj;
            obj = gson.fromJson(jsonObj, Appclient.class);
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
        if (!(obj instanceof BaseAppclient)) {
            return false;
        }
        BaseAppclient compareObj = (BaseAppclient) obj;
        return (compareObj.getClientid().equals(this.getClientid()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.getClientid() != null ? this.getClientid().hashCode() : 0);
        return hash;
    }

}
