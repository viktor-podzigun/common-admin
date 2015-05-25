
package com.googlecode.common.admin.protocol.company;

import java.util.List;
import com.googlecode.common.protocol.DataListResponse;


/**
 * This response contains list of CompanyDTO objects.
 */
public final class CompanyListResponse extends DataListResponse<CompanyDTO> {

    public CompanyListResponse() {
    }
    
    public CompanyListResponse(List<CompanyDTO> dataList) {
        super(dataList);
    }
}
