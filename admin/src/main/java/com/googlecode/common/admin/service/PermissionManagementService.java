
package com.googlecode.common.admin.service;

import java.util.BitSet;
import java.util.List;
import java.util.Set;
import com.googlecode.common.protocol.Permission;
import com.googlecode.common.protocol.RenameDTO;
import com.googlecode.common.protocol.perm.PermissionNodeDTO;
import com.googlecode.common.admin.protocol.role.PermissionListDTO;
import com.googlecode.common.admin.protocol.role.RoleDTO;


/**
 * Contains methods for managing roles/permissions.
 */
public interface PermissionManagementService {

    public static enum IncludeFlags {
        ALL_PERMISSIONS,
        TITLES,
        IDS,
        ALLOWS,
        ROLES,
    }
    
    /**
     * Checks weather the given role is super user's role.
     * 
     * @param roles roles bit set
     * @return      <code>true</code> if the specified role is super user, 
     *              or <code>false</code> otherwise
     */
    public boolean isSuperUserRole(BitSet roles);
    
    /**
     * Checks if any of the specified roles has the specified permission.
     * 
     * @param roles roles bit set
     * @param p     permission to check
     * @return      <code>true</code> if any of the specified roles has 
     *              the specified permission, or <code>false</code> otherwise
     */
    public boolean hasRolePermission(BitSet roles, Permission p);
    
    /**
     * Returns roles list for the given system.
     * 
     * @param systemId      system's ID
     * @return              roles list for the given system
     */
    public List<RoleDTO> getRoles(int systemId);

    /**
     * Returns permissions tree root by the given roles.
     * 
     * @param systemId      system's ID
     * @param roles         roles bit set
     * @param includeFlags  set of include flags, that indicates what data 
     *                      should be returned
     * @return permissions tree root for the given roles mask
     */
    public PermissionNodeDTO getRolePermissions(int systemId, BitSet roles, 
            Set<IncludeFlags> includeFlags);

    /**
     * Reads/initializes system permissions.
     * 
     * @param systemId      system's id
     * @param root          system's permissions
     * @return              current system permissions
     */
    public PermissionNodeDTO getSystemPermissions(int systemId, 
            PermissionNodeDTO root);

    /**
     * Updates given role's permissions.
     * 
     * @param roleId    role's ID
     * @param dto       permissions DTO
     * @return          operation status response
     */
    public void updateRolePermissions(long roleId, PermissionListDTO dto);

    /**
     * Renames role.
     * 
     * @param reqData   roles data
     */
    public void renameRole(RenameDTO reqData);
    
    /**
     * Creates new role for the given system.
     * 
     * @param systemId  system's ID
     * @param dto       role's data
     * @return created role
     */
    public RoleDTO createRole(int systemId, RoleDTO dto);
    
}
