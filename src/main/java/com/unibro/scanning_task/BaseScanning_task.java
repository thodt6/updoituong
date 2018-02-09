/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.scanning_task;

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
public class BaseScanning_task implements Serializable {

    private Integer taskid = 0;
    private String client_id = "";
    private java.util.Date start_time = new java.util.Date();
    private String state = "";
    private Integer range_id = 0;
    private String start_position = "";
    private String stop_position = "";
    private Integer total_result = 0 ;
    private String current_pos;

    public void setTaskid(Integer taskid) {
        this.taskid = taskid;
    }

    public Integer getTaskid() {
        return this.taskid;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_id() {
        return this.client_id;
    }

    public void setStart_time(java.util.Date start_time) {
        this.start_time = start_time;
    }

    public java.util.Date getStart_time() {
        return this.start_time;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    public void setRange_id(Integer range_id) {
        this.range_id = range_id;
    }

    public Integer getRange_id() {
        return this.range_id;
    }

    public void setStart_position(String start_position) {
        this.start_position = start_position;
    }

    public String getStart_position() {
        return this.start_position;
    }

    public void setStop_position(String stop_position) {
        this.stop_position = stop_position;
    }

    public String getStop_position() {
        return this.stop_position;
    }

    public String toJsonStr() {
        Gson gson = Global.getGsonObject();
        return gson.toJson(this);
    }

    public JsonObject toJson() {
        Gson gson = Global.getGsonObject();
        return gson.toJsonTree(this).getAsJsonObject();
    }

    public static String toJsonArrayString(List<Scanning_task> list) {
        Gson gson = Global.getGsonObject();
        return gson.toJson(list);
    }

    public static JsonArray toJsonArray(List<Scanning_task> list) {
        Gson gson = Global.getGsonObject();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<Scanning_task>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        return jsonArray;
    }

    public static Scanning_task getObjectFromJsonString(String jsonObj) {
        try {
            Gson gson = Global.getGsonObject();
            Scanning_task obj;
            obj = gson.fromJson(jsonObj, Scanning_task.class);
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
        if (!(obj instanceof BaseScanning_task)) {
            return false;
        }
        BaseScanning_task compareObj = (BaseScanning_task) obj;
        return (compareObj.getTaskid().equals(this.getTaskid()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.getTaskid() != null ? this.getTaskid().hashCode() : 0);
        return hash;
    }

    /**
     * @return the total_result
     */
    public Integer getTotal_result() {
        return total_result;
    }

    /**
     * @param total_result the total_result to set
     */
    public void setTotal_result(Integer total_result) {
        this.total_result = total_result;
    }

    /**
     * @return the current_pos
     */
    public String getCurrent_pos() {
        return current_pos;
    }

    /**
     * @param current_pos the current_pos to set
     */
    public void setCurrent_pos(String current_pos) {
        this.current_pos = current_pos;
    }

}
