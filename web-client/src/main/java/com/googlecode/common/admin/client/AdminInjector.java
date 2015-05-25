
package com.googlecode.common.admin.client;

import com.google.gwt.core.client.GWT;
import com.googlecode.common.client.app.AppInjector;
import com.googlecode.common.admin.client.ui.server.ServerBrowsePanel;
import com.googlecode.common.admin.client.ui.system.SystemBrowsePanel;


/**
 * Admin DI definitions.
 */
public interface AdminInjector extends AppInjector {
    
    static final AdminInjector INSTANCE = GWT.create(AdminInjector.class);
    
    SystemBrowsePanel systemPanel();
    
    ServerBrowsePanel serverPanel();

}
