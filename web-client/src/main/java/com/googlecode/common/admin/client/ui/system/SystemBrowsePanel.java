
package com.googlecode.common.admin.client.ui.system;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.ActionProvider;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.ButtonsPanel;
import com.googlecode.common.protocol.UserPermissions;
import com.googlecode.common.admin.protocol.system.SystemDTO;
import com.googlecode.common.admin.protocol.system.SystemPerm;
import com.googlecode.common.admin.protocol.system.SystemResponse;


/**
 * System browse panel.
 */
public final class SystemBrowsePanel extends Composite implements 
        ActionProvider {
    
    private static final String CMD_EDIT = ButtonType.EDIT.getCommand();
    
    private static final Binder BINDER = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, SystemBrowsePanel> {
    }
    
    @UiField(provided=true)
    ButtonsPanel        buttonsPanel;
    
    @UiField(provided=true)
    SystemPanel         infoPanel;
    
    private final Set<String> cmdSet = new HashSet<String>();
    
    private SystemDTO   currDto;
    
    
    public SystemBrowsePanel() {
        if (UserPermissions.has(SystemPerm.UPDATE)) {
            cmdSet.add(CMD_EDIT);
            
            buttonsPanel = new ButtonsPanel(ButtonType.EDIT);
            buttonsPanel.setActionProvider(this);
        } else {
            buttonsPanel = new ButtonsPanel();
            buttonsPanel.setVisible(false);
        }
        
        infoPanel = new SystemPanel(true);
        
        initWidget(BINDER.createAndBindUi(this));
    }

    public void setSystemDTO(SystemDTO dto) {
        currDto = dto;
        infoPanel.setSystemDTO(dto);
    }
    
    @Override
    public Collection<String> getActionCommands() {
        return cmdSet;
    }

    @Override
    public void actionPerformed(String cmd) {
        if (CMD_EDIT.equals(cmd)) {
            onEditAction();
        }
    }

    private void onEditAction() {
        final EditSystemDialog dlg = new EditSystemDialog(currDto);
        dlg.setOkCommand(new Command() {
            @Override
            public void execute() {
                SystemDTO dto = dlg.getSystemDTO();
                dto.setId(currDto.safeGetId());
                TaskManager.INSTANCE.execute(new UpdateSystemTask(dlg, dto));
            }
        });
        dlg.show();
    }
    

    private class UpdateSystemTask extends RequestTask<SystemResponse> {

        private final EditSystemDialog  dlg;
        private final SystemDTO         dto;
        
        public UpdateSystemTask(EditSystemDialog dlg, SystemDTO dto) {
            super("Updating system...");
            
            this.dlg = dlg;
            this.dto = dto;
        }

        @Override
        protected void runTask() throws Exception {
            SystemService service = GWT.create(SystemService.class);
            RequestService.prepare(service).updateSystem(dto, this);
        }

        @Override
        protected void processSuccessResponse(SystemResponse resp) {
            dlg.hide();
            
            SystemDTO dto = resp.getData();
            currDto.setTitle(dto.getTitle());
            currDto.setUrl(dto.getUrl());
            
            setSystemDTO(dto);
        }
    }

}
