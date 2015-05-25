
package com.googlecode.common.admin.signin.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import com.googlecode.common.protocol.login.LoginRedirectResponse;
import com.googlecode.common.admin.protocol.signin.SigninRequests;


/**
 * Signin server requests definition.
 */
public interface SigninService extends RestService {

    @GET
    @Path(SigninRequests.SIGNIN)
    public void signin(
            @QueryParam(SigninRequests.QPARAM_REMEMBER_ME) boolean rm, 
            @QueryParam(SigninRequests.QPARAM_TARGET_URL) String targetUrl, 
            MethodCallback<LoginRedirectResponse> callback);
    
}
