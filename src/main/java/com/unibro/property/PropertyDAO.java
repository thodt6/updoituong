package com.unibro.property;

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
public class PropertyDAO {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    public PropertyDAO() {
        //super(); 
        //this.setConnectionPool(Global.getDbConnectionPool());
    }

    public long getTotalObject(List<RequestFilter> filters) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.USER.API_ADDRESS") + "/property/datasize");
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

    public Property getObjectByKey(String id) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.USER.API_ADDRESS") + "/property/" + id);
        JsonObject data = client.executeGetQuery(null);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result")!=null && !data.get("result").toString().equals("null")) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Property.class);
            } else {
                return null;
            }
        }
    }

    public List<Property> load(int first, int size, String sortfield, int sortorder, List<RequestFilter> filters) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.USER.API_ADDRESS") + "/property/filter/" + first + "/" + size + "/" + sortfield + "/" + sortorder);
        JsonObject data = client.executePostQuery(filters);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result")!=null && !data.get("result").toString().equals("null")) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonArray().toString(), new TypeToken<ArrayList<Property>>() {
                }.getType());
            } else {
                return null;
            }
        }
    }

    public Property create(Property obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.USER.API_ADDRESS") + "/property");
        JsonObject data = client.executePostQuery(obj);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result")!=null && !data.get("result").toString().equals("null")) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Property.class);
            } else {
                return null;
            }
        }
    }

    public int create(List<Property> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        //Write your code here   
        return 0;
    }

    public Property edit(Property obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.USER.API_ADDRESS") + "/property");
        JsonObject data = client.executePutQuery(obj);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result")!=null && !data.get("result").toString().equals("null")) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Property.class);
            } else {
                return null;
            }
        }
    }

    public int delete(Property obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.USER.API_ADDRESS") + "/property/" + obj.getProp_id());
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

    public int delete(List<Property> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        //Write your code here   
        return 0;
    }
}
