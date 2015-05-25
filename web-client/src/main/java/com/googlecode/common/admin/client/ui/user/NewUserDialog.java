
package com.googlecode.common.admin.client.ui.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.ButtonImages;
import com.googlecode.common.client.ui.LoadableComboBox;
import com.googlecode.common.client.ui.PasswordField;
import com.googlecode.common.client.ui.TextField;
import com.googlecode.common.client.ui.panel.BaseOkCancelDialog;
import com.googlecode.common.client.ui.panel.ErrorPanel;
import com.googlecode.common.client.util.StringHelpers;
import com.googlecode.common.admin.client.ui.company.CompanyService;
import com.googlecode.common.admin.protocol.company.CompanyDTO;
import com.googlecode.common.admin.protocol.company.CompanyListResponse;
import com.googlecode.common.admin.protocol.company.ContactDTO;
import com.googlecode.common.admin.protocol.user.LdapUserDTO;
import com.googlecode.common.admin.protocol.user.LdapUserListResponse;
import com.googlecode.common.admin.protocol.user.NewUserDTO;


/**
 * New user dialog.
 */
public final class NewUserDialog extends BaseOkCancelDialog {

    private static final Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, NewUserDialog> {
    }

    @UiField TextField      loginText;
    @UiField PasswordField  passwordText;
    @UiField TextField      firstNameText;
    @UiField TextField      secondNameText;
    @UiField TextField      emailText;
    @UiField TextField      phoneText;
    
    @UiField LoadableComboBox<CompanyDTO>   companyCombo;
    
    
    public NewUserDialog(CompanyDTO dto) {
        super("New User", "Save");
        
        setOkIcon(ButtonImages.INSTANCE.dbSave(), 
                ButtonImages.INSTANCE.dbSaveDisabled());
        
        setContent(binder.createAndBindUi(this));
//        content.setPixelSize(400, 350);
        
        if (dto != null) {
            companyCombo.setInitValue(dto);
            companyCombo.setEnabled(false);
        }
    
        companyCombo.setLoadCommand(new Command() {
            @Override
            public void execute() {
                TaskManager.INSTANCE.execute(new LoadCompaniesTask());
            }
        });
    }
    
    public NewUserDTO getNewUserDTO() {
        Long phoneNum = null;
        final String phone = phoneText.getText();
        if (!StringHelpers.isNullOrEmpty(phone)) {
            try {
                phoneNum = Long.parseLong(phone);
            
            } catch (Exception x) {
                ErrorPanel.show("Invalid phone number");
                return null;
            }
        }
        
        ContactDTO contact = new ContactDTO();
        contact.setFirstName(firstNameText.getText());
        contact.setSecondName(secondNameText.getText());
        contact.setEmail(emailText.getText());
        contact.setPhone(phoneNum);
        
        CompanyDTO company = companyCombo.getSelected();
        if (company == null) {
            String companyName = companyCombo.getText();
            if (StringHelpers.isNullOrEmpty(companyName)) {
                ErrorPanel.show("Invalid company name");
                return null;
            }
            
            company = new CompanyDTO();
            company.setName(companyName);
        }
        
        NewUserDTO dto = new NewUserDTO();
        dto.setLogin(loginText.getText());
        dto.setPassword(passwordText.getText());
        dto.setContact(contact);
        dto.setCompany(company);
        return dto;
    }

    public void clearCompanies() {
        companyCombo.clear();
    }
    
    void showSelectLdapDialog() {
        final SelectLdapUserDialog dlg = new SelectLdapUserDialog(null);
        dlg.setOkCommand(new Command() {
            @Override
            public void execute() {
                LdapUserDTO dto = dlg.getLdapUserDTO();
                if (dto != null) {
                    setLdapUser(dto);
                }
                dlg.hide();
            }
        });
        dlg.show();
    }
    
    private void setLdapUser(LdapUserDTO dto) {
        loginText.setValue(dto.getLogin());
        loginText.setReadOnly(true);
        firstNameText.setValue(dto.getFullName());
        
        String email = dto.safeGetMail();
        emailText.setValue(email);
        
        if (!StringHelpers.isNullOrEmpty(email)) {
            emailText.setReadOnly(true);
        } else {
            emailText.setReadOnly(false);
        }
        
        //ldapDomainText.setValue(dto.getDomain());
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
            companyCombo.setLoadedDataAndShow(resp.safeGetDataList());
        }
    }
    
    
    class LoadLdapUsersTask extends RequestTask<LdapUserListResponse> {

        public LoadLdapUsersTask() {
            super("Fetching LDAP users...");
        }

        @Override
        protected void runTask() throws Exception {
            UserService service = GWT.create(UserService.class);
            RequestService.prepare(service).getLdapUsers(this);
        }

        @Override
        protected void processFailureResponse(LdapUserListResponse response) {
            //ldapUsers = null;
            super.processFailureResponse(response);
        }
        
        @Override
        protected void processSuccessResponse(LdapUserListResponse resp) {
            //ldapUsers = resp.getDataList();
            showSelectLdapDialog();
        }
    }

}
