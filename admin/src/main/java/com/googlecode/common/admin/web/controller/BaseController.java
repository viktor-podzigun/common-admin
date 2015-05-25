
package com.googlecode.common.admin.web.controller;

import java.util.BitSet;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import com.googlecode.common.protocol.Permission;
import com.googlecode.common.service.CommonResponses;
import com.googlecode.common.service.ex.OperationFailedException;
import com.googlecode.common.web.ServletHelpers;
import com.googlecode.common.admin.domain.UserToken;
import com.googlecode.common.admin.service.AuthorizationService;
import com.googlecode.common.admin.service.PermissionManagementService;
import com.googlecode.common.admin.service.SystemService;
import com.googlecode.common.admin.service.UserManagementService;
import com.googlecode.common.admin.service.UserManagementService.RolesType;


/**
 * Base Controller
 */
public abstract class BaseController {

    @Autowired
    protected AuthorizationService      authorizationService;
    
    @Autowired
    protected UserManagementService     usersService;
    
    @Autowired
    private PermissionManagementService permissionService;
    
    @Autowired
    private SystemService               systemsService;

    
    protected static String[] getAuthInfo(HttpServletRequest req) {
        String authInfo[] = ServletHelpers.getBasicAuthInfo(req);
        if (authInfo == null || authInfo.length != 2) {
            throw new OperationFailedException(
                    CommonResponses.AUTHENTICATION_FAILED, 
                    "Authentication failed");
        }
        
        return authInfo;
    }

    /**
     * Authenticate user for the given request.
     * 
     * @param request   request object
     * @return          user object or <code>null</code> if no such found
     */
    protected UserToken getAuthenticateUserToken(HttpServletRequest request) {
        UserToken t = authorizationService.getAuthenticateUserToken(request);
        if (t == null) {
            throw new OperationFailedException(
                    CommonResponses.AUTHENTICATION_FAILED, 
                    "User authentication failed");
        }

        return t;
    }
    
    /**
     * Checks if the specified user has the specified permission.
     * 
     * @param token user token entity
     * @param p     permission to check
     * 
     * @throws OperationFailedException if the specified user doesn't have 
     *              the specified permission
     * 
     * @see #checkUserAnyPermissions(UserToken, Permission...)
     * @see #hasUserPermission(UserToken, Permission)
     */
    protected void checkUserPermission(UserToken token, Permission p) {
        checkUserPermission(usersService.getRoles(
                usersService.getSystemUserById(systemsService.getAdminSystem(), 
                        token.getUser()), RolesType.ALL), p);
    }

    private void checkUserPermission(BitSet roles, Permission p) {
        if (!hasUserPermission(roles, p)) {
            throw new OperationFailedException(CommonResponses.ACCESS_DENIED, 
                    "Access denied for specified entity");
        }
    }

    /**
     * Checks if the specified user has at least on of the specified 
     * permission(s).
     * 
     * @param token         user token entity
     * @param permissions   permission(s) to check
     * 
     * @throws OperationFailedException if the specified user doesn't have 
     *              the specified permission
     * 
     * @see #checkUserAllPermissions(UserToken, Permission...)
     * @see #checkUserPermission(UserToken, Permission)
     * @see #hasUserPermission(UserToken, Permission)
     */
    protected void checkUserAnyPermissions(UserToken token, 
            Permission... permissions) {
        
        BitSet roles = usersService.getRoles(
                usersService.getSystemUserById(systemsService.getAdminSystem(), 
                token.getUser()), RolesType.ALL);
        
        for (Permission p : permissions) {
            if (permissionService.hasRolePermission(roles, p)) {
                return;
            }
        }
        
        throw new OperationFailedException(CommonResponses.ACCESS_DENIED, 
                "Access denied for specified entity");
    }

    /**
     * Checks if the specified user has all of the specified permissions.
     * 
     * @param token         user token entity
     * @param permissions   permissions to check
     * 
     * @throws OperationFailedException if the specified user doesn't have 
     *              the specified permissions
     * 
     * @see #checkUserAnyPermissions(UserToken, Permission...)
     * @see #checkUserPermission(UserToken, Permission)
     * @see #hasUserPermission(UserToken, Permission)
     */
    protected void checkUserAllPermissions(UserToken token, Permission p1, 
            Permission p2) {
        
        BitSet roles = usersService.getRoles(
                usersService.getSystemUserById(systemsService.getAdminSystem(), 
                        token.getUser()), RolesType.ALL);
        
        checkUserPermission(roles, p1);
        checkUserPermission(roles, p2);
    }

    /**
     * Checks if the specified user has the specified permission.
     * 
     * @param token user token entity
     * @param p     permission to check
     * @return      <code>true</code> if the specified user has the specified 
     *              permission, or <code>false</code> otherwise
     * 
     * @see #checkUserPermission(UserToken, Permission)
     * @see #checkUserAnyPermissions(UserToken, Permission...)
     */
    protected boolean hasUserPermission(UserToken token, Permission p) {
        return hasUserPermission(usersService.getRoles(
                usersService.getSystemUserById(systemsService.getAdminSystem(), 
                        token.getUser()), RolesType.ALL), p);
    }
    
    private boolean hasUserPermission(BitSet userRoles, Permission p) {
        return permissionService.hasRolePermission(userRoles, p);        
    }
    
}
