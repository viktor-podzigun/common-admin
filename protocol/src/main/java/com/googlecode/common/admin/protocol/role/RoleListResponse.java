
package com.googlecode.common.admin.protocol.role;

import java.util.List;
import com.googlecode.common.protocol.DataListResponse;


/**
 * This response contains list of {@link RoleDTO} objects.
 */
public final class RoleListResponse extends DataListResponse<RoleDTO>{

    public RoleListResponse() {
    }
    
    public RoleListResponse(List<RoleDTO> dataList) {
        super(dataList);
    }
}
