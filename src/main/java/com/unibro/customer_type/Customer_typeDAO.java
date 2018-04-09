package com.unibro.customer_type;

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
public class Customer_typeDAO {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    public Customer_typeDAO() {
        //super(); 
        //this.setConnectionPool(Global.getDbConnectionPool());
    }

    public long getTotalObject(List<RequestFilter> filters) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.ATP.API_ADDRESS") + "/customer_type/datasize");
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

    public Customer_type getObjectByKey(String id) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.ATP.API_ADDRESS") + "/customer_type/" + id);
        JsonObject data = client.executeGetQuery(null);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200) {
                Gson gson = Global.getGsonObject();
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Customer_type.class);
            } else {
                return null;
            }
        }
    }

    public List<Customer_type> load(int first, int size, String sortfield, int sortorder, List<RequestFilter> filters) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.ATP.API_ADDRESS") + "/customer_type/filter/" + first + "/" + size + "/" + sortfield + "/" + sortorder);
        JsonObject data = client.executePostQuery(filters);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200) {
                Gson gson = Global.getGsonObject();
                return gson.fromJson(data.get("result").getAsJsonArray().toString(), new TypeToken<ArrayList<Customer_type>>() {
                }.getType());
            } else {
                return null;
            }
        }
    }

    public Customer_type create(Customer_type obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.ATP.API_ADDRESS") + "/customer_type");
        JsonObject data = client.executePostQuery(obj);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200) {
                Gson gson = Global.getGsonObject();
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Customer_type.class);
            } else {
                return null;
            }
        }
    }

    public int create(List<Customer_type> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        //Write your code here   
        return 0;
    }

    public Customer_type edit(Customer_type obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.ATP.API_ADDRESS") + "/customer_type");
        JsonObject data = client.executePutQuery(obj);
        if (data == null) {
            return null;
        } else {
            if (data.get("status").getAsInt() == 200) {
                Gson gson = Global.getGsonObject();
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Customer_type.class);
            } else {
                return null;
            }
        }
    }

    public int delete(Customer_type obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.ATP.API_ADDRESS") + "/customer_type/" + obj.getUniqueKey());
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

    public int delete(List<Customer_type> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        //Write your code here   
        return 0;
    }
}
