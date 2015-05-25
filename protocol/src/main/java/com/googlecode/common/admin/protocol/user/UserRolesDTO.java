
package com.googlecode.common.admin.protocol.user;

import java.util.Collections;
import java.util.List;
import com.googlecode.common.protocol.perm.PermissionNodeDTO;
import com.googlecode.common.admin.protocol.role.RoleDTO;


/**
 * Part of server responses, containing user roles data.
 */
public final class UserRolesDTO {

    private List<RoleDTO>       baseRoles;
    private List<RoleDTO>       availableRoles;
    private List<RoleDTO>       assignedRoles;
    
    private PermissionNodeDTO   permissionRoot;

    
    public UserRolesDTO() {
    }
    
    public UserRolesDTO(List<RoleDTO> baseRoles, List<RoleDTO> availableRoles, 
            List<RoleDTO> assignedRoles, PermissionNodeDTO permissionRoot) {
    
        this.baseRoles      = baseRoles;
        this.availableRoles = availableRoles;
        this.assignedRoles  = assignedRoles;
        
        this.permissionRoot = permissionRoot;
    }
    
    public List<RoleDTO> safeGetBaseRoles() {
        if (baseRoles == null) {
            return Collections.emptyList();
        }
        
        return baseRoles;
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see #safeGetBaseRoles()
     */
    @Deprecated
    public List<RoleDTO> getBaseRoles() {
        return baseRoles;
    }
    
    public void setBaseRoles(List<RoleDTO> baseRoles) {
        this.baseRoles = baseRoles;
    }
    
    public List<RoleDTO> safeGetAvailableRoles() {
        if (availableRoles == null) {
            return Collections.emptyList();
        }
        
        return availableRoles;
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see #safeGetAvailableRoles()
     */
    @Deprecated
    public List<RoleDTO> getAvailableRoles() {
        return availableRoles;
    }
    
    public void setAvailableRoles(List<RoleDTO> availableRoles) {
        this.availableRoles = availableRoles;
    }
    
    public List<RoleDTO> safeGetAssignedRoles() {
        if (assignedRoles == null) {
            return Collections.emptyList();
        }
        
        return assignedRoles;
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see #safeGetAssignedRoles()
     */
    @Deprecated
    public List<RoleDTO> getAssignedRoles() {
        return assignedRoles;
    }
    
    public void setAssignedRoles(List<RoleDTO> assignedRoles) {
        this.assignedRoles = assignedRoles;
    }
    
    public PermissionNodeDTO getPermissionRoot() {
        return permissionRoot;
    }
    
    public void setPermissionRoot(PermissionNodeDTO permissionRoot) {
        this.permissionRoot = permissionRoot;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{baseRoles: " + baseRoles
                + (availableRoles != null ? ", availableRoles: " + availableRoles : "")
                + (assignedRoles != null ? ", assignedRoles: " + assignedRoles : "")
                + (permissionRoot != null ? ", permissionRoot: " + permissionRoot : "") 
                + "}";
    }

}
