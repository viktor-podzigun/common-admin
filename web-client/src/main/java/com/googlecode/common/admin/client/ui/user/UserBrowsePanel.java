
package com.googlecode.common.admin.client.ui.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.googlecode.common.admin.client.AdminImages;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.admin.protocol.user.NewUserDTO;
import com.googlecode.common.admin.protocol.user.UserDTO;
import com.googlecode.common.admin.protocol.user.UserListReqDTO;
import com.googlecode.common.admin.protocol.user.UserListResponse;


/**
 * Users browse panel.
 */
public final class UserBrowsePanel extends AbstractUserPanel {

    private final UserPermissionsPanel  permissionsPanel;
    private final UserContactsPanel     contactsPanel;
    
    private final int                   systemId;
    private long                        currGroupId = -1L;
    
    
    public UserBrowsePanel(int systemId, boolean allUsers) {
        super(allUsers, true);
        
        this.systemId = systemId;
        
        contactsPanel = new UserContactsPanel();
        tabPanel.add(contactsPanel, "Contacts", 
                AdminImages.INSTANCE.contacts());
        
        permissionsPanel = new UserPermissionsPanel(systemId, false);
        tabPanel.add(permissionsPanel, "Permissions", 
                AdminImages.INSTANCE.key());
    }

    public void setData(long groupId) {
        currGroupId = groupId;
        if (!allUsers) {
            setNeedLoad(true);
        }
    }
    
    @Override
    public void onLoadData() {
        if (currGroupId != -1L) {
            TaskManager.INSTANCE.execute(new LoadUsersTask(null, 
                    DEFAULT_PAGE_SIZE));
        }
    }
    
    @Override
    protected void onUserRangeChange(int start, int length) {
        TaskManager.INSTANCE.execute(new LoadUsersTask(start, length));
    }
    
    @Override
    protected void onAddAction() {
        final NewUserDialog dlg = new NewUserDialog(null);
        dlg.setOkCommand(new Command() {
            @Override
            public void execute() {
                NewUserDTO dto = dlg.getNewUserDTO();
                if (dto != null) {
                    TaskManager.INSTANCE.execute(new CreateUserTask(dlg, dto,
                            systemId, currGroupId));
                }
            }
        });
        dlg.show();
    }
    
    @Override
    protected void onUserSelected(UserDTO dto) {
        contactsPanel.setUserDTO(dto);
        permissionsPanel.setData(dto.safeGetId());
        tabPanel.reloadActiveTab();
        
        super.onUserSelected(dto);
    }
    

    private class LoadUsersTask extends RequestTask<UserListResponse> {

        private final UserListReqDTO    dto;
        
        public LoadUsersTask(Integer startIndex, int limit) {
            super("Fetching users...");
        
            dto = new UserListReqDTO();
            dto.setStartIndex(startIndex);
            dto.setLimit(limit);
        }
        
        @Override
        protected void runTask() throws Exception {
            UserService service = GWT.create(UserService.class);
            RequestService.prepare(service).getSystemUsers(systemId, 
                    currGroupId, dto, this);
        }
        
        @Override
        protected void processSuccessResponse(UserListResponse resp) {
            refreshData(dto.safeGetStartIndex(), dto.safeGetLimit(), resp);
        }
    }

}
