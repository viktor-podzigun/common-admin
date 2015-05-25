
package com.googlecode.common.admin.client.ui.role;

import com.googlecode.common.admin.client.AdminImages;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.panel.MessageBox;
import com.googlecode.common.client.ui.tree.BrowseTreeItem;
import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.protocol.RenameDTO;
import com.googlecode.common.admin.protocol.role.RoleDTO;


/**
 * Represents role node in the roles subtree.
 */
public final class RoleTreeNode extends BrowseTreeItem {
    
    private static final String         CMD_RENAME = 
        ButtonType.EDIT.getCommand();
    
    private static final Set<String>    CMD_SET = new HashSet<String>(
            Arrays.asList(CMD_REFRESH, CMD_RENAME));
    
    private final RoleDTO               dto;
    
    
    public RoleTreeNode(RoleDTO dto, RoleBrowsePanel panel) {
        super(getNodeName(dto.getTitle(), dto.safeGetBitIndex()), panel, 
                AdminImages.INSTANCE.role());
        
        this.dto = dto;
    }
    
    @Override
    public Widget getContentPanel() {
        Widget contentPanel = super.getContentPanel();
        ((RoleBrowsePanel)contentPanel).setRoleDTO(dto);
        return contentPanel;
    }
    
    @Override
    public Collection<String> getActionCommands() {
        return CMD_SET;
    }

    @Override
    public void actionPerformed(String cmd) {
        if (CMD_RENAME.equals(cmd)) {
            onRenameAction();
        } else {
            super.actionPerformed(cmd);
        }
    }
    
    private void onRenameAction() {
        final String oldName = dto.getTitle();
        final MessageBox box = new MessageBox();
        box.showInput("Enter new role name", oldName, new Command() {
            @Override
            public void execute() {
                String name = box.getInputValue();
                if (name != null && !name.equals(oldName)) {
                    TaskManager.INSTANCE.execute(
                            new RenameRoleTask(dto.safeGetId(), name, box));
                }
            }
        });
    }
    
    private static String getNodeName(String role, int index) {
        return role + " [" + index + "]";
    }
    
    private void applyNewName(String name) {
        dto.setTitle(name);
        setText(getNodeName(name, dto.safeGetBitIndex()));
        getParent().sortChildren();
    }


    private class RenameRoleTask extends RequestTask<BaseResponse> {

        private final RenameDTO     dto;
        private final MessageBox    box;
        
        public RenameRoleTask(long id, String name, MessageBox box) {
            super("Renaming role...");
            
            dto = new RenameDTO(id, name);
            this.box = box;
        }

        @Override
        protected void runTask() throws IOException {
            RoleService service = GWT.create(RoleService.class);
            RequestService.prepare(service).renameRole(dto, this);
        }

        @Override
        protected void processSuccessResponse(BaseResponse resp) {
            applyNewName(dto.getName());
            box.hide();
        }
    }

}

