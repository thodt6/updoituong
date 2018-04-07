package com.unibro.fb_cookie;

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
public class Fb_cookieDAO {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    public Fb_cookieDAO() {
        //super(); 
        //this.setConnectionPool(Global.getDbConnectionPool());
    }

    public long getTotalObject(List<RequestFilter> filters) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.FB_PHONE.API_ADDRESS") + "/fb_cookie/datasize");
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

    public Fb_cookie getObjectByKey(String id) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.FB_PHONE.API_ADDRESS") + "/fb_cookie/" + id);
        JsonObject data = client.executeGetQuery(null);
        if (data == null) {
            return null;
        } else {
            if (data.get("code").getAsInt() == 200) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Fb_cookie.class);
            } else {
                return null;
            }
        }
    }

    public List<Fb_cookie> load(int first, int size, String sortfield, int sortorder, List<RequestFilter> filters) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.FB_PHONE.API_ADDRESS") + "/fb_cookie/filter/" + first + "/" + size + "/" + sortfield + "/" + sortorder);
        JsonObject data = client.executePostQuery(filters);
        if (data == null) {
            return null;
        } else {
            if (data.get("code").getAsInt() == 200) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonArray().toString(), new TypeToken<ArrayList<Fb_cookie>>() {
                }.getType());
            } else {
                return null;
            }
        }
    }

    public Fb_cookie create(Fb_cookie obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.FB_PHONE.API_ADDRESS") + "/fb_cookie");
        JsonObject data = client.executePostQuery(obj);
        if (data == null) {
            return null;
        } else {
            if (data.get("code").getAsInt() == 200) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Fb_cookie.class);
            } else {
                return null;
            }
        }
    }

    public int update_state(int start, int stop, String state) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.FB_PHONE.API_ADDRESS") + "/update_state/" + start + "/" + stop + "/" + state);
        JsonObject data = client.executePostQuery(null);
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

    public int create(List<Fb_cookie> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        //Write your code here   
        return 0;
    }

    public Fb_cookie edit(Fb_cookie obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.FB_PHONE.API_ADDRESS") + "/fb_cookie");
        JsonObject data = client.executePutQuery(obj);
        if (data == null) {
            return null;
        } else {
            if (data.get("code").getAsInt() == 200) {
                Gson gson = Global.getGsonObject("yyyy-MM-dd'T'HH:mm:ss.Z");
                return gson.fromJson(data.get("result").getAsJsonObject().toString(), Fb_cookie.class);
            } else {
                return null;
            }
        }
    }

    public int delete(Fb_cookie obj) {
        //Write your code here   
        ApiClient client = new ApiClient(Global.getConfigValue("APP.FB_PHONE.API_ADDRESS") + "/fb_cookie/" + obj.getFbid());
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

    public int delete(List<Fb_cookie> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        //Write your code here   
        return 0;
    }
}
