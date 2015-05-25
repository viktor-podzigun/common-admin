
package com.googlecode.common.admin.client.ui.role;

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
import com.googlecode.common.admin.protocol.role.PermissionListDTO;
import com.googlecode.common.admin.protocol.role.RoleDTO;
import com.googlecode.common.admin.protocol.role.RoleListResponse;
import com.googlecode.common.admin.protocol.role.RoleResponse;
import com.googlecode.common.admin.protocol.role.RolesRequests;


/**
 * Role service interface declaration.
 */
public interface RoleService extends RestService {

    @GET
    @Path(RolesRequests.GET_ROLES)
    public void getRoles(@PathParam(Requests.PARAM_ID) int systemId, 
            MethodCallback<RoleListResponse> callback);
    
    @GET
    @Path(RolesRequests.GET_ALL_PERMISSIONS)
    public void getAllPermissions(@PathParam(Requests.PARAM_ID) int systemId, 
            @PathParam(RolesRequests.PARAM_BIT_INDEX) int bitIndex, 
            MethodCallback<PermissionNodeResponse> callback);
    
    @GET
    @Path(RolesRequests.GET_ALLOWED_PERMISSIONS)
    public void getAllowedPermissions(@PathParam(Requests.PARAM_ID) int systemId, 
            @PathParam(RolesRequests.PARAM_BIT_INDEX) int bitIndex, 
            MethodCallback<PermissionNodeResponse> callback);
    
    @PUT
    @Path(RolesRequests.UPDATE_ROLE_PERMISSIONS)
    public void updateRolePermissions(@PathParam(Requests.PARAM_ID) long roleId, 
            PermissionListDTO reqData, MethodCallback<BaseResponse> callback);
    
    @POST
    @Path(RolesRequests.CREATE_ROLE)
    public void createRole(@PathParam(Requests.PARAM_ID) int systemId, 
            RoleDTO reqData, MethodCallback<RoleResponse> callback);
    
    @PUT
    @Path(RolesRequests.RENAME_ROLE)
    public void renameRole(RenameDTO reqData, 
            MethodCallback<BaseResponse> callback);
    
}
