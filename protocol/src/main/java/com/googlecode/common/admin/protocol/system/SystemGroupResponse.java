
package com.googlecode.common.admin.protocol.system;

import com.googlecode.common.protocol.DataResponse;


/**
 * This response contains {@link SystemGroupDTO} object.
 */
public final class SystemGroupResponse extends DataResponse<SystemGroupDTO> {

    public SystemGroupResponse() {
    }
    
    public SystemGroupResponse(SystemGroupDTO data) {
        super(data);
    }

}
