package com.unibro.vnphone;

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
public class VnphoneLazyService implements Serializable {

    private String uid_list="";
    private LazyDataModel<Vnphone> lazyModel;
    private Vnphone selectedObject = new Vnphone();
    private Vnphone[] selectedObjects;
    private Vnphone newObject = new Vnphone();
    private String selectedId;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public VnphoneLazyService() {
        this.loadObjects();
    }

    public void initSelectedObject() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            VnphoneDAO dao = new VnphoneDAO();
            this.selectedObject = dao.getObjectByKey(selectedId);
        }
    }

    public final void loadObjects() {
        this.lazyModel = new VnphoneLazyModel();
    }
    
    public final void filterUidObjects() {
        this.lazyModel = new VnphoneLazyModel(this.uid_list);
    }

    public void setLazyModel(LazyDataModel<Vnphone> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public LazyDataModel<Vnphone> getLazyModel() {
        return lazyModel;
    }

    public void setNewObject(Vnphone newObject) {
        this.newObject = newObject;
    }

    public Vnphone getNewObject() {
        return newObject;
    }

    public void setSelectedObject(Vnphone selectedObject) {
        this.selectedObject = selectedObject;
    }

    public Vnphone getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObjects(Vnphone[] selectedObjects) {
        this.selectedObjects = selectedObjects;
    }

    public Vnphone[] getSelectedObjects() {
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
            VnphoneDAO dao = new VnphoneDAO();
            Vnphone result = dao.create(getNewObject());
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
            VnphoneDAO dao = new VnphoneDAO();
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
            VnphoneDAO dao = new VnphoneDAO();
            dao.delete(selectedObject);
            this.loadObjects();
        }
    }

    public void deleteObjects() {
        if (this.selectedObjects != null) {
            VnphoneDAO dao = new VnphoneDAO();
            for (Vnphone selectedObject1 : this.selectedObjects) {
                dao.delete(selectedObject1);
            }
            this.loadObjects();
        }
    }

    public void rowEdit(RowEditEvent event) {
        Vnphone sf = (Vnphone) event.getObject();
        if (sf != null) {
            this.selectedObject = sf;
            this.editSelected();
        }
    }

    public List<Vnphone> completeObject(String query) {
        VnphoneDAO dao = new VnphoneDAO();
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
