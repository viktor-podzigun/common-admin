
package com.googlecode.common.admin.protocol.profile;

import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.protocol.login.LoginRespDTO;


/**
 * This response contains {@link ProfileDTO} object.
 */
public final class ProfileResponse extends BaseResponse {

    private LoginRespDTO    loginData;
    private ProfileDTO      profile;
    
    
    public ProfileResponse() {
    }
    
    public ProfileResponse(LoginRespDTO loginData, ProfileDTO profile) {
        this.loginData = loginData;
        this.profile = profile;
    }
    
    public LoginRespDTO getLoginData() {
        return loginData;
    }
    
    public void setLoginData(LoginRespDTO loginData) {
        this.loginData = loginData;
    }
    
    public ProfileDTO getProfile() {
        return profile;
    }
    
    public void setProfile(ProfileDTO profile) {
        this.profile = profile;
    }

}
