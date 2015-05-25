
package com.googlecode.common.admin.web.controller;

import com.googlecode.common.admin.service.CompanyService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.protocol.RenameDTO;
import com.googlecode.common.admin.domain.UserToken;
import com.googlecode.common.admin.protocol.company.CompanyDTO;
import com.googlecode.common.admin.protocol.company.CompanyListResponse;
import com.googlecode.common.admin.protocol.company.CompanyRequests;
import com.googlecode.common.admin.protocol.company.CompanyResponse;
import com.googlecode.common.admin.protocol.company.ContactResponse;
import com.googlecode.common.admin.protocol.user.UserPerm;


/**
 * Controller for contacts requests.
 */
@Controller
public class CompanyController extends BaseController {

    @Autowired
    private CompanyService companyService;

    
    @RequestMapping(value   = CompanyRequests.GET_CONTACT,
                    method  = RequestMethod.GET)
    public @ResponseBody ContactResponse getContacts(
            HttpServletRequest req,
            @PathVariable(CompanyRequests.PARAM_ID) long contactId) {
        
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, UserPerm.READ);
        
        return new ContactResponse(companyService.getContactDTO(contactId));
    }
    
    @RequestMapping(value   = CompanyRequests.GET_COMPANIES,
                    method  = RequestMethod.GET)
    public @ResponseBody CompanyListResponse getCompanies(
            HttpServletRequest req) {
        
        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, UserPerm.READ);
        
        return new CompanyListResponse(companyService.getAllCompanies());
    }

    @RequestMapping(value   = CompanyRequests.CREATE_NEW_COMPANY,
                    method  = RequestMethod.POST)
    public @ResponseBody CompanyResponse createCompany(
            HttpServletRequest req,
            @RequestBody CompanyDTO reqData) {

        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, UserPerm.CREATE);

        return new CompanyResponse(companyService.createCompany(reqData));
    }

    @RequestMapping(value   = CompanyRequests.RENAME_COMPANY,
                    method  = RequestMethod.PUT)
    public @ResponseBody BaseResponse renameCompany(
            HttpServletRequest req,
            @RequestBody RenameDTO reqData) {

        // check permissions
        UserToken userToken = getAuthenticateUserToken(req);
        checkUserPermission(userToken, UserPerm.UPDATE);

        companyService.renameCompany(reqData);
        
        return BaseResponse.OK;
    }
}
