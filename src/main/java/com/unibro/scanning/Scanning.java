/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.scanning;

import com.unibro.utils.Global;
import org.apache.log4j.Logger;


/**
 *
 * @author THOND
 */
public class Scanning {

    String cookie;
    String user_agent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
    
    Logger logger=Logger.getLogger(this.getClass().getName());

    public Scanning(String cookie) {
        this.cookie = cookie;
    }

    private String queryPage(String url, String domain) {
        String[] cookies = cookie.split(";");
        int size = cookies.length;
        String[] ck_names = new String[size];
        String[] ck_values = new String[size];
        int id = 0;
        for (String cookie : cookies) {
            int index = cookie.indexOf("=");
            if (index >= 0) {
                String name = cookie.substring(0, index);
                String value = cookie.substring(index + 1);
                ck_names[id] = name;
                ck_values[id] = value;
            }
            id++;
        }
        String data = Global.executeGetQuery(null, user_agent, url, domain, ck_names, ck_values);
        return data;
    }

    public String queryFacebookProfile(String mobile) {
        String url = "https://www.facebook.com/search/str/number/keywords_users";
        url = url.replace("number", mobile);
//        System.out.println(url);
        String domain = "facebook.com";
        String ret = this.queryPage(url, domain);
//        System.out.println(ret);
        if (ret != null) {
            String str1 = "<a class=\"_32mo\" href=\"";
            String str2 = "\"";
            return Global.queryString(ret, str1, str2);
        }
        return "";
    }
    
    public String queryMobileFacebookProfile(String mobile) {
        String url = "https://m.facebook.com/search/str/number/keywords_users";
        url = url.replace("number", mobile);
//        System.out.println(url);
        String domain = "facebook.com";
        String ret = this.queryPage(url, domain);
        logger.info("Mobile Ret:" + ret);
//        System.out.println(ret);
        if (ret != null) {
            String str1 = "\"url\":\"https:\\/\\/m.facebook.com\\/profile.php?id=";
            String str2 = "\"";
            return Global.queryString(ret, str1, str2);
        }
        return "";
    }
    
    
    public String queryFacebookUID(String mobile) {
        String ret=this.queryFacebookProfile(mobile);
        System.out.println(ret);
        if(ret!=null && ret.length()>0){
            return this.getUid(ret);
        }
        return "";
    }
    
//    public String queryByMobileFacebookUID(String mobile) {
//        String ret=this.queryMobileFacebookProfile(mobile);
//        System.out.println(ret);
//        if(ret!=null && ret.length()>0){
//            return this.getUid("http://facebook.com" + ret);
//        }
//        return "";
//    }

    private String getUid(String profile_url) {

        profile_url = profile_url.replace("https://m.facebook.com", "https://facebook.com");
        String ret = Global.executeGetQuery(null, profile_url);
        if (ret != null) {
            String id = Global.queryString(ret, "fb://profile/", "\"");
            if (id != null && !id.equals("")) {
                return id;
            } else {
                id = Global.queryString(ret, "entity_id=", "&");
                if (id != null && !id.equals("")) {
                    return id;
                } else {
                    id = Global.queryString(ret, "pagelet_timeline_app_collection_", ":");
                    if (id != null && !id.equals("")) {
                        return id;
                    } else {
                        id = Global.queryString(ret, "\"entity_id\":\"", "\"");
                        if (id != null && !id.equals("")) {
                            return id;
                        } else {
                            return "";
                        }
                    }
                }
            }
        }
        return "";
    }
}
