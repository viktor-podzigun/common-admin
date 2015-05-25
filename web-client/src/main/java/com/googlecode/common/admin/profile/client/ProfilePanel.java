
package com.googlecode.common.admin.profile.client;

import java.io.IOException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.PasswordField;
import com.googlecode.common.client.ui.TextField;
import com.googlecode.common.client.ui.panel.ErrorPanel;
import com.googlecode.common.client.ui.panel.MessageBox;
import com.googlecode.common.client.util.StringHelpers;
import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.admin.protocol.company.ContactDTO;
import com.googlecode.common.admin.protocol.profile.ProfileDTO;


/**
 * User profile info panel.
 */
public final class ProfilePanel extends Composite {

    private static Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, ProfilePanel> {
    }
    
    @UiField Anchor         returnUrl;
    
    @UiField TextField      firstNameText;
    @UiField TextField      secondNameText;
    @UiField TextField      emailText;
    @UiField TextField      phoneText;
    @UiField TextField      companyText;
    @UiField PasswordField  oldPassText;
    @UiField PasswordField  newPassText;
    @UiField PasswordField  newPassRepeatText;
    
    private ProfileDTO      currDto;
    
    
    public ProfilePanel() {
        initWidget(binder.createAndBindUi(this));
        
        final String targetUrl = Window.Location.getParameter("continue");
        if (targetUrl != null) {
            returnUrl.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    event.preventDefault();
                    
                    Window.Location.replace(URL.decode(targetUrl));
                }
            });
        } else {
            returnUrl.setVisible(false);
        }
    }
    
    public void setProfileDTO(ProfileDTO dto) {
        ContactDTO contactDto = dto.getContact();
        this.currDto = dto;
        
        firstNameText.setText(contactDto.getFirstName());
        secondNameText.setText(contactDto.getSecondName());
        emailText.setText(contactDto.getEmail());
        
        Long phone = contactDto.getPhone();
        if (phone != null) {
            phoneText.setText(String.valueOf(phone));
        }
        
        companyText.setText(contactDto.getCompanyName());
    }

    private ProfileDTO getProfileDTO() {
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
        contact.setId(currDto.getContact().safeGetId());
        contact.setFirstName(firstNameText.getText());
        contact.setSecondName(secondNameText.getText());
        contact.setEmail(emailText.getText());
        contact.setPhone(phoneNum);
        
        ProfileDTO dto = new ProfileDTO();
        dto.setContact(contact);
        
        String newPass = StringHelpers.trim(newPassText.getText());
        if (!newPass.isEmpty()) {
            String newPassRepeat = StringHelpers.trim(
                    newPassRepeatText.getText());
            if (!newPass.equals(newPassRepeat)) {
                ErrorPanel.show(
                        "New password doesn't match with repeat password");
                return null;
            }
            
            dto.setOldPass(oldPassText.getText());
            dto.setNewPass(newPass);
        }
        
        return dto;
    }

    @UiHandler("btnUpdate")
    void onUpdateClick(ClickEvent event) {
        ProfileDTO dto = getProfileDTO();
        if (dto != null) {
            TaskManager.INSTANCE.execute(new UpdateTask(dto));
        }
    }
    

    private class UpdateTask extends RequestTask<BaseResponse> {

        private final ProfileDTO    dto;
        
        public UpdateTask(ProfileDTO dto) {
            super("Updating profile...");
            this.dto = dto;
        }

        @Override
        protected void runTask() throws IOException {
            ProfileService service = GWT.create(ProfileService.class);
            RequestService.prepare(service).update(dto, this);
        }

        @Override
        protected void processSuccessResponse(BaseResponse resp) {
            MessageBox.showMessage("Profile was successfully updated");
        }
    }

}
