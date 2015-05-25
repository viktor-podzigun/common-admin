
package com.googlecode.common.admin.protocol.company;

import com.googlecode.common.protocol.DataResponse;


/**
 * This response contains ContactDTO object.
 */
public final class ContactResponse extends DataResponse<ContactDTO> {

    public ContactResponse() {
    }
    
    public ContactResponse(ContactDTO data) {
        super(data);
    }
    
}
