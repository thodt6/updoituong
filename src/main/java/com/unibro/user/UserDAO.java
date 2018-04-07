package com.unibro.user;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
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
public class UserDAO {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    public UserDAO() {
        //super(); 
        //this.setConnectionPool(Global.getDbConnectionPool());
    }

    public long getTotalObject(List<RequestFilter> filters) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.USER.API_ADDRESS") + "/user/datasize");
        JsonObject data = client.executePostQuery(filters);
        if (data == null) {
            return 0;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result")!=null && !data.get("result").toString().equals("null")) {
                JsonElement result = data.get("result");
                if (result == null || (result instanceof JsonNull)) {
                    return 0;
                }
                return data.get("result").getAsLong();
            } else {
                return 0;
            }
        }
    }

    public User getObjectByKey(String id) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.USER.API_ADDRESS") + "/user/" + id);
        JsonObject data = client.executeGetQuery(null);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result")!=null && !data.get("result").toString().equals("null")) {
                JsonElement result = data.get("result");
                if (result == null || (result instanceof JsonNull)) {
                    return null;
                }
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), User.class);
            } else {
                return null;
            }
        }
    }

    public User getUserByField(String field, String value) {
        List filter = new ArrayList();
        RequestFilter rf = new RequestFilter();
        rf.setName(field);
        rf.setRequired(true);
        rf.setType(RequestFilter.EQUAL);
        rf.setValue(value);
        filter.add(rf);
        List<User> list_user = this.load(0, -1, "id", 1, filter);
        if (list_user != null && !list_user.isEmpty()) {
            return list_user.get(0);
        }
        return null;
    }

    public List<User> load(int first, int size, String sortfield, int sortorder, List<RequestFilter> filters) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.USER.API_ADDRESS") + "/user/filter/" + first + "/" + size + "/" + sortfield + "/" + sortorder);
        JsonObject data = client.executePostQuery(filters);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result")!=null && !data.get("result").toString().equals("null")) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                JsonElement result = data.get("result");
                if (result != null && !(result instanceof JsonNull)) {
                    return gson.fromJson(data.get("result").getAsJsonArray().toString(), new TypeToken<ArrayList<User>>() {
                    }.getType());
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    public User create(User obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.USER.API_ADDRESS") + "/user");
        JsonObject data = client.executePostQuery(obj);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result")!=null && !data.get("result").toString().equals("null")) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), User.class);
            } else {
                return null;
            }
        }
    }

    public int create(List<User> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        //Write your code here   
        return 0;
    }

    public User edit(User obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.USER.API_ADDRESS") + "/user");
        JsonObject data = client.executePutQuery(obj);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result")!=null && !data.get("result").toString().equals("null")) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), User.class);
            } else {
                return null;
            }
        }
    }

    public int delete(User obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.USER.API_ADDRESS") + "/user/" + obj.getUserid());
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

    public int delete(List<User> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        //Write your code here   
        return 0;
    }
}
