
package com.googlecode.common.admin.profile.client;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.admin.protocol.profile.ProfileDTO;
import com.googlecode.common.admin.protocol.profile.ProfileRequests;
import com.googlecode.common.admin.protocol.profile.ProfileResponse;


/**
 * Profile server requests definition.
 */
public interface ProfileService extends RestService {

    @GET
    @Path(ProfileRequests.READ)
    public void read(MethodCallback<ProfileResponse> callback);
    
    @PUT
    @Path(ProfileRequests.UPDATE)
    public void update(ProfileDTO dto, MethodCallback<BaseResponse> callback);
    
}
