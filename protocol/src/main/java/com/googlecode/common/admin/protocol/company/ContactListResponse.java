
package com.googlecode.common.admin.protocol.company;

import java.util.List;
import com.googlecode.common.protocol.DataListResponse;


/**
 * Represents response that hold list of ContactDTO objects.
 */
public final class ContactListResponse extends DataListResponse<ContactDTO> {
    
    public ContactListResponse() {
    }
    
    public ContactListResponse(List<ContactDTO> dataList) {
        super(dataList);
    }
    
}
