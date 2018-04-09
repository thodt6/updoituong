package com.unibro.customer_type;

import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class Customer_typeLazyModel extends LazyDataModel<Customer_type> {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<Customer_type> datasources = new ArrayList<Customer_type>();

    public Customer_typeLazyModel() {
        this.datasources = new ArrayList();
    }

    public Customer_typeLazyModel(ArrayList<Customer_type> datasources) {
        this.datasources = datasources;
    }

    public List<Customer_type> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<Customer_type> datasources) {
        this.datasources = datasources;
    }

    @Override
    public Customer_type getRowData(String rowKey) {
        for (Customer_type obj : datasources) {
            if (obj.getUniqueKey().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(Customer_type obj) {
        return obj.getUniqueKey();
    }

    @Override
    public List<Customer_type> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        Customer_typeDAO dao = new Customer_typeDAO();
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
