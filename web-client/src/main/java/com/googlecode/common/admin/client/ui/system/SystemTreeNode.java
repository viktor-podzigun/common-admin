
package com.googlecode.common.admin.client.ui.system;

import com.googlecode.common.admin.client.AdminImages;
import com.googlecode.common.admin.client.AdminInjector;
import com.googlecode.common.admin.client.ui.role.RoleRootTreeNode;
import com.googlecode.common.admin.client.ui.server.ServerRootTreeNode;
import com.googlecode.common.admin.client.ui.user.UserGroupRootTreeNode;
import java.util.Collection;
import java.util.Collections;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.ui.tree.BrowseTreeNode;
import com.googlecode.common.client.ui.tree.LoadableBrowseTreeNode;
import com.googlecode.common.admin.protocol.system.SystemDTO;


/**
 * Represents system node in the system groups sub-tree.
 */
public final class SystemTreeNode extends LoadableBrowseTreeNode {

    private final SystemDTO dto;
    private boolean         isLoaded;
    
    
    public SystemTreeNode(SystemDTO dto) {
        super(dto.getName(), AdminInjector.INSTANCE.systemPanel(),
                AdminImages.INSTANCE.system());
        
        this.dto  = dto;
    }
    
    public SystemDTO getSystemDTO() {
        return dto;
    }
    
    @Override
    public Widget getContentPanel() {
        Widget browsePanel = super.getContentPanel();
        ((SystemBrowsePanel)browsePanel).setSystemDTO(dto);
        return browsePanel;
    }
    
    @Override
    public void onNodeClose() {
        setNeedLoad(false);
    }
    
    @Override
    public void onLoadChildren() {
        if (!isLoaded) {
            isLoaded = true;
            
            final int systemId = dto.safeGetId();
            add(new UserGroupRootTreeNode(systemId));
            
            BrowseTreeNode config = new BrowseTreeNode("Configuration", null, 
                    AdminImages.INSTANCE.config());
            config.add(new RoleRootTreeNode(systemId));
            add(config);
            
            add(new ServerRootTreeNode(systemId));
        }
    }
    
    @Override
    public Collection<String> getActionCommands() {
        return Collections.emptyList();
    }

}
