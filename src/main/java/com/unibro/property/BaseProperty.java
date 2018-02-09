/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.property;

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
public class BaseProperty implements Serializable {

    private String prop_id = Global.getRandomString();
    private String name = "";
    private String uri = "";
    private Integer type = 0;
    private String func_id = "";
    private java.util.Date update_time = new java.util.Date();
    private Integer created_id = 0;
    private String application_id = "";

    public void setProp_id(String prop_id) {
        this.prop_id = prop_id;
    }

    public String getProp_id() {
        return this.prop_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return this.uri;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return this.type;
    }

    public void setFunc_id(String func_id) {
        this.func_id = func_id;
    }

    public String getFunc_id() {
        return this.func_id;
    }

    public void setUpdate_time(java.util.Date update_time) {
        this.update_time = update_time;
    }

    public java.util.Date getUpdate_time() {
        return this.update_time;
    }

    public void setCreated_id(Integer created_id) {
        this.created_id = created_id;
    }

    public Integer getCreated_id() {
        return this.created_id;
    }

    public void setApplication_id(String application_id) {
        this.application_id = application_id;
    }

    public String getApplication_id() {
        return this.application_id;
    }

    public String toJsonStr() {
        Gson gson = Global.getGsonObject();
        return gson.toJson(this);
    }

    public JsonObject toJson() {
        Gson gson = Global.getGsonObject();
        return gson.toJsonTree(this).getAsJsonObject();
    }

    public static String toJsonArrayString(List<Property> list) {
        Gson gson = Global.getGsonObject();
        return gson.toJson(list);
    }

    public static JsonArray toJsonArray(List<Property> list) {
        Gson gson = Global.getGsonObject();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<Property>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
    }

    public static Property getObjectFromJsonString(String jsonObj) {
        try {
            Gson gson = Global.getGsonObject();
            Property obj;
            obj = gson.fromJson(jsonObj, Property.class);
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
        if (!(obj instanceof BaseProperty)) {
            return false;
        }
        BaseProperty compareObj = (BaseProperty) obj;
        return (compareObj.getProp_id().equals(this.getProp_id()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.getProp_id() != null ? this.getProp_id().hashCode() : 0);
        return hash;
    }

}
