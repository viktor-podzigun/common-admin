
package com.googlecode.common.admin.client.ui.system;

import com.googlecode.common.admin.client.AdminImages;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.panel.MessageBox;
import com.googlecode.common.client.ui.tree.LoadableBrowseTreeNode;
import com.googlecode.common.protocol.UserPermissions;
import com.googlecode.common.admin.protocol.system.SystemGroupDTO;
import com.googlecode.common.admin.protocol.system.SystemGroupListResponse;
import com.googlecode.common.admin.protocol.system.SystemGroupResponse;
import com.googlecode.common.admin.protocol.system.SystemPerm;


/**
 * Represents system group tree node.
 */
public final class SystemGroupRootTreeNode extends LoadableBrowseTreeNode {

    private final Set<String> cmdSet = new HashSet<String>();
    
    
    public SystemGroupRootTreeNode() {
        super("Environments", null, AdminImages.INSTANCE.system());
    
        cmdSet.add(CMD_REFRESH);
        if (UserPermissions.has(SystemPerm.CREATE)) {
            cmdSet.add(SystemGroupTreeNode.CMD_ADD);
        }
    }

    @Override
    public void onNodeClose() {
        setNeedLoad(false);
    }

    @Override
    public void onLoadChildren() {
        TaskManager.INSTANCE.execute(new LoadGroupsTask());
    }

    @Override
    public Collection<String> getActionCommands() {
        return cmdSet;
    }
    
    @Override
    public void actionPerformed(String cmd) {
        if (SystemGroupTreeNode.CMD_ADD.equals(cmd)) {
            onAddAction(null);
        } else {
            super.actionPerformed(cmd);
        }
    }
    
    void onAddAction(final SystemTreeNode parent) {
        final MessageBox dlg = new MessageBox();
        dlg.showInput("Enter new environment name", "", new Command() {
            @Override
            public void execute() {
                TaskManager.INSTANCE.execute(new CreateGroupTask(dlg, 
                        new SystemGroupDTO(null, dlg.getInputValue())));
            }
        });
    }
    
    private void refreshTree(List<SystemGroupDTO> rootDto) {
        removeAll();
        
        if (!rootDto.isEmpty()) {
            for (SystemGroupDTO n : rootDto) {
                add(new SystemGroupTreeNode(n));
            }
        } else {
            addDummyItem();
        }
        
        notifyStructureChanged();
    }
    
    private void addGroup(SystemGroupDTO dto) {
        addAndSelect(new SystemGroupTreeNode(dto), false, true);
    }
    
    
    private class LoadGroupsTask extends RequestTask<SystemGroupListResponse> {

        public LoadGroupsTask() {
            super("Fetching groups...");
        }

        @Override
        protected void runTask() throws Exception {
            SystemService service = GWT.create(SystemService.class);
            RequestService.prepare(service).getGroups(this);
        }

        @Override
        protected void processSuccessResponse(SystemGroupListResponse resp) {
            refreshTree(resp.safeGetDataList());
        }
    }


    private class CreateGroupTask extends RequestTask<SystemGroupResponse> {

        private final MessageBox        dlg;
        private final SystemGroupDTO    dto;
        
        public CreateGroupTask(MessageBox dlg, SystemGroupDTO dto) {
            super("Creating group...");
            
            this.dlg = dlg;
            this.dto = dto;
        }

        @Override
        protected void runTask() throws Exception {
            SystemService service = GWT.create(SystemService.class);
            RequestService.prepare(service).createGroup(dto, this);
        }

        @Override
        protected void processSuccessResponse(SystemGroupResponse resp) {
            dlg.hide();
            addGroup(resp.getData());
        }
    }

}
