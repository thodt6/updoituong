package com.unibro.customer_type;

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
public class Customer_typeLazyService implements Serializable {

    private LazyDataModel<Customer_type> lazyModel;
    private Customer_type selectedObject = new Customer_type();
    private Customer_type[] selectedObjects;
    private Customer_type newObject = new Customer_type();
    private String selectedId;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public Customer_typeLazyService() {
        this.loadObjects();
    }

    public void initSelectedObject() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            Customer_typeDAO dao = new Customer_typeDAO();
            this.selectedObject = dao.getObjectByKey(selectedId);
        }
    }

    public final void loadObjects() {
        this.lazyModel = new Customer_typeLazyModel();
    }

    public void setLazyModel(LazyDataModel<Customer_type> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public LazyDataModel<Customer_type> getLazyModel() {
        return lazyModel;
    }

    public void setNewObject(Customer_type newObject) {
        this.newObject = newObject;
    }

    public Customer_type getNewObject() {
        return newObject;
    }

    public void setSelectedObject(Customer_type selectedObject) {
        this.selectedObject = selectedObject;
    }

    public Customer_type getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObjects(Customer_type[] selectedObjects) {
        this.selectedObjects = selectedObjects;
    }

    public Customer_type[] getSelectedObjects() {
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
            Customer_typeDAO dao = new Customer_typeDAO();
            Customer_type result = dao.create(getNewObject());
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
            Customer_typeDAO dao = new Customer_typeDAO();
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
            Customer_typeDAO dao = new Customer_typeDAO();
            dao.delete(selectedObject);
            this.loadObjects();
        }
    }

    public void deleteObjects() {
        if (this.selectedObjects != null) {
            Customer_typeDAO dao = new Customer_typeDAO();
            for (Customer_type selectedObject1 : this.selectedObjects) {
                dao.delete(selectedObject1);
            }
            this.loadObjects();
        }
    }

    public void rowEdit(RowEditEvent event) {
        Customer_type sf = (Customer_type) event.getObject();
        if (sf != null) {
            this.selectedObject = sf;
            this.editSelected();
        }
    }

    public List<Customer_type> completeObject(String query) {
        Customer_typeDAO dao = new Customer_typeDAO();
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
