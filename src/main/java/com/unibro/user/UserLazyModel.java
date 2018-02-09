package com.unibro.user;

import com.unibro.user_group.User_group;
import com.unibro.user_group.User_groupDAO;
import com.unibro.utils.Global;
import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class UserLazyModel extends LazyDataModel<User> {

    final Logger logger = Logger.getLogger(this.getClass().getName());
    private String groupid;

    private List<User> datasources = new ArrayList<User>();

    private String getApplicationIdList() {
        String appid_list = Global.getConfigValue("app.applicationid_list");
        if (appid_list.equals("") || appid_list.equals("all")) {
            return null;
        }
        return appid_list + ",any";
    }

    public UserLazyModel(String groupid) {
        this.groupid = groupid;
        this.datasources = new ArrayList();
    }

    public UserLazyModel() {
        this.datasources = new ArrayList();
    }

    public UserLazyModel(ArrayList<User> datasources) {
        this.datasources = datasources;
    }

    public List<User> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<User> datasources) {
        this.datasources = datasources;
    }

    @Override
    public User getRowData(String rowKey) {
        for (User obj : datasources) {
            if (obj.getUserid().toString().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(User obj) {
        return obj.getUserid().toString();
    }

    @Override
    public List<User> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        UserDAO dao = new UserDAO();
        if (filters == null || filters.isEmpty()) {
            int sort = 0;
            if (sortOrder.equals(SortOrder.ASCENDING)) {
                sort = 1;
            }
            if (sortOrder.equals(SortOrder.DESCENDING)) {
                sort = -1;
            }
            List filter = new ArrayList();
            if (this.getApplicationIdList() != null) {
                RequestFilter rf = new RequestFilter();
                rf.setName("application_id");
                rf.setRequired(true);
                rf.setType(RequestFilter.CONTAIN);
                rf.setFunction("");
                rf.setValue(this.getApplicationIdList());

                filter.add(rf);
            }
            if (groupid != null && !groupid.equals("")) {
                User_groupDAO dao1 = new User_groupDAO();
                List<User_group> list = dao1.loadUserByGroupId(groupid);
                if (list != null && !list.isEmpty()) {
                    ArrayList user_id_list = new ArrayList();
                    for (User_group u_g : list) {
                        user_id_list.add(u_g.getUserid());
                    }
                    RequestFilter rf = new RequestFilter();
                    rf.setName("userid");
                    rf.setRequired(true);
                    rf.setType(RequestFilter.IN);
                    rf.setFunction("");
                    rf.setValue(user_id_list);
                    filter.add(rf);
                }
            }
            this.datasources = dao.load(first, pageSize, sortField, sort, filter);
            this.setRowCount(Long.valueOf(dao.getTotalObject(filter)).intValue());
            return datasources;
        } else {
            int sort = 0;
            if (sortOrder.equals(SortOrder.ASCENDING)) {
                sort = 1;
            }
            if (sortOrder.equals(SortOrder.DESCENDING)) {
                sort = -1;
            }
            List filter = RequestFilter.fromHashMap(filters);
            if (this.getApplicationIdList() != null) {
                RequestFilter rf = new RequestFilter();
                rf.setName("application_id");
                rf.setRequired(true);
                rf.setType(RequestFilter.CONTAIN);
                rf.setFunction("");
                rf.setValue(this.getApplicationIdList());
                filter.add(rf);
            }
            if (groupid != null && !groupid.equals("")) {
                User_groupDAO dao1 = new User_groupDAO();
                List<User_group> list = dao1.loadUserByGroupId(groupid);
                if (list != null && !list.isEmpty()) {
                    ArrayList user_id_list = new ArrayList();
                    for (User_group u_g : list) {
                        user_id_list.add(u_g.getUserid());
                    }
                    RequestFilter rf = new RequestFilter();
                    rf.setName("userid");
                    rf.setRequired(true);
                    rf.setType(RequestFilter.IN);
                    rf.setFunction("");
                    rf.setValue(user_id_list);
                    filter.add(rf);
                }
            }
            this.datasources = dao.load(first, pageSize, sortField, sort, filter);
            this.setRowCount(Long.valueOf(dao.getTotalObject(filter)).intValue());
            return datasources;
        }
    }

}
