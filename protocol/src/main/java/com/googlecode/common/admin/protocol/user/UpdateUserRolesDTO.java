
package com.googlecode.common.admin.protocol.user;

import java.util.Collections;
import java.util.List;


/**
 * Part of server requests, containing update roles data.
 */
public final class UpdateUserRolesDTO {

    private int             systemId;
    private long            id;
    
    /** Roles bit set                                   */
    private List<Integer>   roles;
    
    /** Indicates weather roles are added or removed    */
    private boolean         added;
    
    
    public UpdateUserRolesDTO() {
    }
    
    public int getSystemId() {
        return systemId;
    }
    
    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public List<Integer> safeGetRoles() {
        if (roles == null) {
            return Collections.emptyList();
        }
        
        return roles;
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see #safeGetRoles()
     */
    @Deprecated
    public List<Integer> getRoles() {
        return roles;
    }
    
    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }
    
    public boolean isAdded() {
        return added;
    }
    
    public void setAdded(boolean added) {
        this.added = added;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{systemId: " + systemId 
                + ", id: " + id
                + ", added: " + added 
                + "}";
    }

}
