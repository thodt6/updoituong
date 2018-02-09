package com.unibro.client_transaction;

import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class Client_transactionLazyModel extends LazyDataModel<Client_transaction> {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<Client_transaction> datasources = new ArrayList<Client_transaction>();

    public Client_transactionLazyModel() {
        this.datasources = new ArrayList();
    }

    public Client_transactionLazyModel(ArrayList<Client_transaction> datasources) {
        this.datasources = datasources;
    }

    public List<Client_transaction> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<Client_transaction> datasources) {
        this.datasources = datasources;
    }

    @Override
    public Client_transaction getRowData(String rowKey) {
        for (Client_transaction obj : datasources) {
            if (obj.getId().toString().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(Client_transaction obj) {
        return obj.getId().toString();
    }

    @Override
    public List<Client_transaction> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        Client_transactionDAO dao = new Client_transactionDAO();
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
