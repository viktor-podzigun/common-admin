
package com.googlecode.common.admin.web.controller;

import com.googlecode.common.admin.service.SystemService;
import com.googlecode.common.admin.service.UserManagementService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.protocol.login.AppMenuDTO;
import com.googlecode.common.protocol.login.LoginRespDTO;
import com.googlecode.common.admin.domain.User;
import com.googlecode.common.admin.domain.UserToken;
import com.googlecode.common.admin.protocol.profile.ProfileDTO;
import com.googlecode.common.admin.protocol.profile.ProfileRequests;
import com.googlecode.common.admin.protocol.profile.ProfileResponse;


/**
 * Controller for profile requests.
 */
@Controller
public class ProfileController extends BaseController {

    @Autowired
    private SystemService systemsService;
    
    @Autowired
    private UserManagementService usersService;
    

    @RequestMapping(value   = ProfileRequests.READ,
                    method  = RequestMethod.GET)
    public @ResponseBody ProfileResponse read(
            HttpServletRequest req) {
        
        UserToken token = authorizationService.getAuthenticateUserToken(req);
        User user = authorizationService.checkUser(token != null ? 
                token.getUser() : null);
        
        LoginRespDTO loginDto = new LoginRespDTO();
        loginDto.setLogin(user.getLogin());
        
        // prepare application menu
        AppMenuDTO appMenu = systemsService.getAppMenu(-1, user.getId());
        appMenu.setTitle("Settings");
        appMenu.setUrl("#");
        loginDto.setAppMenu(appMenu);
        
        return new ProfileResponse(loginDto, 
                usersService.getProfile(user.getId()));
    }
    
    @RequestMapping(value   = ProfileRequests.UPDATE,
                    method  = RequestMethod.PUT)
    public @ResponseBody BaseResponse update(HttpServletRequest req, 
            @RequestBody ProfileDTO reqDto) {
        
        UserToken token = authorizationService.getAuthenticateUserToken(req);
        User user = authorizationService.checkUser(token != null ? 
                token.getUser() : null);
        
        usersService.updateProfile(user.getId(), reqDto);
        return BaseResponse.OK;
    }

}
