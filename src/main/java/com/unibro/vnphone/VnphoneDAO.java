package com.unibro.vnphone;

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
public class VnphoneDAO {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    public VnphoneDAO() {
        //super(); 
        //this.setConnectionPool(Global.getDbConnectionPool());
    }

    public long getTotalObject(List<RequestFilter> filters) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.ATP.API_ADDRESS") + "/vnphone/datasize");
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

    public Vnphone getObjectByKey(String id) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.ATP.API_ADDRESS") + "/vnphone/" + id);
        JsonObject data = client.executeGetQuery(null);
        if (data == null) {
            return null;
        } else {
            if (data.get("code").getAsInt() == 200) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Vnphone.class);
            } else {
                return null;
            }
        }
    }

    public List<Vnphone> load(int first, int size, String sortfield, int sortorder, List<RequestFilter> filters) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.ATP.API_ADDRESS") + "/vnphone/filter/" + first + "/" + size + "/" + sortfield + "/" + sortorder);
        JsonObject data = client.executePostQuery(filters);
        if (data == null) {
            return null;
        } else {
            if (data.get("code").getAsInt() == 200) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonArray().toString(), new TypeToken<ArrayList<Vnphone>>() {
                }.getType());
            } else {
                return null;
            }
        }
    }
   

    public Vnphone create(Vnphone obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.ATP.API_ADDRESS") + "/vnphone");
        JsonObject data = client.executePostQuery(obj);
        if (data == null) {
            return null;
        } else {
            if (data.get("code").getAsInt() == 200) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Vnphone.class);
            } else {
                return null;
            }
        }
    }

    public int create(List<Vnphone> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        //Write your code here   
        return 0;
    }

    public Vnphone edit(Vnphone obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.ATP.API_ADDRESS") + "/vnphone");
        JsonObject data = client.executePutQuery(obj);
        if (data == null) {
            return null;
        } else {
            if (data.get("code").getAsInt() == 200) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Vnphone.class);
            } else {
                return null;
            }
        }
    }

    public int delete(Vnphone obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.ATP.API_ADDRESS") + "/vnphone/" + obj.getId());
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

    public int delete(List<Vnphone> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        //Write your code here   
        return 0;
    }
}
