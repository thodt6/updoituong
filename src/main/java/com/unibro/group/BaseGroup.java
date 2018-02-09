/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.group;

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
public class BaseGroup implements Serializable {

    private Integer groupid = 0;
    private String name = "";
    private String description = "";
    private java.util.Date created_time = new java.util.Date();
    private java.util.Date modified_time = new java.util.Date();
    private Integer created_id = 0;
    private String application_id = "";
    private String uri = "";

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public Integer getGroupid() {
        return this.groupid;
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

    public void setCreated_time(java.util.Date created_time) {
        this.created_time = created_time;
    }

    public java.util.Date getCreated_time() {
        return this.created_time;
    }

    public void setModified_time(java.util.Date modified_time) {
        this.modified_time = modified_time;
    }

    public java.util.Date getModified_time() {
        return this.modified_time;
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

    public static String toJsonArrayString(List<Group> list) {
        Gson gson = Global.getGsonObject();
        return gson.toJson(list);
    }

    public static JsonArray toJsonArray(List<Group> list) {
        Gson gson = Global.getGsonObject();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<Group>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
    }

    public static Group getObjectFromJsonString(String jsonObj) {
        try {
            Gson gson = Global.getGsonObject();
            Group obj;
            obj = gson.fromJson(jsonObj, Group.class);
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
        if (!(obj instanceof BaseGroup)) {
            return false;
        }
        BaseGroup compareObj = (BaseGroup) obj;
        return (compareObj.getGroupid().equals(this.getGroupid()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.getGroupid() != null ? this.getGroupid().hashCode() : 0);
        return hash;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

}
