
package com.googlecode.common.admin.service.impl;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.googlecode.common.ldap.LdapClient;
import com.googlecode.common.ldap.LdapUser;
import com.googlecode.common.service.CommonResponses;
import com.googlecode.common.service.SettingsService;
import com.googlecode.common.service.ex.OperationFailedException;
import com.googlecode.common.service.impl.AbstractManageableService;
import com.googlecode.common.util.UriHelpers;
import com.googlecode.common.admin.protocol.user.LdapUserDTO;
import com.googlecode.common.admin.service.LdapService;


/**
 * Default implementation of LdapService interface.
 */
@Service
@Singleton
public class LdapServiceImpl extends AbstractManageableService implements
        LdapService {

    @Autowired
    private SettingsService         settingsService;

    private volatile LdapClient     ldapClient;

    
    @PostConstruct
    @Override
    public void init() {
        super.init();
        
        URI uri = URI.create("ldap://test:test@vins0010.nt.lan:389/OU=UA,OU=Employees,DC=nt,DC=lan");
        String login = null;
        String password = null;
        
        String userInfo = uri.getUserInfo();
        if (userInfo != null) {
            String[] authInfo = UriHelpers.splitUserInfo(userInfo);
            login = authInfo[0];
            password = authInfo[1];
        }
        
        String url = UriHelpers.hideUserInfo(uri);
        ldapClient = new LdapClient(url, login, password);
        
        log.info("ldapSettings:" 
                + "\n\turl   = " + url
                + "\n\tlogin = " + login);
    }
    
    @Override
    public List<LdapUserDTO> getAllUsers() {
        try {
            List<LdapUser> list = ldapClient.getAllUsers();
            List<LdapUserDTO> users = new ArrayList<LdapUserDTO>();
            
            for (LdapUser u : list) {
                users.add(convertToDTO(u));
            }
            
            return users;
        
        } catch (IOException x) {
            throw new OperationFailedException(
                    CommonResponses.INTERNAL_SERVER_ERROR,
                    "Error get all LDAP users", x);
        }
    }

    @Override
    public boolean authenticateUser(String login, String password) {
        try {
            return ldapClient.authenticateUser(login, password);
        } catch (Exception e) {
            throw new OperationFailedException(
                    CommonResponses.INTERNAL_SERVER_ERROR,
                    "Error authenticate LDAP user: " + login, e);
        }
    }
    
    private LdapUserDTO convertToDTO(LdapUser user) {
        LdapUserDTO dto = new LdapUserDTO();
        String userLogin = user.getLogin();
        int index = userLogin.indexOf("@");
        String login = userLogin;
        String domain = null;
        if (index > 0) {
            login = userLogin.substring(0, index);
            domain = userLogin.substring(index + 1, userLogin.length());
        }
        
        dto.setLogin(login);
        dto.setFullName(user.getFullName());
        dto.setMail(user.getEmail());
        dto.setDomain(domain);
        
        return dto;
    }
}
