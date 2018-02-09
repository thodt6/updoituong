/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.client_topup;

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
public class BaseClient_topup implements Serializable {

    private Long topup_id;
    private Integer rate_id = 0;
    private Integer amount_money = 0;
    private Integer amount_uid = 0;
    private java.util.Date topup_date = new java.util.Date();
    private Integer created_id = 0;
    private String client_id = "";

    public void setTopup_id(Long topup_id) {
        this.topup_id = topup_id;
    }

    public Long getTopup_id() {
        return this.topup_id;
    }

    public void setRate_id(Integer rate_id) {
        this.rate_id = rate_id;
    }

    public Integer getRate_id() {
        return this.rate_id;
    }

    public void setAmount_money(Integer amount_money) {
        this.amount_money = amount_money;
    }

    public Integer getAmount_money() {
        return this.amount_money;
    }

    public void setAmount_uid(Integer amount_uid) {
        this.amount_uid = amount_uid;
    }

    public Integer getAmount_uid() {
        return this.amount_uid;
    }

    public void setTopup_date(java.util.Date topup_date) {
        this.topup_date = topup_date;
    }

    public java.util.Date getTopup_date() {
        return this.topup_date;
    }

    public void setCreated_id(Integer created_id) {
        this.created_id = created_id;
    }

    public Integer getCreated_id() {
        return this.created_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_id() {
        return this.client_id;
    }

    public String toJsonStr() {
        Gson gson = Global.getGsonObject();
        return gson.toJson(this);
    }

    public JsonObject toJson() {
        Gson gson = Global.getGsonObject();
        return gson.toJsonTree(this).getAsJsonObject();
    }

    public static String toJsonArrayString(List<Client_topup> list) {
        Gson gson = Global.getGsonObject();
        return gson.toJson(list);
    }

    public static JsonArray toJsonArray(List<Client_topup> list) {
        Gson gson = Global.getGsonObject();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<Client_topup>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
    }

    public static Client_topup getObjectFromJsonString(String jsonObj) {
        try {
            Gson gson = Global.getGsonObject();
            Client_topup obj;
            obj = gson.fromJson(jsonObj, Client_topup.class);
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
        if (!(obj instanceof BaseClient_topup)) {
            return false;
        }
        BaseClient_topup compareObj = (BaseClient_topup) obj;
        return (compareObj.getTopup_id().equals(this.getTopup_id()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.getTopup_id() != null ? this.getTopup_id().hashCode() : 0);
        return hash;
    }

}
