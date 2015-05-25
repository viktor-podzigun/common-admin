
package com.googlecode.common.admin.client.ui.server;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.protocol.Requests;
import com.googlecode.common.protocol.admin.ServerModulesResponse;
import com.googlecode.common.protocol.admin.ServerStatusResponse;
import com.googlecode.common.admin.protocol.server.ServerDTO;
import com.googlecode.common.admin.protocol.server.ServerListResponse;
import com.googlecode.common.admin.protocol.server.ServerRequests;
import com.googlecode.common.admin.protocol.server.ServerResponse;


/**
 * Server service interface declaration.
 */
public interface ServerService extends RestService {

    @GET
    @Path(ServerRequests.GET_APP_SERVERS)
    public void getAppServers(@PathParam(Requests.PARAM_ID) int systemId, 
            MethodCallback<ServerListResponse> callback);
    
    @POST
    @Path(ServerRequests.CREATE_SERVER)
    public void createServer(@PathParam(Requests.PARAM_ID) int systemId, 
            ServerDTO reqData, MethodCallback<ServerResponse> callback);
    
    @PUT
    @Path(ServerRequests.UPDATE_SERVER)
    public void updateServer(ServerDTO reqData, 
            MethodCallback<ServerResponse> callback);
    
    @GET
    @Path(ServerRequests.GET_STATUS)
    public void getStatus(@PathParam(ServerRequests.PARAM_ID) long serverId, 
            MethodCallback<ServerStatusResponse> callback);
    
    @GET
    @Path(ServerRequests.GET_MODULES)
    public void getModules(@PathParam(ServerRequests.PARAM_ID) long serverId,
            MethodCallback<ServerModulesResponse> callback);
    
    @GET
    @Path(ServerRequests.RELOAD_SERVERS)
    public void reloadServers(@PathParam(Requests.PARAM_ID) int systemId, 
            MethodCallback<BaseResponse> callback);
    
}
