/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.client_transaction;

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
public class BaseClient_transaction implements Serializable {

    private Integer id = 0;
    private String uid = Global.getRandomString();
    private String obj_id = "";
    private Integer amount = 0;
    private java.util.Date trans_date = new java.util.Date();
    private Integer status = 0;
    private String reason = "";
    private String client_id="";

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return this.uid;
    }

    public void setObj_id(String obj_id) {
        this.obj_id = obj_id;
    }

    public String getObj_id() {
        return this.obj_id;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public void setTrans_date(java.util.Date trans_date) {
        this.trans_date = trans_date;
    }

    public java.util.Date getTrans_date() {
        return this.trans_date;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return this.reason;
    }

    public String toJsonStr() {
        Gson gson = Global.getGsonObject();
        return gson.toJson(this);
    }

    public JsonObject toJson() {
        Gson gson = Global.getGsonObject();
        return gson.toJsonTree(this).getAsJsonObject();
    }

    public static String toJsonArrayString(List<Client_transaction> list) {
        Gson gson = Global.getGsonObject();
        return gson.toJson(list);
    }

    public static JsonArray toJsonArray(List<Client_transaction> list) {
        Gson gson = Global.getGsonObject();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<Client_transaction>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
    }

    public static Client_transaction getObjectFromJsonString(String jsonObj) {
        try {
            Gson gson = Global.getGsonObject();
            Client_transaction obj;
            obj = gson.fromJson(jsonObj, Client_transaction.class);
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
        if (!(obj instanceof BaseClient_transaction)) {
            return false;
        }
        BaseClient_transaction compareObj = (BaseClient_transaction) obj;
        return (compareObj.getId().equals(this.getId()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    /**
     * @return the client_id
     */
    public String getClient_id() {
        return client_id;
    }

    /**
     * @param client_id the client_id to set
     */
    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

}
