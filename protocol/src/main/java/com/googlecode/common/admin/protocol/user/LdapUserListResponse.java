
package com.googlecode.common.admin.protocol.user;

import java.util.List;
import com.googlecode.common.protocol.DataListResponse;


/**
 * This response contains list of LdapUserDTO objects.
 */
public final class LdapUserListResponse extends DataListResponse<LdapUserDTO>{

    public LdapUserListResponse() {
    }
    
    public LdapUserListResponse(List<LdapUserDTO> dataList) {
        super(dataList);
    }
}
