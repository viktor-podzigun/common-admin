
package com.googlecode.common.admin.protocol.system;

import java.util.List;
import com.googlecode.common.protocol.DataListResponse;


/**
 * This response contains list of {@link SystemGroupDTO} objects.
 */
public final class SystemGroupListResponse extends 
        DataListResponse<SystemGroupDTO> {

    public SystemGroupListResponse() {
    }
    
    public SystemGroupListResponse(List<SystemGroupDTO> dataList) {
        super(dataList);
    }

}
