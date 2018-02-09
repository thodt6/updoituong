package com.unibro.mobile_range;

import com.unibro.utils.Global;
import com.unibro.utils.RequestFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Nguyen Duc Tho
 */
@ManagedBean
@ViewScoped
public class Mobile_rangeLazyService implements Serializable {

    private LazyDataModel<Mobile_range> lazyModel;
    private Mobile_range selectedObject = new Mobile_range();
    private Mobile_range[] selectedObjects;
    private Mobile_range newObject = new Mobile_range();
    private String selectedId;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public Mobile_rangeLazyService() {
        this.loadObjects();
    }

    public void initSelectedObject() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            Mobile_rangeDAO dao = new Mobile_rangeDAO();
            this.selectedObject = dao.getObjectByKey(selectedId);
        }
    }

    public final void loadObjects() {
        this.lazyModel = new Mobile_rangeLazyModel();
    }

    public void setLazyModel(LazyDataModel<Mobile_range> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public LazyDataModel<Mobile_range> getLazyModel() {
        return lazyModel;
    }

    public void setNewObject(Mobile_range newObject) {
        this.newObject = newObject;
    }

    public Mobile_range getNewObject() {
        return newObject;
    }

    public void setSelectedObject(Mobile_range selectedObject) {
        this.selectedObject = selectedObject;
    }

    public Mobile_range getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObjects(Mobile_range[] selectedObjects) {
        this.selectedObjects = selectedObjects;
    }

    public Mobile_range[] getSelectedObjects() {
        return selectedObjects;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    public String getSelectedId() {
        return selectedId;
    }

    public void createObject() {
        if (this.getNewObject() != null) {
            Mobile_rangeDAO dao = new Mobile_rangeDAO();
            Mobile_range result = dao.create(getNewObject());
            if (result != null) {
                this.newObject = result;
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, Global.getResourceLanguage("general.operationSuccess"), "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, Global.getResourceLanguage("general.operationFail"), "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
    }

    public void editSelected() {
        if (this.selectedObject != null) {
            Mobile_rangeDAO dao = new Mobile_rangeDAO();
            if (dao.edit(this.selectedObject) != null) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, Global.getResourceLanguage("general.operationSuccess"), "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, Global.getResourceLanguage("general.operationFail"), "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
    }

    public void deleteObject() {
        if (this.selectedObject != null) {
            Mobile_rangeDAO dao = new Mobile_rangeDAO();
            dao.delete(selectedObject);
            this.loadObjects();
        }
    }

    public void deleteObjects() {
        if (this.selectedObjects != null) {
            Mobile_rangeDAO dao = new Mobile_rangeDAO();
            for (Mobile_range selectedObject1 : this.selectedObjects) {
                dao.delete(selectedObject1);
            }
            this.loadObjects();
        }
    }

    public void rowEdit(RowEditEvent event) {
        Mobile_range sf = (Mobile_range) event.getObject();
        if (sf != null) {
            this.selectedObject = sf;
            this.editSelected();
        }
    }

    public List<Mobile_range> completeObject(String query) {
        Mobile_rangeDAO dao = new Mobile_rangeDAO();
        if (query == null || query.equals("")) {
            return dao.load(0, -1, "null", 0, new ArrayList());
        } else {
            RequestFilter filter = new RequestFilter();
            //replace name by the query field
            filter.setName("name");
            filter.setType(RequestFilter.CONTAIN);
            filter.setValue(query);
            List filter_list = new ArrayList();
            filter_list.add(filter);
            return dao.load(0, -1, "null", 0, filter_list);
        }
    }

}
