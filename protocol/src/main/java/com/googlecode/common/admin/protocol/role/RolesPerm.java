
package com.googlecode.common.admin.protocol.role;

import java.util.ArrayList;
import com.googlecode.common.protocol.Permission;
import com.googlecode.common.protocol.PermissionNode;


/**
 * Defines roles permissions.
 */
public final class RolesPerm extends PermissionNode {

    private static final ArrayList<Permission> PERM_LIST = 
        new ArrayList<Permission>();

    public static final Permission RENAME = add(PERM_LIST,
            Permission.newRenamePerm());

    public static final Permission CREATE = add(PERM_LIST,
            Permission.newCreatePerm());

    
    public RolesPerm() {
        super("Roles", "Roles", PERM_LIST);
    }
}
