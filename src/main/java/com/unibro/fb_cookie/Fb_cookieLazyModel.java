package com.unibro.fb_cookie;

import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class Fb_cookieLazyModel extends LazyDataModel<Fb_cookie> {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<Fb_cookie> datasources = new ArrayList<Fb_cookie>();

    public Fb_cookieLazyModel() {
        this.datasources = new ArrayList();
    }

    public Fb_cookieLazyModel(ArrayList<Fb_cookie> datasources) {
        this.datasources = datasources;
    }

    public List<Fb_cookie> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<Fb_cookie> datasources) {
        this.datasources = datasources;
    }

    @Override
    public Fb_cookie getRowData(String rowKey) {
        for (Fb_cookie obj : datasources) {
            if (obj.getFbid().toString().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(Fb_cookie obj) {
        return obj.getFbid().toString();
    }

    @Override
    public List<Fb_cookie> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        Fb_cookieDAO dao = new Fb_cookieDAO();
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
