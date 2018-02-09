package com.unibro.client;

import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class ClientLazyModel extends LazyDataModel<Client> {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<Client> datasources = new ArrayList<Client>();

    public ClientLazyModel() {
        this.datasources = new ArrayList();
    }

    public ClientLazyModel(ArrayList<Client> datasources) {
        this.datasources = datasources;
    }

    public List<Client> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<Client> datasources) {
        this.datasources = datasources;
    }

    @Override
    public Client getRowData(String rowKey) {
        for (Client obj : datasources) {
            if (obj.getUid().toString().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(Client obj) {
        return obj.getUid();
    }

    @Override
    public List<Client> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        ClientDAO dao = new ClientDAO();
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
