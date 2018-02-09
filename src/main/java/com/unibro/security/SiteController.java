package com.unibro.security;

import com.unibro.access_list.Access_list;
import com.unibro.func.Func;
import com.unibro.func.FuncDAO;
import com.unibro.group.Group;
import com.unibro.group.GroupDAO;
import com.unibro.property.Property;
import com.unibro.sysuser.UserSessionBean;
import com.unibro.utils.Global;
import com.unibro.utils.RequestFilter;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Nguyen Duc Tho
 */
@ManagedBean
@ViewScoped
public class SiteController implements Serializable {

    private TreeNode root;
    private List<Property> objects;
    private String selectedId;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private DefaultTreeNode selectedNode;

    private List<Group> selectedGroups;
    private List<Group> allGroup;

    public SiteController() {
        //this.loadObjects();
        this.loadObjects();
        GroupDAO dao = new GroupDAO();
        this.allGroup = Group.loadAllGroups();
    }
//
//    @PostConstruct
//    public void init() {
//        
//    }

    private void buildTree() {
//        ModuleDAO bean = new ModuleDAO();
//        this.objects = bean.load("ORDER BY uri", null);
        File f = new File(Global.getConfigValue("FILE_SITE_PATH") + "/portal");
        this.root = new DefaultTreeNode(f, null);
        this.buildTree(root);
    }

    private void buildTree(TreeNode node) {
//        node.setExpanded(false);
        File file = (File) node.getData();
//        logger.info(file.getAbsolutePath());
        if (file.isDirectory()) {
            File[] childs = file.listFiles();
            if (childs.length > 0) {
                for (File f : childs) {
                    if (f.isDirectory()) {
                        TreeNode child = new DefaultTreeNode(f, node);
                        this.buildTree(child);
                    } else {
                        TreeNode child = new DefaultTreeNode("file", f, node);
                        this.buildTree(child);
                    }
                }
            }

        }
    }

    public final void loadObjects() {
        this.buildTree();
    }

    public void setObjects(List<Property> objects) {
        this.objects = objects;
    }

