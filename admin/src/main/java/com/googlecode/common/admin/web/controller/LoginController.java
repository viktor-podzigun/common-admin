
package com.googlecode.common.admin.web.controller;

import java.net.URI;
import java.util.BitSet;
import java.util.EnumSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.googlecode.common.admin.domain.SystemEntity;
import com.googlecode.common.admin.domain.SystemUser;
import com.googlecode.common.admin.domain.User;
import com.googlecode.common.admin.domain.UserToken;
import com.googlecode.common.admin.protocol.signin.SigninRequests;
import com.googlecode.common.admin.protocol.user.UserDTO;
import com.googlecode.common.admin.service.PermissionManagementService;
import com.googlecode.common.admin.service.SystemService;
import com.googlecode.common.admin.service.UserManagementService;
import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.protocol.admin.AdminRequests;
import com.googlecode.common.protocol.login.LoginRedirectResponse;
import com.googlecode.common.protocol.login.LoginRequests;
import com.googlecode.common.protocol.login.LoginRespDTO;
import com.googlecode.common.protocol.login.LoginResponse;
import com.googlecode.common.protocol.perm.PermissionNodeDTO;
import com.googlecode.common.service.AdminService;
import com.googlecode.common.util.Bits;
import com.googlecode.common.util.CollectionsUtil;
import com.googlecode.common.util.UriHelpers;
import com.googlecode.common.web.ServletHelpers;


/**
 * Controller for login requests.
 */
@Controller
public class LoginController extends BaseController {

    @Autowired
    private SystemService               systemsService;
    
    @Autowired
    private AdminService                adminService;

    @Autowired
    private UserManagementService       usersService;
    
    @Autowired
    private PermissionManagementService permissionsService;
    

    @RequestMapping(value   = SigninRequests.SIGNIN,
                    method  = RequestMethod.GET)
    public @ResponseBody LoginRedirectResponse signin(
            HttpServletRequest req, HttpServletResponse resp, 
            @RequestParam(value=SigninRequests.QPARAM_REMEMBER_ME, 
                defaultValue="false") boolean rm, 
            @RequestParam(value=SigninRequests.QPARAM_TARGET_URL, 
                defaultValue="") String targetUrl) {
        
        User user = authorizationService.checkUser(
                authorizationService.getUserForLogin(req));
        String token = authorizationService.createUserToken(user, rm);
        
        ServletHelpers.addTokenCookie(resp, token);
        return new LoginRedirectResponse(targetUrl);
    }
    
    @RequestMapping(value   = LoginRequests.LOGIN,
                    method  = RequestMethod.GET)
    public @ResponseBody LoginResponse login(
            HttpServletRequest req, HttpServletResponse resp, 
            @RequestParam(value="rm", defaultValue="false") boolean rm) {
        
        SystemEntity system = systemsService.getAdminSystem();
        User user = authorizationService.checkUser(
                authorizationService.getUserForLogin(req));
        
        LoginRespDTO loginResp = prepareLoginResponse(system.getId(), 
                usersService.getSystemUserById(system, user), 
                user.getId(), rm);
        
        ServletHelpers.addTokenCookie(resp, loginResp.getToken());
        return new LoginResponse(loginResp);
    }
    
    @RequestMapping(value   = LoginRequests.LOGIN_TOKEN,
                    method  = RequestMethod.GET)
    public @ResponseBody LoginResponse loginToken(
            HttpServletRequest req, HttpServletResponse resp) {
        
        SystemEntity system = systemsService.getAdminSystem();
        UserToken token = authorizationService.getAuthenticateUserToken(req);
        User user = authorizationService.checkUser(token != null ? 
                token.getUser() : null);
        
        LoginRespDTO loginResp = prepareLoginResponse(system.getId(), 
                usersService.getSystemUserById(system, user), 
                user.getId(), token.getId());
        
        ServletHelpers.addTokenCookie(resp, loginResp.getToken());
        return new LoginResponse(loginResp);
    }
    
    @RequestMapping(value   = AdminRequests.APP_LOGIN,
                    method  = RequestMethod.GET)
    public @ResponseBody LoginResponse appLogin(HttpServletRequest req,
            @PathVariable(AdminRequests.PARAM_SYSTEM) String systemName, 
            @RequestParam(value="rm", defaultValue="false") boolean rm) {
        
        SystemEntity system = systemsService.getSystemByName(systemName);
        User user = authorizationService.checkUser(
                authorizationService.getUserForLogin(req));

        return new LoginResponse(prepareLoginResponse(system.getId(), 
                usersService.getSystemUserById(system, user), 
                user.getId(), rm));
    }

    @RequestMapping(value   = AdminRequests.APP_LOGIN_TOKEN,
                    method  = RequestMethod.GET)
    public @ResponseBody LoginResponse appLoginToken(HttpServletRequest req,
            @PathVariable(AdminRequests.PARAM_SYSTEM) String systemName) {
        
        SystemEntity system = systemsService.getSystemByName(systemName);
        UserToken token = authorizationService.getAuthenticateUserToken(req);
        User user = authorizationService.checkUser(token != null ? 
                token.getUser() : null);
        
        return new LoginResponse(prepareLoginResponse(system.getId(), 
                usersService.getSystemUserById(system, user), 
                user.getId(), token.getId()));
    }

    @RequestMapping(value   = LoginRequests.LOGOUT,
                    method  = RequestMethod.GET)
    public @ResponseBody BaseResponse logout(
            HttpServletRequest req, HttpServletResponse resp) {
        
        authorizationService.logoutUser(req);
        
        ServletHelpers.removeTokenCookie(resp);
        
        // put login redirect URL only if in the same domain
        if (req.getServerName().endsWith(ServletHelpers.getAppDomain())) {
            return new LoginRedirectResponse(
                    adminService.getLoginRedirectUrl(req, null));
        }
        
        return BaseResponse.OK;
    }

    private LoginRespDTO prepareLoginResponse(int systemId, SystemUser su, 
            int userId, boolean rememberMe) {
        
        String token = authorizationService.createUserToken(su.getPk().getUser(), rememberMe);
        return prepareLoginResponse(systemId, su, userId, token);
    }
    
    private LoginRespDTO prepareLoginResponse(int systemId, SystemUser su, 
            int userId, String token) {
        
        usersService.setLastLoginDate(userId);
        
        UserDTO dto = usersService.getUserDTOById(userId);
        BitSet roles = usersService.getRoles(su, UserManagementService.RolesType.ALL);
        
        LoginRespDTO resp;
        if (permissionsService.isSuperUserRole(roles)) {
            resp = new LoginRespDTO(dto.getLogin(), token, true);
        } else {
            PermissionNodeDTO perm = permissionsService.getRolePermissions(
                    systemId, roles, EnumSet.noneOf(PermissionManagementService.IncludeFlags.class));
    
            resp = new LoginRespDTO(dto.getLogin(), token, 
                    CollectionsUtil.toIntList(Bits.toIntArray(roles)), 
                    perm != null ? perm.safeGetNodes() : null);
        }
        
        // prepare application menu
        resp.setAppMenu(systemsService.getAppMenu(systemId, userId));
        
        // prepare user settings URL
        final URI url = URI.create(ServletHelpers.getRequestUrl(ServletHelpers.getRequest()));

        final String root = adminService.getAdminAppRoot();
        resp.setSettingsUrl(UriHelpers.setPath(url, (!root.equals("/") ? root : "")
                + "/profile.html"));
        return resp;
    }
}
