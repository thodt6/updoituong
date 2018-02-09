/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.sysuser;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.unibro.group.Group;
import com.unibro.user.User;
import com.unibro.utils.ApiClient;
import com.unibro.utils.Global;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

/**
 *
 * @author Nguyen Duc Tho
 */
@SuppressWarnings("serial")
@ManagedBean
@SessionScoped

public class UserSessionBean implements Serializable {

    private String userid;
    private String password;
    private Boolean logged = true;
    private User user;
    private String localCode = "vi";
    private String code;
//    private Client customer;
    private String access_token;

    static final Logger log = Logger.getLogger(UserSessionBean.class.getName());

    public UserSessionBean() {
        this.setLocalCode("vi");
    }

    /**
     * @return the username
     */
    public String getUserid() {
        return userid;
    }

    /**
     * @param userid the username to set
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the logged
     */
    public Boolean getLogged() {
        return logged;
    }

    /**
     * @param logged the logged to set
     */
    public void setLogged(Boolean logged) {
        this.logged = logged;
    }

    public void doFacebookLogin() {
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(this.getFbLoginUrl());
        } catch (IOException ex) {
            log.error(ex);
        }
    }

    private String getFbLoginUrl() {
        try {
            String url = Global.getConfigValue("FB.LOGIN_URL") + "?client_id=" + Global.getConfigValue("FB.APP_ID")
                    + "&redirect_uri=" + URLEncoder.encode(Global.getConfigValue("FB.REDIRECT_URL"))
                    + "&scope=" + URLEncoder.encode(Global.getConfigValue("FB.SCOPE"))
                    + "&response_type=code"
                    + "&cancel_url=" + URLEncoder.encode(Global.getConfigValue("FB.REDIRECT_URL")) + "?error_reason=user_denied&error=access_denied&error_description=the+user+denied+your+request";

            return url;
        } catch (Exception ex) {
            return null;
        }
    }

    public void loginFacebookByToken() {
        JsonObject user_profile = this.queryUserProfile(access_token);
        if (user_profile != null) {
            this.user = User.signInByFacebook(user_profile, access_token, 10000);
            if (this.user != null) {
                this.user.loadGroupList();
                this.setLogged(true);
                gotoHomepage();
            } else {
                this.gotoPage("/access-denied.html");
            }
        }

    }

    public void initFacebookLoginSession() {
        log.info("FB code:" + getCode());
        ApiClient client = new ApiClient("https://graph.facebook.com/v2.11/oauth/access_token");

        List<NameValuePair> param = new ArrayList();
        NameValuePair p1 = new BasicNameValuePair("client_id", Global.getConfigValue("FB.APP_ID"));
        NameValuePair p2 = new BasicNameValuePair("redirect_uri", Global.getConfigValue("FB.REDIRECT_URL"));
        NameValuePair p3 = new BasicNameValuePair("client_secret", Global.getConfigValue("FB.APP_SECRET"));
        NameValuePair p4 = new BasicNameValuePair("code", getCode());

        param.add(p1);
        param.add(p2);
        param.add(p3);
        param.add(p4);

        JsonObject obj = client.executeGetQuery(param);
        if (obj != null) {
            String access_token = obj.get("access_token").getAsString();
            long expires_in = obj.get("expires_in").getAsLong();
            JsonObject user_profile = this.queryUserProfile(access_token);
            this.user = User.signInByFacebook(user_profile, access_token, expires_in);
            if (this.user != null) {
                this.user.loadGroupList();
                this.setLogged(true);
                gotoHomepage();
            } else {
                this.gotoPage("/access-denied.html");
            }
        } else {
            this.gotoPage("/access-denied.html");
        }
    }

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

    public void doLogin() {
        ApiClient client = new ApiClient(Global.getConfigValue("APP.USER.API_ADDRESS") + "/sso/login");
        BasicUser u = new BasicUser(this.userid, Global.MD5(this.password));
        JsonObject ret = client.executePostQuery(u);
        if (ret != null) {
            Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
            JsonElement result = ret.get("result");
            if (result != null && !(result instanceof JsonNull)) {
                String token = ret.get("result").getAsString();
                String user_json = Jwts.parser().setSigningKey(Global.getConfigValue("app.secret")).parseClaimsJws(token).getBody().getSubject();
                log.info("user_json:" + user_json);
                TokenUser token_user=gson.fromJson(user_json, TokenUser.class);
                this.user = User.getObject(token_user.getUserid());
//                this.user = gson.fromJson(user_json, User.class);
                this.user.setLogin_token(token);
                this.user.loadGroupList();
                this.setLogged(true);
                gotoHomepage();
            } else {
                this.gotoPage("/error.html");
            }
        } else {
            this.gotoPage("/access-denied.html");
        }

    }

    public void gotoHomepage() {
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ServletContext servletContext = (ServletContext) ec.getContext();
            log.info("Real path:" + servletContext.getRealPath("/"));
            if (this.user.getDefaultGroup() == null) {
                this.gotoPage("/access-denied.html");
            } else {
                ec.redirect(ec.getRequestContextPath() + this.user.getDefaultGroup().getUri());
            }

        } catch (IOException ex) {
            log.error(ex);
        }
    }

    public void gotoPage(String uri) {
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ServletContext servletContext = (ServletContext) ec.getContext();
            log.info("Real path:" + servletContext.getRealPath("/"));
            ec.redirect(ec.getRequestContextPath() + uri);
        } catch (IOException ex) {
            log.error(ex);
        }
    }

    public void verifyAccessPage() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        if (!this.logged) {
            try {
                this.userid = "";
                this.password = "";
                this.setUser(null);
                context.redirect(context.getRequestContextPath() + "/access-denied.html");
            } catch (IOException ex) {

            }
        } else {
            boolean validSysUser = false;
            this.user.loadGroupList();
            List<Group> list_group = this.user.getGroupList();
            if (list_group != null) {
                for (Group g : list_group) {
                    if (g.checkAccessPage(context.getRequestServletPath())) {
                        validSysUser = true;
                        break;
                    }
                }
            }
//            if (user == null) {
//                validSysUser = false;
//            } else {
//                validSysUser = user.getGroup().checkAccessableModules(context.getRequestServletPath());
//            }
            if (!validSysUser) {
                try {
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    context.redirect(facesContext.getExternalContext().getRequestContextPath() + "/access-denied.html");
                } catch (IOException e) {
                }
            } else {
//                HttpServletResponse response = (HttpServletResponse) context.getResponse();
//                response.setHeader("Access-Control-Allow-Origin", "*");
//                response.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT");
//                response.setHeader("Access-Control-Allow-Headers", "Content-Type");
//                log.info("Add header");
            }
        }
    }

    public void doLogoutFacebook() {

    }

    @SuppressWarnings("unused")
    public void doLogout() {
        if (this.logged) {
            try {
                log.info("Log out");
                this.logged = false;
                this.userid = "";
                this.password = "";
                this.user = null;
                FacesContext facesContext = FacesContext.getCurrentInstance();
                Global.loadConfig();

                facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/login.html");

            } catch (IOException ex) {
                log.error(ex);
            }
        }
    }
