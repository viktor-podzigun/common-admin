
package com.googlecode.common.admin.protocol.user;

import com.googlecode.common.protocol.DataResponse;


/**
 * This response contains UserDTO object.
 */
public final class UserResponse extends DataResponse<UserDTO> {

    public UserResponse() {
    }
    
    public UserResponse(UserDTO data) {
        super(data);
    }

}
