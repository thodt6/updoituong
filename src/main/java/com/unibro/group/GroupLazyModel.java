package com.unibro.group;

import com.unibro.utils.Global;
import com.unibro.utils.RequestFilter;
import java.util.*;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nguyen Duc Tho
 */
public class GroupLazyModel extends LazyDataModel<Group> {

    final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<Group> datasources = new ArrayList<Group>();

    public GroupLazyModel() {
        this.datasources = new ArrayList();
    }

    public GroupLazyModel(ArrayList<Group> datasources) {
        this.datasources = datasources;
    }

    public List<Group> getDatasources() {
        return this.datasources;
    }

    public void setDatasources(List<Group> datasources) {
        this.datasources = datasources;
    }

    @Override
    public Group getRowData(String rowKey) {
        for (Group obj : datasources) {
            if (obj.getGroupid().toString().equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public String getRowKey(Group obj) {
        return obj.getGroupid().toString();
    }

    @Override
    public List<Group> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        GroupDAO dao = new GroupDAO();
        if (filters == null || filters.isEmpty()) {
            int sort = 0;
            if (sortOrder.equals(SortOrder.ASCENDING)) {
                sort = 1;
            }
            if (sortOrder.equals(SortOrder.DESCENDING)) {
                sort = -1;
            }
            RequestFilter filter=new RequestFilter();
            filter.setName("application_id");
            filter.setRequired(Boolean.TRUE);
            filter.setType(RequestFilter.EQUAL);
            filter.setValue(Global.getConfigValue("app.applicationid_list"));
            List list=new ArrayList();
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
            List list=RequestFilter.fromHashMap(filters);
            RequestFilter filter=new RequestFilter();
            filter.setName("application_id");
            filter.setRequired(Boolean.TRUE);
            filter.setType(RequestFilter.EQUAL);
            filter.setValue(Global.getConfigValue("app.applicationid_list"));
            
            list.add(filter);
            this.datasources = dao.load(first, pageSize, sortField, sort, list);
            this.setRowCount(Long.valueOf(dao.getTotalObject(list)).intValue());
            return datasources;
        }
    }

}
