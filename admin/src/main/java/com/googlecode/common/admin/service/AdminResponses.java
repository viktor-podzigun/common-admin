
package com.googlecode.common.admin.service;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import com.googlecode.common.i18n.MessageControl;
import com.googlecode.common.service.ResponseMessage;
import com.googlecode.i18n.annotations.MessageProvider;


/**
 * Localized Admin response messages.
 */
@MessageProvider
public enum AdminResponses implements ResponseMessage {
    
    //
    // User specific response codes
    //

    /** User not found */
    USER_NOT_FOUND(5001),
    
    /** User already exists */
    USER_ALREADY_EXISTS(5002),

    /** User is not active */
    USER_NOT_ACTIVE(5003),
    
    /** User login is invalid */
    USER_INVALID_LOGIN(5004),
    
    /** User group doesn't exists */
    USER_GROUP_NOT_FOUND(5005),
    
    /** User group already exists */
    USER_GROUP_ALREADY_EXISTS(5006),

    /** User group's name is invalid */
    USER_GROUP_INVALID_NAME(5007),

    /** User group is not empty */
    USER_GROUP_NOT_EMPTY(5008),
    
    /** Password is not valid */
    USER_INVALID_PASSWORD(5009),

    /** Contact id is not valid */
    USER_INVALID_CONTACT_ID(5010),
    
    /** Old password is wrong */
    USER_OLD_PASSWORD_WRONG(5011),

    /** New password is not valid */
    USER_NEW_PASSWORD_INVALID(5012),

    /** LDAP domain is not valid */
    USER_LDAP_DAMAIN_INVALID(5013),

    
    //
    // Contact specific response codes
    //
    
    /** Contact not found */
    CONTACT_NOT_FOUND(5101),

    /** Contact already exists */
    CONTACT_ALREADY_EXISTS(5102),

    /** Contact first name is not valid */
    CONTACT_INVALID_FIRST_NAME(5103),

    /** Contact second name is not valid */
    CONTACT_INVALID_SECOND_NAME(5104),

    /** Contact middle name is not valid */
    CONTACT_INVALID_MIDDLE_NAME(5105),

    /** Contact phone number is not valid */
    CONTACT_INVALID_PHONE(5106),

    /** Contact e-mail is not valid */
    CONTACT_INVALID_EMAIL(5107),

    /** Contact with such email already exists */
    CONTACT_EMAIL_ALREADY_EXISTS(5108),

    // Company specific response codes

    /** Company not found */
    COMPANY_NOT_FOUND(5121),

    /** Company already exists */
    COMPANY_ALREADY_EXISTS(5122),
    
    /** Company not found */
    COMPANY_INVALID_NAME(5123),

    
    //
    // Permission specific response codes
    //

    /** Permission not found */
    PERMISSION_NOT_FOUND(5201),
    
    /** Permission Role not found */
    PERMISSION_ROLE_NOT_FOUND(5202),
    
    /** Permission role is invalid */
    PERMISSION_INVALID_ROLE(5203),

    /** Permission roles maximum count achieved */
    PERMISSION_ROLES_MAX_COUNT_ACHIEVED(5204),


    //
    // Setting specific response codes
    //


    
    //
    // Admin specific response codes
    //

    /** Server name is invalid */
    ADMIN_INVALID_SERVER_NAME(5401),
    
    /** Server URL is invalid */
    ADMIN_INVALID_SERVER_URL(5402),
    

    //
    // System specific response codes
    //

    /** System not found */
    SYSTEM_NOT_FOUND(5501),
    
    /** System already exists */
    SYSTEM_ALREADY_EXISTS(5502),

    /** System name is invalid */
    SYSTEM_INVALID_NAME(5503),

    /** System password is invalid */
    SYSTEM_INVALID_PASSWORD(5504),

    /** System title is invalid */
    SYSTEM_INVALID_TITLE(5505),

    /** System URL is invalid */
    SYSTEM_INVALID_URL(5506),

    /** System group name is invalid */
    SYSTEM_GROUP_INVALID_NAME(5507),

    ;
    
    private final int   status;
    
    
    private AdminResponses(int status) {
        this.status = status;
    }
    
    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage(Locale locale) {
        String key = toString();
        ResourceBundle bundle = ResourceBundle.getBundle(
                AdminResponses.class.getName(), locale, 
                MessageControl.INSTANCE);
        
        try {
            return bundle.getString(key);
        
        } catch (MissingResourceException x) {
            return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
    
}
