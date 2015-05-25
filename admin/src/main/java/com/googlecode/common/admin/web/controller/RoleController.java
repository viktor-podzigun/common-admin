
package com.googlecode.common.admin.web.controller;

import com.googlecode.common.admin.service.PermissionManagementService;
import java.util.BitSet;
import java.util.EnumSet;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.protocol.RenameDTO;
import com.googlecode.common.protocol.Requests;
import com.googlecode.common.protocol.perm.PermissionNodeResponse;
import com.googlecode.common.admin.domain.UserToken;
import com.googlecode.common.admin.protocol.role.PermissionListDTO;
import com.googlecode.common.admin.protocol.role.RoleDTO;
import com.googlecode.common.admin.protocol.role.RoleListResponse;
import com.googlecode.common.admin.protocol.role.RoleResponse;
import com.googlecode.common.admin.protocol.role.RolesPerm;
import com.googlecode.common.admin.protocol.role.RolesRequests;
import com.googlecode.common.admin.protocol.user.UserPerm;


/**
 * Controller for roles requests.
 */
@Controller
public class RoleController extends BaseController {

    @Autowired
    private PermissionManagementService permissionService;

    
    @RequestMapping(value   = RolesRequests.GET_ROLES,
                    method  = RequestMethod.GET)
    public @ResponseBody RoleListResponse getRoles(
            HttpServletRequest req, 
            @PathVariable(Requests.PARAM_ID) int systemId) {
        
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, UserPerm.READ);
        
        return new RoleListResponse(permissionService.getRoles(systemId));
    }

    @RequestMapping(value   = RolesRequests.GET_ALL_PERMISSIONS,
                    method  = RequestMethod.GET)
    public @ResponseBody PermissionNodeResponse getAllPermissions(
            HttpServletRequest req,
            @PathVariable(Requests.PARAM_ID) int systemId,
            @PathVariable(RolesRequests.PARAM_BIT_INDEX) int bitIndex) {
        
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, UserPerm.READ);
        
        BitSet roles = new BitSet();
        roles.set(bitIndex);
        return new PermissionNodeResponse(
                permissionService.getRolePermissions(systemId, roles, 
                        EnumSet.of(PermissionManagementService.IncludeFlags.ALL_PERMISSIONS,
                                PermissionManagementService.IncludeFlags.TITLES, PermissionManagementService.IncludeFlags.IDS,
                                PermissionManagementService.IncludeFlags.ALLOWS)));
    }

    @RequestMapping(value   = RolesRequests.GET_ALLOWED_PERMISSIONS,
                    method  = RequestMethod.GET)
    public @ResponseBody PermissionNodeResponse getAllowedPermissions(
            HttpServletRequest req,
            @PathVariable(Requests.PARAM_ID) int systemId,
            @PathVariable(RolesRequests.PARAM_BIT_INDEX) int bitIndex) {
        
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, UserPerm.READ);
        
        BitSet roles = new BitSet();
        roles.set(bitIndex);
        return new PermissionNodeResponse(
                permissionService.getRolePermissions(systemId, roles, 
                        EnumSet.noneOf(PermissionManagementService.IncludeFlags.class)));
    }

    @RequestMapping(value   = RolesRequests.UPDATE_ROLE_PERMISSIONS,
                    method  = RequestMethod.PUT)
    public @ResponseBody BaseResponse updateRolePermissions(
            HttpServletRequest req, 
            @PathVariable(Requests.PARAM_ID) long roleId,
            @RequestBody PermissionListDTO reqData) {

        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, UserPerm.ASSIGN_PERMISSIONS);
        
        permissionService.updateRolePermissions(roleId, reqData);
        return BaseResponse.OK;
    }

    @RequestMapping(value  = RolesRequests.RENAME_ROLE,
                    method = RequestMethod.PUT)
    public @ResponseBody BaseResponse renameRole(
            HttpServletRequest req,
            @RequestBody RenameDTO reqData) {

        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, RolesPerm.RENAME);

        permissionService.renameRole(reqData);
        return BaseResponse.OK;
    }

    @RequestMapping(value  = RolesRequests.CREATE_ROLE,
                    method = RequestMethod.POST)
    public @ResponseBody RoleResponse createRole(
            HttpServletRequest req,
            @PathVariable(Requests.PARAM_ID) int systemId, 
            @RequestBody RoleDTO reqData) {

        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, RolesPerm.CREATE);

        return new RoleResponse(permissionService.createRole(
                systemId, reqData));
    }

}
