
package com.googlecode.common.admin.web.controller;

import com.googlecode.common.admin.service.LdapService;
import com.googlecode.common.admin.service.PermissionManagementService;
import com.googlecode.common.admin.service.UserManagementService;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
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
import com.googlecode.common.protocol.perm.PermissionNodeDTO;
import com.googlecode.common.protocol.perm.PermissionNodeResponse;
import com.googlecode.common.util.CollectionsUtil;
import com.googlecode.common.admin.domain.SystemUser;
import com.googlecode.common.admin.domain.UserGroup;
import com.googlecode.common.admin.domain.UserToken;
import com.googlecode.common.admin.protocol.role.RoleDTO;
import com.googlecode.common.admin.protocol.user.LdapUserListResponse;
import com.googlecode.common.admin.protocol.user.NewUserDTO;
import com.googlecode.common.admin.protocol.user.UpdateUserRolesDTO;
import com.googlecode.common.admin.protocol.user.UserGroupDTO;
import com.googlecode.common.admin.protocol.user.UserGroupListResponse;
import com.googlecode.common.admin.protocol.user.UserGroupPerm;
import com.googlecode.common.admin.protocol.user.UserGroupResponse;
import com.googlecode.common.admin.protocol.user.UserListReqDTO;
import com.googlecode.common.admin.protocol.user.UserListResponse;
import com.googlecode.common.admin.protocol.user.UserMoveDTO;
import com.googlecode.common.admin.protocol.user.UserPerm;
import com.googlecode.common.admin.protocol.user.UserRequests;
import com.googlecode.common.admin.protocol.user.UserResponse;
import com.googlecode.common.admin.protocol.user.UserRolesDTO;
import com.googlecode.common.admin.protocol.user.UserRolesResponse;
import com.googlecode.common.admin.service.SystemService;


/**
 * Controller for users requests.
 */
@Controller
public class UserController extends BaseController {

    @Autowired
    private SystemService               systemsService;

    @Autowired
    private UserManagementService usersService;

    @Autowired
    private LdapService ldapService;
    
    @Autowired
    private PermissionManagementService permissionService;
    
    
    @RequestMapping(value   = UserRequests.GET_SYSTEM_USERS,
                    method  = RequestMethod.POST)
    public @ResponseBody UserListResponse getSystemUsers(
            HttpServletRequest req,
            @PathVariable(Requests.PARAM_ID) int systemId,
            @PathVariable(UserRequests.PARAM_GROUP) long groupId, 
            @RequestBody UserListReqDTO reqData) {
        
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, UserPerm.READ);
        
