
package com.googlecode.common.admin.client.ui.user;

import java.util.Arrays;
import java.util.List;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.ui.ButtonImages;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.tree.BrowseTreeNode;
import com.googlecode.common.admin.protocol.user.UserGroupDTO;


/**
 * Represents user group node in the users subtree.
 */
public final class UserGroupTreeNode extends BrowseTreeNode {

    private static final String CMD_ADD     = ButtonType.ADD.getCommand();
    private static final String CMD_DELETE  = ButtonType.REMOVE.getCommand();
    private static final String CMD_RENAME  = ButtonType.EDIT.getCommand();
    
    private static final List<String> CMD_SET = 
            Arrays.asList(CMD_REFRESH, CMD_ADD, CMD_DELETE, CMD_RENAME);
    
    private final UserGroupRootTreeNode root;
    private final UserGroupDTO          dto;
    
    
    public UserGroupTreeNode(UserGroupRootTreeNode root, UserGroupDTO dto, 
            UserGroupBrowsePanel contentPanel) {
        
        super(dto.getName(), contentPanel, ButtonImages.INSTANCE.folder());
        
        this.root = root;
        this.dto  = dto;
    }
    
    public UserGroupDTO getUserGroupDTO() {
        return dto;
    }
    
    @Override
    public Widget getContentPanel() {
        Widget contentPanel = super.getContentPanel();
        ((UserGroupBrowsePanel)contentPanel).setUserGroupDTO(dto);
        return contentPanel;
    }
    
    @Override
    public List<String> getActionCommands() {
        return CMD_SET;
    }

    @Override
    public void actionPerformed(String cmd) {
        if (CMD_ADD.equals(cmd)) {
            root.onAddAction(this);
        
        } else if (CMD_DELETE.equals(cmd)) {
            root.onDeleteAction(this);
        
        } else if (CMD_RENAME.equals(cmd)) {
            root.onRenameAction(this);
        } else {
            super.actionPerformed(cmd);
        }
    }
    
    void applyNewName(String name) {
        dto.setName(name);
        setText(name);
        getParent().sortChildren();
    }

}
