package com.unibro.application;

import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class ApplicationLazyModel extends LazyDataModel<Application> {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<Application> datasources = new ArrayList<Application>();   
    

    public ApplicationLazyModel() {
        this.datasources = new ArrayList();
    }

    public ApplicationLazyModel(ArrayList<Application> datasources) {
        this.datasources = datasources;
    }

    public List<Application> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<Application> datasources) {
        this.datasources = datasources;
    }

    @Override
    public Application getRowData(String rowKey) {
        for (Application obj : datasources) {
            if (obj.getAppid().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(Application obj) {
        return obj.getAppid();
    }

    @Override
    public List<Application> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        ApplicationDAO dao = new ApplicationDAO();
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
