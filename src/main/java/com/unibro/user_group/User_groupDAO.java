package com.unibro.user_group;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.unibro.utils.ApiClient;
import com.unibro.utils.Global;
import com.unibro.utils.RequestFilter;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Nguyen Duc Tho
 */
public class User_groupDAO {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    public User_groupDAO() {
        //super(); 
        //this.setConnectionPool(Global.getDbConnectionPool());
    }

    public long getTotalObject(List<RequestFilter> filters) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.USER.API_ADDRESS") + "/user_group/datasize");
        JsonObject data = client.executePostQuery(filters);
        if (data == null) {
            return 0;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result")!=null && !data.get("result").toString().equals("null")) {
                return data.get("result").getAsLong();
            } else {
                return 0;
            }
        }
    }

    public List<User_group> loadUserByGroupId(String groupid) {
        List filter = new ArrayList();
        RequestFilter rf = new RequestFilter();
        rf.setFunction("");
        rf.setName("groupid_list");
        rf.setRequired(true);
        rf.setType(RequestFilter.REGEX);
        rf.setValue(".*_" + groupid + "_.*");
        filter.add(rf);
        return this.load(0, 0, "userid", 1, filter);
    }

    public User_group getObjectByKey(String id) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.USER.API_ADDRESS") + "/user_group/" + id);
        JsonObject data = client.executeGetQuery(null);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result")!=null && !data.get("result").toString().equals("null")) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                JsonElement element = data.get("result");
                if (element == null || element.isJsonNull()) {
                    return null;
                }
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), User_group.class);
            } else {
                return null;
            }
        }
    }

    public List<User_group> load(int first, int size, String sortfield, int sortorder, List<RequestFilter> filters) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.USER.API_ADDRESS") + "/user_group/filter/" + first + "/" + size + "/" + sortfield + "/" + sortorder);
        JsonObject data = client.executePostQuery(filters);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result")!=null && !data.get("result").toString().equals("null")) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonArray().toString(), new TypeToken<ArrayList<User_group>>() {
                }.getType());
            } else {
                return null;
            }
        }
    }

    public User_group create(User_group obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.USER.API_ADDRESS") + "/user_group");
        JsonObject data = client.executePostQuery(obj);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result")!=null && !data.get("result").toString().equals("null")) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), User_group.class);
            } else {
                return null;
            }
        }
    }

    public int create(List<User_group> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        //Write your code here   
        return 0;
    }

    public User_group edit(User_group obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.USER.API_ADDRESS") + "/user_group");
        JsonObject data = client.executePutQuery(obj);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result")!=null && !data.get("result").toString().equals("null")) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), User_group.class);
            } else {
                return null;
            }
        }
    }

    public int delete(User_group obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.USER.API_ADDRESS") + "/user_group/" + obj.getUserid());
        JsonObject data = client.executeDeleteQuery();
        if (data == null) {
            return 0;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result")!=null && !data.get("result").toString().equals("null")) {
                return data.get("result").getAsInt();
            } else {
                return 0;
            }
        }
    }

    public int delete(List<User_group> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        //Write your code here   
        return 0;
    }
}
