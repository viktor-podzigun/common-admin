
package com.googlecode.common.admin.protocol.user;

import com.googlecode.common.protocol.DataResponse;


/**
 * This response contains {@link UserRolesDTO} object.
 */
public final class UserRolesResponse extends DataResponse<UserRolesDTO>{

    public UserRolesResponse() {
    }
    
    public UserRolesResponse(UserRolesDTO data) {
        super(data);
    }

}
