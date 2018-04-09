/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.facebook_group;

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
public class BaseFacebook_group implements Serializable {

    private String groupid = Global.getRandomString();
    private String name = "";
    private java.util.Date created_time = new java.util.Date();
    private String uid_file = "";
    private String state = "";
    private String created_id = "";
    private Integer total_member = 0;
    private String description = "";
    private String email = "";
    private String icon = "";
    private String owner_id = "";
    private String privacy = "";
    private String updated_time = "";
    private Integer customer_type = 0;

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupid() {
        return this.groupid;
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

    public void setUid_file(String uid_file) {
        this.uid_file = uid_file;
    }

    public String getUid_file() {
        return this.uid_file;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    public void setCreated_id(String created_id) {
        this.created_id = created_id;
    }

    public String getCreated_id() {
        return this.created_id;
    }

    public void setTotal_member(Integer total_member) {
        this.total_member = total_member;
    }

    public Integer getTotal_member() {
        return this.total_member;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwner_id() {
        return this.owner_id;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getPrivacy() {
        return this.privacy;
    }

    public void setUpdated_time(String updated_time) {
        this.updated_time = updated_time;
    }

    public String getUpdated_time() {
        return this.updated_time;
    }

    public String toJsonStr() {
        Gson gson = Global.getGsonObject();
        return gson.toJson(this);
    }

    public JsonObject toJson() {
        Gson gson = Global.getGsonObject();
        return gson.toJsonTree(this).getAsJsonObject();
    }

    public static String toJsonArrayString(List<Facebook_group> list) {
        Gson gson = Global.getGsonObject();
        return gson.toJson(list);
    }

    public static JsonArray toJsonArray(List<Facebook_group> list) {
        Gson gson = Global.getGsonObject();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<Facebook_group>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
    }

    public static Facebook_group getObjectFromJsonString(String jsonObj) {
        try {
            Gson gson = Global.getGsonObject();
            Facebook_group obj;
            obj = gson.fromJson(jsonObj, Facebook_group.class);
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
        if (!(obj instanceof BaseFacebook_group)) {
            return false;
        }
        BaseFacebook_group compareObj = (BaseFacebook_group) obj;
        return (compareObj.getUniqueKey().equals(this.getUniqueKey()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.getUniqueKey() != null ? this.getUniqueKey().hashCode() : 0);
        return hash;
    }

    public String getUniqueKey() {
        return groupid;
    }

    /**
     * @return the customer_type
     */
    public Integer getCustomer_type() {
        return customer_type;
    }

    /**
     * @param customer_type the customer_type to set
     */
    public void setCustomer_type(Integer customer_type) {
        this.customer_type = customer_type;
    }

}
