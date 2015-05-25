
package com.googlecode.common.admin.protocol.signin;


/**
 * Contains signin requests' path constants.
 */
public final class SigninRequests {

    public static final String  SIGNIN  = "/signin/user";
    
    // Query params definition
    
    public static final String  QPARAM_REMEMBER_ME  = "rm";
    public static final String  QPARAM_TARGET_URL   = "continue";
    
    
    private SigninRequests() {
    }

}
