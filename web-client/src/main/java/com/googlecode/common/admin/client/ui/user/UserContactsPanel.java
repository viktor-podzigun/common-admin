
package com.googlecode.common.admin.client.ui.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.admin.client.ui.company.CompanyService;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.panel.LoadablePanel;
import com.googlecode.common.client.util.StringHelpers;
import com.googlecode.common.admin.protocol.company.ContactDTO;
import com.googlecode.common.admin.protocol.company.ContactResponse;
import com.googlecode.common.admin.protocol.user.UserDTO;


/**
 * User contacts panel.
 */
public final class UserContactsPanel extends LoadablePanel {

    private final TextBox   firstNameText   = new TextBox();
    private final TextBox   middleNameText  = new TextBox();
    private final TextBox   secondNameText  = new TextBox();
    private final TextBox   emailText       = new TextBox();
    private final TextBox   phoneText       = new TextBox();
    
    private UserDTO         userDto;
    
    
    public UserContactsPanel() {
        Widget contentPanel = createFormPanel();
        contentPanel.setPixelSize(450, 250);
        
        initWidget(contentPanel);
    }
    
    private Widget createFormPanel() {
        FlexTable table = new FlexTable();

        firstNameText.setWidth("100%");
        firstNameText.setReadOnly(true);
        table.setHTML(0, 0, "First name:");
        table.setWidget(0, 1, firstNameText);
        
        middleNameText.setWidth("100%");
        middleNameText.setReadOnly(true);
        table.setHTML(1, 0, "Middle name:");
        table.setWidget(1, 1, middleNameText);
        
        secondNameText.setWidth("100%");
        secondNameText.setReadOnly(true);
        table.setHTML(2, 0, "Second name:");
        table.setWidget(2, 1, secondNameText);
        
        emailText.setWidth("100%");
        emailText.setReadOnly(true);
        table.setHTML(3, 0, "E-mail:");
        table.setWidget(3, 1, emailText);
        
        phoneText.setWidth("100%");
        phoneText.setReadOnly(true);
        table.setHTML(4, 0, "Phone:");
        table.setWidget(4, 1, phoneText);
        
        return table;
    }
    
    private void updateContacts(ContactDTO dto) {
        firstNameText.setText(dto != null ? dto.getFirstName() : "");
        middleNameText.setText(dto != null ? dto.getMiddleName() : "");
        secondNameText.setText(dto != null ? dto.getSecondName() : "");
        emailText.setText(dto != null ? dto.getEmail() : "");
        phoneText.setText(dto != null ? 
                StringHelpers.str(dto.getPhone()) : "");
    }

    public void setUserDTO(UserDTO userDto) {
        this.userDto = userDto;
        setNeedLoad(true);
    }
    
    @Override
    public void onDeactivated() {
        setNeedLoad(false);
    }
    
    @Override
    public void onLoadData() {
        if (userDto != null) {
            TaskManager.INSTANCE.execute(new LoadContactsTask());
        }
    }


    private class LoadContactsTask extends RequestTask<ContactResponse> {
        
        public LoadContactsTask() {
            super("Fetching contacts...");
        }
        
        @Override
        protected void runTask() throws Exception {
            CompanyService service = GWT.create(CompanyService.class);
            RequestService.prepare(service).getContact(
                    userDto.safeGetContactId(), this);
        }
        
        @Override
        protected void processFailureResponse(ContactResponse response) {
            updateContacts(null);
            super.processFailureResponse(response);
        }
        
        @Override
        protected void processSuccessResponse(ContactResponse resp) {
            updateContacts(resp.getData());
        }
    }

}
