package com.unibro.obj_upload;

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
public class Obj_uploadLazyService implements Serializable {

    private LazyDataModel<Obj_upload> lazyModel;
    private Obj_upload selectedObject = new Obj_upload();
    private Obj_upload[] selectedObjects;
    private Obj_upload newObject = new Obj_upload();
    private String selectedId;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public Obj_uploadLazyService() {
        this.loadObjects();
    }

    public void initSelectedObject() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            Obj_uploadDAO dao = new Obj_uploadDAO();
            this.selectedObject = dao.getObjectByKey(selectedId);
        }
    }

    public final void loadObjects() {
        this.lazyModel = new Obj_uploadLazyModel();
    }

    public void setLazyModel(LazyDataModel<Obj_upload> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public LazyDataModel<Obj_upload> getLazyModel() {
        return lazyModel;
    }

    public void setNewObject(Obj_upload newObject) {
        this.newObject = newObject;
    }

    public Obj_upload getNewObject() {
        return newObject;
    }

    public void setSelectedObject(Obj_upload selectedObject) {
        this.selectedObject = selectedObject;
    }

    public Obj_upload getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObjects(Obj_upload[] selectedObjects) {
        this.selectedObjects = selectedObjects;
    }

    public Obj_upload[] getSelectedObjects() {
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
            Obj_uploadDAO dao = new Obj_uploadDAO();
            Obj_upload result = dao.create(getNewObject());
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
            Obj_uploadDAO dao = new Obj_uploadDAO();
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
            Obj_uploadDAO dao = new Obj_uploadDAO();
            dao.delete(selectedObject);
            this.loadObjects();
        }
    }

    public void deleteObjects() {
        if (this.selectedObjects != null) {
            Obj_uploadDAO dao = new Obj_uploadDAO();
            for (Obj_upload selectedObject1 : this.selectedObjects) {
                dao.delete(selectedObject1);
            }
            this.loadObjects();
        }
    }

    public void rowEdit(RowEditEvent event) {
        Obj_upload sf = (Obj_upload) event.getObject();
        if (sf != null) {
            this.selectedObject = sf;
            this.editSelected();
        }
    }

    public List<Obj_upload> completeObject(String query) {
        Obj_uploadDAO dao = new Obj_uploadDAO();
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
