package com.unibro.client_topup;

import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class Client_topupLazyModel extends LazyDataModel<Client_topup> {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<Client_topup> datasources = new ArrayList<Client_topup>();

    public Client_topupLazyModel() {
        this.datasources = new ArrayList();
    }

    public Client_topupLazyModel(ArrayList<Client_topup> datasources) {
        this.datasources = datasources;
    }

    public List<Client_topup> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<Client_topup> datasources) {
        this.datasources = datasources;
    }

    @Override
    public Client_topup getRowData(String rowKey) {
        for (Client_topup obj : datasources) {
            if (obj.getTopup_id().toString().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(Client_topup obj) {
        return obj.getTopup_id().toString();
    }

    @Override
    public List<Client_topup> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        Client_topupDAO dao = new Client_topupDAO();
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
