
package com.googlecode.common.admin.client.ui.company;

import java.util.Arrays;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.ButtonImages;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.panel.MessageBox;
import com.googlecode.common.client.ui.tree.BrowseTreeItem;
import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.protocol.RenameDTO;
import com.googlecode.common.admin.protocol.company.CompanyDTO;


/**
 * Represents company node in the companies subtree.
 */
public final class CompanyTreeNode extends BrowseTreeItem {

//    private static final String CMD_ADD     = ButtonType.ADD.getCommand();
    private static final String CMD_RENAME  = ButtonType.EDIT.getCommand();
    
    private static final List<String> CMD_LIST = 
            Arrays.asList(CMD_REFRESH, CMD_RENAME/*, CMD_ADD*/);
    
    private final CompanyDTO    dto;
    
    
    public CompanyTreeNode(CompanyDTO dto, CompanyBrowsePanel contentPanel) {
        super(dto.getName(), contentPanel, ButtonImages.INSTANCE.folder());
        this.dto  = dto;
    }
    
    @Override
    public Widget getContentPanel() {
        Widget contentPanel = super.getContentPanel();
        ((CompanyBrowsePanel)contentPanel).setData(dto, dto.safeGetId());
        return contentPanel;
    }
    
    @Override
    public List<String> getActionCommands() {
        return CMD_LIST;
    }

    @Override
    public void actionPerformed(String cmd) {
        if (CMD_RENAME.equals(cmd)) {
            onRenameAction();
        } else {
            super.actionPerformed(cmd);
        }
    }
    
    void applyNewName(String name) {
        dto.setName(name);
        setText(name);
        getParent().sortChildren();
    }

    private void onRenameAction() {
        final String oldName = this.getText();
        final long id = dto.safeGetId();
        final MessageBox box = new MessageBox();
        box.showInput("Enter new company name", oldName,  new Command() {
            @Override
            public void execute() {
                String name = box.getInputValue();
                if (name != null && !name.equals(oldName)) {
                    TaskManager.INSTANCE.execute(
                            new RenameCompanyTask(id, name, box));
                }
            }
        });
    }
    
    
    private class RenameCompanyTask extends RequestTask<BaseResponse> {

        private final RenameDTO         dto;
        private final MessageBox        box;
        
        public RenameCompanyTask(long id, String name, MessageBox box) {
            super("Renaming company...");
            
            this.dto    = new RenameDTO(id, name);
            this.box    = box;
        }

        @Override
        protected void runTask() throws Exception {
            CompanyService service = GWT.create(CompanyService.class);
            RequestService.prepare(service).renameCompany(dto, this);
        }

        @Override
        protected void processSuccessResponse(BaseResponse resp) {
            applyNewName(dto.getName());
            box.hide();
        }
    }

}
