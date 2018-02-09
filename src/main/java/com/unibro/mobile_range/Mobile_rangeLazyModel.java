package com.unibro.mobile_range;

import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class Mobile_rangeLazyModel extends LazyDataModel<Mobile_range> {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<Mobile_range> datasources = new ArrayList<Mobile_range>();

    public Mobile_rangeLazyModel() {
        this.datasources = new ArrayList();
    }

    public Mobile_rangeLazyModel(ArrayList<Mobile_range> datasources) {
        this.datasources = datasources;
    }

    public List<Mobile_range> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<Mobile_range> datasources) {
        this.datasources = datasources;
    }

    @Override
    public Mobile_range getRowData(String rowKey) {
        for (Mobile_range obj : datasources) {
            if (obj.getRange_id().toString().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(Mobile_range obj) {
        return obj.getRange_id().toString();
    }

    @Override
    public List<Mobile_range> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        Mobile_rangeDAO dao = new Mobile_rangeDAO();
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
