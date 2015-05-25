
package com.googlecode.common.admin.client.ui.role;

import com.google.gwt.user.client.ui.TreeItem;
import com.googlecode.common.client.ui.CheckBoxTree;
import com.googlecode.common.client.ui.tree.CheckBoxTreeNode;
import com.googlecode.common.protocol.perm.PermissionDTO;
import com.googlecode.common.protocol.perm.PermissionNodeDTO;


/**
 * Represents permission node in the permissions tree.
 */
public final class PermissionTreeNode extends CheckBoxTreeNode {
    
    private final String        name;

    
    public PermissionTreeNode(CheckBoxTree tree, PermissionNodeDTO dto) {
        super(tree, dto.getTitle());
        
        this.name = dto.getName();
        addNodes(dto);
    }
    
    public PermissionTreeNode(CheckBoxTreeNode parent, PermissionNodeDTO dto) {
        super(parent, dto.getTitle());
        
        this.name = dto.getName();
        addNodes(dto);
    }
    
    private void addNodes(PermissionNodeDTO dto) {
        for (PermissionNodeDTO n : dto.safeGetNodes()) {
            addItem(new PermissionTreeNode(this, n));
        }
    
        for (PermissionDTO p : dto.safeGetPermissions()) {
            addItem(new PermissionTreeLeaf(this, p));
        }
    }

    public String getNodeName() {
        return name;
    }
    
    public void updatePermissions(PermissionNodeDTO dto) {
        for (int i = 0, count = getChildCount(); i < count; i++) {
            TreeItem item = getChild(i);
            
            if (item instanceof PermissionTreeNode) {
                PermissionTreeNode node = (PermissionTreeNode)item;
                String nodeName = node.getNodeName();
                PermissionNodeDTO nodeDto = null;
                
                if (dto != null) {
                    for (PermissionNodeDTO n : dto.safeGetNodes()) {
                        if (nodeName.equals(n.getName())) {
                            nodeDto = n;
                            break;
                        }
                    }
                }
                
                node.updatePermissions(nodeDto);
                
            } else if (item instanceof PermissionTreeLeaf) {
                PermissionTreeLeaf leaf = (PermissionTreeLeaf)item;
                String permName = leaf.getPermissionName();
                boolean allowed = false;
                
                if (dto != null) {
                    for (PermissionDTO p : dto.safeGetPermissions()) {
                        if (permName.equals(p.getName())) {
                            allowed = true;
                            break;
                        }
                    }
                }
                
                leaf.setNodeSelected(allowed);
            }
        }
    }
    
}
