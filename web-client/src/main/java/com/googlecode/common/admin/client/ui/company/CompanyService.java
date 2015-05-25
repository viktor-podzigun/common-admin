
package com.googlecode.common.admin.client.ui.company;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.protocol.RenameDTO;
import com.googlecode.common.admin.protocol.company.CompanyDTO;
import com.googlecode.common.admin.protocol.company.CompanyListResponse;
import com.googlecode.common.admin.protocol.company.CompanyRequests;
import com.googlecode.common.admin.protocol.company.CompanyResponse;
import com.googlecode.common.admin.protocol.company.ContactResponse;


/**
 * Company service declaration.
 */
public interface CompanyService extends RestService {

    @GET
    @Path(CompanyRequests.GET_CONTACT)
    public void getContact(@PathParam(CompanyRequests.PARAM_ID) long contactId, 
            MethodCallback<ContactResponse> callback);
    
    @GET
    @Path(CompanyRequests.GET_COMPANIES)
    public void getCompanies(MethodCallback<CompanyListResponse> callback);

    @POST
    @Path(CompanyRequests.CREATE_NEW_COMPANY)
    public void createCompany(CompanyDTO reqData,
            MethodCallback<CompanyResponse> callback);
    
    @PUT
    @Path(CompanyRequests.RENAME_COMPANY)
    public void renameCompany(RenameDTO reqData,
            MethodCallback<BaseResponse> callback);
}
