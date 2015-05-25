
package com.googlecode.common.admin.client.ui.system;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.ui.PasswordField;
import com.googlecode.common.client.ui.TextField;
import com.googlecode.common.client.util.StringHelpers;
import com.googlecode.common.admin.protocol.system.SystemDTO;


/**
 * System info panel.
 */
public final class SystemPanel extends Composite {

    private static final Binder BINDER = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, SystemPanel> {
    }
    
    @UiField TextField      textName;
    @UiField PasswordField  textPassword;
    @UiField TextField      textTitle;
    @UiField TextField      textUrl;
    
    private SystemDTO       currDto;

    
    public SystemPanel(boolean readOnly) {
        initWidget(BINDER.createAndBindUi(this));
        
        if (readOnly) {
            textName.setReadOnly(readOnly);
            textPassword.setText("****");
            textPassword.setReadOnly(readOnly);
            textTitle.setReadOnly(readOnly);
            textUrl.setReadOnly(readOnly);
        }
    }

    public void setSystemDTO(SystemDTO dto) {
        this.currDto = dto;
        
        textName.setReadOnly(true);
        textName.setText(dto.getName());
        textTitle.setText(dto.getTitle());
        textUrl.setText(dto.getUrl());
    }
    
    public SystemDTO getSystemDTO() {
        SystemDTO dto = new SystemDTO();
        dto.setName(textName.getText());
        dto.setPassword(StringHelpers.nullIfEmpty(textPassword.getText()));
        dto.setTitle(StringHelpers.nullIfEmpty(textTitle.getText()));
        dto.setUrl(StringHelpers.nullIfEmpty(textUrl.getText()));
        
        if (currDto != null) {
            dto.setId(currDto.safeGetId());
        }
        
        return dto;
    }
    
}
