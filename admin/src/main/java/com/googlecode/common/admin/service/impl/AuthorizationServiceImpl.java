
package com.googlecode.common.admin.service.impl;

import com.googlecode.common.admin.service.AdminResponses;
import com.googlecode.common.admin.service.AuthorizationService;
import com.googlecode.common.admin.service.LdapService;
import com.googlecode.common.admin.service.UserManagementService;
import java.util.BitSet;
import java.util.Date;
import java.util.UUID;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import com.googlecode.common.protocol.admin.AuthUserDTO;
import com.googlecode.common.service.AuthData;
import com.googlecode.common.service.CommonResponses;
import com.googlecode.common.service.ex.OperationFailedException;
import com.googlecode.common.util.Bits;
import com.googlecode.common.util.CollectionsUtil;
import com.googlecode.common.util.SafeDigest;
import com.googlecode.common.util.StringHelpers;
import com.googlecode.common.admin.dao.UserDao;
import com.googlecode.common.admin.dao.UserTokenDao;
import com.googlecode.common.admin.domain.SystemEntity;
import com.googlecode.common.admin.domain.SystemUser;
import com.googlecode.common.admin.domain.User;
import com.googlecode.common.admin.domain.UserToken;


/**
 * Default implementation of authorization service.
 * 
 * @see com.googlecode.common.admin.service.AuthorizationService
 */
@Service
@Singleton
public class AuthorizationServiceImpl implements AuthorizationService {

    private static final long   TOKEN_TTL_DEFAULT  = 20 * 60 * 1000L; //20 minutes
    private static final long   TOKEN_TTL_REMEMBER = 14 * 24 * 60 * 60 * 1000L; //14 days
    
	private final Logger       log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserDao                     usersDao;

    @Autowired
    private UserManagementService usersService;

    @Autowired
    private UserTokenDao                usersTokensDao;
    
    @Autowired
    private PlatformTransactionManager  txManager;
    
    @Autowired
    private LdapService ldapService;
	
	
    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public User getUserForLogin(HttpServletRequest request) {
        AuthData authInfo = AuthData.parse(request);
        if (authInfo == null) {
            if (log.isWarnEnabled()) {
                log.warn("No authorization data provided");
            }
            
            return null;
        }
        
        if (authInfo.isToken()) {
            if (log.isWarnEnabled()) {
                log.warn("Cannot login by token");
            }
            return null;
        }
        
        return authUserInternal(authInfo.getLogin(), authInfo.getPassword());
    }
    
    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public UserToken getAuthenticateUserToken(HttpServletRequest request) {
        return authUserTokenInternal(request);
    }
    
    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public AuthUserDTO getAuthenticateUserDTO(HttpServletRequest request, 
            SystemEntity system) {
        
        UserToken t = authUserTokenInternal(request);
        if (t == null) {
            return null;
        }
        
        User user = t.getUser();
        SystemUser su = usersService.getSystemUserById(system, user);
        BitSet roles = usersService.getRoles(su, UserManagementService.RolesType.ALL);
        
        return new AuthUserDTO(user.getId(), user.getLogin(), 
                CollectionsUtil.toIntList(Bits.toIntArray(roles)));
    }

    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public void logoutUser(HttpServletRequest request) {
        AuthData authInfo = AuthData.parse(request);
        if (authInfo == null) {
            if (log.isWarnEnabled()) {
                log.warn("No authorization data provided");
            }
            
            return;
        }
        
        if (!authInfo.isToken()) {
            log.warn("No user token in request data");
            return;
        }
        
        String token = authInfo.getToken();
        UserToken t = usersTokensDao.get(token);
        if (t == null) {
            log.warn("No such token: " + token);
            return;
        }
        
        usersTokensDao.delete(t);
    }

