/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.token;

import com.google.gson.JsonObject;
import com.unibro.utils.ApiClient;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

/**
 *
 * @author THOND
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class TokenService {

    private String token_id;
    private String token = "";
    private String name;
    private String url;
    Logger log = Logger.getLogger(this.getClass().getName());
    static final String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36";
    static final String aws_url = "https://eqbp1t60hl.execute-api.ap-northeast-1.amazonaws.com/prod/fb_token";

    private JsonObject queryUserProfile(String access_token) {
        String g = "https://graph.facebook.com/me?fields=id,name,about,birthday,email,picture,locale,address,cover,first_name,gender,last_name,link,location";
        ApiClient client = new ApiClient(g, "Bearer " + access_token);
        JsonObject obj = client.executeGetQuery(null);
        if (obj != null) {
            log.info(obj.toString());
        } else {
            log.info("null");
        }
        return obj;
    }

    public void initSelectedObject() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            //Add code for init object here
            this.queryForToken();
            JsonObject obj = this.queryUserProfile(getToken());
            if (obj != null) {
                name = obj.get("name").getAsString();
                if (obj.has("picture")) {
                    if (obj.get("picture").isJsonObject()) {
                        JsonObject picture = obj.get("picture").getAsJsonObject();
                        if (picture.has("data")) {
                            this.setUrl(picture.get("data").getAsJsonObject().get("url").getAsString());
                        }
                    } else {
                        this.setUrl(obj.get("picture").getAsString());
                    }
                }
            }
        }
    }

    class Token_request {

        public String fb_id;
    }

    private void queryForToken() {
        ApiClient client = new ApiClient(aws_url);
        Token_request request = new Token_request();
        request.fb_id = this.token_id;
        String ret = client.doPostQuery(request);
        if (ret != null) {
            this.token = ret.replace("\"", "");
        }

    }

//    private void queryForToken(String token_id) {
//        byte[] decodedBytes = Base64.getDecoder().decode(token_id);
//        String decodeValue = new String(decodedBytes);
//        String[] cookies = decodeValue.split(";");
//        int size = cookies.length;
//        String[] ck_names = new String[size];
//        String[] ck_values = new String[size];
//        int id = 0;
//        for (String cookie : cookies) {
//            int index = cookie.indexOf("=");
//            if (index >= 0) {
//                String name = cookie.substring(0, index);
//                String value = cookie.substring(index + 1);
//                ck_names[id] = name;
//                ck_values[id] = value;
//            }
//            id++;
//        }
//        String profile_data = Global.executeGetQuery(null, user_agent, "https://www.facebook.com/profile", "facebook.com", ck_names, ck_values);
//        if (profile_data != null) {
//            this.setToken(Global.queryString(profile_data, "access_token:\"", "\""));
//        }
//
//    }
    @PostConstruct
    public void init() {

    }

    /**
     * @return the token_id
     */
    public String getToken_id() {
        return token_id;
    }

    /**
     * @param token_id the token_id to set
     */
    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
