
package com.googlecode.common.admin.protocol.user;

import java.util.Date;


/**
 * Part of requests and server response, containing user
 * details.
 */
public final class UserDTO {

    private Integer     id;
    private Long        parentId;
    private String      parentName;
    
    private String      login;
    private String      passHash;
    
    private Date        created;
    private boolean     active;
    private Date        modified;
    private Date        lastLogin;
    
    private Long        contactId;
    private String      companyName;
    
    private String      ldapDomain;
    
    
    public UserDTO() {
    }
    
    /**
     * Returns unique identifier of the user.
     * @return unique identifier of the user
     */
    public int safeGetId() {
        return (id != null ? id.intValue() : 0);
    }

    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see safeGetId()
     */
    @Deprecated
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns users alias that is used for name.
     * @return users alias.
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets user name string for user.
     * User name has to be unique or insert will fail.
     * 
     * @param alias the alias to set
     */
    public void setLogin(String name) {
        this.login = name;
    }

    /**
     * Returns hash of users password.
     * @return the passHash hash of users password.
     */
    public String getPassHash() {
        return passHash;
    }

    /**
     * Sets double SHA-1 of user password.
     * One SHA-1 is calculated when user enters password on the 
     * client side, second is for storing on the server calculated from
     * previous hash. It helps to avoid vocabularies and database obtaining
     * vulnerabilities.
     * 
     * @param passHash the password hash
     */
    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }
    
    /**
     * Returns user creation timestamp.
     * @return the creation timestamp.
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Sets user creation timestamp.
     * NOTE: The field is not updatable in database, so cannot be changed after creation. 
     * @param created user creation time.
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * Returns active state of this user.
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets active or inactive state for this user.
     * @param active the active state to set.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    public long safeGetParentId() {
        return (parentId != null ? parentId.longValue() : 0L);
    }

    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see safeGetParentId()
     */
    @Deprecated
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentName() {
        return parentName;
    }

    public long safeGetContactId() {
        return (contactId != null ? contactId.longValue() : 0L);
    }

    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see safeGetContactId()
     */
    @Deprecated
    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setLdapDomain(String ldapDomain) {
        this.ldapDomain = ldapDomain;
    }

    public String getLdapDomain() {
        return ldapDomain;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{id: " + id
                + (parentId != null ? ", parentId: " + parentId : "")
                + (parentName != null ? ", parentName: " + parentName : "")
                + (login != null ? ", login: " + login : "")
                + (passHash != null ? ", passHash: " + passHash : "")
                + (created != null ? ", created: " + created : "")
                + (contactId != null ? ", contactId: " + contactId : "")
                + (modified != null ? ", modified: " + modified : "")
                + (lastLogin != null ? ", lastLogin: " + lastLogin : "")
                + (companyName != null ? ", companyName: " + companyName : "")
                + (ldapDomain != null ? ", ldapDomain: " + ldapDomain : "")
                + ", active: " + active 
                + "}";
    }
}
