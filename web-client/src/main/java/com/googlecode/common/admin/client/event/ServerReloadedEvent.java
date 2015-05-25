
package com.googlecode.common.admin.client.event;

import com.google.web.bindery.event.shared.binder.GenericEvent;


/**
 * Fired when server is reloaded.
 */
public final class ServerReloadedEvent extends GenericEvent {
    
    private final int   systemId;
    
    
    public ServerReloadedEvent(int systemId) {
        this.systemId = systemId;
    }
    
    public int getSystemId() {
        return systemId;
    }
    
}
