
package com.googlecode.common.admin.protocol.system;

import java.util.List;
import com.googlecode.common.protocol.DataListResponse;


/**
 * This response contains list of SystemDTO object.
 */
public final class SystemListResponse extends DataListResponse<SystemDTO> {

    public SystemListResponse() {
    }
    
    public SystemListResponse(List<SystemDTO> dataList) {
        super(dataList);
    }

}
