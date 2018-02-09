package com.unibro.country;

import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class CountryLazyModel extends LazyDataModel<Country> {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<Country> datasources = new ArrayList<Country>();

    public CountryLazyModel() {
        this.datasources = new ArrayList();
    }

    public CountryLazyModel(ArrayList<Country> datasources) {
        this.datasources = datasources;
    }

    public List<Country> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<Country> datasources) {
        this.datasources = datasources;
    }

    @Override
    public Country getRowData(String rowKey) {
        for (Country obj : datasources) {
            if (obj.getCode().toString().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(Country obj) {
        return obj.getCode();
    }

    @Override
    public List<Country> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        CountryDAO dao = new CountryDAO();
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
