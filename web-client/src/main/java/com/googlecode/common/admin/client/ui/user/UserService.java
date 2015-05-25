
package com.googlecode.common.admin.client.ui.user;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.protocol.RenameDTO;
import com.googlecode.common.protocol.Requests;
import com.googlecode.common.protocol.perm.PermissionNodeResponse;
import com.googlecode.common.admin.protocol.user.LdapUserListResponse;
import com.googlecode.common.admin.protocol.user.NewUserDTO;
import com.googlecode.common.admin.protocol.user.UpdateUserRolesDTO;
import com.googlecode.common.admin.protocol.user.UserGroupDTO;
import com.googlecode.common.admin.protocol.user.UserGroupListResponse;
import com.googlecode.common.admin.protocol.user.UserGroupResponse;
import com.googlecode.common.admin.protocol.user.UserListReqDTO;
import com.googlecode.common.admin.protocol.user.UserListResponse;
import com.googlecode.common.admin.protocol.user.UserRequests;
import com.googlecode.common.admin.protocol.user.UserResponse;
import com.googlecode.common.admin.protocol.user.UserRolesResponse;


/**
 * User service interface declaration.
 */
public interface UserService extends RestService {

    @GET
    @Path(UserRequests.GET_ALL_GROUPS)
    public void getAllGroups(@PathParam(Requests.PARAM_ID) int systemId, 
            MethodCallback<UserGroupListResponse> callback);
    
    @POST
    @Path(UserRequests.CREATE_NEW_GROUP)
    public void createGroup(@PathParam(Requests.PARAM_ID) int systemId, 
            @PathParam(UserRequests.PARAM_GROUP) long parentId,
            UserGroupDTO reqData, MethodCallback<UserGroupResponse> callback);
    
    @DELETE
    @Path(UserRequests.DELETE_GROUP)
    public void deleteGroup(@PathParam(Requests.PARAM_ID) long groupId, 
            MethodCallback<BaseResponse> callback);
    
    @PUT
    @Path(UserRequests.RENAME_GROUP)
    public void renameGroup(RenameDTO reqData, 
            MethodCallback<BaseResponse> callback);
    
    @GET
    @Path(UserRequests.GET_USER_ROLES)
    public void getUserRoles(@PathParam(Requests.PARAM_ID) int systemId, 
            @PathParam(UserRequests.PARAM_USER) int userId, 
            @PathParam(UserRequests.PARAM_ALL_PERM) boolean allPermission, 
            MethodCallback<UserRolesResponse> callback);
    
    @GET
    @Path(UserRequests.GET_GROUP_ROLES)
    public void getGroupRoles(@PathParam(Requests.PARAM_ID) int systemId, 
            @PathParam(UserRequests.PARAM_GROUP) long groupId, 
            @PathParam(UserRequests.PARAM_ALL_PERM) boolean allPermission, 
            MethodCallback<UserRolesResponse> callback);
    
    @PUT
    @Path(UserRequests.UPDATE_USER_ROLES)
    public void updateUserRoles(UpdateUserRolesDTO reqData, 
            MethodCallback<PermissionNodeResponse> callback);
    
    @PUT
    @Path(UserRequests.UPDATE_GROUP_ROLES)
    public void updateGroupRoles(UpdateUserRolesDTO reqData, 
            MethodCallback<PermissionNodeResponse> callback);
    
    @POST
    @Path(UserRequests.GET_SYSTEM_USERS)
    public void getSystemUsers(@PathParam(Requests.PARAM_ID) int systemId, 
            @PathParam(UserRequests.PARAM_GROUP) long groupId, 
            UserListReqDTO dto, MethodCallback<UserListResponse> callback);
    
    @POST
    @Path(UserRequests.GET_COMPANY_USERS)
    public void getCompanyUsers(@PathParam(Requests.PARAM_ID) long companyId, 
            UserListReqDTO dto, MethodCallback<UserListResponse> callback);
    
    @POST
    @Path(UserRequests.CREATE_NEW_USER)
    public void createUser(@PathParam(Requests.PARAM_ID) int systemId, 
            @PathParam(UserRequests.PARAM_GROUP) long parentId,
            NewUserDTO reqData, MethodCallback<UserResponse> callback);
    
    @GET
    @Path(UserRequests.GET_ALL_LDAP_USERS)
    public void getLdapUsers(MethodCallback<LdapUserListResponse> callback);
}
