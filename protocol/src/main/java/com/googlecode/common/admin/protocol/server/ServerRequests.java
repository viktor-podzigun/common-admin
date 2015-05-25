
package com.googlecode.common.admin.protocol.server;

import com.googlecode.common.protocol.Requests;


/**
 * Contains admin requests' path constants.
 */
public final class ServerRequests extends Requests {

    public static final String  GET_APP_SERVERS = "/server/{id}/list";
    
    public static final String  CREATE_SERVER   = "/server/{id}/create";
    public static final String  UPDATE_SERVER   = "/server/update";
    
    public static final String  GET_STATUS      = "/server/{id}/status";
    public static final String  GET_MODULES     = "/server/{id}/modules";
    public static final String  RELOAD_SERVERS  = "/server/{id}/reload";
    
    
    private ServerRequests() {
    }
    
}
