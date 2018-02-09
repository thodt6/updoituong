package com.unibro.scanning_task;

import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class Scanning_taskLazyModel extends LazyDataModel<Scanning_task> {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<Scanning_task> datasources = new ArrayList<Scanning_task>();

    public Scanning_taskLazyModel() {
        this.datasources = new ArrayList();
    }

    public Scanning_taskLazyModel(ArrayList<Scanning_task> datasources) {
        this.datasources = datasources;
    }

    public List<Scanning_task> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<Scanning_task> datasources) {
        this.datasources = datasources;
    }

    @Override
    public Scanning_task getRowData(String rowKey) {
        for (Scanning_task obj : datasources) {
            if (obj.getTaskid().toString().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(Scanning_task obj) {
        return obj.getTaskid().toString();
    }

    @Override
    public List<Scanning_task> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        Scanning_taskDAO dao = new Scanning_taskDAO();
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
