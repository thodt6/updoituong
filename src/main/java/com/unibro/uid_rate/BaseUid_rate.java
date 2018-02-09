/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.uid_rate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.unibro.sysuser.UserSessionBean;
import com.unibro.utils.Global;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author THOND
 */
public class BaseUid_rate implements Serializable {

    private Long rate_id;
    private String name = "";
    private String description = "";
    private Integer value = 0;
    private Integer active = 0;
    private java.util.Date update_time = new java.util.Date();
    private Integer update_id = UserSessionBean.getUserSession().getUser().getUserid();
    private String currency = "VND";

    public void setRate_id(Long rate_id) {
        this.rate_id = rate_id;
    }

    public Long getRate_id() {
        return this.rate_id;
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

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getActive() {
        return this.active;
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

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return this.currency;
    }

    public String toJsonStr() {
        Gson gson = Global.getGsonObject();
        return gson.toJson(this);
    }

    public JsonObject toJson() {
        Gson gson = Global.getGsonObject();
        return gson.toJsonTree(this).getAsJsonObject();
    }

    public static String toJsonArrayString(List<Uid_rate> list) {
        Gson gson = Global.getGsonObject();
        return gson.toJson(list);
    }

    public static JsonArray toJsonArray(List<Uid_rate> list) {
        Gson gson = Global.getGsonObject();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<Uid_rate>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
    }

    public static Uid_rate getObjectFromJsonString(String jsonObj) {
        try {
            Gson gson = Global.getGsonObject();
            Uid_rate obj;
            obj = gson.fromJson(jsonObj, Uid_rate.class);
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
        if (!(obj instanceof BaseUid_rate)) {
            return false;
        }
        BaseUid_rate compareObj = (BaseUid_rate) obj;
        return (compareObj.getRate_id().equals(this.getRate_id()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.getRate_id() != null ? this.getRate_id().hashCode() : 0);
        return hash;
    }

}
