
package com.googlecode.common.admin.protocol.user;

import com.googlecode.common.protocol.Requests;


/**
 * Contains users requests' path constants.
 */
public final class UserRequests extends Requests {

    public static final String  GET_COMPANY_USERS   = "/user/list/company/{id}";
    public static final String  GET_SYSTEM_USERS    = "/user/list/{id}/{groupId}";
    public static final String  CREATE_NEW_USER     = "/user/{id}/user/{groupId}";
    public static final String  CREATE_NEW_GROUP    = "/user/{id}/group/{groupId}";
    public static final String  GET_ALL_GROUPS      = "/user/{id}/groups";
    public static final String  DELETE_GROUP        = "/user/group/{id}/delete";
    public static final String  RENAME_GROUP        = "/user/group/rename";
    public static final String  MOVE_USERS          = "/user/move";
    public static final String  CHANGE_PASSWORD     = "/user/changepass";
    public static final String  GET_ALL_LDAP_USERS  = "/user/list/ldap";
    
    public static final String  GET_USER_ROLES      = "/user/{id}/roles/user/{userId}/{isAllPerm}";
    public static final String  GET_GROUP_ROLES     = "/user/{id}/roles/group/{groupId}/{isAllPerm}";
    
    public static final String  UPDATE_USER_ROLES   = "/user/roles/user";
    public static final String  UPDATE_GROUP_ROLES  = "/user/roles/group";
    
    
    // Parameters names
    
    public static final String  PARAM_ALL_PERM      = "isAllPerm";
    public static final String  PARAM_GROUP         = "groupId";
    public static final String  PARAM_USER          = "userId";
    
    
    private UserRequests() {
    }

}
