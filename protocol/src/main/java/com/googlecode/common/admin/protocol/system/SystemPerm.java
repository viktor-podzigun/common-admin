
package com.googlecode.common.admin.protocol.system;

import java.util.ArrayList;
import com.googlecode.common.protocol.Permission;
import com.googlecode.common.protocol.PermissionNode;


/**
 * Defines system permissions.
 */
public final class SystemPerm extends PermissionNode {

    private static final ArrayList<Permission> PERM_LIST = 
        new ArrayList<Permission>();
    
    public static final Permission READ = add(PERM_LIST,
            Permission.newReadPerm());
    
    public static final Permission CREATE = add(PERM_LIST,
            Permission.newCreatePerm());
    
    public static final Permission UPDATE = add(PERM_LIST,
            Permission.newUpdatePerm());
    
    
    public SystemPerm() {
        super("System", "Systems", PERM_LIST);
    }

}
