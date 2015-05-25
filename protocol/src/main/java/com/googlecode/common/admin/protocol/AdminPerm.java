
package com.googlecode.common.admin.protocol;

import com.googlecode.common.admin.protocol.role.RolesPerm;
import com.googlecode.common.admin.protocol.server.ServerPerm;
import com.googlecode.common.admin.protocol.system.SystemPerm;
import com.googlecode.common.protocol.PermissionNode;
import com.googlecode.common.admin.protocol.user.UserGroupPerm;
import com.googlecode.common.admin.protocol.user.UserPerm;


/**
 * Defines admin permissions.
 */
public final class AdminPerm extends PermissionNode {
    
    public static final AdminPerm   ROOT = new AdminPerm();

    
    private AdminPerm() {
        super("Admin", "Admin permissions", null, 
                new UserPerm(),
                new UserGroupPerm(),
                new RolesPerm(),
                new ServerPerm(),
                new SystemPerm());
    }

}
