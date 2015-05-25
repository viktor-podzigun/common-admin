
package com.googlecode.common.admin.protocol.company;

import com.googlecode.common.protocol.DataResponse;


/**
 * This response contains CompanyDTO object.
 */
public final class CompanyResponse extends DataResponse<CompanyDTO> {

    public CompanyResponse() {
    }
    
    public CompanyResponse(CompanyDTO data) {
        super(data);
    }
}
