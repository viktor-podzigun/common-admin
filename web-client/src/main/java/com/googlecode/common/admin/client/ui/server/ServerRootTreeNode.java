
package com.googlecode.common.admin.client.ui.server;

import com.googlecode.common.admin.client.AdminImages;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.tree.BrowseTreeItem;
import com.googlecode.common.client.ui.tree.LoadableBrowseTreeNode;
import com.googlecode.common.admin.client.AdminInjector;
import com.googlecode.common.admin.client.event.ConfigChangedEvent;
import com.googlecode.common.admin.client.event.ServerReloadedEvent;
import com.googlecode.common.admin.protocol.server.ServerDTO;
import com.googlecode.common.admin.protocol.server.ServerListResponse;
import com.googlecode.common.admin.protocol.server.ServerResponse;


/**
 * Represents server root tree node.
 */
public final class ServerRootTreeNode extends LoadableBrowseTreeNode {
    
    private static final Events EVENTS = GWT.create(Events.class);
    interface Events extends EventBinder<ServerRootTreeNode> {
    }
    
    private static final String         CMD_ADD = ButtonType.ADD.getCommand();
    private static final List<String>   CMD_SET = 
        Arrays.asList(CMD_REFRESH, CMD_ADD);

    private final int                   systemId;
    private HandlerRegistration         eventHandlers;
    
    
    public ServerRootTreeNode(int systemId) {
        super("Servers", new ServersBrowsePanel(systemId), 
                AdminImages.INSTANCE.server());
        
        this.systemId = systemId;
    }
    
    public int getSystemId() {
        return systemId;
    }
    
    @Override
    public void addNotify() {
        super.addNotify();
        
        eventHandlers = EVENTS.bindEventHandlers(this, 
                AdminInjector.INSTANCE.eventBus());
    }
    
    @Override
    public void removeNotify() {
        super.removeNotify();
        
        if (eventHandlers != null) {
            eventHandlers.removeHandler();
            eventHandlers = null;
        }
    }

    @EventHandler
    void onConfigChanged(ConfigChangedEvent event) {
        updateIcons(event.getSystemId(), AdminImages.INSTANCE.serverWarn());
    }
    
    @EventHandler
    void onServerReloaded(ServerReloadedEvent event) {
        updateIcons(event.getSystemId(), AdminImages.INSTANCE.server());
    }
    
    private void updateIcons(long systemId, ImageResource icon) {
        if (this.systemId == systemId) {
            setIcon(icon);
            
            for (BrowseTreeItem item : getDataProvider().getList()) {
                if (item.getIcon() != icon) {
                    item.setIcon(icon);
                }
            }
        }
    }
    
    @Override
    public void onLoadChildren() {
        TaskManager.INSTANCE.execute(new LoadServersTask());
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
    
    void onAddAction() {
        final EditServerDialog dlg = new EditServerDialog(null);
        dlg.setOkCommand(new Command() {
            @Override
            public void execute() {
                TaskManager.INSTANCE.execute(new CreateServerTask(dlg));
            }
        });
        dlg.show();
    }
    
    protected void refreshTree(List<ServerDTO> rootDto) {
        removeAll();
        
        if (!rootDto.isEmpty()) {
            for (ServerDTO n : rootDto) {
                add(new ServerTreeNode(this, n));
            }
        } else {
            addDummyItem();
        }
        
        notifyStructureChanged();
    }
    
    private void addNew(ServerDTO dto) {
        addAndSelect(new ServerTreeNode(this, dto), false, true);
    }
    
    
    private class LoadServersTask extends RequestTask<ServerListResponse> {

        public LoadServersTask() {
            super("Fetching servers...");
        }

        @Override
        protected void runTask() throws IOException {
            ServerService service = GWT.create(ServerService.class);
            RequestService.prepare(service).getAppServers(systemId, this);
        }

        @Override
        protected void processSuccessResponse(ServerListResponse resp) {
            refreshTree(resp.safeGetDataList());
        }
    }

    
    private class CreateServerTask extends RequestTask<ServerResponse> {

        private final EditServerDialog  dlg;
        private final ServerDTO         dto;
        
        public CreateServerTask(EditServerDialog dlg) {
            super("Creating server...");
            
            this.dlg = dlg;
            this.dto = dlg.getServerDTO();
        }

        @Override
        protected void runTask() throws Exception {
            ServerService service = GWT.create(ServerService.class);
            RequestService.prepare(service).createServer(systemId, dto, this);
        }

        @Override
        protected void processSuccessResponse(ServerResponse resp) {
            dlg.hide();
            addNew(resp.getData());
        }
    }

}