    public List<Property> getObjects() {
        return objects;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    public String getSelectedId() {
        return selectedId;
    }

    public TreeNode getRoot() {
        return root;
    }

    /**
     * @return the selectedNode
     */
    public DefaultTreeNode getSelectedNode() {
        return selectedNode;
    }

    /**
     * @param selectedNode the selectedNode to set
     */
    public void setSelectedNode(DefaultTreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public void onNodeSelect(NodeSelectEvent event) {
        this.selectedNode = (DefaultTreeNode) event.getTreeNode();
        this.loadPermission();
    }

    /**
     * @return the selectedGroups
     */
    public List<Group> getSelectedGroups() {
        return selectedGroups;
    }

    /**
     * @param selectedGroups the selectedGroups to set
     */
    public void setSelectedGroups(List<Group> selectedGroups) {
        this.selectedGroups = selectedGroups;
    }

    public void loadPermission() {
        if (this.selectedNode == null) {
            logger.info("Selected node is null");
            return;
        }
        File f = (File) this.selectedNode.getData();
        if (f.isFile()) {
            String path = f.getAbsolutePath().replaceAll("\\\\", "/").replace(Global.getConfigValue("FILE_SITE_PATH"), "");
            path = path.replace("xhtml", Global.getConfigValue("APP.EXTENSION"));
            logger.info("PATH:" + path);
            String prop_id = Global.getConfigValue("app.applicationid_list") + ":" + path;
            List<Access_list> acl = Access_list.getAccessList(prop_id, Global.getConfigValue("app.applicationid_list"),1);
            if(acl==null){
                this.selectedGroups = null;
                return;
            }
            List<Integer> groupid_list = new ArrayList();
            for (Access_list a : acl) {
                groupid_list.add(a.getPrinciple_id());
            }
            RequestFilter rf = new RequestFilter();
            rf.setFunction("");
            rf.setName("groupid");
            rf.setRequired(true);
            rf.setType(RequestFilter.IN);
            rf.setValue(groupid_list);
            GroupDAO dao = new GroupDAO();
            List filter = new ArrayList();
            filter.add(rf);
            this.selectedGroups = dao.load(0, 0, "groupid", 1, filter);
        } else {
            this.selectedGroups = null;
        }

    }

    public void setPermission() {
        File f = (File) this.selectedNode.getData();
        logger.info(f.getAbsolutePath());
        this.setPermission(f);
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, Global.getResourceLanguage("general.operationSuccess"), "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private void setPermission(File f) {
        if (f.isDirectory()) {
            File[] child = f.listFiles();
            if (child.length > 0) {
                for (File child1 : child) {
                    setPermission(child1);
                }
            }
        } else {
            this.setAccesslist(f);
        }
    }

    private void setAccesslist(File f) {
        String path = f.getAbsolutePath().replaceAll("\\\\", "/").replace(Global.getConfigValue("FILE_SITE_PATH"), "");
        path = path.replace("xhtml", Global.getConfigValue("APP.EXTENSION"));
        String application_id=Global.getConfigValue("app.applicationid_list");
        logger.info("Path:" + path);
        Func func = new Func();
        func.setFunc_id(application_id + ":" + f.getParentFile().getName());
        func.setApplication_id(application_id);
        func.setCreated_id(UserSessionBean.getUserSession().getUser().getUserid());
        func.setDescription("");
        func.setName(f.getParentFile().getName());
        func.setUri(f.getParentFile().getName() + path);
        if (Func.getFuncById(func.getFunc_id()) == null) {
            FuncDAO dao = new FuncDAO();
            dao.create(func);
        }

        Property p = new Property();
        p.setApplication_id(Global.getConfigValue("app.applicationid_list"));
        p.setCreated_id(UserSessionBean.getUserSession().getUser().getUserid());
        p.setType(0);
        p.setName(path);
        p.setFunc_id(func.getFunc_id());
        p.setProp_id(Global.getConfigValue("app.applicationid_list") + ":" + path);
        p.setUri(path);

        if (Property.getProperty(p.getProp_id()) == null) {
            Property.createProperty(p);
        }

        if (this.allGroup != null) {
            for (Group g : this.allGroup) {
                Access_list acl = Access_list.getAccessListByGroup(p.getProp_id(), g.getGroupid(), Global.getConfigValue("app.applicationid_list"));
                if (acl == null) {
                    acl=new Access_list();
                    acl.setFunc_id(func.getFunc_id());
                    acl.setProp_id(p.getProp_id());
                    acl.setPrinciple_id(g.getGroupid());
                    acl.setPrinciple_type("GROUP");
                    acl.setUpdate_id(UserSessionBean.getUserSession().getUser().getUserid());
                    acl.setApplication_id(Global.getConfigValue("app.applicationid_list"));
                    if (this.checkGroupInSelectedGroup(g)) {
                        acl.setPermission(1);
                    } else {
                        acl.setPermission(0);
                    }
                    acl = Access_list.create(acl);
                } else {
                    acl.setUpdate_id(UserSessionBean.getUserSession().getUser().getUserid());
                    acl.setUpdate_time(new java.util.Date());
                    if (this.checkGroupInSelectedGroup(g)) {
                        acl.setPermission(1);
                    } else {
                        acl.setPermission(0);
                    }
                    acl = Access_list.update(acl);
                }
            }
        }
    }

    /**
     * @return the allGroup
     */
    public List<Group> getAllGroup() {
        return allGroup;
    }

    /**
     * @param allGroup the allGroup to set
     */
    public void setAllGroup(List<Group> allGroup) {
        this.allGroup = allGroup;
    }

    private boolean checkGroupInSelectedGroup(Group g) {
        if (this.selectedGroups == null) {
            return false;
        }
        for (Group group : this.selectedGroups) {
            if (g.getGroupid().intValue() == group.getGroupid().intValue()) {
                return true;
            }
        }
        return false;
    }
}
