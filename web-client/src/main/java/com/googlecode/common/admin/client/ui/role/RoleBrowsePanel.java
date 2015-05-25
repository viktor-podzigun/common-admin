
package com.googlecode.common.admin.client.ui.role;

import com.googlecode.common.admin.client.AdminInjector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.panel.LoadablePanel;
import com.googlecode.common.client.ui.tree.CheckBoxTreeNode;
import com.googlecode.common.client.util.CollectionsUtil;
import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.protocol.UserPermissions;
import com.googlecode.common.protocol.perm.PermissionNodeResponse;
import com.googlecode.common.admin.client.event.ConfigChangedEvent;
import com.googlecode.common.admin.protocol.role.PermissionListDTO;
import com.googlecode.common.admin.protocol.role.RoleDTO;
import com.googlecode.common.admin.protocol.user.UserPerm;


/**
 * Role browse panel.
 */
public final class RoleBrowsePanel extends LoadablePanel {

    private final int                   systemId;
    private final RolePermissionsTree   tree;
    
    private RoleDTO                     currRoleDto;
    
    
    public RoleBrowsePanel(int systemId) {
        this.systemId = systemId;
        this.tree     = new RolePermissionsTree();
        
        initWidget(new ScrollPanel(tree));
    }
    
    public void setRoleDTO(RoleDTO dto) {
        currRoleDto = dto;
    }

    @Override
    public void onLoadData() {
        if (currRoleDto != null) {
            TaskManager.INSTANCE.execute(new LoadPermissionsTask(
                    currRoleDto.safeGetBitIndex()));
        }
    }

    private List<Long> getIdsFromList(List<PermissionTreeLeaf> list) {
        final int size = list.size();
        List<Long> array = new ArrayList<Long>(size);
        
        for (int i = 0; i < size; i++) {
            array.add(list.get(i).getPermissionId());
        }
        
        return array;
    }
    
    private List<PermissionTreeLeaf> getNodesList(
            List<PermissionTreeLeaf> list, PermissionTreeNode root, 
            boolean isSelected) {
        
        int count = root.getChildCount();
        if (count <= 0) {
            return list;
        }
        
        for (int i = 0; i < count; i++) {
            TreeItem node = root.getChild(i);
            
            if (node instanceof PermissionTreeLeaf) {
                PermissionTreeLeaf n = (PermissionTreeLeaf) node;
                if (n.isNodeSelected() == isSelected) {
                    list = CollectionsUtil.addToList(list, 
                            (PermissionTreeLeaf)node);
                }
            } else if (node instanceof PermissionTreeNode) {
                list = getNodesList(list, (PermissionTreeNode)node, 
                        isSelected);
            }
        }

        return list;
    }    

    private void updatePermissions(List<Long> dataList, boolean isSelected, 
            CheckBoxTreeNode node) {

        TaskManager.INSTANCE.execute(new UpdatePermissionsTask(
                dataList, isSelected, node));
    }
    
    
    private class RolePermissionsTree extends PermissionTree {
        
        public RolePermissionsTree() {
            super(false);
        }
        
        @Override
        protected void onNodeSelection(boolean isSelected, CheckBoxTreeNode node) {
            if (!UserPermissions.has(UserPerm.ASSIGN_PERMISSIONS)) {
                return;
            }
            
            if (node instanceof PermissionTreeNode) {
                List<PermissionTreeLeaf> list = getNodesList(null,
                        (PermissionTreeNode) node, !isSelected);

                if (list != null) {
                    updatePermissions(getIdsFromList(list), isSelected, node);
                }
            } else if (node instanceof PermissionTreeLeaf) {
                long id = ((PermissionTreeLeaf)node).getPermissionId();
                updatePermissions(Arrays.asList(id), isSelected, node);
            }
        }
    }
    
    
    private class LoadPermissionsTask extends RequestTask<PermissionNodeResponse> {

        private final int   bitIndex;
        
        
        public LoadPermissionsTask(int bitIndex) {
            super("Fetching permissions...");
            
            this.bitIndex = bitIndex;
        }

        @Override
        protected void runTask() throws Exception {
            RoleService service = GWT.create(RoleService.class);
            RequestService.prepare(service);
            if (tree.getPermissionRoot() == null) {
                service.getAllPermissions(systemId, bitIndex, this);
            } else {
                service.getAllowedPermissions(systemId, bitIndex, this);
            }
        }

        @Override
        protected void processFailureResponse(PermissionNodeResponse resp) {
            tree.refreshTree(null);
            super.processFailureResponse(resp);
        }
        
        @Override
        protected void processSuccessResponse(PermissionNodeResponse resp) {
            tree.refreshTree(resp.getData());
        }
    }

    
    private class UpdatePermissionsTask extends RequestTask<BaseResponse> {

        private final PermissionListDTO dto;
        private final CheckBoxTreeNode  node;
        private final boolean           isSelected;

        
        public UpdatePermissionsTask(List<Long> dataList, boolean isSelected, 
                CheckBoxTreeNode node) {

            super("Update permissions...");
            
            this.node       = node;
            this.isSelected = isSelected;

            dto = new PermissionListDTO();
            if (isSelected) {
                dto.setAllowList(dataList);
            } else {
                dto.setDisallowList(dataList);
            }
        }

        @Override
        protected void runTask() throws Exception {
            RoleService service = GWT.create(RoleService.class);
            RequestService.prepare(service).updateRolePermissions(
                    currRoleDto.safeGetId(), dto, this);
        }

        @Override
        protected void processSuccessResponse(BaseResponse resp) {
            tree.setNodeSelection(isSelected, node);
            
            AdminInjector.INSTANCE.eventBus().fireEvent(
                    new ConfigChangedEvent(systemId));
        }
    }
    
}
