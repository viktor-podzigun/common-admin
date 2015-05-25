
package com.googlecode.common.admin.protocol.user;

import java.util.List;
import com.googlecode.common.protocol.DataListResponse;


/**
 * This response contains list of UserGroupDTO objects.
 */
public final class UserGroupListResponse extends DataListResponse<UserGroupDTO> {

    public UserGroupListResponse() {
    }
    
    public UserGroupListResponse(List<UserGroupDTO> dataList) {
        super(dataList);
    }
}
