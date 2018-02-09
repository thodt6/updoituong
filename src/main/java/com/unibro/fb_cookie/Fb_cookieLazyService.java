package com.unibro.fb_cookie;

import com.unibro.scanning.Scanning;
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
public class Fb_cookieLazyService implements Serializable {

    private LazyDataModel<Fb_cookie> lazyModel;
    private Fb_cookie selectedObject = new Fb_cookie();
    private Fb_cookie[] selectedObjects;
    private Fb_cookie newObject = new Fb_cookie();
    private String selectedId;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private String test_number;

    public Fb_cookieLazyService() {
        this.loadObjects();
    }

    public void initSelectedObject() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            Fb_cookieDAO dao = new Fb_cookieDAO();
            this.selectedObject = dao.getObjectByKey(selectedId);
        }
    }

    public final void loadObjects() {
        this.lazyModel = new Fb_cookieLazyModel();
    }

    public void setLazyModel(LazyDataModel<Fb_cookie> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public LazyDataModel<Fb_cookie> getLazyModel() {
        return lazyModel;
    }

    public void setNewObject(Fb_cookie newObject) {
        this.newObject = newObject;
    }

    public Fb_cookie getNewObject() {
        return newObject;
    }

    public void setSelectedObject(Fb_cookie selectedObject) {
        this.selectedObject = selectedObject;
    }

    public Fb_cookie getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObjects(Fb_cookie[] selectedObjects) {
        this.selectedObjects = selectedObjects;
    }

    public Fb_cookie[] getSelectedObjects() {
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
            Fb_cookieDAO dao = new Fb_cookieDAO();
            Fb_cookie result = dao.create(getNewObject());
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
            Fb_cookieDAO dao = new Fb_cookieDAO();
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
            Fb_cookieDAO dao = new Fb_cookieDAO();
            dao.delete(selectedObject);
            this.loadObjects();
        }
    }

    public void deleteObjects() {
        if (this.selectedObjects != null) {
            Fb_cookieDAO dao = new Fb_cookieDAO();
            for (Fb_cookie selectedObject1 : this.selectedObjects) {
                dao.delete(selectedObject1);
            }
            this.loadObjects();
        }
    }

    public void rowEdit(RowEditEvent event) {
        Fb_cookie sf = (Fb_cookie) event.getObject();
        if (sf != null) {
            this.selectedObject = sf;
            this.editSelected();
        }
    }

    public List<Fb_cookie> completeObject(String query) {
        Fb_cookieDAO dao = new Fb_cookieDAO();
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
     * @return the test_number
     */
    public String getTest_number() {
        return test_number;
    }

    /**
     * @param test_number the test_number to set
     */
    public void setTest_number(String test_number) {
        this.test_number = test_number;
    }

    public void doTestAccount() {
        if (this.selectedObject != null && this.test_number != null) {
            String number = test_number.replace("+", "%2B");
            Scanning s = new Scanning(this.getSelectedObject().getCookie());
            String result = s.queryFacebookUID(number);
            if (result != null && !result.equals("")) {
                this.selectedObject.setState("A");
                Fb_cookieDAO dao = new Fb_cookieDAO();
                dao.edit(this.selectedObject);
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Result web search " + result, "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                result = s.queryMobileFacebookProfile(number);
                if (result != null && !result.equals("")) {
                    this.selectedObject.setState("M");
                    Fb_cookieDAO dao = new Fb_cookieDAO();
                    dao.edit(this.selectedObject);
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Result mobile web search " + result, "");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                } else {
                    this.selectedObject.setState("P");
                    Fb_cookieDAO dao = new Fb_cookieDAO();
                    dao.edit(this.selectedObject);
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Not found, not valid account", "");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }
            }
        }
    }

}
