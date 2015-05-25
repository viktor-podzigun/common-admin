
package com.googlecode.common.admin.protocol.user;

import java.util.ArrayList;
import com.googlecode.common.protocol.Permission;
import com.googlecode.common.protocol.PermissionNode;


/**
 * Defines user group permissions.
 */
public final class UserGroupPerm extends PermissionNode {

    private static final ArrayList<Permission> PERM_LIST = 
        new ArrayList<Permission>();

    public static final Permission RENAME = add(PERM_LIST,
            Permission.newRenamePerm());

    public static final Permission DELETE = add(PERM_LIST,
            Permission.newDeletePerm());

    
    public UserGroupPerm() {
        super("UserGroup", "UserGroups", PERM_LIST);
    }
}
