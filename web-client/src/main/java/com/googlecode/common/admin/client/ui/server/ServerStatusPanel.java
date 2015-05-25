
package com.googlecode.common.admin.client.ui.server;

import java.util.Date;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.panel.LoadablePanel;
import com.googlecode.common.client.util.DateHelpers;
import com.googlecode.common.protocol.admin.ServerStatusDTO;
import com.googlecode.common.protocol.admin.ServerStatusResponse;
import com.googlecode.common.admin.protocol.server.ServerDTO;


/**
 * Server status panel.
 */
public final class ServerStatusPanel extends LoadablePanel {

    private static final Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, ServerStatusPanel> {
    }

    @UiField Anchor serverUrl;
    
    @UiField Label  startDate;
    @UiField Label  appVersion;
    @UiField Label  appBuild;
    
    @UiField Label  succeededReqCount;
    @UiField Label  failedReqCount;
    @UiField Label  activeReqCount;
    @UiField Label  maxActiveReqCount;
    
    @UiField Label  restartDate;
    @UiField Label  restartAuthorLogin;
    @UiField Label  restartCount;
    
    private final ServerBrowsePanel browsePanel;
    
    
    public ServerStatusPanel(ServerBrowsePanel browsePanel) {
        this.browsePanel = browsePanel;
        
        initWidget(binder.createAndBindUi(this));
    }
    
    @Override
    public void onDeactivated() {
        setNeedLoad(false);
    }
    
    @Override
    public void onLoadData() {
        ServerDTO dto = browsePanel.getServerDTO();
        if (dto != null) {
            serverUrl.setText(str(dto != null ? dto.getUrl() : null));
            
            TaskManager.INSTANCE.execute(new LoadStatusTask(dto));
        }
    }

    private void refreshData(ServerStatusDTO dto) {
        startDate.setText(str(dto != null ? dto.getStartDate() : null));
        appVersion.setText(str(dto != null ? dto.getAppVersion() : null));
        appBuild.setText(str(dto != null ? dto.getAppBuild() : null));
        
        succeededReqCount.setText(str(dto != null ? 
                dto.getSucceededReqCount() : null));
        failedReqCount.setText(str(dto != null ? 
                dto.getFailedReqCount() : null));
        activeReqCount.setText(str(dto != null ? 
                dto.getActiveReqCount() : null));
        maxActiveReqCount.setText(str(dto != null ? 
                dto.getMaxActiveReqCount() : null));
        
        restartDate.setText(str(dto != null ? dto.getRestartDate() : null));
        restartAuthorLogin.setText(str(dto != null ? 
                dto.getRestartAuthorLogin() : null));
        restartCount.setText(str(dto != null ? dto.getRestartCount() : null));
    }

    private String str(Object obj) {
        return (obj != null ? obj.toString() : "-");
    }
    
    private String str(Date date) {
        return (date != null ? DateHelpers.formatDate(date) : "-");
    }
    

    private class LoadStatusTask extends RequestTask<ServerStatusResponse> {

        private final ServerDTO dto;
        
        public LoadStatusTask(ServerDTO dto) {
            super("Fetching server status...");
            this.dto = dto;
        }

        @Override
        protected void runTask() throws Exception {
            ServerService service = GWT.create(ServerService.class);
            RequestService.prepare(service).getStatus(dto.safeGetId(), this);
        }

        @Override
        protected void processFailureResponse(ServerStatusResponse response) {
            refreshData(null);
            super.processFailureResponse(response);
        }
        
        @Override
        protected void processSuccessResponse(ServerStatusResponse resp) {
            refreshData(resp.getData());
        }
    }

}
