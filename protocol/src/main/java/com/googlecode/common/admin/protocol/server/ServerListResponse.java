
package com.googlecode.common.admin.protocol.server;

import java.util.List;
import com.googlecode.common.protocol.DataListResponse;


/**
 * Represents response that hold list of ServerDTO objects.
 */
public final class ServerListResponse extends DataListResponse<ServerDTO> {
    
    public ServerListResponse() {
    }
    
    public ServerListResponse(List<ServerDTO> dataList) {
        super(dataList);
    }
    
}