//    public void doLogout(){
//        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
//        if (this.logged) {
//            try {
//                this.logged=false;
//                this.username="";
//                this.password="";
//                this.user=null;
//                Map<String,Object> props = new HashMap<String, Object>();  
//                props.put("maxAge", 0);  
//                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("remember", "", props); // the cookie I want to overwrite (expire)  
//                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("userName", "", props); // random dummy cookie to see whether any is inserted
//                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("userPassword", "", props); // random dummy cookie to see whether any is inserted
//                context.redirect(context.getRequestContextPath());
//            } catch (IOException ex) {
//                //Logger.getLogger(homebean.class.getName()).log(Level.SEVERE, null, ex);
//                //log.info(ex);
//            }
//        }        
//    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    public static UserSessionBean getUserSession() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            UserSessionBean user = (UserSessionBean) session.getAttribute("userSessionBean");
            //log.info("SysUserBean:" + user.toString());
            //log.info(user.getSysUser());
            return user;
        }
        return null;
    }

//    public void countryLocaleCodeChanged(ValueChangeEvent e) {
//
//        String newLocaleValue = e.getNewValue().toString();
//
//        //loop country map to compare the locale code
//        for (Map.Entry<String, Object> entry : countries.entrySet()) {
//
//            if (entry.getValue().toString().equals(newLocaleValue)) {
//                Locale local = new Locale((String) entry.getValue(), entry.getKey());
//                FacesContext.getCurrentInstance()
//                        .getViewRoot().setLocale(local);
//
//            }
//        }
//    }
    public String setVietnameaseLanguage() {
        Locale locale = new Locale("vi");
        this.localCode = "vi";
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        log.info("Set Vietnamease language");
        return null;
    }

    public String setEnglishLanguage() {
        Locale locale = new Locale("en");
        this.localCode = "en";
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        return null;
    }

    public String setLanguage(String lang) {
        log.info("Set Language");
        Locale locale = new Locale(lang);
        this.localCode = lang;
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        return null;
    }

    /**
     * @return the localCode
     */
    public String getLocalCode() {
        return localCode;
    }

    /**
     * @param localCode the localCode to set
     */
    public final void setLocalCode(String localCode) {
        this.localCode = localCode;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

//    /**
//     * @return the customer
//     */
//    public Client getCustomer() {
//        return customer;
//    }
//
//    /**
//     * @param customer the client to set
//     */
//    public void setCustomer(Client customer) {
//        this.customer = customer;
//    }
    /**
     * @return the access_token
     */
    public String getAccess_token() {
        return access_token;
    }

    /**
     * @param access_token the access_token to set
     */
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

}
