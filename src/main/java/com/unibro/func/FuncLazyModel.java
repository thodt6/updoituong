package com.unibro.func;

import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class FuncLazyModel extends LazyDataModel<Func> {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<Func> datasources = new ArrayList<Func>();

    public FuncLazyModel() {
        this.datasources = new ArrayList();
    }

    public FuncLazyModel(ArrayList<Func> datasources) {
        this.datasources = datasources;
    }

    public List<Func> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<Func> datasources) {
        this.datasources = datasources;
    }

    @Override
    public Func getRowData(String rowKey) {
        for (Func obj : datasources) {
            if (obj.getFunc_id().toString().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(Func obj) {
        return obj.getFunc_id().toString();
    }

    @Override
    public List<Func> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        FuncDAO dao = new FuncDAO();
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