        return usersService.getSystemUsers(systemId, groupId, reqData);
    }

    @RequestMapping(value   = UserRequests.GET_COMPANY_USERS,
                    method  = RequestMethod.POST)
    public @ResponseBody UserListResponse getCompanyUsers(
            HttpServletRequest req,
            @PathVariable(Requests.PARAM_ID) long companyId, 
            @RequestBody UserListReqDTO reqData) {
        
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, UserPerm.READ);
        
        return usersService.getCompanyUsers(companyId, reqData);
    }

    @RequestMapping(value   = UserRequests.CREATE_NEW_USER,
                    method  = RequestMethod.POST)
    public @ResponseBody UserResponse createNewUser(
            HttpServletRequest req,
            @PathVariable(Requests.PARAM_ID) int systemId,
            @PathVariable(UserRequests.PARAM_GROUP) long parentId,
            @RequestBody NewUserDTO reqData) {
        
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, UserPerm.CREATE);
        
        return new UserResponse(usersService.createNewUser(systemId, 
                parentId, reqData, userToken.getUser()));
    }

    @RequestMapping(value   = UserRequests.CREATE_NEW_GROUP,
                    method  = RequestMethod.POST)
    public @ResponseBody UserGroupResponse createNewGroup(
            HttpServletRequest req,
            @PathVariable(Requests.PARAM_ID) int systemId,
            @PathVariable(UserRequests.PARAM_GROUP) long parentId,
            @RequestBody UserGroupDTO reqData) {
 
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, UserPerm.CREATE);
        
        return new UserGroupResponse(usersService.createNewGroup(systemId, 
                parentId, reqData, userToken.getUser()));
    }

    @RequestMapping(value   = UserRequests.RENAME_GROUP,
                    method  = RequestMethod.PUT)
    public @ResponseBody BaseResponse renameUserGroup(
            HttpServletRequest req,
            @RequestBody RenameDTO reqData) {
        
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, UserGroupPerm.RENAME);
        
        usersService.renameUserGroup(reqData, userToken.getUser());
        return BaseResponse.OK;
    }

    @RequestMapping(value   = UserRequests.MOVE_USERS,
                    method  = RequestMethod.POST)
    public @ResponseBody BaseResponse moveUsers(
            HttpServletRequest req, 
            @RequestBody UserMoveDTO reqData) {
        
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, UserPerm.ASSIGN_ROLES);
        
        usersService.moveUsers(reqData, userToken.getUser());
        return BaseResponse.OK;
    }

    @RequestMapping(value   = UserRequests.UPDATE_USER_ROLES,
                    method  = RequestMethod.PUT)
    public @ResponseBody PermissionNodeResponse updateUserRoles(
            HttpServletRequest req, 
            @RequestBody UpdateUserRolesDTO reqData) {
        
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, UserPerm.ASSIGN_ROLES);
        
        BitSet roles = usersService.updateUserRoles(reqData, 
                usersService.getSystemUserById(systemsService.getAdminSystem(), 
                        userToken.getUser()));
        
        return new PermissionNodeResponse(getRolePermissions(
                reqData.getSystemId(), roles, false));
    }

    @RequestMapping(value   = UserRequests.UPDATE_GROUP_ROLES,
                    method  = RequestMethod.PUT)
    public @ResponseBody PermissionNodeResponse updateGroupRoles(
            HttpServletRequest req, 
            @RequestBody UpdateUserRolesDTO reqData) {
        
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, UserPerm.ASSIGN_ROLES);
        
        BitSet roles = usersService.updateGroupRoles(reqData, 
                usersService.getSystemUserById(systemsService.getAdminSystem(), 
                        userToken.getUser()));
        
        return new PermissionNodeResponse(getRolePermissions(
                reqData.getSystemId(), roles, false));
    }

    @RequestMapping(value   = UserRequests.GET_USER_ROLES,
                    method  = RequestMethod.GET)
    public @ResponseBody UserRolesResponse getUserRoles(
            HttpServletRequest req, 
            @PathVariable(UserRequests.PARAM_ID) int systemId,
            @PathVariable(UserRequests.PARAM_USER) int userId,
            @PathVariable(UserRequests.PARAM_ALL_PERM) boolean allPermission) {
        
        // check permissions
        UserToken author = getAuthenticateUserToken(req);
        checkUserPermission(author, UserPerm.READ);
        
        SystemUser user = usersService.getSystemUserById(
                systemsService.getSystemById(systemId), 
                usersService.getUserById(userId));
        
        return new UserRolesResponse(getRoles(false, systemId, 
                usersService.getRoles(usersService.getSystemUserById(
                        systemsService.getAdminSystem(), author.getUser()), 
                        UserManagementService.RolesType.ALL),
                usersService.getRoles(user, UserManagementService.RolesType.INHERITED),
                usersService.getRoles(user, UserManagementService.RolesType.ROLES),
                allPermission));
    }
    
    @RequestMapping(value   = UserRequests.GET_GROUP_ROLES,
                    method  = RequestMethod.GET)
    public @ResponseBody UserRolesResponse getGroupRoles(
            HttpServletRequest req, 
            @PathVariable(UserRequests.PARAM_ID) int systemId,
            @PathVariable(UserRequests.PARAM_GROUP) long groupId,
            @PathVariable(UserRequests.PARAM_ALL_PERM) boolean allPermission) {
        
        // check permissions
        UserToken author = getAuthenticateUserToken(req);
        checkUserPermission(author, UserPerm.READ);
        
        UserGroup g = usersService.getGroupById(groupId);
        
        return new UserRolesResponse(getRoles(true, systemId, 
                usersService.getRoles(usersService.getSystemUserById(
                        systemsService.getAdminSystem(), author.getUser()), 
                        UserManagementService.RolesType.ALL),
                usersService.getRoles(g, UserManagementService.RolesType.INHERITED),
                usersService.getRoles(g, UserManagementService.RolesType.ROLES),
                allPermission));
    }

    @RequestMapping(value  = UserRequests.DELETE_GROUP,
                    method = RequestMethod.DELETE)
    public @ResponseBody BaseResponse deleteUserGroup(
            HttpServletRequest req,
            @PathVariable(UserRequests.PARAM_ID) long groupId) {

        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, UserGroupPerm.DELETE);

        usersService.deleteUserGroup(groupId);
        return BaseResponse.OK;
    }

    @RequestMapping(value   = UserRequests.GET_ALL_GROUPS,
                    method  = RequestMethod.GET)
    public @ResponseBody UserGroupListResponse getAllGroups(
            HttpServletRequest req,
            @PathVariable(Requests.PARAM_ID) int systemId) {

        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, UserPerm.READ);

        return new UserGroupListResponse(usersService.getAllGroups(systemId));
    }

    @RequestMapping(value   = UserRequests.GET_ALL_LDAP_USERS,
                    method  = RequestMethod.GET)
    public @ResponseBody LdapUserListResponse getAllUsersLdap(
            HttpServletRequest req) {
    
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, UserPerm.READ);
    
        return new LdapUserListResponse(ldapService.getAllUsers());
    }
    
