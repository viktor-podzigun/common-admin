
package com.googlecode.common.admin.client.event;

import com.google.web.bindery.event.shared.binder.GenericEvent;


/**
 * Fired when server configuration is changed.
 */
public final class ConfigChangedEvent extends GenericEvent {
    
    private final int   systemId;
    
    
    public ConfigChangedEvent(int systemId) {
        this.systemId = systemId;
    }
    
    public int getSystemId() {
        return systemId;
    }
    
}
