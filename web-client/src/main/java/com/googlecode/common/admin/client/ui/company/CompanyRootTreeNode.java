
package com.googlecode.common.admin.client.ui.company;

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
import com.googlecode.common.client.ui.panel.MessageBox;
import com.googlecode.common.client.ui.tree.LoadableBrowseTreeNode;
import com.googlecode.common.admin.client.AdminImages;
import com.googlecode.common.admin.protocol.company.CompanyDTO;
import com.googlecode.common.admin.protocol.company.CompanyListResponse;
import com.googlecode.common.admin.protocol.company.CompanyResponse;


/**
 * Represents company root tree node.
 */
public final class CompanyRootTreeNode extends LoadableBrowseTreeNode {

    private static final String         CMD_ADD  = ButtonType.ADD.getCommand();
    private static final List<String>   CMD_LIST = 
        Arrays.asList(CMD_REFRESH, CMD_ADD);
    
    private final CompanyBrowsePanel browsePanel;
    private final CompanyBrowsePanel companyPanel;
    
    
    public CompanyRootTreeNode() {
        super("Users", null, AdminImages.INSTANCE.userGroup());
        
        browsePanel  = new CompanyBrowsePanel(true);
        companyPanel = new CompanyBrowsePanel(false);
    }

    @Override
    public Collection<String> getActionCommands() {
        return CMD_LIST;
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
        box.showInput("Enter new company name", "NewCompany", new Command() {
            @Override
            public void execute() {
                String name = box.getInputValue();
                if (name != null) {
                    TaskManager.INSTANCE.execute(new CreateCompanyTask(name, box));
                }
            }
        });
    }

    @Override
    public Widget getContentPanel() {
        browsePanel.setData(null, 0L);
        return browsePanel;
    }
    
    @Override
    public void onLoadChildren() {
        TaskManager.INSTANCE.execute(new LoadCompaniesTask());
    }

    private void refreshTree(List<CompanyDTO> rootDto) {
        removeAll();
        
        if (!rootDto.isEmpty()) {
            for (CompanyDTO n : rootDto) {
                add(new CompanyTreeNode(n, companyPanel));
            }
        } else {
            addDummyItem();
        }
        
        notifyStructureChanged();
    }
    
    private void addNew(CompanyDTO dto) {
        addAndSelect(new CompanyTreeNode(dto, companyPanel), true, true);
    }
    
    
    private class LoadCompaniesTask extends RequestTask<CompanyListResponse> {

        public LoadCompaniesTask() {
            super("Fetching companies...");
        }

        @Override
        protected void runTask() throws Exception {
            CompanyService service = GWT.create(CompanyService.class);
            RequestService.prepare(service).getCompanies(this);
        }

        @Override
        protected void processSuccessResponse(CompanyListResponse resp) {
            refreshTree(resp.safeGetDataList());
        }
    }


    private class CreateCompanyTask extends RequestTask<CompanyResponse> {

        private final CompanyDTO    dto;
        private final MessageBox    box;
        

        public CreateCompanyTask(String name, MessageBox box) {
            super("Creating company...");
            
            this.dto    = new CompanyDTO(name);
            this.box = box;
        }

        @Override
        protected void runTask() throws Exception {
            CompanyService service = GWT.create(CompanyService.class);
            RequestService.prepare(service).createCompany(dto, this);
        }

        @Override
        protected void processSuccessResponse(CompanyResponse resp) {
            addNew(resp.getData());
            box.hide();
        }
    }
}
