/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.fb_cookie;

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
public class BaseFb_cookie implements Serializable {

    private Integer fbid = 0;
    private String name = "";
    private String cookie = "";
    private java.util.Date createdtime = new java.util.Date();
    private String state = "";

    public void setFbid(Integer fbid) {
        this.fbid = fbid;
    }

    public Integer getFbid() {
        return this.fbid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getCookie() {
        return this.cookie;
    }

    public void setCreatedtime(java.util.Date createdtime) {
        this.createdtime = createdtime;
    }

    public java.util.Date getCreatedtime() {
        return this.createdtime;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    public String toJsonStr() {
        Gson gson = Global.getGsonObject();
        return gson.toJson(this);
    }

    public JsonObject toJson() {
        Gson gson = Global.getGsonObject();
        return gson.toJsonTree(this).getAsJsonObject();
    }

    public static String toJsonArrayString(List<Fb_cookie> list) {
        Gson gson = Global.getGsonObject();
        return gson.toJson(list);
    }

    public static JsonArray toJsonArray(List<Fb_cookie> list) {
        Gson gson = Global.getGsonObject();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<Fb_cookie>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
    }

    public static Fb_cookie getObjectFromJsonString(String jsonObj) {
        try {
            Gson gson = Global.getGsonObject();
            Fb_cookie obj;
            obj = gson.fromJson(jsonObj, Fb_cookie.class);
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
        if (!(obj instanceof BaseFb_cookie)) {
            return false;
        }
        BaseFb_cookie compareObj = (BaseFb_cookie) obj;
        return (compareObj.getFbid().equals(this.getFbid()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.getFbid() != null ? this.getFbid().hashCode() : 0);
        return hash;
    }

}
