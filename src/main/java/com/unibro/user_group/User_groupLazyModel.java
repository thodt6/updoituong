package com.unibro.user_group;

import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class User_groupLazyModel extends LazyDataModel<User_group> {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<User_group> datasources = new ArrayList<User_group>();

    public User_groupLazyModel() {
        this.datasources = new ArrayList();
    }

    public User_groupLazyModel(ArrayList<User_group> datasources) {
        this.datasources = datasources;
    }

    public List<User_group> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<User_group> datasources) {
        this.datasources = datasources;
    }

    @Override
    public User_group getRowData(String rowKey) {
        for (User_group obj : datasources) {
            if (obj.getUserid().toString().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(User_group obj) {
        return obj.getUserid().toString();
    }

    @Override
    public List<User_group> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        User_groupDAO dao = new User_groupDAO();
        if (filters == null || filters.isEmpty()) {
            int sort = 0;
            if (sortOrder.equals(SortOrder.ASCENDING)) {
                sort = 1;
            }
            if (sortOrder.equals(SortOrder.DESCENDING)) {
                sort = -1;
            }
            this.datasources = dao.load(first, pageSize, sortField, sort, new ArrayList());
            this.setRowCount(Long.valueOf(dao.getTotalObject(new ArrayList())).intValue());
            return datasources;
        } else {
            int sort = 0;
            if (sortOrder.equals(SortOrder.ASCENDING)) {
                sort = 1;
            }
            if (sortOrder.equals(SortOrder.DESCENDING)) {
                sort = -1;
            }
            this.datasources = dao.load(first, pageSize, sortField, sort, RequestFilter.fromHashMap(filters));
            this.setRowCount(Long.valueOf(dao.getTotalObject(RequestFilter.fromHashMap(filters))).intValue());
            return datasources;
        }
    }

}
