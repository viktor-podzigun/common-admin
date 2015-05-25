
package com.googlecode.common.admin.protocol.role;

import com.googlecode.common.protocol.DataResponse;


/**
 * This response contains {@link RoleDTO} object.
 */
public final class RoleResponse extends DataResponse<RoleDTO> {

    public RoleResponse() {
        
    }
    
    public RoleResponse(RoleDTO data) {
        super(data);
    }
}
