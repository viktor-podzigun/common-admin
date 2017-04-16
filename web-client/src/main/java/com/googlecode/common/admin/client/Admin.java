
package com.googlecode.common.admin.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.googlecode.common.admin.client.ui.company.CompanyRootTreeNode;
import com.googlecode.common.client.app.AbstractClientApp;
import com.googlecode.common.client.app.AppMainPanel;
import com.googlecode.common.client.app.TestingEventBus;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.ui.tree.BrowseTreeNode;
import com.googlecode.common.protocol.UserPermissions;
import com.googlecode.common.protocol.login.LoginRespDTO;
import com.googlecode.common.admin.client.event.ConfigChangedEvent;
import com.googlecode.common.admin.client.event.ServerReloadedEvent;
import com.googlecode.common.admin.client.ui.system.SystemGroupRootTreeNode;
import com.googlecode.common.admin.client.ui.system.SystemGroupTreeNode;
import com.googlecode.common.admin.protocol.AdminPerm;
import com.googlecode.common.admin.protocol.system.SystemPerm;
import com.googlecode.common.admin.protocol.user.UserPerm;


/**
 * Defines application entry point <code>onModuleLoad()</code>.
 */
public class Admin extends AbstractClientApp {

    
    @Override
    protected AppMainPanel createUI(LoginRespDTO loginDto) {
        if (!GWT.isProdMode()) {
            TestingEventBus.regEvent(ConfigChangedEvent.class);
            TestingEventBus.regEvent(ServerReloadedEvent.class);
            
            TestingEventBus.trace(AdminInjector.INSTANCE.eventBus());
        }
        
        updateUserInfo(loginDto);
        
        AppMainPanel mainPanel = AdminInjector.INSTANCE.appPanel();
        mainPanel.setCopyright(loginDto.getAppMenu().getTitle());
        
        BrowseTreeNode root = mainPanel.getBrowsePanel()
                .getTreePanel().getRoot();
        
        if (UserPermissions.has(UserPerm.READ)) {
            root.add(new CompanyRootTreeNode());
        }
        
        SystemGroupRootTreeNode env = null;
        if (UserPermissions.has(SystemPerm.READ)) {
            // add Applications node
            root.add(new SystemGroupTreeNode(null));
            
            // add Environments node
            env = new SystemGroupRootTreeNode();
            root.add(env);
        }
        
        root.notifyStructureChanged();
        
        // open env sub-tree
        CellTree tree = mainPanel.getBrowsePanel().getTreePanel()
                .getBrowseTree();
        TreeNode rootNode = tree.getRootTreeNode();
        for (int i = 0, count = rootNode.getChildCount(); i < count; i++) {
            if (rootNode.getChildValue(i) == env) {
                rootNode.setChildOpen(i, true);
                break;
            }
        }
        
        return mainPanel;
    }
    
    private void updateUserInfo(LoginRespDTO loginDto) {
        // apply received user permissions
        UserPermissions.apply(loginDto.safeIsSuperUser(), 
                AdminPerm.ROOT, loginDto.safeGetPermissions());
        
        // set user token
        RequestService.INSTANCE.setUserLogin(loginDto.getLogin());
    }
    
}
