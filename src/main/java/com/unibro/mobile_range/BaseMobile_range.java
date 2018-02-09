/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.mobile_range;

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
public class BaseMobile_range implements Serializable {

    private Integer range_id = 0;
    private String prefix = "";
    private String network_operator = "";
    private String start_number = "";
    private String stop_number = "";
    private String state = "U";
    private java.util.Date created_time = new java.util.Date();
    private Integer windows_size = 1000;
    private String country_code = "";

    public void setRange_id(Integer range_id) {
        this.range_id = range_id;
    }

    public Integer getRange_id() {
        return this.range_id;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setNetwork_operator(String network_operator) {
        this.network_operator = network_operator;
    }

    public String getNetwork_operator() {
        return this.network_operator;
    }

    public void setStart_number(String start_number) {
        this.start_number = start_number;
    }

    public String getStart_number() {
        return this.start_number;
    }

    public void setStop_number(String stop_number) {
        this.stop_number = stop_number;
    }

    public String getStop_number() {
        return this.stop_number;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    public void setCreated_time(java.util.Date created_time) {
        this.created_time = created_time;
    }

    public java.util.Date getCreated_time() {
        return this.created_time;
    }

    public void setWindows_size(Integer windows_size) {
        this.windows_size = windows_size;
    }

    public Integer getWindows_size() {
        return this.windows_size;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getCountry_code() {
        return this.country_code;
    }

    public String toJsonStr() {
        Gson gson = Global.getGsonObject();
        return gson.toJson(this);
    }

    public JsonObject toJson() {
        Gson gson = Global.getGsonObject();
        return gson.toJsonTree(this).getAsJsonObject();
    }

    public static String toJsonArrayString(List<Mobile_range> list) {
        Gson gson = Global.getGsonObject();
        return gson.toJson(list);
    }

    public static JsonArray toJsonArray(List<Mobile_range> list) {
        Gson gson = Global.getGsonObject();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<Mobile_range>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
    }

    public static Mobile_range getObjectFromJsonString(String jsonObj) {
        try {
            Gson gson = Global.getGsonObject();
            Mobile_range obj;
            obj = gson.fromJson(jsonObj, Mobile_range.class);
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
        if (!(obj instanceof BaseMobile_range)) {
            return false;
        }
        BaseMobile_range compareObj = (BaseMobile_range) obj;
        return (compareObj.getRange_id().equals(this.getRange_id()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.getRange_id() != null ? this.getRange_id().hashCode() : 0);
        return hash;
    }

}
