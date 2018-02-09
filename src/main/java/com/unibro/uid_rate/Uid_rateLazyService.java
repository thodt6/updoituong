package com.unibro.uid_rate;

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
public class Uid_rateLazyService implements Serializable {

    private LazyDataModel<Uid_rate> lazyModel;
    private Uid_rate selectedObject = new Uid_rate();
    private Uid_rate[] selectedObjects;
    private Uid_rate newObject = new Uid_rate();
    private String selectedId;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public Uid_rateLazyService() {
        this.loadObjects();
    }

    public void initSelectedObject() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            Uid_rateDAO dao = new Uid_rateDAO();
            this.selectedObject = dao.getObjectByKey(selectedId);
        }
    }

    public final void loadObjects() {
        this.lazyModel = new Uid_rateLazyModel();
    }

    public void setLazyModel(LazyDataModel<Uid_rate> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public LazyDataModel<Uid_rate> getLazyModel() {
        return lazyModel;
    }

    public void setNewObject(Uid_rate newObject) {
        this.newObject = newObject;
    }

    public Uid_rate getNewObject() {
        return newObject;
    }

    public void setSelectedObject(Uid_rate selectedObject) {
        this.selectedObject = selectedObject;
    }

    public Uid_rate getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObjects(Uid_rate[] selectedObjects) {
        this.selectedObjects = selectedObjects;
    }

    public Uid_rate[] getSelectedObjects() {
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
            Uid_rateDAO dao = new Uid_rateDAO();
            Uid_rate result = dao.create(getNewObject());
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
            Uid_rateDAO dao = new Uid_rateDAO();
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
            Uid_rateDAO dao = new Uid_rateDAO();
            dao.delete(selectedObject);
            this.loadObjects();
        }
    }

    public void deleteObjects() {
        if (this.selectedObjects != null) {
            Uid_rateDAO dao = new Uid_rateDAO();
            for (Uid_rate selectedObject1 : this.selectedObjects) {
                dao.delete(selectedObject1);
            }
            this.loadObjects();
        }
    }

    public void rowEdit(RowEditEvent event) {
        Uid_rate sf = (Uid_rate) event.getObject();
        if (sf != null) {
            this.selectedObject = sf;
            this.editSelected();
        }
    }

    public List<Uid_rate> completeObject(String query) {
        Uid_rateDAO dao = new Uid_rateDAO();
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
    
    public void activateRate(){
        if(this.selectedObject!=null){
            Uid_rateDAO dao=new Uid_rateDAO();
            if(dao.activateRate(this.selectedObject)==1){
                this.loadObjects();
            }
        }
    }


}
