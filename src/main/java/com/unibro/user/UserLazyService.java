package com.unibro.user;

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
public class UserLazyService implements Serializable {

    private LazyDataModel<User> lazyModel;
    private User selectedObject = new User();
    private User[] selectedObjects;
    private User newObject = new User();
    private String selectedId;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private String groupid = "";

    public UserLazyService() {
        this.loadObjects();
    }

    public void initGroupId() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            if (this.selectedId != null) {
                this.groupid = this.selectedId;
            } else {
                this.groupid = "";
            }
            this.loadObjects();
        }
    }

    public void initSelectedObject() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            UserDAO dao = new UserDAO();
            this.selectedObject = dao.getObjectByKey(selectedId);
        }
    }

    public final void loadObjects() {
        this.lazyModel = new UserLazyModel(groupid);
    }

    public void setLazyModel(LazyDataModel<User> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public LazyDataModel<User> getLazyModel() {
        return lazyModel;
    }

    public void setNewObject(User newObject) {
        this.newObject = newObject;
    }

    public User getNewObject() {
        return newObject;
    }

    public void setSelectedObject(User selectedObject) {
        this.selectedObject = selectedObject;
    }

    public User getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObjects(User[] selectedObjects) {
        this.selectedObjects = selectedObjects;
    }

    public User[] getSelectedObjects() {
        return selectedObjects;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    public String getSelectedId() {
        return selectedId;
    }

    public void createObject() {
        UserDAO dao = new UserDAO();
        User u = dao.getUserByField("username", this.getNewObject().getUsername());
        if (u != null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, Global.getResourceLanguage("general.operationFail"), "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        if (this.getNewObject() != null) {
            this.getNewObject().setPassword_hash(Global.MD5(this.getNewObject().getPassword()));
            this.getNewObject().setApplication_id(Global.getConfigValue("app.applicationid_list"));

            User result = dao.create(getNewObject());
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
            UserDAO dao = new UserDAO();
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
            UserDAO dao = new UserDAO();
            dao.delete(selectedObject);
            this.loadObjects();
        }
    }

    public void deleteObjects() {
        if (this.selectedObjects != null) {
            UserDAO dao = new UserDAO();
            for (User selectedObject1 : this.selectedObjects) {
                dao.delete(selectedObject1);
            }
            this.loadObjects();
        }
    }

    public void rowEdit(RowEditEvent event) {
        User sf = (User) event.getObject();
        if (sf != null) {
            this.selectedObject = sf;
            this.editSelected();
        }
    }

    public List<User> completeObject(String query) {
        UserDAO dao = new UserDAO();
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

    /**
     * @return the groupid
     */
    public String getGroupid() {
        return groupid;
    }

    /**
     * @param groupid the groupid to set
     */
    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

}
