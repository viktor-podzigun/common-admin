
package com.googlecode.common.admin.client.ui.server;

import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.TablePanel;
import com.googlecode.common.client.ui.panel.LoadablePanel;
import com.googlecode.common.protocol.admin.ServerModuleDTO;
import com.googlecode.common.protocol.admin.ServerModulesResponse;
import com.googlecode.common.admin.protocol.server.ServerDTO;


/**
 * Server modules panel.
 */
public final class ServerModulesPanel extends LoadablePanel {

    private final TablePanel<ServerModuleDTO> paramTable;
    
    private final ListDataProvider<ServerModuleDTO> paramTableData = 
        new ListDataProvider<ServerModuleDTO>();
    
    private final ServerBrowsePanel     browsePanel;
    
    
    public ServerModulesPanel(ServerBrowsePanel browsePanel) {
        this.browsePanel = browsePanel;
        
        paramTable = new TablePanel<ServerModuleDTO>(50);
        
        // connect the params table to the data provider.
        paramTableData.addDataDisplay(paramTable);
        
        initWidget(new ScrollPanel(paramTable));
        
        initParamTableColumns();
    }
    
    private void initParamTableColumns() {
        TextColumn<ServerModuleDTO> title = new TextColumn<ServerModuleDTO>() {
            @Override
            public String getValue(ServerModuleDTO dto) {
                return dto.getTitle();
            }
        };
        
        TextColumn<ServerModuleDTO> ver = new TextColumn<ServerModuleDTO>() {
            @Override
            public String getValue(ServerModuleDTO dto) {
                return dto.getVersion();
            }
        };
    
        TextColumn<ServerModuleDTO> build = new TextColumn<ServerModuleDTO>() {
            @Override
            public String getValue(ServerModuleDTO dto) {
                return dto.getBuild();
            }
        };
    
        TextColumn<ServerModuleDTO> author = new TextColumn<ServerModuleDTO>() {
            @Override
            public String getValue(ServerModuleDTO dto) {
                return dto.getAuthor();
            }
        };
    
        // add the columns
        paramTable.addColumn(title,  "Title");
        paramTable.addColumn(ver,    "Version");
        paramTable.addColumn(build,  "Build");
        paramTable.addColumn(author, "Author");
    }

    @Override
    public void onDeactivated() {
        setNeedLoad(false);
    }
    
    @Override
    public void onLoadData() {
        ServerDTO dto = browsePanel.getServerDTO();
        if (dto != null) {
            TaskManager.INSTANCE.execute(new LoadModulesTask(dto));
        }
    }
    
    private void refreshData(List<ServerModuleDTO> params) {
        paramTableData.setList(params);
    }
    

    private class LoadModulesTask extends RequestTask<ServerModulesResponse> {

        private final ServerDTO dto;
        
        public LoadModulesTask(ServerDTO dto) {
            super("Fetching server modules...");
            this.dto = dto;
        }

        @Override
        protected void runTask() throws Exception {
            ServerService service = GWT.create(ServerService.class);
            RequestService.prepare(service).getModules(dto.safeGetId(), this);
        }

        @Override
        protected void processSuccessResponse(ServerModulesResponse resp) {
            refreshData(resp.safeGetDataList());
        }
    }

}
