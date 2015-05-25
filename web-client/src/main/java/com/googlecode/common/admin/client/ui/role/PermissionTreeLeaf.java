
package com.googlecode.common.admin.client.ui.role;

import com.googlecode.common.admin.client.AdminImages;
import com.googlecode.common.client.ui.tree.CheckBoxTreeNode;
import com.googlecode.common.protocol.perm.PermissionDTO;


/**
 * Represents permission under permissions node.
 */
public final class PermissionTreeLeaf extends CheckBoxTreeNode {

    private final String        name;
    private final long          id;
    
    
    public PermissionTreeLeaf(PermissionTreeNode parent, PermissionDTO dto) {
        super(parent, dto.getTitle());
        
        this.name   = dto.getName();
        this.id     = dto.safeGetId();
        
        setNodeSelected(dto.safeIsAllowed());
        setImage(AdminImages.INSTANCE.keySmall());
    }
    
    public String getPermissionName() {
        return name;
    }

    public long getPermissionId() {
        return id;
    }
    
}
