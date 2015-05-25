
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
import com.googlecode.common.client.ui.ButtonImages;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.panel.MessageBox;
import com.googlecode.common.client.ui.tree.LoadableBrowseTreeNode;
import com.googlecode.common.protocol.UserPermissions;
import com.googlecode.common.admin.protocol.system.SystemDTO;
import com.googlecode.common.admin.protocol.system.SystemGroupDTO;
import com.googlecode.common.admin.protocol.system.SystemGroupResponse;
import com.googlecode.common.admin.protocol.system.SystemListResponse;
import com.googlecode.common.admin.protocol.system.SystemPerm;
import com.googlecode.common.admin.protocol.system.SystemResponse;


/**
 * Represents system group tree node.
 */
public final class SystemGroupTreeNode extends LoadableBrowseTreeNode {

    static final String     CMD_ADD  = ButtonType.ADD.getCommand();
    static final String     CMD_EDIT = ButtonType.EDIT.getCommand();
    
    private final Set<String>       cmdSet = new HashSet<String>();
    private final SystemGroupDTO    groupDto;
    
    
    public SystemGroupTreeNode(SystemGroupDTO groupDto) {
        super(groupDto != null ? groupDto.getName() : "Applications", null, 
                groupDto != null ? ButtonImages.INSTANCE.folder() 
                        : AdminImages.INSTANCE.system());
        
        this.groupDto = groupDto;
        
        cmdSet.add(CMD_REFRESH);
        if (UserPermissions.has(SystemPerm.CREATE)) {
            cmdSet.add(CMD_ADD);
        }
        
        if (groupDto != null && UserPermissions.has(SystemPerm.UPDATE)) {
            cmdSet.add(CMD_EDIT);
        }
    }
    
    @Override
    public void onNodeClose() {
        setNeedLoad(false);
    }

    @Override
    public void onLoadChildren() {
        TaskManager.INSTANCE.execute(new LoadSystemsTask());
    }

    @Override
    public Collection<String> getActionCommands() {
        return cmdSet;
    }
    
    @Override
    public void actionPerformed(String cmd) {
        if (CMD_ADD.equals(cmd)) {
            onAddAction();
        
        } else if (CMD_EDIT.equals(cmd)) {
            onEditAction();
        } else {
            super.actionPerformed(cmd);
        }
    }
    
    private void onAddAction() {
        final EditSystemDialog dlg = new EditSystemDialog(null);
        dlg.setOkCommand(new Command() {
            @Override
            public void execute() {
                SystemDTO dto = dlg.getSystemDTO();
                TaskManager.INSTANCE.execute(new CreateSystemTask(dlg, dto));
            }
        });
        dlg.show();
    }
    
    private void onEditAction() {
        final MessageBox dlg = new MessageBox();
        dlg.showInput("Enter new name", groupDto.getName(), new Command() {
            @Override
            public void execute() {
                TaskManager.INSTANCE.execute(new UpdateGroupTask(dlg, 
                        new SystemGroupDTO(groupDto.safeGetId(), 
                                dlg.getInputValue())));
            }
        });
    }
    
    private void refreshTree(List<SystemDTO> rootDto) {
        removeAll();
        
        if (!rootDto.isEmpty()) {
            for (SystemDTO n : rootDto) {
                add(new SystemTreeNode(n));
            }
        } else {
            addDummyItem();
        }
        
        notifyStructureChanged();
    }
    
    private void addSystem(SystemDTO dto) {
        addAndSelect(new SystemTreeNode(dto), false, true);
    }
    
    private void updateGroup(SystemGroupDTO dto) {
        groupDto.setName(dto.getName());
        
        setText(dto.getName());
        sortChildren();
    }

    
    private class LoadSystemsTask extends RequestTask<SystemListResponse> {

        public LoadSystemsTask() {
            super("Fetching systems...");
        }

        @Override
        protected void runTask() throws Exception {
            SystemService service = GWT.create(SystemService.class);
            RequestService.prepare(service).getSystems(
                    groupDto != null ? groupDto.safeGetId() : 0L, this);
        }

        @Override
        protected void processSuccessResponse(SystemListResponse resp) {
            refreshTree(resp.safeGetDataList());
        }
    }


    private class CreateSystemTask extends RequestTask<SystemResponse> {

        private final EditSystemDialog  dlg;
        private final SystemDTO         dto;
        
        public CreateSystemTask(EditSystemDialog dlg, SystemDTO dto) {
            super("Creating system...");
            
            this.dlg = dlg;
            this.dto = dto;
        }

        @Override
        protected void runTask() throws Exception {
            SystemService service = GWT.create(SystemService.class);
            RequestService.prepare(service).createSystem(
                    groupDto != null ? groupDto.safeGetId() : 0L, dto, this);
        }

        @Override
        protected void processSuccessResponse(SystemResponse resp) {
            dlg.hide();
            addSystem(resp.getData());
        }
    }

    
    private class UpdateGroupTask extends RequestTask<SystemGroupResponse> {

        private final MessageBox        dlg;
        private final SystemGroupDTO    dto;
        
        public UpdateGroupTask(MessageBox dlg, SystemGroupDTO dto) {
            super("Updating group...");
            
            this.dlg = dlg;
            this.dto = dto;
        }

        @Override
        protected void runTask() throws Exception {
            SystemService service = GWT.create(SystemService.class);
            RequestService.prepare(service).updateGroup(dto, this);
        }

        @Override
        protected void processSuccessResponse(SystemGroupResponse resp) {
            dlg.hide();
            
            updateGroup(resp.getData());
        }
    }
    
}