//    @RequestMapping(value   = UserRequests.CHANGE_PASSWORD,
//                    method  = RequestMethod.POST)
//    public @ResponseBody BaseResponse changePassword(
//            HttpServletRequest req,
//            @RequestBody LoginUserDTO loginDTO) {
//
//        UserToken userToken = getAuthenticateUserToken(req);
////        checkUserPermission(userToken, UserPerm.LOGIN);
//
//        usersService.changeUserPassword(userToken.getUser().getId(),
//                loginDTO.getPassword(), loginDTO.getNewPassword());
//
//        return BaseResponse.OK;
//    }

    private UserRolesDTO getRoles(boolean group, int systemId, 
            BitSet authorRoles, BitSet inheritedRoles, BitSet roles, 
            boolean allPermission) {
        
        boolean isAuthorSuper  = permissionService.isSuperUserRole(authorRoles);
        boolean isRolesSuper   = permissionService.isSuperUserRole(roles);
        
        List<RoleDTO> baseRoles      = null;
        List<RoleDTO> availableRoles = null;
        List<RoleDTO> assignedRoles  = null;
        
        for (RoleDTO r : permissionService.getRoles(systemId)) {
            final int bitIndex = r.safeGetBitIndex();
            
            if (inheritedRoles.get(bitIndex)) {
                baseRoles = CollectionsUtil.addToList(baseRoles, r);
            
            } else if (!isRolesSuper && roles.get(bitIndex)) {
                assignedRoles = CollectionsUtil.addToList(assignedRoles, r);
            
            } else if (!isRolesSuper) {
                availableRoles = CollectionsUtil.addToList(availableRoles, r);
            }
        }
        
        if (!group) {
            // add SUPERUSER role for user
            RoleDTO r = RoleDTO.createSuperUser();
            if (!isAuthorSuper && isRolesSuper) {
                baseRoles = CollectionsUtil.addToList(baseRoles, r);
            
            } else if (isRolesSuper) {
                assignedRoles = CollectionsUtil.addToList(assignedRoles, r);
            
            } else if (isAuthorSuper) {
                availableRoles = CollectionsUtil.addToList(availableRoles, r);
            }
        }
        
        roles.or(inheritedRoles);
        return new UserRolesDTO(baseRoles, availableRoles, assignedRoles, 
                getRolePermissions(systemId, roles, allPermission));
    }

    private PermissionNodeDTO getRolePermissions(int systemId, BitSet roles, 
            boolean allPermission) {
        
        Set<PermissionManagementService.IncludeFlags> flags;
        if (allPermission) {
            flags = EnumSet.of(PermissionManagementService.IncludeFlags.ALL_PERMISSIONS,
                    PermissionManagementService.IncludeFlags.TITLES, PermissionManagementService.IncludeFlags.ALLOWS);
        } else {
            flags = EnumSet.noneOf(PermissionManagementService.IncludeFlags.class);
        }
        
        return permissionService.getRolePermissions(systemId, roles, flags);
    }
}
