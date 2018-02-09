package com.unibro.obj_upload;

import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class Obj_uploadLazyModel extends LazyDataModel<Obj_upload> {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<Obj_upload> datasources = new ArrayList<Obj_upload>();

    public Obj_uploadLazyModel() {
        this.datasources = new ArrayList();
    }

    public Obj_uploadLazyModel(ArrayList<Obj_upload> datasources) {
        this.datasources = datasources;
    }

    public List<Obj_upload> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<Obj_upload> datasources) {
        this.datasources = datasources;
    }

    @Override
    public Obj_upload getRowData(String rowKey) {
        for (Obj_upload obj : datasources) {
            if (obj.getObj_id().toString().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(Obj_upload obj) {
        return obj.getObj_id();
    }

    @Override
    public List<Obj_upload> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        Obj_uploadDAO dao = new Obj_uploadDAO();
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
