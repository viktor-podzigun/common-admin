
package com.googlecode.common.admin.protocol.system;

import com.googlecode.common.protocol.Requests;


/**
 * Contains systems requests' path constants.
 */
public final class SystemRequests extends Requests {

    public static final String  GET_GROUP_LIST      = "/system/group/list";
    public static final String  CREATE_GROUP        = "/system/group";
    public static final String  UPDATE_GROUP        = "/system/group";

    public static final String  GET_SYSTEM_LIST     = "/system/{id}/list";
    public static final String  CREATE_SYSTEM       = "/system/{id}/create";
    public static final String  UPDATE_SYSTEM       = "/system/update";

    public static final String  GET_USER_SYSTEMS    = "/system/list/user/{id}";
    public static final String  UPDATE_USER_SYSTEMS = "/system/list/user/{id}/{isAdded}";
    
    
    // Parameters names
    
    public static final String  PARAM_IS_ADDED      = "isAdded";
    
    
    private SystemRequests() {
    }

}
