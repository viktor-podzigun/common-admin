
package com.googlecode.common.admin.protocol.user;

import com.googlecode.common.protocol.DataResponse;


/**
 * This response contains UserGroupDTO object.
 */
public final class UserGroupResponse extends DataResponse<UserGroupDTO>{

    public UserGroupResponse() {
    }
    
    public UserGroupResponse(UserGroupDTO data) {
        super(data);
    }

}
