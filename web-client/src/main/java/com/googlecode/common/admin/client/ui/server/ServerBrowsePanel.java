
package com.googlecode.common.admin.client.ui.server;

import javax.inject.Singleton;
import com.googlecode.common.client.ui.LoadableTabPanel;
import com.googlecode.common.client.ui.panel.LoadablePanel;
import com.googlecode.common.admin.protocol.server.ServerDTO;


/**
 * Server browse panel.
 */
@Singleton
public final class ServerBrowsePanel extends LoadablePanel {
    
    private final LoadableTabPanel      tabPanel;
    private final ServerStatusPanel     statusPanel;
    private final ServerModulesPanel    modulesPanel;
    
    private int                         currSystemId;
    private ServerDTO                   currDto;
    
    
    public ServerBrowsePanel() {
        statusPanel  = new ServerStatusPanel(this);
        modulesPanel = new ServerModulesPanel(this);
        
        tabPanel = new LoadableTabPanel();
        tabPanel.add(statusPanel, "Status");
        tabPanel.add(modulesPanel, "Modules");
        
        initWidget(tabPanel);
    }
    
    public int getSystemId() {
        return currSystemId;
    }
    
    public ServerDTO getServerDTO() {
        return currDto;
    }
    
    public void setServerDTO(int systemId, ServerDTO dto) {
        this.currSystemId   = systemId;
        this.currDto        = dto;
        
        statusPanel.setNeedLoad(true);
        modulesPanel.setNeedLoad(true);
    }
    
    @Override
    public void onLoadData() {
        tabPanel.reloadActiveTab();
    }

}
