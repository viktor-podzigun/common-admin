
package com.googlecode.common.admin.protocol.user;


/**
 * Part of server requests and responses, containing LDAP user info.
 */
public final class LdapUserDTO {
    
    private String login;
    private String fullName;
    private String mail;
    private String domain;

    
    public LdapUserDTO() {
    }
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public String safeGetLogin() {
        return (login != null ? login : "");
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String safeGetFullName() {
        return (fullName != null ? fullName : "");
    }

    public String getMail() {
        return mail;
    }
    
    public void setMail(String mail) {
        this.mail = mail;
    }

    public String safeGetMail() {
        return (mail != null ? mail : "");
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }
    
    public String safeGetDomain() {
        return (domain != null ? domain : "");
    }

    @Override
    public String toString() {
        return getClass().getName() + "{"
                + (login != null ? ", login: " + login : "")
                + (fullName != null ? ", fullName: " + fullName : "")
                + (mail != null ? ", mail: " + mail : "")
                + (domain != null ? ", domain: " + domain : "")
                + "}";
    }
}
