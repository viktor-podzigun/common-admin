
package com.googlecode.common.admin.protocol.user;

import com.googlecode.common.protocol.DataResponse;


/**
 * This response contains {@link UserSystemsDTO} object.
 */
public final class UserSystemsResponse extends DataResponse<UserSystemsDTO>{

    public UserSystemsResponse() {
    }
    
    public UserSystemsResponse(UserSystemsDTO data) {
        super(data);
    }

}
