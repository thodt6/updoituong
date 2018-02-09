/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.user;

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
public class BaseUser implements Serializable {

    private Integer userid= 0;
    private String oauth_provider = "username";
    private String uid = "";
    private String username = "";
    private String password_hash = "";
    private String email = "";
    private String phone = "";
    private String first_name = "";
    private String last_name = "";
    private String gender = "male";
    private String picture = "defaults/defaultuser.png";
    private String country_code = "";
    private String state = "";
    private String city = "";
    private String post_code = "";
    private java.util.Date created_time = new java.util.Date();
    private java.util.Date modified_time = new java.util.Date();
    private java.util.Date last_login = new java.util.Date();
    private java.util.Date last_logout = new java.util.Date();
    private Integer status = 0;
    private String login_token = "";
    private Long expired_time = Long.valueOf(0);
    private String password_reset_token = "";
    private String application_id = Global.getConfigValue("app.applicationid_list");

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getUserid() {
        return this.userid;
    }

    public void setOauth_provider(String oauth_provider) {
        this.oauth_provider = oauth_provider;
    }

    public String getOauth_provider() {
        return this.oauth_provider;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getPassword_hash() {
        return this.password_hash;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getFirst_name() {
        return this.first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getLast_name() {
        return this.last_name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return this.gender;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getCountry_code() {
        return this.country_code;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return this.city;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
    }

    public String getPost_code() {
        return this.post_code;
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

    public void setLast_login(java.util.Date last_login) {
        this.last_login = last_login;
    }

    public java.util.Date getLast_login() {
        return this.last_login;
    }

    public void setLast_logout(java.util.Date last_logout) {
        this.last_logout = last_logout;
    }

    public java.util.Date getLast_logout() {
        return this.last_logout;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setLogin_token(String login_token) {
        this.login_token = login_token;
    }

    public String getLogin_token() {
        return this.login_token;
    }

    public void setExpired_time(Long expired_time) {
        this.expired_time = expired_time;
    }

    public Long getExpired_time() {
        return this.expired_time;
    }

    public void setPassword_reset_token(String password_reset_token) {
        this.password_reset_token = password_reset_token;
    }

    public String getPassword_reset_token() {
        return this.password_reset_token;
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

    public static String toJsonArrayString(List<User> list) {
        Gson gson = Global.getGsonObject();
        return gson.toJson(list);
    }

    public static JsonArray toJsonArray(List<User> list) {
        Gson gson = Global.getGsonObject();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<User>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
    }

    public static User getObjectFromJsonString(String jsonObj) {
        try {
            Gson gson = Global.getGsonObject();
            User obj;
            obj = gson.fromJson(jsonObj, User.class);
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
        if (!(obj instanceof BaseUser)) {
            return false;
        }
        BaseUser compareObj = (BaseUser) obj;
        return (compareObj.getUserid().equals(this.getUserid()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.getUserid() != null ? this.getUserid().hashCode() : 0);
        return hash;
    }

}
