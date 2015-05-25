
package com.googlecode.common.admin.client.ui.user;

import com.googlecode.common.client.ui.LoadableTabPanel;
import com.googlecode.common.client.ui.panel.LoadablePanel;
import com.googlecode.common.admin.protocol.user.UserGroupDTO;


/**
 * Users group browse panel.
 */
public final class UserGroupBrowsePanel extends LoadablePanel {
    
    private final LoadableTabPanel      tabPanel;
    
    private final UserBrowsePanel       userBrowsePanel;
    private final UserPermissionsPanel  userPermissionsPanel;
    
    
    public UserGroupBrowsePanel(int systemId) {
        userBrowsePanel      = new UserBrowsePanel(systemId, false);
        userPermissionsPanel = new UserPermissionsPanel(systemId, true);
        
        tabPanel = new LoadableTabPanel();
        tabPanel.add(userBrowsePanel, "Users");
        tabPanel.add(userPermissionsPanel, "Permissions");
        
        initWidget(tabPanel);
    }

    public void setUserGroupDTO(UserGroupDTO dto) {
        long groupId = (dto != null ? dto.safeGetId() : 0L);
        userBrowsePanel.setData(groupId);
        userPermissionsPanel.setData(groupId);
    }
    
    @Override
    public void onLoadData() {
        tabPanel.reloadActiveTab();
    }

}
