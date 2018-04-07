package com.unibro.facebook_group;

import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class Facebook_groupLazyModel extends LazyDataModel<Facebook_group> {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<Facebook_group> datasources = new ArrayList<Facebook_group>();

    public Facebook_groupLazyModel() {
        this.datasources = new ArrayList();
    }

    public Facebook_groupLazyModel(ArrayList<Facebook_group> datasources) {
        this.datasources = datasources;
    }

    public List<Facebook_group> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<Facebook_group> datasources) {
        this.datasources = datasources;
    }

    @Override
    public Facebook_group getRowData(String rowKey) {
        for (Facebook_group obj : datasources) {
            if (obj.getUniqueKey().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(Facebook_group obj) {
        return obj.getUniqueKey();
    }

    @Override
    public List<Facebook_group> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        Facebook_groupDAO dao = new Facebook_groupDAO();
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
