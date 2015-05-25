
package com.googlecode.common.admin.client.ui.role;

import com.googlecode.common.client.ui.CheckBoxTree;
import com.googlecode.common.protocol.perm.PermissionNodeDTO;


/**
 * Permissions tree.
 */
public class PermissionTree extends CheckBoxTree {

    private PermissionTreeNode  root;
    

    public PermissionTree(boolean isReadOnly) {
        super(isReadOnly);
    }
    
    public PermissionTreeNode getPermissionRoot() {
        return root;
    }

    public void refreshTree(PermissionNodeDTO dto) {
        if (dto == null) {
            return;
        }
        
        if (root == null) {
            root = new PermissionTreeNode(this, dto);
            addItem(root);
            root.setState(true);
        } else {
            root.updatePermissions(dto);
        }
        
        refreshNodesState();
    }
    
}
