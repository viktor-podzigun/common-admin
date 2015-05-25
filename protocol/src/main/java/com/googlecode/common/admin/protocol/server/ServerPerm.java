
package com.googlecode.common.admin.protocol.server;

import java.util.ArrayList;
import com.googlecode.common.protocol.Permission;
import com.googlecode.common.protocol.PermissionNode;


/**
 * Defines servers permissions.
 */
public final class ServerPerm extends PermissionNode {

    private static final ArrayList<Permission> PERM_LIST = 
        new ArrayList<Permission>();
    
    public static final Permission READ = add(PERM_LIST,
            Permission.newReadPerm());
    
    public static final Permission CREATE = add(PERM_LIST,
            Permission.newCreatePerm());
    
    public static final Permission UPDATE = add(PERM_LIST,
            Permission.newUpdatePerm());
    
    public static final Permission RELOAD = add(PERM_LIST,
            Permission.newPerm("reload", "Reload"));
    
    
    public ServerPerm() {
        super("Servers", "Servers", PERM_LIST);
    }

}
