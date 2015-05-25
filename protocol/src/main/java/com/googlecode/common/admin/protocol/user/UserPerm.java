
package com.googlecode.common.admin.protocol.user;

import java.util.ArrayList;
import com.googlecode.common.protocol.Permission;
import com.googlecode.common.protocol.PermissionNode;


/**
 * Defines user permissions.
 */
public final class UserPerm extends PermissionNode {

    private static final ArrayList<Permission> PERM_LIST = 
        new ArrayList<Permission>();
    
    public static final Permission READ = add(PERM_LIST,
            Permission.newReadPerm());
    
    public static final Permission CREATE = add(PERM_LIST,
            Permission.newCreatePerm());
    
    public static final Permission UPDATE = add(PERM_LIST,
            Permission.newUpdatePerm());
    
    public static final Permission ASSIGN_PERMISSIONS = add(PERM_LIST,
            Permission.newPerm("assignPermissions", "Assign permissions"));
    
    public static final Permission ASSIGN_ROLES = add(PERM_LIST,
            Permission.newPerm("assignRoles", "Assign roles"));
    
    public UserPerm() {
        super("User", "Users", PERM_LIST);
    }

}
