
package com.googlecode.common.admin.profile.client;

import java.io.IOException;
import com.google.gwt.core.client.GWT;
import com.googlecode.common.client.app.AbstractClientApp;
import com.googlecode.common.client.app.AppMainPanel;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.protocol.login.LoginRespDTO;
import com.googlecode.common.admin.protocol.profile.ProfileResponse;


/**
 * Defines profile page entry point.
 */
public class ProfileClient extends AbstractClientApp {

    private final ProfilePanel  panel = new ProfilePanel();
    
    
    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {
        RequestService.INSTANCE.setModuleBaseUrl("admin");
        
        super.onModuleLoad();
    }
    
    @Override
    protected void onLoginByToken() {
        TaskManager.INSTANCE.execute(new ProfileTask());
    }
    
    @Override
    protected AppMainPanel createUI(LoginRespDTO loginDto) {
        AppMainPanel mainPanel = new AppMainPanel();
        mainPanel.setCopyright(loginDto.getAppMenu().getTitle());
        
        mainPanel.showPanel(panel);
        return mainPanel;
    }
    
    private class ProfileTask extends RequestTask<ProfileResponse> {
        
        public ProfileTask() {
            super("Loading profile...");
        }
        
        @Override
        protected void runTask() throws IOException {
            ProfileService service = GWT.create(ProfileService.class);
            RequestService.prepare(service).read(this);
        }
        
        @Override
        protected void processSuccessResponse(ProfileResponse resp) {
            panel.setProfileDTO(resp.getProfile());
            onLogin(resp.getLoginData());
        }
    }
        
}
