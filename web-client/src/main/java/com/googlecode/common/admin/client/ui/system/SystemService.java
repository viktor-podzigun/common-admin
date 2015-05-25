
package com.googlecode.common.admin.client.ui.system;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.protocol.IntListDTO;
import com.googlecode.common.protocol.Requests;
import com.googlecode.common.admin.protocol.system.SystemDTO;
import com.googlecode.common.admin.protocol.system.SystemGroupDTO;
import com.googlecode.common.admin.protocol.system.SystemGroupListResponse;
import com.googlecode.common.admin.protocol.system.SystemGroupResponse;
import com.googlecode.common.admin.protocol.system.SystemListResponse;
import com.googlecode.common.admin.protocol.system.SystemRequests;
import com.googlecode.common.admin.protocol.system.SystemResponse;
import com.googlecode.common.admin.protocol.user.UserSystemsResponse;


/**
 * Systems service interface declaration.
 */
public interface SystemService extends RestService {

    @GET
    @Path(SystemRequests.GET_GROUP_LIST)
    public void getGroups(MethodCallback<SystemGroupListResponse> callback);
    
    @POST
    @Path(SystemRequests.CREATE_GROUP)
    public void createGroup(SystemGroupDTO reqData, 
            MethodCallback<SystemGroupResponse> callback);
    
    @PUT
    @Path(SystemRequests.UPDATE_GROUP)
    public void updateGroup(SystemGroupDTO reqData, 
            MethodCallback<SystemGroupResponse> callback);
    
    @GET
    @Path(SystemRequests.GET_SYSTEM_LIST)
    public void getSystems(@PathParam(Requests.PARAM_ID) long parentId, 
            MethodCallback<SystemListResponse> callback);
    
    @POST
    @Path(SystemRequests.CREATE_SYSTEM)
    public void createSystem(@PathParam(Requests.PARAM_ID) long parentId, 
            SystemDTO reqData, MethodCallback<SystemResponse> callback);
    
    @PUT
    @Path(SystemRequests.UPDATE_SYSTEM)
    public void updateSystem(SystemDTO reqData, 
            MethodCallback<SystemResponse> callback);
    
    @GET
    @Path(SystemRequests.GET_USER_SYSTEMS)
    public void getUserSystems(@PathParam(Requests.PARAM_ID) int userId, 
            MethodCallback<UserSystemsResponse> callback);
    
    @PUT
    @Path(SystemRequests.UPDATE_USER_SYSTEMS)
    public void updateUserSystems(@PathParam(Requests.PARAM_ID) int userId, 
            @PathParam(SystemRequests.PARAM_IS_ADDED) boolean isAdded, 
            IntListDTO reqData, MethodCallback<BaseResponse> callback);
    
}
