package com.unibro.appclient;

import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class AppclientLazyModel extends LazyDataModel<Appclient> {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<Appclient> datasources = new ArrayList<Appclient>();

    public AppclientLazyModel() {
        this.datasources = new ArrayList();
    }

    public AppclientLazyModel(ArrayList<Appclient> datasources) {
        this.datasources = datasources;
    }

    public List<Appclient> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<Appclient> datasources) {
        this.datasources = datasources;
    }

    @Override
    public Appclient getRowData(String rowKey) {
        for (Appclient obj : datasources) {
            if (obj.getClientid().toString().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(Appclient obj) {
        return obj.getClientid();
    }

    @Override
    public List<Appclient> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        AppclientDAO dao = new AppclientDAO();
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
