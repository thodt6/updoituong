/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
/**
 *
 * @author THOND
 */
public class ApiClient {

    String url;
    String secret;
    Logger logger = Logger.getLogger(this.getClass().getName());

    public ApiClient() {

    }

    public ApiClient(String url) {
        this.url = url;
        this.secret = "verysecure";
    }

    public ApiClient(String url, String secret) {
        this.url = url;
        this.secret = secret;
    }

    public JsonObject executeGetQuery(List<NameValuePair> param) {
        try {
            org.apache.http.client.HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            request.setHeader("Authorization", secret);
            request.setHeader("Accept-Encoding", "UTF-8");
            request.setHeader("Accept-Charset", "UTF-8");
            request.setHeader("Content-Encoding", "UTF-8");
            request.setHeader("Content-Type", "application/json;charset=UTF-8");
            URIBuilder builder = new URIBuilder(request.getURI());
            if (param != null) {
                builder.setParameters(param);
            }
            request.setURI(builder.build());
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String respStr = EntityUtils.toString(entity, "UTF-8");
            logger.info("Get " + request.getURI().toURL().toString() + ":" + respStr);
            Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            return gson.fromJson(respStr, JsonObject.class);
        } catch (IOException ex) {
            logger.error("Error:" + ex);
            return null;
        } catch (java.net.URISyntaxException ex) {
            logger.error("Error:" + ex);
            return null;
        } catch (com.google.gson.JsonIOException ex) {
            logger.error("Error:" + ex);
            return null;
        }
    }

    public JsonObject executePostQuery(Object object) {
        try {
            org.apache.http.client.HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-type", "application/json");
            post.setHeader("Authorization", this.secret);
            Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            if (object != null) {
                post.setEntity(new StringEntity(gson.toJson(object), "UTF-8"));
            }
            BasicResponseHandler responseHandler = new BasicResponseHandler();
            String response = (String) httpClient.execute(post, responseHandler);
            logger.info("Http Response:" + response);
            response = response.trim();
            logger.info("Get " + post.getURI().toURL().toString() + ":" + response);
            return gson.fromJson(response, JsonObject.class);
        } catch (IOException ex) {
            logger.error("Error:" + ex);
            return null;
        } catch (com.google.gson.JsonIOException ex) {
            logger.error("Error:" + ex);
            return null;
        }
    }
    
    public String doPostQuery(Object object) {
        try {
            org.apache.http.client.HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-type", "application/json");
            Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            if (object != null) {
                post.setEntity(new StringEntity(gson.toJson(object), "UTF-8"));
            }
            BasicResponseHandler responseHandler = new BasicResponseHandler();
            String response = (String) httpClient.execute(post, responseHandler);
            return response;
        } catch (IOException ex) {
            logger.error("Error:" + ex);
            return null;
        } catch (com.google.gson.JsonIOException ex) {
            logger.error("Error:" + ex);
            return null;
        }
    }

    public JsonObject executePutQuery(Object object) {
        try {
            org.apache.http.client.HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPut request = new HttpPut(url);
            request.setHeader("Content-type", "application/json");
            request.setHeader("Authorization", this.secret);
            Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            if (object != null) {
                request.setEntity(new StringEntity(gson.toJson(object), "UTF-8"));
            }
            BasicResponseHandler responseHandler = new BasicResponseHandler();
            String response = (String) httpClient.execute(request, responseHandler);
            logger.info("Http Response:" + response);
            response = response.trim();
            logger.info("Get " + request.getURI().toURL().toString() + ":" + response);
            return gson.fromJson(response, JsonObject.class);
        } catch (IOException ex) {
            logger.error("Error:" + ex);
            return null;
        } catch (com.google.gson.JsonIOException ex) {
            logger.error("Error:" + ex);
            return null;
        }
    }

    public JsonObject executeDeleteQuery() {
        try {
            org.apache.http.client.HttpClient httpClient = HttpClientBuilder.create().build();
            HttpDelete request = new HttpDelete(url);
            request.setHeader("Content-type", "application/json");
            request.setHeader("Authorization", this.secret);
            Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            BasicResponseHandler responseHandler = new BasicResponseHandler();
            String response = (String) httpClient.execute(request, responseHandler);
            logger.info("Http Response:" + response);
            response = response.trim();
            logger.info("Delete " + request.getURI().toURL().toString() + ":" + response);
            return gson.fromJson(response, JsonObject.class);
        } catch (IOException ex) {
            logger.error("Error:" + ex);
            return null;
        } catch (com.google.gson.JsonIOException ex) {
            logger.error("Error:" + ex);
            return null;
        }
    }
}
