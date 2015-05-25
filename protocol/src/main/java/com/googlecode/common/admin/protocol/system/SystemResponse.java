
package com.googlecode.common.admin.protocol.system;

import com.googlecode.common.protocol.DataResponse;


/**
 * This response contains SystemDTO object.
 */
public final class SystemResponse extends DataResponse<SystemDTO> {

    public SystemResponse() {
    }
    
    public SystemResponse(SystemDTO data) {
        super(data);
    }

}
