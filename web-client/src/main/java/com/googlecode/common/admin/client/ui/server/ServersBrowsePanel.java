
package com.googlecode.common.admin.client.ui.server;

import com.googlecode.common.admin.client.AdminImages;
import com.googlecode.common.admin.client.AdminInjector;
import com.googlecode.common.admin.client.event.ServerReloadedEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.ActionProvider;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.ButtonsPanel;
import com.googlecode.common.protocol.BaseResponse;


/**
 * Servers browse panel.
 */
public final class ServersBrowsePanel extends Composite implements 
        ActionProvider {

    private static final String         CMD_APPLY = "apply";
    private static final List<String>   CMD_LIST  = Arrays.asList(CMD_APPLY);
    
    private final ButtonsPanel  buttonsPanel = new ButtonsPanel();
    private final int           systemId;

    
    public ServersBrowsePanel(int systemId) {
        this.systemId = systemId;
        
        buttonsPanel.setActionProvider(this);
        buttonsPanel.addButton(new ButtonType("Apply Configuration", 
                CMD_APPLY, AdminImages.INSTANCE.serverRestart()));
        
        initWidget(buttonsPanel);
    }

    @Override
    public void actionPerformed(String cmd) {
        if (CMD_APPLY.equals(cmd)) {
            onApplyAction();
        }
    }

    @Override
    public Collection<String> getActionCommands() {
        return CMD_LIST;
    }

    private void onApplyAction() {
        TaskManager.INSTANCE.execute(new ReloadServerTask());
    }
    

    private class ReloadServerTask extends RequestTask<BaseResponse> {

        public ReloadServerTask() {
            super("Reloading servers...");
        }

        @Override
        protected void runTask() throws Exception {
            ServerService service = GWT.create(ServerService.class);
            RequestService.prepare(service).reloadServers(systemId, this);
        }

        @Override
        protected void processSuccessResponse(BaseResponse resp) {
            AdminInjector.INSTANCE.eventBus().fireEvent(
                    new ServerReloadedEvent(systemId));
        }
    }

}
