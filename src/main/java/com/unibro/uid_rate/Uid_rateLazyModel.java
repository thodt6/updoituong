package com.unibro.uid_rate;

import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class Uid_rateLazyModel extends LazyDataModel<Uid_rate> {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<Uid_rate> datasources = new ArrayList<Uid_rate>();

    public Uid_rateLazyModel() {
        this.datasources = new ArrayList();
    }

    public Uid_rateLazyModel(ArrayList<Uid_rate> datasources) {
        this.datasources = datasources;
    }

    public List<Uid_rate> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<Uid_rate> datasources) {
        this.datasources = datasources;
    }

    @Override
    public Uid_rate getRowData(String rowKey) {
        for (Uid_rate obj : datasources) {
            if (obj.getRate_id().toString().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(Uid_rate obj) {
        return obj.getRate_id().toString();
    }

    @Override
    public List<Uid_rate> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        Uid_rateDAO dao = new Uid_rateDAO();
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
