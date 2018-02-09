/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.unibro.utils.Global;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author THOND
 */
public class BaseClient implements Serializable {

    private String uid = Global.getRandomString();
    private String user_code = "";
    private Integer balance = 0;
    private java.util.Date update_time = new java.util.Date();
    private String full_name = "";
    private Integer status = 0;
    
    private Integer last_return_uid = 0;
    private Date last_return_time = new java.util.Date();
    

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getUser_code() {
        return this.user_code;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getBalance() {
        return this.balance;
    }

    public void setUpdate_time(java.util.Date update_time) {
        this.update_time = update_time;
    }

    public java.util.Date getUpdate_time() {
        return this.update_time;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getFull_name() {
        return this.full_name;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String toJsonStr() {
        Gson gson = Global.getGsonObject();
        return gson.toJson(this);
    }

    public JsonObject toJson() {
        Gson gson = Global.getGsonObject();
        return gson.toJsonTree(this).getAsJsonObject();
    }

    public static String toJsonArrayString(List<Client> list) {
        Gson gson = Global.getGsonObject();
        return gson.toJson(list);
    }

    public static JsonArray toJsonArray(List<Client> list) {
        Gson gson = Global.getGsonObject();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<Client>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
    }

    public static Client getObjectFromJsonString(String jsonObj) {
        try {
            Gson gson = Global.getGsonObject();
            Client obj;
            obj = gson.fromJson(jsonObj, Client.class);
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
        if (!(obj instanceof BaseClient)) {
            return false;
        }
        BaseClient compareObj = (BaseClient) obj;
        return (compareObj.getUid().equals(this.getUid()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.getUid() != null ? this.getUid().hashCode() : 0);
        return hash;
    }

    /**
     * @return the last_return_uid
     */
    public Integer getLast_return_uid() {
        return last_return_uid;
    }

    /**
     * @param last_return_uid the last_return_uid to set
     */
    public void setLast_return_uid(Integer last_return_uid) {
        this.last_return_uid = last_return_uid;
    }

    /**
     * @return the last_return_time
     */
    public Date getLast_return_time() {
        return last_return_time;
    }

    /**
     * @param last_return_time the last_return_time to set
     */
    public void setLast_return_time(Date last_return_time) {
        this.last_return_time = last_return_time;
    }

}
