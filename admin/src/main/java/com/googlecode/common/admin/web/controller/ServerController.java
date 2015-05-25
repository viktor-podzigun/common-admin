
package com.googlecode.common.admin.web.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.protocol.admin.ServerModulesResponse;
import com.googlecode.common.protocol.admin.ServerStatusResponse;
import com.googlecode.common.admin.domain.User;
import com.googlecode.common.admin.domain.UserToken;
import com.googlecode.common.admin.protocol.server.ServerDTO;
import com.googlecode.common.admin.protocol.server.ServerListResponse;
import com.googlecode.common.admin.protocol.server.ServerPerm;
import com.googlecode.common.admin.protocol.server.ServerRequests;
import com.googlecode.common.admin.protocol.server.ServerResponse;
import com.googlecode.common.admin.service.ServerService;


/**
 * Controller for server requests.
 */
@Controller
public class ServerController extends BaseController {

    @Autowired
    private ServerService   serverService;

    
    @RequestMapping(value   = ServerRequests.GET_APP_SERVERS,
                    method  = RequestMethod.GET)
    public @ResponseBody ServerListResponse getAppServers(
            HttpServletRequest req, 
            @PathVariable(ServerRequests.PARAM_ID) int systemId) {
        
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, ServerPerm.READ);
        
        return new ServerListResponse(serverService.getAppServers(systemId));
    }

    @RequestMapping(value   = ServerRequests.CREATE_SERVER,
                    method  = RequestMethod.POST)
    public @ResponseBody ServerResponse createServer(
            HttpServletRequest req, 
            @PathVariable(ServerRequests.PARAM_ID) int systemId, 
            @RequestBody ServerDTO reqData) {
        
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, ServerPerm.CREATE);
        
        return new ServerResponse(serverService.createServer(systemId, 
                reqData));
    }

    @RequestMapping(value   = ServerRequests.UPDATE_SERVER,
                    method  = RequestMethod.PUT)
    public @ResponseBody ServerResponse updateServer(
            HttpServletRequest req, 
            @RequestBody ServerDTO reqData) {
        
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, ServerPerm.UPDATE);
        
        return new ServerResponse(serverService.updateServer(reqData));
    }

    @RequestMapping(value   = ServerRequests.GET_STATUS,
                    method  = RequestMethod.GET)
    public @ResponseBody ServerStatusResponse getStatus(
            HttpServletRequest req,
            @PathVariable(ServerRequests.PARAM_ID) long serverId) {
        
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, ServerPerm.READ);
        
        User user = userToken.getUser();
        return serverService.readServerStatus(
                serverService.getServer(serverId), 
                user.getLogin(), user.getPassHash());
    }

    @RequestMapping(value   = ServerRequests.GET_MODULES,
                    method  = RequestMethod.GET)
    public @ResponseBody ServerModulesResponse getModules(
            HttpServletRequest req,
            @PathVariable(ServerRequests.PARAM_ID) long serverId) {
        
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, ServerPerm.READ);
        
        User user = userToken.getUser();
        return serverService.readServerModules(
                serverService.getServer(serverId), 
                user.getLogin(), user.getPassHash());
    }

    @RequestMapping(value   = ServerRequests.RELOAD_SERVERS,
                    method  = RequestMethod.GET)
    public @ResponseBody BaseResponse reloadServers(
            HttpServletRequest req,
            @PathVariable(ServerRequests.PARAM_ID) int systemId) {
        
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, ServerPerm.RELOAD);
        
        User user = userToken.getUser();
        List<ServerDTO> servers = serverService.getAppServers(systemId);
        if (servers != null) {
            for (ServerDTO dto : servers) {
                serverService.reloadServer(dto, user.getLogin(), 
                        user.getPassHash());
            }
        }
        
        return BaseResponse.OK;
    }

}
