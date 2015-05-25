
package com.googlecode.common.admin.service;

import javax.servlet.http.HttpServletRequest;
import com.googlecode.common.protocol.admin.AuthUserDTO;
import com.googlecode.common.service.ex.OperationFailedException;
import com.googlecode.common.admin.domain.SystemEntity;
import com.googlecode.common.admin.domain.User;
import com.googlecode.common.admin.domain.UserToken;


/**
 * Service used to perform users authentication.
 */
public interface AuthorizationService {
    
    /**
     * Authenticate user for the given login request.
     * 
     * @param request   login request object
     * @return          user object or <code>null</code> if no such found
     */
    public User getUserForLogin(HttpServletRequest request);
    
    /**
     * Authenticate user for the given request.
     * 
     * @param request   request object
     * @return          user token object or <code>null</code> if no such 
     *                  found
     */
    public UserToken getAuthenticateUserToken(HttpServletRequest request);
    
    /**
     * Checks if the given user can login.
     * 
     * @param user      user entity
     * @return          the same as input parameter
     */
    public User checkUser(User user);
    
    /**
     * Authenticate user by token.
     * 
     * @param request   HTTP request containing user token
     * 
     * @throws OperationFailedException if cannot authenticate user
     */
    public void authUserToken(HttpServletRequest request);
    
    /**
     * Authenticate user for the given request and system.
     * 
     * @param request   request object
     * @param system    system entity
     * @return          user's authorization info or <code>null</code> 
     *                  if no such found
     */
    public AuthUserDTO getAuthenticateUserDTO(HttpServletRequest request, 
            SystemEntity system);
    
    /**
     * Creates new token for the given user.
     * 
     * @param user          user entity
     * @param rememberMe    indicates whether to create rememberMe token
     * @return              new token for the given user
     */
    public String createUserToken(User user, boolean rememberMe);
    
    /**
     * Logouts user for the given request object.
     * 
     * @param request   request object
     */
    public void logoutUser(HttpServletRequest request);

}
