
package com.googlecode.common.admin.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.googlecode.common.admin.client.AdminImages;
import com.googlecode.common.admin.client.ui.system.UserSystemsPanel;
import com.googlecode.common.admin.client.ui.user.NewUserDialog;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.admin.client.ui.user.AbstractUserPanel;
import com.googlecode.common.admin.client.ui.user.UserContactsPanel;
import com.googlecode.common.admin.client.ui.user.UserService;
import com.googlecode.common.admin.protocol.company.CompanyDTO;
import com.googlecode.common.admin.protocol.user.NewUserDTO;
import com.googlecode.common.admin.protocol.user.UserDTO;
import com.googlecode.common.admin.protocol.user.UserListReqDTO;
import com.googlecode.common.admin.protocol.user.UserListResponse;


/**
 * Company users browse panel.
 */
public final class CompanyBrowsePanel extends AbstractUserPanel {

    private final UserContactsPanel     contactsPanel;
    private final UserSystemsPanel systemsPanel;
    
    private CompanyDTO                  companyDTO;
    private long                        companyId = -1L;
    
    
    public CompanyBrowsePanel(boolean allUsers) {
        super(allUsers, false);

        systemsPanel = new UserSystemsPanel();
        tabPanel.add(systemsPanel, "Applications", AdminImages.INSTANCE.system());

        contactsPanel = new UserContactsPanel();
        tabPanel.add(contactsPanel, "Contacts", AdminImages.INSTANCE.contacts());
    }

    public void setData(CompanyDTO dto, long companyId) {
        this.companyDTO = dto;
        this.companyId = companyId;
    }
    
    @Override
    public void onLoadData() {
        if (companyId != -1L) {
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
        final NewUserDialog dlg = new NewUserDialog(companyDTO);
        dlg.setOkCommand(new Command() {
            @Override
            public void execute() {
                NewUserDTO dto = dlg.getNewUserDTO();
                if (dto != null) {
                    TaskManager.INSTANCE.execute(new CreateUserTask(dlg, dto, 
                            0, companyId));
                }
            }
        });
        dlg.show();
    }
    
    @Override
    protected void onUserSelected(UserDTO dto) {
        contactsPanel.setUserDTO(dto);
        systemsPanel.setData(dto.safeGetId());
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
            RequestService.prepare(service).getCompanyUsers(companyId, 
                    dto, this);
        }
        
        @Override
        protected void processSuccessResponse(UserListResponse resp) {
            refreshData(dto.safeGetStartIndex(), dto.safeGetLimit(), resp);
        }
    }

}
