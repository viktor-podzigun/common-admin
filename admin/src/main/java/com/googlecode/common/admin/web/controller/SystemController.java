
package com.googlecode.common.admin.web.controller;

import java.util.Iterator;
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
import com.googlecode.common.protocol.IntListDTO;
import com.googlecode.common.protocol.Requests;
import com.googlecode.common.protocol.admin.AdminRequests;
import com.googlecode.common.protocol.admin.AppConfReqDTO;
import com.googlecode.common.protocol.admin.AppConfResponse;
import com.googlecode.common.protocol.admin.AuthUserDTO;
import com.googlecode.common.protocol.admin.AuthUserResponse;
import com.googlecode.common.service.CommonResponses;
import com.googlecode.common.service.ex.OperationFailedException;
import com.googlecode.common.util.CollectionsUtil;
import com.googlecode.common.admin.domain.SystemEntity;
import com.googlecode.common.admin.domain.UserToken;
import com.googlecode.common.admin.protocol.system.SystemDTO;
import com.googlecode.common.admin.protocol.system.SystemGroupDTO;
import com.googlecode.common.admin.protocol.system.SystemGroupListResponse;
import com.googlecode.common.admin.protocol.system.SystemGroupResponse;
import com.googlecode.common.admin.protocol.system.SystemListResponse;
import com.googlecode.common.admin.protocol.system.SystemPerm;
import com.googlecode.common.admin.protocol.system.SystemRequests;
import com.googlecode.common.admin.protocol.system.SystemResponse;
import com.googlecode.common.admin.protocol.user.UserPerm;
import com.googlecode.common.admin.protocol.user.UserSystemsDTO;
import com.googlecode.common.admin.protocol.user.UserSystemsResponse;
import com.googlecode.common.admin.service.SystemService;


/**
 * Controller for system requests.
 */
@Controller
public class SystemController extends BaseController {

    @Autowired
    private SystemService   systemsService;


    @RequestMapping(value   = SystemRequests.GET_GROUP_LIST,
                    method  = RequestMethod.GET)
    public @ResponseBody SystemGroupListResponse getSystemGroups(
            HttpServletRequest req) {
        
        // check permissions
        UserToken author = getAuthenticateUserToken(req);
        checkUserPermission(author, SystemPerm.READ);
        
        return new SystemGroupListResponse(systemsService.getSystemGroups());
    }
    
    @RequestMapping(value   = SystemRequests.CREATE_GROUP,
                    method  = RequestMethod.POST)
    public @ResponseBody SystemGroupResponse createSystemGroup(
            HttpServletRequest req, 
            @RequestBody SystemGroupDTO reqData) {
        
        // check permissions
        UserToken author = getAuthenticateUserToken(req);
        checkUserPermission(author, SystemPerm.CREATE);
        
        return new SystemGroupResponse(systemsService.createSystemGroup(
                reqData, author.getUser()));
    }
    
    @RequestMapping(value   = SystemRequests.UPDATE_GROUP,
                    method  = RequestMethod.PUT)
    public @ResponseBody SystemGroupResponse updateSystemGroup(
            HttpServletRequest req, 
            @RequestBody SystemGroupDTO reqData) {
        
        // check permissions
        UserToken author = getAuthenticateUserToken(req);
        checkUserPermission(author, SystemPerm.UPDATE);
        
        return new SystemGroupResponse(systemsService.updateSystemGroup(
                reqData, author.getUser()));
    }

    @RequestMapping(value   = SystemRequests.GET_SYSTEM_LIST,
                    method  = RequestMethod.GET)
    public @ResponseBody SystemListResponse getSystems(
            HttpServletRequest req, 
            @PathVariable(Requests.PARAM_ID) long parentId) {
        
        // check permissions
        UserToken author = getAuthenticateUserToken(req);
        checkUserPermission(author, SystemPerm.READ);
        
        return new SystemListResponse(systemsService.getSystems(parentId));
    }