    @Override
    public String createUserToken(User user, boolean rememberMe) {
        while (true) {
            // Manually create write transaction
            TransactionStatus txStatus = txManager.getTransaction(
                    new DefaultTransactionDefinition(
                            TransactionDefinition.PROPAGATION_REQUIRES_NEW));
            
            try {
                String token = SafeDigest.digest(UUID.randomUUID().toString());
                
                UserToken t;
                if (rememberMe) {
                    t = new UserToken(token, new Date(
                            System.currentTimeMillis() + TOKEN_TTL_REMEMBER));
                    t.setRememberMe(true);
                } else {
                    t = new UserToken(token, new Date(
                            System.currentTimeMillis() + TOKEN_TTL_DEFAULT));
                }
                
                t.setUser(user);
                usersTokensDao.save(t);

                txManager.commit(txStatus);
                return token;
            
            } catch (DataIntegrityViolationException x) {
                log.warn("Token collision, will regenerate, for user: " 
                        + user.getId());
                
                continue;
                
            } catch (Exception x) {
                if (!txStatus.isCompleted()) {
                    txManager.rollback(txStatus);
                }
                
                throw new RuntimeException(x);
            }
        }
    }
    
    @Override
    public User checkUser(User user) {
        if (user == null) {
            throw new OperationFailedException(
                    CommonResponses.AUTHENTICATION_FAILED, 
                    "User authentication failure");
        }
        
        if (!user.isActive()) {
            throw new OperationFailedException(
                    AdminResponses.USER_NOT_ACTIVE,
                    "User (" + user.getLogin() + ") is not active");
        }
        
        return user;
    }
    
    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public void authUserToken(HttpServletRequest req) {
        UserToken token = getAuthenticateUserToken(req);
        checkUser(token != null ? token.getUser() : null);
    }

    private UserToken authUserTokenInternal(HttpServletRequest request) {
        AuthData authInfo = AuthData.parse(request);
        if (authInfo == null) {
            if (log.isWarnEnabled()) {
                log.warn("No authorization data provided");
            }
            
            return null;
        }
        
        if (!authInfo.isToken()) {
            if (log.isWarnEnabled()) {
                log.warn("No user token in request data");
            }
            
            return null;
        }
        
        UserToken t = getToken(authInfo.getToken());
        if (t != null) {
            // prefetch user entity in this transaction
            User user = t.getUser();
            if (!user.isActive()) {
                if (log.isWarnEnabled()) {
                    log.warn("User is not active, login: " + user.getLogin());
                }
                
                return null;
            }
        }
        
        return t;
    }
    
    private User authUserInternal(String login, String passHash) {
        if (login == null || login.isEmpty() 
                || passHash == null || passHash.isEmpty()) {
            
            if (log.isInfoEnabled()) {
                log.info("User's login or password is empty, login: " + login);
            }
            
            return null;
        }
                
        User user = usersDao.getByLogin(login);
        if (user == null) {
            if (log.isInfoEnabled()) {
                log.info("User with login: \"" + login + "\" not found");
            }
            
            return null;
        }
        
        if (!user.isActive()) {
            if (log.isInfoEnabled()) {
                log.info("User is not active, login: " + login);
            }
            
            return null;
        }
        
        // LDAP user authorithation
        String ldapDomain = user.getLdapDomain();
        if (!StringHelpers.isNullOrEmpty(ldapDomain)) {
            boolean authorized = ldapService.authenticateUser(login + "@"
                    + ldapDomain, passHash);
            if (authorized) {
                return user;
            }

            return null;
        }
        
        final String digest = SafeDigest.digest(passHash);
        if (!digest.equals(user.getPassHash())) {
            if (log.isInfoEnabled()) {
                log.info("Wrong password for login: " + login);
            }
            
            return null;
        }
        
        return user;
    }
    
    private UserToken getToken(String tokenStr) {
        UserToken token = usersTokensDao.get(tokenStr);
        if (token == null) {
            if (log.isTraceEnabled()) {
                log.trace("Token not found:" + tokenStr);
            }
            
            return null;
        }
    
        Date expDate  = token.getExpireDate();
        long currTime = System.currentTimeMillis();
        if (expDate.getTime() < currTime) {
            if (log.isDebugEnabled()) {
                log.debug("Token expired for user: " 
                        + token.getUser().getLogin());
            }
            
            usersTokensDao.delete(token);
            return null;
        }
        
        Boolean rememberMe = token.getRememberMe();
        if (rememberMe != null && rememberMe) {
            token.setExpireDate(new Date(currTime + TOKEN_TTL_REMEMBER));
        } else {
            token.setExpireDate(new Date(currTime + TOKEN_TTL_DEFAULT));
        }
        
        return usersTokensDao.merge(token);
    }

}
