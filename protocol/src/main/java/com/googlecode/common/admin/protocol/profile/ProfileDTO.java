
package com.googlecode.common.admin.protocol.profile;

import com.googlecode.common.admin.protocol.company.ContactDTO;


/**
 * Part of server requests, containing user profile info.
 */
public final class ProfileDTO {
    
    private String      oldPass;
    private String      newPass;
    
    private ContactDTO  contact;

    
    public ProfileDTO() {
    }
    
    public String getOldPass() {
        return oldPass;
    }
    
    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }
    
    public String getNewPass() {
        return newPass;
    }
    
    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public ContactDTO getContact() {
        return contact;
    }
    
    public void setContact(ContactDTO contact) {
        this.contact = contact;
    }

}
