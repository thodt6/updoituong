package com.unibro.group;

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
public class GroupLazyService implements Serializable {

    private LazyDataModel<Group> lazyModel;
    private Group selectedObject = new Group();
    private Group[] selectedObjects;
    private Group newObject = new Group();
    private String selectedId;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public GroupLazyService() {
        this.loadObjects();
    }

    public void initSelectedObject() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            GroupDAO dao = new GroupDAO();
            this.selectedObject = dao.getObjectByKey(selectedId);
        }
    }

    public final void loadObjects() {
        this.lazyModel = new GroupLazyModel();
    }

    public void setLazyModel(LazyDataModel<Group> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public LazyDataModel<Group> getLazyModel() {
        return lazyModel;
    }

    public void setNewObject(Group newObject) {
        this.newObject = newObject;
    }

    public Group getNewObject() {
        return newObject;
    }

    public void setSelectedObject(Group selectedObject) {
        this.selectedObject = selectedObject;
    }

    public Group getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObjects(Group[] selectedObjects) {
        this.selectedObjects = selectedObjects;
    }

    public Group[] getSelectedObjects() {
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
            this.getNewObject().setApplication_id(Global.getConfigValue("app.applicationid_list"));
            GroupDAO dao = new GroupDAO();
            Group result = dao.create(getNewObject());
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
            GroupDAO dao = new GroupDAO();
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
            GroupDAO dao = new GroupDAO();
            dao.delete(selectedObject);
            this.loadObjects();
        }
    }

    public void deleteObjects() {
        if (this.selectedObjects != null) {
            GroupDAO dao = new GroupDAO();
            for (Group selectedObject1 : this.selectedObjects) {
                dao.delete(selectedObject1);
            }
            this.loadObjects();
        }
    }

    public void rowEdit(RowEditEvent event) {
        Group sf = (Group) event.getObject();
        if (sf != null) {
            this.selectedObject = sf;
            this.editSelected();
        }
    }

    public List<Group> completeObject(String query) {
        GroupDAO dao = new GroupDAO();
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
