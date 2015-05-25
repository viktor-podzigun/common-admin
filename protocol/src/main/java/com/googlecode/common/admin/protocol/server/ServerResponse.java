
package com.googlecode.common.admin.protocol.server;

import com.googlecode.common.protocol.DataResponse;


/**
 * This response contains {@link ServerDTO} object.
 */
public final class ServerResponse extends DataResponse<ServerDTO> {

    public ServerResponse() {
    }
    
    public ServerResponse(ServerDTO data) {
        super(data);
    }
    
}
