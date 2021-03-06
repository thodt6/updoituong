package com.unibro.appclient;

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
public class AppclientDAO {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    public AppclientDAO() {
        //super(); 
        //this.setConnectionPool(Global.getDbConnectionPool());
    }

    public long getTotalObject(List<RequestFilter> filters) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.FB_PHONE.API_ADDRESS") + "/appclient/datasize");
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

    public Appclient getObjectByKey(String id) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.FB_PHONE.API_ADDRESS") + "/appclient/" + id);
        JsonObject data = client.executeGetQuery(null);
        if (data == null) {
            return null;
        } else {
            if (data.get("code").getAsInt() == 200) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Appclient.class);
            } else {
                return null;
            }
        }
    }

    public List<Appclient> load(int first, int size, String sortfield, int sortorder, List<RequestFilter> filters) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.FB_PHONE.API_ADDRESS") + "/appclient/filter/" + first + "/" + size + "/" + sortfield + "/" + sortorder);
        JsonObject data = client.executePostQuery(filters);
        if (data == null) {
            return null;
        } else {
            if (data.get("code").getAsInt() == 200) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonArray().toString(), new TypeToken<ArrayList<Appclient>>() {
                }.getType());
            } else {
                return null;
            }
        }
    }

    public Appclient create(Appclient obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.FB_PHONE.API_ADDRESS") + "/appclient");
        JsonObject data = client.executePostQuery(obj);
        if (data == null) {
            return null;
        } else {
            if (data.get("code").getAsInt() == 200) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Appclient.class);
            } else {
                return null;
            }
        }
    }

    public int create(List<Appclient> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        //Write your code here   
        return 0;
    }

    public Appclient edit(Appclient obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.FB_PHONE.API_ADDRESS") + "/appclient");
        JsonObject data = client.executePutQuery(obj);
        if (data == null) {
            return null;
        } else {
            if (data.get("code").getAsInt() == 200) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Appclient.class);
            } else {
                return null;
            }
        }
    }

    public int delete(Appclient obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.FB_PHONE.API_ADDRESS") + "/appclient/" + obj.getClientid());
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

    public int delete(List<Appclient> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        //Write your code here   
        return 0;
    }
}
