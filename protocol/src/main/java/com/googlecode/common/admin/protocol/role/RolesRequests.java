
package com.googlecode.common.admin.protocol.role;

import com.googlecode.common.protocol.Requests;


/**
 * Contains roles requests path's constants.
 */
public final class RolesRequests extends Requests {

    public static final String  GET_ROLES               = "/role/{id}";
    public static final String  CREATE_ROLE             = "/role/{id}";
    public static final String  GET_ALL_PERMISSIONS     = "/role/{id}/perm/{bitIndex}/all";
    public static final String  GET_ALLOWED_PERMISSIONS = "/role/{id}/perm/{bitIndex}/allowed";
    public static final String  UPDATE_ROLE_PERMISSIONS = "/role/{id}/perm/update";
    public static final String  RENAME_ROLE             = "/role/rename";
    
    // Params
    
    public static final String  PARAM_BIT_INDEX = "bitIndex";
    
    
    private RolesRequests() {
    }

}
