
package com.googlecode.common.admin.signin.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.common.client.app.task.DefaultTaskManagerUi;
import com.googlecode.common.client.http.RequestErrorHandler;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.AbstractTask;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.panel.ErrorPanel;


/**
 * Defines signin page entry point.
 */
public class SigninClient implements EntryPoint {

    private SigninPanel panel;
    
    
    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {
        RequestService.INSTANCE.setModuleBaseUrl("admin");
        
        GWT.log("onModuleLoad() started, baseURL: " 
                + RequestService.INSTANCE.getURL());
        
        // register uncaught exceptions handler
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void onUncaughtException(Throwable x) {
                ErrorPanel.showError(null, x);
            }
        });
        
        TaskManager.INSTANCE.setUi(new DefaultTaskManagerUi());
        
        // register our global requests error handler
        RequestService.INSTANCE.setErrorHandler(new RequestErrorHandler() {
            @Override
            public boolean handleErrorStatus(AbstractTask task, Response resp) {
                if (resp.getStatusCode() == Response.SC_UNAUTHORIZED) {
                    showPanel();
                    return true;
                }
                
                return false;
            }
        });
        
        RootPanel.get("loading").setVisible(false);
        
        showPanel();
        
        GWT.log("onModuleLoad() finished");
    }
    
    private void showPanel() {
        if (panel != null) {
            return;
        }
        
        panel = new SigninPanel() {
            @Override
            protected void onSuccessSignin(String redirectUrl) {
                doSignin(redirectUrl);
            }
        };
        
        RootPanel.get().add(panel);
    }
    
    private void doSignin(String redirectUrl) {
        if (redirectUrl == null || redirectUrl.isEmpty()) {
            Window.Location.reload();
            return;
        }
        
        // resolve relative path
        if (redirectUrl.startsWith("/")) {
            redirectUrl = RequestService.INSTANCE.getURL() + redirectUrl;
        }
        
        Window.Location.replace(redirectUrl);
    }
    
}
