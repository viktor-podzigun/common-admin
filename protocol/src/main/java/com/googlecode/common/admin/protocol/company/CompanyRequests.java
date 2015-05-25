
package com.googlecode.common.admin.protocol.company;

import com.googlecode.common.protocol.Requests;


/**
 * Contains company requests' path constants.
 */
public final class CompanyRequests extends Requests {

    public static final String  CREATE_NEW_COMPANY  = "/company";
    public static final String  GET_COMPANIES       = "/company/list";
    public static final String  GET_CONTACT         = "/company/contact/{id}";
    public static final String  RENAME_COMPANY      = "/company/rename";
    
    private CompanyRequests() {
    }

}