    @RequestMapping(value   = SystemRequests.CREATE_SYSTEM,
                    method  = RequestMethod.POST)
    public @ResponseBody SystemResponse createSystem(
            HttpServletRequest req, 
            @PathVariable(Requests.PARAM_ID) long parentId, 
            @RequestBody SystemDTO reqData) {
        
        // check permissions
        UserToken author = getAuthenticateUserToken(req);
        checkUserPermission(author, SystemPerm.CREATE);
        
        return new SystemResponse(systemsService.createSystem(parentId, 
                reqData));
    }
    
    @RequestMapping(value   = SystemRequests.UPDATE_SYSTEM,
                    method  = RequestMethod.PUT)
    public @ResponseBody SystemResponse updateSystem(
            HttpServletRequest req, 
            @RequestBody SystemDTO reqData) {
        
        // check permissions
        UserToken author = getAuthenticateUserToken(req);
        checkUserPermission(author, SystemPerm.UPDATE);
        
        return new SystemResponse(systemsService.updateSystem(reqData));
    }

    @RequestMapping(value   = SystemRequests.GET_USER_SYSTEMS,
                    method  = RequestMethod.GET)
    public @ResponseBody UserSystemsResponse getUserSystems(
            HttpServletRequest req, 
            @PathVariable(Requests.PARAM_ID) int userId) {
        
        // check permissions
        UserToken author = getAuthenticateUserToken(req);
        checkUserPermission(author, UserPerm.READ);
        
        List<SystemDTO> allSystems  = CollectionsUtil.ensureNotNull(
                systemsService.getAllSystems());
        
        List<SystemDTO> userSystems = CollectionsUtil.ensureNotNull(
                systemsService.getUserSystems(userId));
        
        for (SystemDTO userSys : userSystems) {
            Iterator<SystemDTO> iter = allSystems.iterator();
            while (iter.hasNext()) {
                SystemDTO allSys = iter.next();
                if (allSys.safeGetId() == userSys.safeGetId()) {
                    iter.remove();
                }
            }
        }
        
        return new UserSystemsResponse(new UserSystemsDTO(
                allSystems, userSystems));
    }
    
    @RequestMapping(value   = SystemRequests.UPDATE_USER_SYSTEMS,
                    method  = RequestMethod.PUT)
    public @ResponseBody BaseResponse updateUserSystems(
            HttpServletRequest req, 
            @PathVariable(Requests.PARAM_ID) int userId, 
            @PathVariable(SystemRequests.PARAM_IS_ADDED) boolean isAdded,
            @RequestBody IntListDTO reqData) {
        
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, UserPerm.UPDATE);
        
        systemsService.updateUserSystems(userId, reqData, isAdded, 
                userToken.getUser());
        return BaseResponse.OK;
    }

    @RequestMapping(value   = AdminRequests.APP_READ_SETTINGS,
                    method  = RequestMethod.POST)
    public @ResponseBody AppConfResponse appReadSettings(
            HttpServletRequest req, 
            @RequestBody AppConfReqDTO reqData) {
        
        String authInfo[] = getAuthInfo(req);
        SystemEntity system = systemsService.getAuthenticateSystem(
                authInfo[0], authInfo[1]);
        
        return new AppConfResponse(systemsService.readAppSettings(
                reqData, system));
    }

    @RequestMapping(value   = AdminRequests.APP_AUTH_USER,
                    method  = RequestMethod.GET)
    public @ResponseBody AuthUserResponse appAuthUser(HttpServletRequest req,
            @PathVariable(AdminRequests.PARAM_SYSTEM) String systemName) {
        
        SystemEntity system = systemsService.getSystemByName(systemName);
        AuthUserDTO dto = authorizationService.getAuthenticateUserDTO(req, 
                system);
        
        if (dto == null) {
            throw new OperationFailedException(
                    CommonResponses.AUTHENTICATION_FAILED, 
                    "User authentication failed");
        }

        return new AuthUserResponse(dto);
    }

}
