package com.unibro.facebook_group;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.unibro.sysuser.UserSessionBean;
import com.unibro.utils.Global;
import com.unibro.utils.RequestFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
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
public class Facebook_groupLazyService implements Serializable {

    private Integer customer_type_id;
    private LazyDataModel<Facebook_group> lazyModel;
    private Facebook_group selectedObject = new Facebook_group();
    private Facebook_group[] selectedObjects;
    private Facebook_group newObject = new Facebook_group();
    private String selectedId;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private String importData = "";

    public Facebook_groupLazyService() {
        this.loadObjects();
    }

    public void initSelectedObject() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            Facebook_groupDAO dao = new Facebook_groupDAO();
            this.selectedObject = dao.getObjectByKey(selectedId);
        }
    }

    public final void loadObjects() {
        this.lazyModel = new Facebook_groupLazyModel();
    }

    public void setLazyModel(LazyDataModel<Facebook_group> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public LazyDataModel<Facebook_group> getLazyModel() {
        return lazyModel;
    }

    public void setNewObject(Facebook_group newObject) {
        this.newObject = newObject;
    }

    public Facebook_group getNewObject() {
        return newObject;
    }

    public void setSelectedObject(Facebook_group selectedObject) {
        this.selectedObject = selectedObject;
    }

    public Facebook_group getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObjects(Facebook_group[] selectedObjects) {
        this.selectedObjects = selectedObjects;
    }

    public Facebook_group[] getSelectedObjects() {
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
            this.getNewObject().setCreated_id(UserSessionBean.getUserSession().getUser().getUserid() + "");
            this.getNewObject().setState("P");
            Facebook_groupDAO dao = new Facebook_groupDAO();
            Facebook_group result = dao.create(getNewObject());
            if (result != null) {
                this.newObject = result;
                try {
                    this.deliverConvertTask(this.newObject);
                } catch (IOException ex) {
                    logger.error(ex);
                } catch (TimeoutException ex) {
                    logger.error(ex);
                }
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
            Facebook_groupDAO dao = new Facebook_groupDAO();
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
            Facebook_groupDAO dao = new Facebook_groupDAO();
            dao.delete(selectedObject);
            this.loadObjects();
        }
    }

    public void deleteObjects() {
        if (this.selectedObjects != null) {
            Facebook_groupDAO dao = new Facebook_groupDAO();
            for (Facebook_group selectedObject1 : this.selectedObjects) {
                dao.delete(selectedObject1);
            }
            this.loadObjects();
        }
    }

    public void rowEdit(RowEditEvent event) {
        Facebook_group sf = (Facebook_group) event.getObject();
        if (sf != null) {
            this.selectedObject = sf;
            this.editSelected();
        }
    }

    public List<Facebook_group> completeObject(String query) {
        Facebook_groupDAO dao = new Facebook_groupDAO();
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

    public void doRetryConvert() {
        if (this.selectedObject != null) {
            try {
                this.deliverConvertTask(this.selectedObject);
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, Global.getResourceLanguage("general.operationSuccess"), "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } catch (IOException ex) {
                logger.error(ex);
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, Global.getResourceLanguage("general.operationFail"), "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } catch (TimeoutException ex) {
                logger.error(ex);
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, Global.getResourceLanguage("general.operationFail"), "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
    }

    public void doImportData() {
        String[] group_id_list = this.getImportData().split("\n");
        logger.info("List:" + this.getImportData());
        Facebook_groupDAO dao = new Facebook_groupDAO();
        int size = 0;
        for (String id : group_id_list) {
            Facebook_group g = new Facebook_group();
            g.setGroupid(id.trim());
            g.setName(id.trim());
            g.setCreated_id(UserSessionBean.getUserSession().getUser().getUserid() + "");
            g.setState("P");
            g.setTotal_member(0);
            g.setUid_file("");
            g.setCustomer_type(customer_type_id);
            if (dao.create(g) != null) {
                size++;
                logger.info("Added group task:" + g.getGroupid() + ". Start delivered to MQ");
                try {
                    this.deliverConvertTask(g);
                } catch (IOException ex) {
                    logger.error(ex);
                } catch (TimeoutException ex) {
                    logger.error(ex);
                }
            }
        }
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Import  and delivered to MQ " + size + " group", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void deliverConvertTask(Facebook_group group) throws java.io.IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Global.getConfigValue("APP.RABBITMQ.HOST"));
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String TASK_QUEUE_NAME = Global.getConfigValue("APP.RABBITMQ.FB_QUERY_GID.QUEUE");
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

        String message = group.toJsonStr();

        channel.basicPublish("", TASK_QUEUE_NAME,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes("UTF-8"));
        logger.info(" [x] Sent '" + message + "'");
        channel.close();
        connection.close();
    }

    /**
     * @return the importData
     */
    public String getImportData() {
        return importData;
    }

    /**
     * @param importData the importData to set
     */
    public void setImportData(String importData) {
        this.importData = importData;
    }

    /**
     * @return the customer_type_id
     */
    public Integer getCustomer_type_id() {
        return customer_type_id;
    }

    /**
     * @param customer_type_id the customer_type_id to set
     */
    public void setCustomer_type_id(Integer customer_type_id) {
        this.customer_type_id = customer_type_id;
    }

    public void updateAllGroupInfo() {
        Facebook_groupDAO dao = new Facebook_groupDAO();
        List<Facebook_group> list = dao.load(0, -1, "null", 0, new ArrayList());
        for (Facebook_group g : list) {
            g.extracInfoFromFacebook();
            g.editMe();
        }
    }

}
