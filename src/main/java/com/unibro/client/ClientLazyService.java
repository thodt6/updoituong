package com.unibro.client;

import com.unibro.client_topup.Client_topup;
import com.unibro.sysuser.UserSessionBean;
import com.unibro.uid_rate.Uid_rate;
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
public class ClientLazyService implements Serializable {

    private LazyDataModel<Client> lazyModel;
    private Client selectedObject = new Client();
    private Client[] selectedObjects;
    private Client newObject = new Client();
    private String selectedId;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public ClientLazyService() {
        this.loadObjects();
    }

    public void initSelectedObject() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            ClientDAO dao = new ClientDAO();
            this.selectedObject = dao.getObjectByKey(selectedId);
        }
    }

    public final void loadObjects() {
        this.lazyModel = new ClientLazyModel();
    }

    public void setLazyModel(LazyDataModel<Client> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public LazyDataModel<Client> getLazyModel() {
        return lazyModel;
    }

    public void setNewObject(Client newObject) {
        this.newObject = newObject;
    }

    public Client getNewObject() {
        return newObject;
    }

    public void setSelectedObject(Client selectedObject) {
        this.selectedObject = selectedObject;
    }

    public Client getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObjects(Client[] selectedObjects) {
        this.selectedObjects = selectedObjects;
    }

    public Client[] getSelectedObjects() {
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
            ClientDAO dao = new ClientDAO();
            Client result = dao.create(getNewObject());
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
            ClientDAO dao = new ClientDAO();
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
            ClientDAO dao = new ClientDAO();
            dao.delete(selectedObject);
            this.loadObjects();
        }
    }

    public void deleteObjects() {
        if (this.selectedObjects != null) {
            ClientDAO dao = new ClientDAO();
            for (Client selectedObject1 : this.selectedObjects) {
                dao.delete(selectedObject1);
            }
            this.loadObjects();
        }
    }

    public void rowEdit(RowEditEvent event) {
        Client sf = (Client) event.getObject();
        if (sf != null) {
            this.selectedObject = sf;
            this.editSelected();
        }
    }

    public List<Client> completeObject(String query) {
        ClientDAO dao = new ClientDAO();
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

    private Uid_rate rate;
    private int money = 0;

    public void loadRate() {
        this.rate = Uid_rate.getCurrentRate();
    }

    public void doTopup() {
        if (this.selectedObject != null) {
            if (this.rate != null && rate.getValue() > 0 && this.getMoney() > 0) {
                int num_uid = Math.round(money / this.rate.getValue());
                this.selectedObject.setBalance(this.selectedObject.getBalance() + num_uid);
                if (Client.updateObject(this.selectedObject) != null) {
                    Client_topup topup = new Client_topup();
                    topup.setAmount_money(money);
                    topup.setAmount_uid(num_uid);
                    topup.setClient_id(this.selectedObject.getUid());
                    topup.setCreated_id(UserSessionBean.getUserSession().getUser().getUserid());
                    topup.setRate_id(rate.getRate_id().intValue());
                    topup.setTopup_date(new java.util.Date());
                    Client_topup.createObject(topup);
                    this.loadObjects();
                    this.setMoney(0);
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, Global.getResourceLanguage("general.operationSuccess"), "");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, Global.getResourceLanguage("general.operationFail"), "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, Global.getResourceLanguage("general.operationFail"), "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    /**
     * @return the money
     */
    public int getMoney() {
        return money;
    }

    /**
     * @param money the money to set
     */
    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * @return the rate
     */
    public Uid_rate getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(Uid_rate rate) {
        this.rate = rate;
    }

}
