package com.unibro.client_topup;

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
public class Client_topupDAO {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    public Client_topupDAO() {
        //super(); 
        //this.setConnectionPool(Global.getDbConnectionPool());
    }

    public long getTotalObject(List<RequestFilter> filters) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.API_ADDRESS") + "/client_topup/datasize");
        JsonObject data = client.executePostQuery(filters);
        if (data == null) {
            return 0;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result") != null && !data.get("result").toString().equals("null")) {
                return data.get("result").getAsLong();
            } else {
                return 0;
            }
        }
    }

    public Client_topup getObjectByKey(String id) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.API_ADDRESS") + "/client_topup/" + id);
        JsonObject data = client.executeGetQuery(null);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result") != null && !data.get("result").toString().equals("null")) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Client_topup.class);
            } else {
                return null;
            }
        }
    }

    public List<Client_topup> load(int first, int size, String sortfield, int sortorder, List<RequestFilter> filters) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.API_ADDRESS") + "/client_topup/filter/" + first + "/" + size + "/" + sortfield + "/" + sortorder);
        JsonObject data = client.executePostQuery(filters);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result") != null && !data.get("result").toString().equals("null")) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonArray().toString(), new TypeToken<ArrayList<Client_topup>>() {
                }.getType());
            } else {
                return null;
            }
        }
    }

    public Client_topup create(Client_topup obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.API_ADDRESS") + "/client_topup");
        JsonObject data = client.executePostQuery(obj);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result") != null && !data.get("result").toString().equals("null")) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Client_topup.class);
            } else {
                return null;
            }
        }
    }

    public int create(List<Client_topup> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        //Write your code here   
        return 0;
    }

    public Client_topup edit(Client_topup obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.API_ADDRESS") + "/client_topup");
        JsonObject data = client.executePutQuery(obj);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result") != null && !data.get("result").toString().equals("null")) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Client_topup.class);
            } else {
                return null;
            }
        }
    }

    public int delete(Client_topup obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.API_ADDRESS") + "/client_topup/" + obj.getTopup_id());
        JsonObject data = client.executeDeleteQuery();
        if (data == null) {
            return 0;
        } else {
            if (data.get("status").getAsInt() == 200 && data.get("result") != null && !data.get("result").toString().equals("null")) {
                return data.get("result").getAsInt();
            } else {
                return 0;
            }
        }
    }

    public int delete(List<Client_topup> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        //Write your code here   
        return 0;
    }
}
