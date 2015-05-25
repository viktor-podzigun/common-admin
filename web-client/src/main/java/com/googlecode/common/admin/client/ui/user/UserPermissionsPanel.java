
package com.googlecode.common.admin.client.ui.user;

import com.googlecode.common.admin.client.AdminImages;
import com.googlecode.common.admin.client.ui.role.PermissionTree;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.FlowPanel;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.ImageTextCell;
import com.googlecode.common.client.ui.PickList;
import com.googlecode.common.client.ui.PickList.Item;
import com.googlecode.common.client.ui.panel.LoadablePanel;
import com.googlecode.common.protocol.UserPermissions;
import com.googlecode.common.protocol.perm.PermissionNodeResponse;
import com.googlecode.common.admin.protocol.role.RoleDTO;
import com.googlecode.common.admin.protocol.user.UpdateUserRolesDTO;
import com.googlecode.common.admin.protocol.user.UserPerm;
import com.googlecode.common.admin.protocol.user.UserRolesDTO;
import com.googlecode.common.admin.protocol.user.UserRolesResponse;


/**
 * Panel for displaying and editing user's permissions data.
 */
public final class UserPermissionsPanel extends LoadablePanel {

    private final FlowPanel         container;
    private final RolesPanel        rolesPanel;
    private final PermissionTree tree;
    
    private final boolean           isGroup;
    private final int               systemId;
    
    private long                    currId;
    
    
    public UserPermissionsPanel(int systemId, boolean isGroup) {
        this.systemId = systemId;
        this.isGroup  = isGroup;
        
        container = new FlowPanel();
        container.setStyleName("row-fluid");
        initWidget(container);
        
        rolesPanel = new RolesPanel();
        rolesPanel.addStyleName("span6");
        rolesPanel.setAddEnabled(UserPermissions.has(UserPerm.ASSIGN_ROLES));
        rolesPanel.setRemoveEnabled(UserPermissions.has(UserPerm.ASSIGN_ROLES));
        rolesPanel.setSourceTitle("Available roles");
        rolesPanel.setDestinationTitle("Assigned roles");
        container.add(rolesPanel);
        
        tree = new PermissionTree(true);
        tree.addStyleName("span6");
        
        container.add(tree);
    }
    
    public void setData(long id) {
        this.currId = id;
        setNeedLoad(true);
    }
    
    @Override
    public void onLoadData() {
        if (currId != 0L) {
            TaskManager.INSTANCE.execute(new LoadRolesTask());
        }
    }
    
    @Override
    public void onDeactivated() {
        setNeedLoad(false);
    }

    private void refreshRoles(UserRolesDTO roles) {
        ArrayList<Role> srcList = new ArrayList<Role>();
        for (RoleDTO r : roles.safeGetAvailableRoles()) {
            srcList.add(new Role(r, false));
        }
        
        rolesPanel.setSourceList(srcList);
    
        ArrayList<Role> dstList = new ArrayList<Role>();
        for (RoleDTO r : roles.safeGetBaseRoles()) {
            dstList.add(new Role(r, true));
        }
        
        for (RoleDTO r : roles.safeGetAssignedRoles()) {
            dstList.add(new Role(r, false));
        }
        
        rolesPanel.setDestinationList(dstList);
    }
    
    
    private static class Role implements Item {
        
        private final RoleDTO   dto;
        private final boolean   isBase;

        
        public Role(RoleDTO dto, boolean isBase) {
            this.dto    = dto;
            this.isBase = isBase;
        }
        
        public RoleDTO getDto() {
            return dto;
        }
        
        @Override
        public boolean isMovable() {
            return !isBase;
        }
        
        @Override
        public String toString() {
            return dto.getTitle();
        }

        public ImageResource getImage() {
            return (isBase ? AdminImages.INSTANCE.roleBase()
                    : AdminImages.INSTANCE.role());
        }
    }

    
    private static class RoleCell extends ImageTextCell<Role> {
        
        public static final RoleCell    INSTANCE = new RoleCell();
        
        private RoleCell() {
        }
        
        @Override
        public void render(Context context, Role value, SafeHtmlBuilder sb) {
            setImage(value.getImage());

            super.render(context, value, sb);
        }
    }
    
    
    private class RolesPanel extends PickList<Role> {

        public RolesPanel() {
            super(RoleCell.INSTANCE);
        }
        
        @Override
        protected void onChangeValues(Collection<Role> items, 
                List<Role> srcList, List<Role> dstList, 
                boolean isAddOperation) {
            
            List<Integer> roles = new ArrayList<Integer>(items.size());
            for (Role r : items) {
                roles.add(r.getDto().safeGetBitIndex());
            }
            
            TaskManager.INSTANCE.execute(new UpdateRolesTask(
                    roles, srcList, dstList, isAddOperation));
        }
    }

    
    private class LoadRolesTask extends RequestTask<UserRolesResponse> {
        
        public LoadRolesTask() {
            super("Fetching roles...");
        }
        
        @Override
        protected void runTask() throws Exception {
            boolean allPermission = (tree.getPermissionRoot() == null);
            
            UserService service = GWT.create(UserService.class);
            RequestService.prepare(service);
            if (isGroup) {
                service.getGroupRoles(systemId, currId, allPermission, this);
            } else {
                service.getUserRoles(systemId, (int)currId, allPermission, this);
            }
        }
        
        @Override
        protected void processFailureResponse(UserRolesResponse response) {
            rolesPanel.setLists(null, null);
            tree.refreshTree(null);
            
            super.processFailureResponse(response);
        }
        
        @Override
        protected void processSuccessResponse(UserRolesResponse resp) {
            UserRolesDTO dto = resp.getData();
            refreshRoles(dto);
            tree.refreshTree(dto.getPermissionRoot());
        }
    }

    
    private class UpdateRolesTask extends RequestTask<PermissionNodeResponse> {
        
        private final UpdateUserRolesDTO    dto;
        private final List<Role>            srcList;
        private final List<Role>            dstList;
        
        
        public UpdateRolesTask(List<Integer> roles, List<Role> srcList, 
                List<Role> dstList, boolean isAdd) {
            
            super("Updating roles...");
            
            UpdateUserRolesDTO dto = new UpdateUserRolesDTO();
            dto.setAdded(isAdd);
            dto.setRoles(roles);
            dto.setSystemId(systemId);
            dto.setId(currId);
            
            this.dto     = dto;
            this.srcList = srcList;
            this.dstList = dstList;
        }
        
        @Override
        protected void runTask() throws Exception {
            UserService service = GWT.create(UserService.class);
            RequestService.prepare(service);
            if (isGroup) {
                service.updateGroupRoles(dto, this);
            } else {
                service.updateUserRoles(dto, this);
            }
        }
        
        @Override
        protected void processSuccessResponse(PermissionNodeResponse resp) {
            tree.refreshTree(resp.getData());
            rolesPanel.setLists(srcList, dstList);
        }
    }

}
