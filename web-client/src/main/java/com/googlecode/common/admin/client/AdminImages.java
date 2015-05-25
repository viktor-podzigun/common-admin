
package com.googlecode.common.admin.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;


/**
 * Defines admin client images.
 */
public interface AdminImages extends ClientBundle {

    static final AdminImages INSTANCE = GWT.create(AdminImages.class);
    
    
    @Source("com/googlecode/common/admin/client/wrench.png")
    ImageResource setting();

    @Source("com/googlecode/common/admin/client/group.png")
    ImageResource userGroup();

    @Source("com/googlecode/common/admin/client/role.png")
    ImageResource role();

    @Source("com/googlecode/common/admin/client/role_base.png")
    ImageResource roleBase();

    @Source("com/googlecode/common/admin/client/server.png")
    ImageResource server();

    @Source("com/googlecode/common/admin/client/server_go.png")
    ImageResource serverRestart();

    @Source("com/googlecode/common/admin/client/server_warn.png")
    ImageResource serverWarn();

    @Source("com/googlecode/common/admin/client/monitor.png")
    ImageResource monitor();

    @Source("com/googlecode/common/admin/client/computer.png")
    ImageResource system();

    @Source("com/googlecode/common/admin/client/key_small.png")
    ImageResource keySmall();

    @Source("com/googlecode/common/admin/client/key.png")
    ImageResource key();

    @Source("com/googlecode/common/admin/client/cog.png")
    ImageResource config();

    @Source("com/googlecode/common/admin/client/vcard.png")
    ImageResource contacts();

}
