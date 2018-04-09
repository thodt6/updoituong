/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.customer_type;

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
public class BaseCustomer_type implements Serializable {

    private Integer type_id = 0;
    private String name = "";
    private java.util.Date created_time = new java.util.Date();
    private String description = "";
    private String created_id = UserSessionBean.getUserSession().getUser().getUserid() + "";

    public void setType_id(Integer type_id) {
        this.type_id = type_id;
    }

    public Integer getType_id() {
        return this.type_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setCreated_time(java.util.Date created_time) {
        this.created_time = created_time;
    }

    public java.util.Date getCreated_time() {
        return this.created_time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setCreated_id(String created_id) {
        this.created_id = created_id;
    }

    public String getCreated_id() {
        return this.created_id;
    }

    public String toJsonStr() {
        Gson gson = Global.getGsonObject();
        return gson.toJson(this);
    }

    public JsonObject toJson() {
        Gson gson = Global.getGsonObject();
        return gson.toJsonTree(this).getAsJsonObject();
    }

    public static String toJsonArrayString(List<Customer_type> list) {
        Gson gson = Global.getGsonObject();
        return gson.toJson(list);
    }

    public static JsonArray toJsonArray(List<Customer_type> list) {
        Gson gson = Global.getGsonObject();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<Customer_type>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
    }

    public static Customer_type getObjectFromJsonString(String jsonObj) {
        try {
            Gson gson = Global.getGsonObject();
            Customer_type obj;
            obj = gson.fromJson(jsonObj, Customer_type.class);
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
        if (!(obj instanceof BaseCustomer_type)) {
            return false;
        }
        BaseCustomer_type compareObj = (BaseCustomer_type) obj;
        return (compareObj.getUniqueKey().equals(this.getUniqueKey()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.getUniqueKey() != null ? this.getUniqueKey().hashCode() : 0);
        return hash;
    }

    public String getUniqueKey() {
        return type_id.toString();
    }

}
