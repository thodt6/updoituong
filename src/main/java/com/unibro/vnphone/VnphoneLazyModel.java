package com.unibro.vnphone;

import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class VnphoneLazyModel extends LazyDataModel<Vnphone> {

    private String uid_list = "";

    final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<Vnphone> datasources = new ArrayList<Vnphone>();

    public VnphoneLazyModel() {
        this.uid_list = "";
        this.datasources = new ArrayList();
    }

    public VnphoneLazyModel(String uid_list) {
        this.uid_list = uid_list;
        this.datasources = new ArrayList();
    }

    public VnphoneLazyModel(ArrayList<Vnphone> datasources) {
        this.datasources = datasources;
    }

    public List<Vnphone> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<Vnphone> datasources) {
        this.datasources = datasources;
    }

    @Override
    public Vnphone getRowData(String rowKey) {
        for (Vnphone obj : datasources) {
            if (obj.getId().toString().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(Vnphone obj) {
        return obj.getId().toString();
    }

    private List<String> getUidList() {
        if (this.uid_list != null && this.uid_list.length() > 0) {
            String[] list;
            if (uid_list.contains(",")) {
                list = this.uid_list.trim().split(",");
            } else {
                if (uid_list.contains("\r\n")) {
                    list = this.uid_list.trim().split("\r\n");
                } else {
                    list = this.uid_list.trim().split("\n");
                }
            }
            List ret = new ArrayList();
            ret.addAll(Arrays.asList(list));
            return ret;
        }
        return null;
    }

    @Override
    public List<Vnphone> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        VnphoneDAO dao = new VnphoneDAO();
        if (filters == null || filters.isEmpty()) {
            int sort = 0;
            if (sortOrder.equals(SortOrder.ASCENDING)) {
                sort = 1;
            }
            if (sortOrder.equals(SortOrder.DESCENDING)) {
                sort = -1;
            }
            List list = new ArrayList();
            if (this.getUidList() != null && !this.getUidList().isEmpty()) {
                RequestFilter filter = new RequestFilter();
                filter.setName("uid");
                filter.setRequired(true);
                filter.setType(RequestFilter.IN);
                filter.setFunction("");
                filter.setValue(this.getUidList());
                list.add(filter);
                this.datasources = dao.load(first, pageSize, sortField, sort, list);
                this.setRowCount(Long.valueOf(dao.getTotalObject(list)).intValue());
                return datasources;
            }else{
                this.datasources = new ArrayList();
                this.setRowCount(0);
                return datasources;
            }
        } else {
            int sort = 0;
            if (sortOrder.equals(SortOrder.ASCENDING)) {
                sort = 1;
            }
            if (sortOrder.equals(SortOrder.DESCENDING)) {
                sort = -1;
            }
            List list = RequestFilter.fromHashMap(filters);
            if (this.getUidList() != null) {
                RequestFilter filter = new RequestFilter();
                filter.setName("uid");
                filter.setRequired(true);
                filter.setType(RequestFilter.IN);
                filter.setFunction("");
                filter.setValue(this.getUidList());
                list.add(filter);
            }
            this.datasources = dao.load(first, pageSize, sortField, sort, list);
            this.setRowCount(Long.valueOf(dao.getTotalObject(list)).intValue());
            return datasources;
        }
    }

    /**
     * @return the uid_list
     */
    public String getUid_list() {
        return uid_list;
    }

    /**
     * @param uid_list the uid_list to set
     */
    public void setUid_list(String uid_list) {
        this.uid_list = uid_list;
    }

}
