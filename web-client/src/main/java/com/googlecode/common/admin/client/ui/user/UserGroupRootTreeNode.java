
package com.googlecode.common.admin.client.ui.user;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.panel.MessageBox;
import com.googlecode.common.client.ui.tree.BrowseTreeNode;
import com.googlecode.common.client.ui.tree.LoadableBrowseTreeNode;
import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.protocol.RenameDTO;
import com.googlecode.common.admin.client.AdminImages;
import com.googlecode.common.admin.protocol.user.UserGroupDTO;
import com.googlecode.common.admin.protocol.user.UserGroupListResponse;
import com.googlecode.common.admin.protocol.user.UserGroupResponse;


/**
 * Represents user groups root tree node.
 */
public final class UserGroupRootTreeNode extends LoadableBrowseTreeNode {

    private static final String         CMD_ADD = ButtonType.ADD.getCommand();

    private static final Set<String>    CMD_SET = new HashSet<String>(
            Arrays.asList(CMD_REFRESH, CMD_ADD));
    
    private final UserBrowsePanel       browsePanel;
    private final UserGroupBrowsePanel  groupPanel;
    private final int                   systemId;
    
    
    public UserGroupRootTreeNode(int systemId) {
        super("Users", null, AdminImages.INSTANCE.userGroup());
        
        this.browsePanel = new UserBrowsePanel(systemId, true);
        this.groupPanel  = new UserGroupBrowsePanel(systemId);
        this.systemId    = systemId;
    }
    
    @Override
    public Collection<String> getActionCommands() {
        return CMD_SET;
    }
    
    @Override
    public void actionPerformed(String cmd) {
        if (CMD_ADD.equals(cmd)) {
            onAddAction(null);
        } else {
            super.actionPerformed(cmd);
        }
    }
    
    void onAddAction(UserGroupTreeNode node) {
        final BrowseTreeNode parent;
        final long parentId;
        if (node != null) {
            parent   = node;
            parentId = node.getUserGroupDTO().safeGetId();
        } else {
            parent   = this;
            parentId = 0L;
        }
        
        final MessageBox box = new MessageBox();
        box.showInput("Enter new group name", "NewGroup", new Command() {
            @Override
            public void execute() {
                String name = box.getInputValue();
                if (name != null) {
                    TaskManager.INSTANCE.execute(new CreateGroupTask(
                            parent, parentId, name, box));
                }
            }
        });
    }
    
    void onDeleteAction(UserGroupTreeNode node) {
        TaskManager.INSTANCE.execute(new DeleteGroupTask(node));
    }
    
    void onRenameAction(final UserGroupTreeNode node) {
        final String oldName = node.getText();
        final MessageBox box = new MessageBox();
        box.showInput("Enter new group name", oldName,  new Command() {
            @Override
            public void execute() {
                String name = box.getInputValue();
                if (name != null && !name.equals(oldName)) {
                    TaskManager.INSTANCE.execute(
                            new RenameGroupTask(node, name, box));
                }
            }
        });
    }
    
    @Override
    public Widget getContentPanel() {
        browsePanel.setData(0L);
        return browsePanel;
    }

    @Override
    public void onLoadChildren() {
        TaskManager.INSTANCE.execute(new LoadGroupsTask());
    }

    protected void refreshTree(List<UserGroupDTO> rootList) {
        removeAll();
        
        if (!rootList.isEmpty()) {
            for (UserGroupDTO n : rootList) {
                addNode(this, n);
            }
        } else {
            addDummyItem();
        }
        
        notifyStructureChanged();
    }
    
    private UserGroupTreeNode addNode(BrowseTreeNode parent, 
            UserGroupDTO dto) {
        
        UserGroupTreeNode node = new UserGroupTreeNode(this, dto, groupPanel);
        parent.add(node);
        
        for (UserGroupDTO n : dto.safeGetGroups()) {
            addNode(node, n);
        }
        
        return node;
    }

    private void addNew(BrowseTreeNode parent, UserGroupDTO dto) {
        parent.addAndSelect(new UserGroupTreeNode(this, dto, groupPanel), 
                parent != this, true);
    }
    
    
    private class LoadGroupsTask extends RequestTask<UserGroupListResponse> {

        public LoadGroupsTask() {
            super("Fetching groups...");
        }

        @Override
        protected void runTask() throws Exception {
            UserService service = GWT.create(UserService.class);
            RequestService.prepare(service).getAllGroups(systemId, this);
        }

        @Override
        protected void processSuccessResponse(UserGroupListResponse resp) {
            refreshTree(resp.safeGetDataList());
        }
    }


    private class CreateGroupTask extends RequestTask<UserGroupResponse> {

        private final BrowseTreeNode    parent;
        private final long              parentId;
        private final UserGroupDTO      dto;
        private final MessageBox        box;
        
        public CreateGroupTask(BrowseTreeNode parent, long parentId, 
                String name, MessageBox box) {
            
            super("Creating group...");
            
            this.parent     = parent;
            this.parentId   = parentId;
            this.dto        = new UserGroupDTO(name);
            this.box        = box;
        }

        @Override
        protected void runTask() throws Exception {
            UserService service = GWT.create(UserService.class);
            RequestService.prepare(service).createGroup(systemId, 
                    parentId, dto, this);
        }

        @Override
        protected void processSuccessResponse(UserGroupResponse resp) {
            addNew(parent, resp.getData());
            box.hide();
        }
    }


    private class DeleteGroupTask extends RequestTask<BaseResponse> {

        private final UserGroupTreeNode node;
        
        public DeleteGroupTask(UserGroupTreeNode node) {
            super("Deleting group...");
            
            this.node = node;
        }

        @Override
        protected void runTask() throws Exception {
            UserService service = GWT.create(UserService.class);
            RequestService.prepare(service).deleteGroup(
                    node.getUserGroupDTO().safeGetId(), this);
        }

        @Override
        protected void processSuccessResponse(BaseResponse resp) {
            node.getParent().removeAndSelect(node);
        }
    }

    
    private class RenameGroupTask extends RequestTask<BaseResponse> {

        private final UserGroupTreeNode node;
        private final RenameDTO         dto;
        private final MessageBox        box;
        
        public RenameGroupTask(UserGroupTreeNode node, String name, 
                MessageBox box) {
            
            super("Renaming group...");
            
            this.node = node;
            this.dto  = new RenameDTO(node.getUserGroupDTO().safeGetId(), name);
            this.box  = box;
        }

        @Override
        protected void runTask() throws Exception {
            UserService service = GWT.create(UserService.class);
            RequestService.prepare(service).renameGroup(dto, this);
        }

        @Override
        protected void processSuccessResponse(BaseResponse resp) {
            node.applyNewName(dto.getName());
            box.hide();
        }
    }

}
