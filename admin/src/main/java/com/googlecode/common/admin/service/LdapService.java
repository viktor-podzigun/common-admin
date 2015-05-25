
package com.googlecode.common.admin.service;

import java.util.List;
import com.googlecode.common.admin.protocol.user.LdapUserDTO;


/**
 * This service responsible for the LDAP-part.
 */
public interface LdapService {

    public List<LdapUserDTO> getAllUsers();
    
    public boolean authenticateUser(String login, String password);
}
