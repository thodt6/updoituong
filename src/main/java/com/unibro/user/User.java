/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibro.user;

import com.google.gson.JsonObject;
import com.unibro.group.Group;
import com.unibro.group.GroupDAO;
import com.unibro.sysuser.UserSessionBean;
import com.unibro.user_group.User_group;
import com.unibro.user_group.User_groupDAO;
import com.unibro.utils.Global;
import com.unibro.utils.RequestFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author THOND
 */
public class User extends BaseUser implements Converter {

    public Object getAsObject(FacesContext fc, UIComponent uic, String submittedValue) {
        if (submittedValue.trim().equals("")) {
            return null;
        }
        String id;
        try {
            id = String.valueOf(submittedValue);
        } catch (Exception ex) {
            id = null;
        }
        UserDAO dao = new UserDAO();
        User ret = dao.getObjectByKey(id);
        return ret;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value.equals("")) {
            return "";
        }
        return String.valueOf(((User) value).getUserid());
    }

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile f = event.getFile();
        String filename = f.getFileName();
        String tail = Global.getTailFile(filename);
        filename = Global.getRandomString() + "." + tail;
        File folder = new File(Global.getConfigValue("FILE_ROOT_PATH") + "/user_picture/" + this.getUid());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File saveFile = Global.getNewFileName(new File(folder.getAbsolutePath() + "/" + filename));
        try {
            final int BUFFER_SIZE = 1024;
            FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
            byte[] buffer = new byte[BUFFER_SIZE];
            int bulk;
            InputStream inputStream = f.getInputstream();
            while (true) {
                bulk = inputStream.read(buffer);
                if (bulk < 0) {
                    break;
                }
                fileOutputStream.write(buffer, 0, bulk);
                fileOutputStream.flush();
            }
            fileOutputStream.close();
            inputStream.close();
            if (this.getUid().equals("")) {
                this.setPicture("user_picture/" + filename);
            } else {
                this.setPicture("user_picture/" + this.getUid() + "/" + filename);
            }
        } catch (IOException ex) {
        }
    }

    public String getFullPictureUrl() {
        if (this.getPicture().startsWith("http")) {
            return this.getPicture();
        }
        return Global.getConfigValue("FILE_HTTP_PATH") + "/" + this.getPicture();
    }

    private transient String password;
    private transient int default_groupid = 0;

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

    public void updatePassword() {
        if (!password.equals("")) {
            this.setPassword_hash(Global.MD5(password));
            UserDAO dao = new UserDAO();
            if (dao.edit(this) != null) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, Global.getResourceLanguage("general.operationSuccess"), "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, Global.getResourceLanguage("general.operationFail"), "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, Global.getResourceLanguage("general.operationFail"), "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void doUpdate() {
        UserDAO dao = new UserDAO();
        if (dao.edit(this) != null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, Global.getResourceLanguage("general.operationSuccess"), "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, Global.getResourceLanguage("general.operationFail"), "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    private transient List<Group> groupList;

    private Integer getDefaultGroupId(String groupid_list) {
        this.transientlogger.info("Group ID List:" + groupid_list);
        if (groupid_list == null || groupid_list.equals("")) {
            return 0;
        }
        String[] list = groupid_list.split("\\|");
        if (list != null && list.length > 0) {
            for (String app_default_gid : list) {
                if (app_default_gid.contains(Global.getConfigValue("app.applicationid_list"))) {
                    String[] s_arr = app_default_gid.split("_");
                    if (s_arr != null && s_arr.length >= 2) {
                        return Integer.valueOf(s_arr[1]);
                    }
                }
            }
        }
        return 0;
    }

    public void loadGroupList() {
        User_groupDAO dao = new User_groupDAO();
        User_group u_g = dao.getObjectByKey(this.getUserid().toString());
        if (u_g == null) {
            this.groupList = new ArrayList();
            this.setDefault_groupid(0);
            return;
        }
        this.default_groupid = this.getDefaultGroupId(u_g.getDefault_groupid());
        String group_list = u_g.getGroupid_list();
        if (group_list.equals("") || group_list.isEmpty()) {
            this.groupList = new ArrayList();
            return;
        }
        String[] group_list_arr = group_list.substring(1).split("_");
        List<String> list = new ArrayList();
        Collections.addAll(list, group_list_arr);
        GroupDAO dao1 = new GroupDAO();
        List<RequestFilter> filter = new ArrayList();
        RequestFilter rf = new RequestFilter();
        rf.setName("groupid");
        rf.setRequired(true);
        rf.setType(RequestFilter.IN);
        rf.setValue(list);
        filter.add(rf);
        List<Group> listAllGroup = dao1.load(0, -1, "groupid", 1, filter);
        groupList = new ArrayList();
        if (listAllGroup != null && !listAllGroup.isEmpty()) {
            for (Group g : listAllGroup) {
                if (g.getApplication_id().equals(Global.getConfigValue("app.applicationid_list"))) {
                    groupList.add(g);
                }
            }
        }
        transientlogger.info("Default Group ID:" + this.default_groupid);
    }

    transient Logger transientlogger = Logger.getLogger(this.getClass().getName());

    private String getRealGroupList(String current) {
        if (current == null || current.isEmpty()) {
            String ret = "";
            if (this.groupList != null && !this.groupList.isEmpty()) {
                for (Group g : groupList) {
                    ret += "_" + g.getGroupid();
                }
                ret += "_";
            }
            return ret;
        } else {
            if (this.groupList != null && !this.groupList.isEmpty()) {
                String ret = current;
                for (Group g : this.groupList) {
                    if (!current.contains("_" + g.getGroupid() + "_")) {
                        ret += g.getGroupid() + "_";
                    }
                }
                return ret;
            } else {
                return current;
            }
        }
    }

    public void saveGroupList() {
        String ret = "";
        this.groupList = this.dualGroupList.getTarget();
        User_groupDAO dao = new User_groupDAO();
        User_group u_g = dao.getObjectByKey(this.getUserid().toString());
        if (u_g == null) {
            u_g = new User_group();
            u_g.setCreated_id(UserSessionBean.getUserSession().getUser().getUserid());
            u_g.setGroupid_list(this.getRealGroupList(null));
            u_g.setUserid(this.getUserid());
            u_g.setUpdate_time(new java.util.Date());
            u_g.setDefault_groupid(Global.getConfigValue("app.applicationid_list") + "_" + this.getDefault_groupid());
            dao.create(u_g);
        } else {
            u_g.setCreated_id(UserSessionBean.getUserSession().getUser().getUserid());
            u_g.setGroupid_list(this.getRealGroupList(u_g.getGroupid_list()));
            u_g.setUserid(this.getUserid());
            u_g.setUpdate_time(new java.util.Date());
            u_g.setDefault_groupid(this.saveDefaultGroupid(u_g.getDefault_groupid()));
            dao.edit(u_g);
        }
    }

    private String saveDefaultGroupid(String groupid_list) {
        if (groupid_list == null || groupid_list.equals("")) {
            return Global.getConfigValue("app.applicationid_list") + "_" + this.getDefault_groupid();
        }
        String[] list = groupid_list.split("\\|");
        if (list != null && list.length > 0) {
            for (String app_default_gid : list) {
                if (app_default_gid.contains(Global.getConfigValue("app.applicationid_list"))) {
                    return groupid_list.replace(app_default_gid, Global.getConfigValue("app.applicationid_list") + "_" + this.getDefault_groupid());
                }
            }
            return groupid_list + "|" + Global.getConfigValue("app.applicationid_list") + "_" + this.getDefault_groupid();
        } else {
            return Global.getConfigValue("app.applicationid_list") + "_" + this.getDefault_groupid();
        }

    }

    /**
     * @return the groupList
     */
    public List<Group> getGroupList() {
//        this.loadGroupList();
        return groupList;
    }

    /**
     * @param groupList the groupList to set
     */
    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    /**
     * @return the default_groupid
     */
    public int getDefault_groupid() {
        return default_groupid;
    }

    /**
     * @param default_groupid the default_groupid to set
     */
    public void setDefault_groupid(int default_groupid) {
        this.default_groupid = default_groupid;
    }

    private transient DualListModel<Group> dualGroupList;

    /**
     * @return the dualGroupList
     */
    public DualListModel<Group> getDualGroupList() {
        this.loadGroupList();
        List<Group> source = Group.loadAllGroups();
        source.removeAll(this.groupList);
        this.dualGroupList = new DualListModel<Group>();
        this.dualGroupList.setSource(source);
        this.dualGroupList.setTarget(this.groupList);
        return dualGroupList;
    }

    /**
     * @param dualGroupList the dualGroupList to set
     */
    public void setDualGroupList(DualListModel<Group> dualGroupList) {
        this.dualGroupList = dualGroupList;
    }

    public Group getDefaultGroup() {
        if (this.groupList == null) {
             this.transientlogger.info("Group List null");
            return null;
        }
        this.transientlogger.info("Curren default id:" + this.default_groupid);
        for (Group g : this.groupList) {
            this.transientlogger.info("GID:" + g.getGroupid());
            if (this.default_groupid == g.getGroupid()) {
                return g;
            }
        }
        return null;
    }

    private static User getFacebookUser(String username) {
        RequestFilter ft1 = new RequestFilter();
        ft1.setName("uid");
        ft1.setRequired(Boolean.TRUE);
        ft1.setType(RequestFilter.EQUAL);
        ft1.setValue(username);

        RequestFilter ft2 = new RequestFilter();
        ft2.setName("username");
        ft2.setRequired(Boolean.TRUE);
        ft2.setType(RequestFilter.EQUAL);
        ft2.setValue(username);

        RequestFilter ft3 = new RequestFilter();
        ft3.setName("oauth_provider");
        ft3.setRequired(Boolean.TRUE);
        ft3.setType(RequestFilter.EQUAL);
        ft3.setValue("facebook");

        List<RequestFilter> list = new ArrayList();
        list.add(ft1);
        list.add(ft2);
        list.add(ft3);

        UserDAO dao = new UserDAO();
        List<User> ret = dao.load(0, 1, "uid", 0, list);
        if (ret != null && !ret.isEmpty()) {
            return ret.get(0);
        }
        return null;
    }

    private static User buildFacebookUser(JsonObject profile, String token, long expires) {
        if (profile != null) {
            if (profile.has("id")) {
                User u = new User();
                u.setUid(profile.get("id").getAsString());
                u.setUsername(profile.get("id").getAsString());
                u.setOauth_provider("facebook");
                u.setApplication_id(Global.getConfigValue("app.applicationid_list"));
                if (profile.has("locale")) {
                    u.setCountry_code(profile.get("locale").getAsString());
                }
                if (profile.has("first_name")) {
                    u.setFirst_name(profile.get("first_name").getAsString());
                } else {
                    if (profile.has("name")) {
                        u.setFirst_name(profile.get("name").getAsString());
                        u.setLast_name("");
                    }
                }
                if (profile.has("last_name")) {
                    u.setLast_name(profile.get("last_name").getAsString());
                }
                if (profile.has("email")) {
                    u.setEmail(profile.get("email").getAsString());
                }
                if (profile.has("gender")) {
                    u.setGender(profile.get("gender").getAsString());
                }
                if (profile.has("picture")) {
                    if (profile.get("picture").isJsonObject()) {
                        JsonObject picture = profile.get("picture").getAsJsonObject();
                        if (picture.has("data")) {
                            u.setPicture(picture.get("data").getAsJsonObject().get("url").getAsString());
                        }
                    } else {
                        u.setPicture(profile.get("picture").getAsString());
                    }
                }
                u.setLogin_token(token);
                u.setExpired_time(System.currentTimeMillis() + expires);
                return u;
            }
        }
        return null;
    }

    private static User insertNewFacebookUser(JsonObject profile, String token, long expires) {
        User u = User.buildFacebookUser(profile, token, expires);
        if (u != null) {
            UserDAO dao = new UserDAO();
            u.setStatus(1);
            u = dao.create(u);
            if (u != null) {
                //Add user to default facebook group
                User_group u_g = new User_group();
                u_g.setUserid(u.getUserid());
                u_g.setGroupid_list("_" + Global.getConfigValue("app.fb_group.id") + "_");
                u_g.setCreated_id(0);
                u_g.setDefault_groupid(Global.getConfigValue("app.applicationid_list") + "_" + Global.getConfigValue("app.fb_group.id"));
                User_groupDAO dao1 = new User_groupDAO();
                dao1.create(u_g);
            }
            return u;
        }
        return null;
    }

    private static User updateExistFacebookUser(User user, JsonObject profile, String token, long expires) {
        User u = User.buildFacebookUser(profile, token, expires);
        if (u != null) {
//            user.setCountry_code(u.getCountry_code());
//            user.setEmail(u.getEmail());
            user.setExpired_time(u.getExpired_time());
//            user.setFirst_name(u.getFirst_name());
//            user.setLast_name(u.getLast_name());
//            user.setGender(u.getGender());
//            user.setPicture(u.getPicture());
            user.setLogin_token(token);
            user.setStatus(1);
            if (user.getApplication_id() == null || user.getApplication_id().equals("")) {
                user.setApplication_id(Global.getConfigValue("app.applicationid_list"));
            } else {
                if (!user.getApplication_id().contains(Global.getConfigValue("app.applicationid_list"))) {
                    user.setApplication_id(user.getApplication_id() + "," + Global.getConfigValue("app.applicationid_list"));
                }
            }
            User_groupDAO dao1 = new User_groupDAO();
            User_group u_g = dao1.getObjectByKey(user.getUserid().toString());
            if (u_g == null) {
                u_g = new User_group();
                u_g.setUserid(u.getUserid());
                u_g.setGroupid_list("_" + Global.getConfigValue("app.fb_group.id") + "_");
                u_g.setCreated_id(0);
                u_g.setDefault_groupid(Global.getConfigValue("app.applicationid_list") + "_" + Global.getConfigValue("app.fb_group.id"));
                dao1.create(u_g);
            } else {
                if (u_g.getGroupid_list() == null || u_g.getGroupid_list().equals("")) {
                    u_g.setGroupid_list("_" + Global.getConfigValue("app.fb_group.id") + "_");
                } else {
                    if (!u_g.getGroupid_list().contains("_" + Global.getConfigValue("app.fb_group.id") + "_")) {
                        u_g.setGroupid_list(u_g.getGroupid_list() + Global.getConfigValue("app.fb_group.id") + "_");
                    }
                }
                if (u_g.getDefault_groupid() == null || u_g.getDefault_groupid().equals("")) {
                    u_g.setDefault_groupid(Global.getConfigValue("app.applicationid_list") + "_" + Global.getConfigValue("app.fb_group.id"));
                } else {
                    user.setDefault_groupid(Integer.valueOf(Global.getConfigValue("app.fb_group.id")));
                    String new_default_gid_list = user.saveDefaultGroupid(u_g.getDefault_groupid());
                    u_g.setDefault_groupid(new_default_gid_list);
                }
                dao1.edit(u_g);

            }

            UserDAO dao = new UserDAO();
            user = dao.edit(user);
            return user;
        }
        return null;
    }

    public static User signInByFacebook(JsonObject profile, String access_token, long expires) {
        if (profile != null) {
            if (profile.has("id")) {
                User u = getFacebookUser(profile.get("id").getAsString());
                if (u != null) {
                    return User.updateExistFacebookUser(u, profile, access_token, expires);
                } else {
                    return User.insertNewFacebookUser(profile, access_token, expires);
                }
            }
        }
        return null;
    }

    public String getGroupListStr() {
        String ret = "";
        if (this.groupList != null && !this.groupList.isEmpty()) {
            for (Group g : this.groupList) {
                ret += "," + g.getGroupid();
            }
        }
        if (ret.length() > 0) {
            ret = ret.substring(1);
        }
        return ret;
    }

    public String getFullname() {
        return this.getFirst_name() + " " + this.getLast_name();
    }
    
    public static User getObject(String id){
        UserDAO dao=new UserDAO();
        return dao.getObjectByKey(id);
    }
    public static User updateObject(User user){
        UserDAO dao=new UserDAO();
        return dao.edit(user);
    }

}
