package com.unibro.property;

import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class PropertyLazyModel extends LazyDataModel<Property> {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<Property> datasources = new ArrayList<Property>();

    public PropertyLazyModel() {
        this.datasources = new ArrayList();
    }

    public PropertyLazyModel(ArrayList<Property> datasources) {
        this.datasources = datasources;
    }

    public List<Property> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<Property> datasources) {
        this.datasources = datasources;
    }

    @Override
    public Property getRowData(String rowKey) {
        for (Property obj : datasources) {
            if (obj.getProp_id().toString().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(Property obj) {
        return obj.getProp_id().toString();
    }

    @Override
    public List<Property> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        PropertyDAO dao = new PropertyDAO();
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
