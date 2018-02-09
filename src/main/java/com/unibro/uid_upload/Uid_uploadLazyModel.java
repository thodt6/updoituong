package com.unibro.uid_upload;

import com.unibro.sysuser.UserSessionBean;
import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class Uid_uploadLazyModel extends LazyDataModel<Uid_upload> {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<Uid_upload> datasources = new ArrayList<Uid_upload>();

    public Uid_uploadLazyModel() {
        this.datasources = new ArrayList();
    }

    public Uid_uploadLazyModel(ArrayList<Uid_upload> datasources) {
        this.datasources = datasources;
    }

    public List<Uid_upload> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<Uid_upload> datasources) {
        this.datasources = datasources;
    }

    @Override
    public Uid_upload getRowData(String rowKey) {
        for (Uid_upload obj : datasources) {
            if (obj.getUpload_id().toString().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(Uid_upload obj) {
        return obj.getUpload_id().toString();
    }

    @Override
    public List<Uid_upload> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        Uid_uploadDAO dao = new Uid_uploadDAO();
        if (filters == null || filters.isEmpty()) {
            int sort = 0;
            if (sortOrder.equals(SortOrder.ASCENDING)) {
                sort = 1;
            }
            if (sortOrder.equals(SortOrder.DESCENDING)) {
                sort = -1;
            }
            List list = new ArrayList();
            RequestFilter filter = new RequestFilter();
            filter.setName("update_id");
            filter.setValue(UserSessionBean.getUserSession().getUser().getUserid());
            filter.setType(RequestFilter.EQUAL);
            filter.setRequired(true);
            list.add(filter);
            this.datasources = dao.load(first, pageSize, sortField, sort, list);
            this.setRowCount(Long.valueOf(dao.getTotalObject(list)).intValue());
            return datasources;
        } else {
            int sort = 0;
            if (sortOrder.equals(SortOrder.ASCENDING)) {
                sort = 1;
            }
            if (sortOrder.equals(SortOrder.DESCENDING)) {
                sort = -1;
            }
            List list = RequestFilter.fromHashMap(filters);
            RequestFilter filter = new RequestFilter();
            filter.setName("update_id");
            filter.setValue(UserSessionBean.getUserSession().getUser().getUserid());
            filter.setType(RequestFilter.EQUAL);
            filter.setRequired(true);
            list.add(filter);
            this.datasources = dao.load(first, pageSize, sortField, sort, list);
            this.setRowCount(Long.valueOf(dao.getTotalObject(list)).intValue());
            return datasources;
        }
    }

}
