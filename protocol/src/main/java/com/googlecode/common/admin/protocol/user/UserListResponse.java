
package com.googlecode.common.admin.protocol.user;

import java.util.List;
import com.googlecode.common.protocol.DataListResponse;


/**
 * Represents response that hold list of UserDTO objects.
 */
public final class UserListResponse extends DataListResponse<UserDTO>{

    
    public UserListResponse() {
    }
    
    public UserListResponse(List<UserDTO> dataList) {
        super(dataList);
    }
}
