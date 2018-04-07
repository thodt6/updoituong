package com.unibro.facebook_group;

import com.google.gson.Gson;
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
public class Facebook_groupDAO {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    public Facebook_groupDAO() {
        //super(); 
        //this.setConnectionPool(Global.getDbConnectionPool());
    }

    public long getTotalObject(List<RequestFilter> filters) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.ATP.API_ADDRESS") + "/facebook_group/datasize");
        JsonObject data = client.executePostQuery(filters);
        if (data == null) {
            return 0;
        } else {
            if (data.get("status").getAsInt() == 200) {
                return data.get("result").getAsLong();
            } else {
                return 0;
            }
        }
    }

    public Facebook_group getObjectByKey(String id) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.ATP.API_ADDRESS") + "/facebook_group/" + id);
        JsonObject data = client.executeGetQuery(null);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200) {
                Gson gson = Global.getGsonObject();
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Facebook_group.class);
            } else {
                return null;
            }
        }
    }

    public List<Facebook_group> load(int first, int size, String sortfield, int sortorder, List<RequestFilter> filters) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.ATP.API_ADDRESS") + "/facebook_group/filter/" + first + "/" + size + "/" + sortfield + "/" + sortorder);
        JsonObject data = client.executePostQuery(filters);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200) {
                Gson gson = Global.getGsonObject();
                return gson.fromJson(data.get("result").getAsJsonArray().toString(), new TypeToken<ArrayList<Facebook_group>>() {
                }.getType());
            } else {
                return null;
            }
        }
    }

    public Facebook_group create(Facebook_group obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.ATP.API_ADDRESS") + "/facebook_group");
        JsonObject data = client.executePostQuery(obj);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200) {
                Gson gson = Global.getGsonObject();
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Facebook_group.class);
            } else {
                return null;
            }
        }
    }

    public int create(List<Facebook_group> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        //Write your code here   
        return 0;
    }

    public Facebook_group edit(Facebook_group obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.ATP.API_ADDRESS") + "/facebook_group");
        JsonObject data = client.executePutQuery(obj);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200) {
                Gson gson = Global.getGsonObject();
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Facebook_group.class);
            } else {
                return null;
            }
        }
    }

    public int delete(Facebook_group obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.ATP.API_ADDRESS") + "/facebook_group/" + obj.getUniqueKey());
        JsonObject data = client.executeDeleteQuery();
        if (data == null) {
            return 0;
        } else {
            if (data.get("status").getAsInt() == 200) {
                return data.get("result").getAsInt();
            } else {
                return 0;
            }
        }
    }

    public int delete(List<Facebook_group> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        //Write your code here   
        return 0;
    }
}
