
package com.googlecode.common.admin.client.ui.role;

import com.googlecode.common.admin.client.AdminImages;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.panel.MessageBox;
import com.googlecode.common.client.ui.tree.LoadableBrowseTreeNode;
import com.googlecode.common.admin.client.AdminInjector;
import com.googlecode.common.admin.client.event.ConfigChangedEvent;
import com.googlecode.common.admin.protocol.role.RoleDTO;
import com.googlecode.common.admin.protocol.role.RoleListResponse;
import com.googlecode.common.admin.protocol.role.RoleResponse;


/**
 * Represents role root tree node.
 */
public final class RoleRootTreeNode extends LoadableBrowseTreeNode {

    private static final String         CMD_ADD = ButtonType.ADD.getCommand();

    private static final Set<String>    CMD_SET = new HashSet<String>(
            Arrays.asList(CMD_REFRESH, CMD_ADD));
    
    private final int                   systemId;
    private final RoleBrowsePanel       browsePanel;
    
    
    public RoleRootTreeNode(int systemId) {
        super("Roles", null, AdminImages.INSTANCE.role());
        
        this.systemId    = systemId;
        this.browsePanel = new RoleBrowsePanel(systemId);
    }

    @Override
    public Collection<String> getActionCommands() {
        return CMD_SET;
    }
    
    @Override
    public void actionPerformed(String cmd) {
        if (CMD_ADD.equals(cmd)) {
            onAddAction();
        } else {
            super.actionPerformed(cmd);
        }
    }
    
    private void onAddAction() {
        final MessageBox box = new MessageBox();
        box.showInput("Enter new role name", "NewRole", new Command() {
            @Override
            public void execute() {
                String name = box.getInputValue();
                if (name != null) {
                    TaskManager.INSTANCE.execute(new CreateRoleTask(name, box));
                }
            }
        });
    }
    
    @Override
    public void onLoadChildren() {
        TaskManager.INSTANCE.execute(new LoadRolesTask());
    }

    private void refreshTree(List<RoleDTO> rootDto) {
        removeAll();
        
        if (!rootDto.isEmpty()) {
            for (RoleDTO n : rootDto) {
                add(new RoleTreeNode(n, browsePanel));
            }
        } else {
            addDummyItem();
        }
        
        notifyStructureChanged();
    }
    
    private void addNew(RoleDTO dto) {
        removeDummyItem();
        
        RoleTreeNode item = new RoleTreeNode(dto, browsePanel);
        add(item);
        sortChildren();
        
        if (isOpened()) {
            item.setSelected(true);
        }
    }
    
    
    private class LoadRolesTask extends RequestTask<RoleListResponse> {

        public LoadRolesTask() {
            super("Fetching roles...");
        }

        @Override
        protected void runTask() throws Exception {
            RoleService service = GWT.create(RoleService.class);
            RequestService.prepare(service).getRoles(systemId, this);
        }

        @Override
        protected void processSuccessResponse(RoleListResponse resp) {
            refreshTree(resp.safeGetDataList());
        }
    }

    
    private class CreateRoleTask extends RequestTask<RoleResponse> {

        private final RoleDTO       dto;
        private final MessageBox    box;
        
        public CreateRoleTask(String name, MessageBox box) {
            super("Creating role...");
            
            this.dto = new RoleDTO(name);
            this.box = box;
        }

        @Override
        protected void runTask() throws Exception {
            RoleService service = GWT.create(RoleService.class);
            RequestService.prepare(service).createRole(systemId, dto, this);
        }

        @Override
        protected void processSuccessResponse(RoleResponse resp) {
            addNew(resp.getData());
            box.hide();
            
            AdminInjector.INSTANCE.eventBus().fireEvent(
                    new ConfigChangedEvent(systemId));
        }
    }

}
