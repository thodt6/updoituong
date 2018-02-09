/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.user_group;

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
public class BaseUser_group implements Serializable {

    private Integer userid = 0;
    private String groupid_list = "";
    private java.util.Date update_time = new java.util.Date();
    private Integer created_id = 0;
    private String default_groupid;

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getUserid() {
        return this.userid;
    }

    public void setGroupid_list(String groupid_list) {
        this.groupid_list = groupid_list;
    }

    public String getGroupid_list() {
        return this.groupid_list;
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

    public String toJsonStr() {
        Gson gson = Global.getGsonObject();
        return gson.toJson(this);
    }

    public JsonObject toJson() {
        Gson gson = Global.getGsonObject();
        return gson.toJsonTree(this).getAsJsonObject();
    }

    public static String toJsonArrayString(List<User_group> list) {
        Gson gson = Global.getGsonObject();
        return gson.toJson(list);
    }

    public static JsonArray toJsonArray(List<User_group> list) {
        Gson gson = Global.getGsonObject();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<User_group>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
    }

    public static User_group getObjectFromJsonString(String jsonObj) {
        try {
            Gson gson = Global.getGsonObject();
            User_group obj;
            obj = gson.fromJson(jsonObj, User_group.class);
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
        if (!(obj instanceof BaseUser_group)) {
            return false;
        }
        BaseUser_group compareObj = (BaseUser_group) obj;
        return (compareObj.getUserid().equals(this.getUserid()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.getUserid() != null ? this.getUserid().hashCode() : 0);
        return hash;
    }

    /**
     * @return the default_groupid
     */
    public String getDefault_groupid() {
        return default_groupid;
    }

    /**
     * @param default_groupid the default_groupid to set
     */
    public void setDefault_groupid(String default_groupid) {
        this.default_groupid = default_groupid;
    }

}
