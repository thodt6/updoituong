package com.unibro.scanning_task;

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
public class Scanning_taskLazyService implements Serializable {

    private LazyDataModel<Scanning_task> lazyModel;
    private Scanning_task selectedObject = new Scanning_task();
    private Scanning_task[] selectedObjects;
    private Scanning_task newObject = new Scanning_task();
    private String selectedId;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public Scanning_taskLazyService() {
        this.loadObjects();
    }

    public void initSelectedObject() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            Scanning_taskDAO dao = new Scanning_taskDAO();
            this.selectedObject = dao.getObjectByKey(selectedId);
        }
    }

    public final void loadObjects() {
        this.lazyModel = new Scanning_taskLazyModel();
    }

    public void setLazyModel(LazyDataModel<Scanning_task> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public LazyDataModel<Scanning_task> getLazyModel() {
        return lazyModel;
    }

    public void setNewObject(Scanning_task newObject) {
        this.newObject = newObject;
    }

    public Scanning_task getNewObject() {
        return newObject;
    }

    public void setSelectedObject(Scanning_task selectedObject) {
        this.selectedObject = selectedObject;
    }

    public Scanning_task getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObjects(Scanning_task[] selectedObjects) {
        this.selectedObjects = selectedObjects;
    }

    public Scanning_task[] getSelectedObjects() {
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
            Scanning_taskDAO dao = new Scanning_taskDAO();
            Scanning_task result = dao.create(getNewObject());
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
            Scanning_taskDAO dao = new Scanning_taskDAO();
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
            Scanning_taskDAO dao = new Scanning_taskDAO();
            dao.delete(selectedObject);
            this.loadObjects();
        }
    }

    public void deleteObjects() {
        if (this.selectedObjects != null) {
            Scanning_taskDAO dao = new Scanning_taskDAO();
            for (Scanning_task selectedObject1 : this.selectedObjects) {
                dao.delete(selectedObject1);
            }
            this.loadObjects();
        }
    }

    public void rowEdit(RowEditEvent event) {
        Scanning_task sf = (Scanning_task) event.getObject();
        if (sf != null) {
            this.selectedObject = sf;
            this.editSelected();
        }
    }

    public List<Scanning_task> completeObject(String query) {
        Scanning_taskDAO dao = new Scanning_taskDAO();
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
