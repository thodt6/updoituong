package com.unibro.client_topup;

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
public class Client_topupLazyService implements Serializable {

    private LazyDataModel<Client_topup> lazyModel;
    private Client_topup selectedObject = new Client_topup();
    private Client_topup[] selectedObjects;
    private Client_topup newObject = new Client_topup();
    private String selectedId;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public Client_topupLazyService() {
        this.loadObjects();
    }

    public void initSelectedObject() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            Client_topupDAO dao = new Client_topupDAO();
            this.selectedObject = dao.getObjectByKey(selectedId);
        }
    }

    public final void loadObjects() {
        this.lazyModel = new Client_topupLazyModel();
    }

    public void setLazyModel(LazyDataModel<Client_topup> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public LazyDataModel<Client_topup> getLazyModel() {
        return lazyModel;
    }

    public void setNewObject(Client_topup newObject) {
        this.newObject = newObject;
    }

    public Client_topup getNewObject() {
        return newObject;
    }

    public void setSelectedObject(Client_topup selectedObject) {
        this.selectedObject = selectedObject;
    }

    public Client_topup getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObjects(Client_topup[] selectedObjects) {
        this.selectedObjects = selectedObjects;
    }

    public Client_topup[] getSelectedObjects() {
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
            Client_topupDAO dao = new Client_topupDAO();
            Client_topup result = dao.create(getNewObject());
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
            Client_topupDAO dao = new Client_topupDAO();
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
            Client_topupDAO dao = new Client_topupDAO();
            dao.delete(selectedObject);
            this.loadObjects();
        }
    }

    public void deleteObjects() {
        if (this.selectedObjects != null) {
            Client_topupDAO dao = new Client_topupDAO();
            for (Client_topup selectedObject1 : this.selectedObjects) {
                dao.delete(selectedObject1);
            }
            this.loadObjects();
        }
    }

    public void rowEdit(RowEditEvent event) {
        Client_topup sf = (Client_topup) event.getObject();
        if (sf != null) {
            this.selectedObject = sf;
            this.editSelected();
        }
    }

    public List<Client_topup> completeObject(String query) {
        Client_topupDAO dao = new Client_topupDAO();
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
