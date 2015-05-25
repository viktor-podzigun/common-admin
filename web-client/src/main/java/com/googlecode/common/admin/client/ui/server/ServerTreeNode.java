
package com.googlecode.common.admin.client.ui.server;

import com.googlecode.common.admin.client.AdminInjector;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.tree.BrowseTreeItem;
import com.googlecode.common.admin.protocol.server.ServerDTO;
import com.googlecode.common.admin.protocol.server.ServerResponse;


/**
 * Represents server node in the servers subtree.
 */
public final class ServerTreeNode extends BrowseTreeItem {
    
    private static final String         CMD_EDIT = ButtonType.EDIT.getCommand();
    private static final List<String>   CMD_LIST = 
        Arrays.asList(CMD_REFRESH, CMD_EDIT);
    
    private final ServerRootTreeNode    root;
    private final ServerDTO             dto;
    
    
    public ServerTreeNode(ServerRootTreeNode root, ServerDTO dto) {
        super(dto.getName(), AdminInjector.INSTANCE.serverPanel(),
                root.getIcon());
        
        this.root = root;
        this.dto  = dto;
    }
    
    public ServerDTO getServerDTO() {
        return dto;
    }
    
    @Override
    public Widget getContentPanel() {
        Widget contentPanel = super.getContentPanel();
        ((ServerBrowsePanel)contentPanel).setServerDTO(root.getSystemId(), dto);
        return contentPanel;
    }
    
    @Override
    public Collection<String> getActionCommands() {
        return CMD_LIST;
    }
    
    @Override
    public void actionPerformed(String cmd) {
        if (CMD_EDIT.equals(cmd)) {
            onEditAction();
        } else {
            super.actionPerformed(cmd);
        }
    }
    
    private void onEditAction() {
        final ServerDTO currDto = dto;
        final EditServerDialog dlg = new EditServerDialog(currDto);
        dlg.setOkCommand(new Command() {
            @Override
            public void execute() {
                ServerDTO dto = dlg.getServerDTO();
                dto.setId(currDto.safeGetId());
                TaskManager.INSTANCE.execute(new UpdateServerTask(dlg, dto));
            }
        });
        dlg.show();
    }
    
    private void updateData(ServerDTO dto) {
        ServerDTO currDto = this.dto;
        currDto.setName(dto.getName());
        currDto.setUrl(dto.getUrl());
        
        // update tree item text
        setText(dto.getName());
    }
    

    private class UpdateServerTask extends RequestTask<ServerResponse> {

        private final EditServerDialog  dlg;
        private final ServerDTO         dto;
        
        public UpdateServerTask(EditServerDialog dlg, ServerDTO dto) {
            super("Updating server info...");
            
            this.dlg = dlg;
            this.dto = dto;
        }

        @Override
        protected void runTask() throws Exception {
            ServerService service = GWT.create(ServerService.class);
            RequestService.prepare(service).updateServer(dto, this);
        }

        @Override
        protected void processSuccessResponse(ServerResponse resp) {
            dlg.hide();
            updateData(resp.getData());
        }
    }

}

