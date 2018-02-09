package com.unibro.access_list;

import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class Access_listLazyModel extends LazyDataModel<Access_list> {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<Access_list> datasources = new ArrayList<Access_list>();

    public Access_listLazyModel() {
        this.datasources = new ArrayList();
    }

    public Access_listLazyModel(ArrayList<Access_list> datasources) {
        this.datasources = datasources;
    }

    public List<Access_list> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<Access_list> datasources) {
        this.datasources = datasources;
    }

    @Override
    public Access_list getRowData(String rowKey) {
        for (Access_list obj : datasources) {
            if (obj.getAccess_id().toString().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(Access_list obj) {
        return obj.getAccess_id().toString();
    }

    @Override
    public List<Access_list> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        Access_listDAO dao = new Access_listDAO();
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
