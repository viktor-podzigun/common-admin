
package com.googlecode.common.admin.client.ui.user;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.ui.ActionProvider;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.ButtonsPanel;
import com.googlecode.common.client.ui.LoadableTabPanel;
import com.googlecode.common.client.ui.panel.LoadablePanel;
import com.googlecode.common.admin.protocol.user.NewUserDTO;
import com.googlecode.common.admin.protocol.user.UserDTO;
import com.googlecode.common.admin.protocol.user.UserListResponse;
import com.googlecode.common.admin.protocol.user.UserResponse;


/**
 * Basic user panel.
 */
public abstract class AbstractUserPanel extends LoadablePanel implements 
        ActionProvider {

    public static final int DEFAULT_PAGE_SIZE = 10;
    
    private static final Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, AbstractUserPanel> {
    }

    private static final String         CMD_ADD  = ButtonType.ADD.getCommand();
    private static final List<String>   CMD_LIST = Arrays.asList(CMD_ADD);
    
    @UiField(provided=true)
    ButtonsPanel                        buttonsPanel;
    
    @UiField(provided=true)
    protected UserGridPanel             userGrid;
    
    @UiField(provided=true)
    protected LoadableTabPanel          tabPanel;
    
    protected final boolean             allUsers;
    
    
    protected AbstractUserPanel(boolean allUsers, boolean systemUsers) {
        this.allUsers = allUsers;
        
        buttonsPanel = new ButtonsPanel(ButtonType.ADD);
        buttonsPanel.setActionProvider(this);
        
        userGrid = new UserGridPanel(allUsers, systemUsers) {
            @Override
            protected void onItemSelected(UserDTO dto) {
                onUserSelected(dto);
            }
        
            @Override
            protected void onRangeChange(int start, int length) {
                onUserRangeChange(start, length);
            }
        };
        
        tabPanel = new LoadableTabPanel();

        initWidget(binder.createAndBindUi(this));
    }

    @Override
    public Collection<String> getActionCommands() {
        return CMD_LIST;
    }

    @Override
    public void actionPerformed(String cmd) {
        if (CMD_ADD.equals(cmd)) {
            onAddAction();
        }
    }
    
    protected abstract void onAddAction();

    protected abstract void onUserRangeChange(int start, int length);
    
    protected void onUserSelected(UserDTO dto) {
        tabPanel.setVisible(true);
    }
    
    protected void refreshData(int start, int length, UserListResponse resp) {
        userGrid.refreshData(start, length, resp);
        tabPanel.setVisible(false);
    }
    
    
    protected class CreateUserTask extends RequestTask<UserResponse> {

        private final NewUserDialog dlg;
        private final NewUserDTO    dto;
        private final int           systemId;
        private final long          parentId;

        
        public CreateUserTask(NewUserDialog dlg, NewUserDTO dto, 
                int systemId, long parentId) {

            super("Creating user...");

            this.dlg = dlg;
            this.dto = dto;
            this.systemId = systemId;
            this.parentId = parentId;
        }

        @Override
        protected void runTask() throws Exception {
            UserService service = GWT.create(UserService.class);
            RequestService.prepare(service).createUser(systemId, parentId, 
                    dto, this);
        }

        @Override
        protected void processFailureResponse(UserResponse resp) {
            if (resp.getStatus() == 5122) { // Company already exists
                dlg.clearCompanies();
            }
            super.processFailureResponse(resp);
        }

        @Override
        protected void processSuccessResponse(UserResponse resp) {
            userGrid.addUser(resp.getData());
            dlg.hide();
        }
    }

}
