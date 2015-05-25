
package com.googlecode.common.admin.client.ui.server;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.ui.ButtonImages;
import com.googlecode.common.client.ui.TextField;
import com.googlecode.common.client.ui.panel.BaseOkCancelDialog;
import com.googlecode.common.client.util.StringHelpers;
import com.googlecode.common.admin.protocol.server.ServerDTO;


/**
 * Editing server info dialog.
 */
public final class EditServerDialog extends BaseOkCancelDialog {

    private static final Binder BINDER = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, EditServerDialog> {
    }
    
    @UiField TextField  textName;
    @UiField TextField  textUrl;

    
    public EditServerDialog(ServerDTO dto) {
        super(dto == null ? "New Server" : "Edit Server", "Save");
    
        setOkIcon(ButtonImages.INSTANCE.dbSave(), 
                ButtonImages.INSTANCE.dbSaveDisabled());
        
        setContent(BINDER.createAndBindUi(this));
//        infoPanel.setPixelSize(300, 200);
        
        if (dto != null) {
            setServerDTO(dto);
        }
    }

    private void setServerDTO(ServerDTO dto) {
        textName.setText(dto.getName());
        textUrl.setText(dto.getUrl());
    }
    
    public ServerDTO getServerDTO() {
        ServerDTO dto = new ServerDTO();
        dto.setName(textName.getText());
        dto.setUrl(StringHelpers.nullIfEmpty(textUrl.getText()));
        return dto;
    }
    
}
