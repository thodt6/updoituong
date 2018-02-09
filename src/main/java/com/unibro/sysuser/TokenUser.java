/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.sysuser;

/**
 *
 * @author THOND
 */
public class TokenUser {

    private String userid;
    private String uid;
    private String username;
    private String password_hash;
    private String oauth_provider;

    public TokenUser(String username, String password_hash) {
        this.username = username;
        this.password_hash = password_hash;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password_hash
     */
    public String getPassword_hash() {
        return password_hash;
    }

    /**
     * @param password_hash the password_hash to set
     */
    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    /**
     * @return the userid
     */
    public String getUserid() {
        return userid;
    }

    /**
     * @param userid the userid to set
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * @return the oauth_provider
     */
    public String getOauth_provider() {
        return oauth_provider;
    }

    /**
     * @param oauth_provider the oauth_provider to set
     */
    public void setOauth_provider(String oauth_provider) {
        this.oauth_provider = oauth_provider;
    }
}
