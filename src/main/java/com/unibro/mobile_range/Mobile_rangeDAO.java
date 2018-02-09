package com.unibro.mobile_range;

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
public class Mobile_rangeDAO {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    public Mobile_rangeDAO() {
        //super(); 
        //this.setConnectionPool(Global.getDbConnectionPool());
    }

    public long getTotalObject(List<RequestFilter> filters) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.FB_PHONE.API_ADDRESS") + "/mobile_range/datasize");
        JsonObject data = client.executePostQuery(filters);
        if (data == null) {
            return 0;
        } else {
            if (data.get("code").getAsInt() == 200) {
                return data.get("result").getAsLong();
            } else {
                return 0;
            }
        }
    }

    public Mobile_range getObjectByKey(String id) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.FB_PHONE.API_ADDRESS") + "/mobile_range/" + id);
        JsonObject data = client.executeGetQuery(null);
        if (data == null) {
            return null;
        } else {
            if (data.get("code").getAsInt() == 200) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Mobile_range.class);
            } else {
                return null;
            }
        }
    }

    public List<Mobile_range> load(int first, int size, String sortfield, int sortorder, List<RequestFilter> filters) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.FB_PHONE.API_ADDRESS") + "/mobile_range/filter/" + first + "/" + size + "/" + sortfield + "/" + sortorder);
        JsonObject data = client.executePostQuery(filters);
        if (data == null) {
            return null;
        } else {
            if (data.get("code").getAsInt() == 200) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonArray().toString(), new TypeToken<ArrayList<Mobile_range>>() {
                }.getType());
            } else {
                return null;
            }
        }
    }

    public Mobile_range create(Mobile_range obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.FB_PHONE.API_ADDRESS") + "/mobile_range");
        JsonObject data = client.executePostQuery(obj);
        if (data == null) {
            return null;
        } else {
            if (data.get("code").getAsInt() == 200) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Mobile_range.class);
            } else {
                return null;
            }
        }
    }

    public int createTask(Mobile_range obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.FB_PHONE.API_ADDRESS") + "/mobile_range/create_task");
        JsonObject data = client.executePostQuery(obj);
        if (data == null) {
            return -1;
        } else {
            if (data.get("code").getAsInt() == 200) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return data.get("result").getAsInt();
            } else {
                return -1;
            }
        }
    }

    public int create(List<Mobile_range> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        //Write your code here   
        return 0;
    }

    public Mobile_range edit(Mobile_range obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.FB_PHONE.API_ADDRESS") + "/mobile_range");
        JsonObject data = client.executePutQuery(obj);
        if (data == null) {
            return null;
        } else {
            if (data.get("code").getAsInt() == 200) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Mobile_range.class);
            } else {
                return null;
            }
        }
    }

    public int delete(Mobile_range obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.FB_PHONE.API_ADDRESS") + "/mobile_range/" + obj.getRange_id());
        JsonObject data = client.executeDeleteQuery();
        if (data == null) {
            return 0;
        } else {
            if (data.get("code").getAsInt() == 200) {
                return data.get("result").getAsInt();
            } else {
                return 0;
            }
        }
    }

    public int delete(List<Mobile_range> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        //Write your code here   
        return 0;
    }
}
