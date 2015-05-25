
package com.googlecode.common.admin.client.ui.system;

import com.googlecode.common.admin.client.AdminImages;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.ImageTextCell;
import com.googlecode.common.client.ui.PickList;
import com.googlecode.common.client.ui.PickList.Item;
import com.googlecode.common.client.ui.panel.LoadablePanel;
import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.protocol.IntListDTO;
import com.googlecode.common.protocol.UserPermissions;
import com.googlecode.common.admin.protocol.system.SystemDTO;
import com.googlecode.common.admin.protocol.user.UserPerm;
import com.googlecode.common.admin.protocol.user.UserSystemsDTO;
import com.googlecode.common.admin.protocol.user.UserSystemsResponse;


/**
 * Panel for displaying and editing users to systems mapping.
 */
public final class UserSystemsPanel extends LoadablePanel {

    private final SystemsPanel  systemsPanel = new SystemsPanel();
    
    private int                 currId;
    
    
    public UserSystemsPanel() {
        initWidget(systemsPanel);
        
        systemsPanel.setAddEnabled(UserPermissions.has(UserPerm.UPDATE));
        systemsPanel.setRemoveEnabled(UserPermissions.has(UserPerm.UPDATE));
        systemsPanel.setSourceTitle("Available apps");
        systemsPanel.setDestinationTitle("Assigned apps");
    }
    
    public void setData(int id) {
        this.currId = id;
        setNeedLoad(true);
    }
    
    @Override
    public void onLoadData() {
        if (currId != 0) {
            TaskManager.INSTANCE.execute(new LoadSystemsTask());
        }
    }
    
    @Override
    public void onDeactivated() {
        setNeedLoad(false);
    }

    private void refreshSystems(UserSystemsDTO systems) {
        ArrayList<System> srcList = new ArrayList<System>();
        for (SystemDTO r : systems.safeGetAvailableSystems()) {
            srcList.add(new System(r));
        }
        
        systemsPanel.setSourceList(srcList);
    
        ArrayList<System> dstList = new ArrayList<System>();
        for (SystemDTO r : systems.safeGetAssignedSystems()) {
            dstList.add(new System(r));
        }
        
        systemsPanel.setDestinationList(dstList);
    }
    
    
    private static class System implements Item {
        
        private final SystemDTO dto;

        
        public System(SystemDTO dto) {
            this.dto = dto;
        }
        
        public SystemDTO getDto() {
            return dto;
        }
        
        @Override
        public boolean isMovable() {
            return true;
        }
        
        @Override
        public String toString() {
            return dto.getName();
        }

        public ImageResource getImage() {
            return AdminImages.INSTANCE.system();
        }
    }

    
    private static class SystemCell extends ImageTextCell<System> {
        
        public static final SystemCell  INSTANCE = new SystemCell();
        
        private SystemCell() {
        }
        
        @Override
        public void render(Context context, System value, SafeHtmlBuilder sb) {
            setImage(value.getImage());

            super.render(context, value, sb);
        }
    }
    
    
    private class SystemsPanel extends PickList<System> {

        public SystemsPanel() {
            super(SystemCell.INSTANCE);
        }
        
        @Override
        protected void onChangeValues(Collection<System> items, 
                List<System> srcList, List<System> dstList, 
                boolean isAddOperation) {
            
            List<Integer> systems = new ArrayList<Integer>(items.size());
            for (System r : items) {
                systems.add(r.getDto().safeGetId());
            }
            
            TaskManager.INSTANCE.execute(new UpdateSystemsTask(
                    systems, srcList, dstList, isAddOperation));
        }
    }

    
    private class LoadSystemsTask extends RequestTask<UserSystemsResponse> {
        
        public LoadSystemsTask() {
            super("Fetching systems...");
        }
        
        @Override
        protected void runTask() throws Exception {
            SystemService service = GWT.create(SystemService.class);
            RequestService.prepare(service).getUserSystems(currId, this);
        }
        
        @Override
        protected void processFailureResponse(UserSystemsResponse response) {
            systemsPanel.setLists(null, null);
            
            super.processFailureResponse(response);
        }
        
        @Override
        protected void processSuccessResponse(UserSystemsResponse resp) {
            UserSystemsDTO dto = resp.getData();
            refreshSystems(dto);
        }
    }

    
    private class UpdateSystemsTask extends RequestTask<BaseResponse> {
        
        private final IntListDTO    dto;
        private final List<System>  srcList;
        private final List<System>  dstList;
        private final boolean       isAdded;
        
        
        public UpdateSystemsTask(List<Integer> systems, List<System> srcList, 
                List<System> dstList, boolean isAdded) {
            
            super("Updating systems...");
            
            this.dto     = new IntListDTO(systems);
            this.srcList = srcList;
            this.dstList = dstList;
            this.isAdded = isAdded;
        }
        
        @Override
        protected void runTask() throws Exception {
            SystemService service = GWT.create(SystemService.class);
            RequestService.prepare(service).updateUserSystems(currId, isAdded, 
                    dto, this);
        }
        
        @Override
        protected void processSuccessResponse(BaseResponse resp) {
            systemsPanel.setLists(srcList, dstList);
        }
    }

}
