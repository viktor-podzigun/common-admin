
package com.googlecode.common.admin.client.ui.system;

import com.googlecode.common.client.ui.ButtonImages;
import com.googlecode.common.client.ui.panel.BaseOkCancelDialog;
import com.googlecode.common.admin.protocol.system.SystemDTO;


/**
 * Editing system info dialog.
 */
public final class EditSystemDialog extends BaseOkCancelDialog {

    private final SystemPanel   infoPanel;
    
    
    public EditSystemDialog(SystemDTO dto) {
        super(dto == null ? "New Application" : "Edit Application", "Save");
    
        setOkIcon(ButtonImages.INSTANCE.dbSave(), 
                ButtonImages.INSTANCE.dbSaveDisabled());
        
        infoPanel = new SystemPanel(false);
        setContent(infoPanel);
        //infoPanel.setPixelSize(300, 200);
    
        if (dto != null) {
            infoPanel.setSystemDTO(dto);
        }
    }
    
    public SystemDTO getSystemDTO() {
        return infoPanel.getSystemDTO();
    }
    
}
