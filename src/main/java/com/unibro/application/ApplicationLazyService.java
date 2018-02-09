package com.unibro.application;

import com.unibro.sysuser.UserSessionBean;
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
public class ApplicationLazyService implements Serializable {

    private LazyDataModel<Application> lazyModel;
    private Application selectedObject = new Application();
    private Application[] selectedObjects;
    private Application newObject = new Application();
    private String selectedId;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public ApplicationLazyService() {
        this.loadObjects();
    }

    public void initSelectedObject() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            ApplicationDAO dao = new ApplicationDAO();
            this.selectedObject = dao.getObjectByKey(selectedId);
        }
    }

    public final void loadObjects() {
        this.lazyModel = new ApplicationLazyModel();
    }

    public void setLazyModel(LazyDataModel<Application> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public LazyDataModel<Application> getLazyModel() {
        return lazyModel;
    }

    public void setNewObject(Application newObject) {
        this.newObject = newObject;
    }

    public Application getNewObject() {
        return newObject;
    }

    public void setSelectedObject(Application selectedObject) {
        this.selectedObject = selectedObject;
    }

    public Application getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObjects(Application[] selectedObjects) {
        this.selectedObjects = selectedObjects;
    }

    public Application[] getSelectedObjects() {
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
            this.getNewObject().setUpdate_id(UserSessionBean.getUserSession().getUser().getUserid());
            this.getNewObject().setUpdate_time(new java.util.Date());
            ApplicationDAO dao = new ApplicationDAO();
            Application result = dao.create(getNewObject());
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
            this.selectedObject.setUpdate_id(UserSessionBean.getUserSession().getUser().getUserid());
            this.selectedObject.setUpdate_time(new java.util.Date());
            ApplicationDAO dao = new ApplicationDAO();
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
            ApplicationDAO dao = new ApplicationDAO();
            dao.delete(selectedObject);
            this.loadObjects();
        }
    }

    public void deleteObjects() {
        if (this.selectedObjects != null) {
            ApplicationDAO dao = new ApplicationDAO();
            for (Application selectedObject1 : this.selectedObjects) {
                dao.delete(selectedObject1);
            }
            this.loadObjects();
        }
    }

    public void rowEdit(RowEditEvent event) {
        Application sf = (Application) event.getObject();
        if (sf != null) {
            this.selectedObject = sf;
            this.editSelected();
        }
    }

    public List<Application> completeObject(String query) {
        ApplicationDAO dao = new ApplicationDAO();
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
