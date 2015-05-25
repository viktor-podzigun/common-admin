
package com.googlecode.common.admin.protocol.user;

import com.googlecode.common.admin.protocol.company.CompanyDTO;
import com.googlecode.common.admin.protocol.company.ContactDTO;


/**
 * Part of server requests, containing new user details.
 */
public final class NewUserDTO {
    
    private String          login;
    private String          password;
    
    private ContactDTO      contact;
    private CompanyDTO      company;

    
    public NewUserDTO() {
    }
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public ContactDTO getContact() {
        return contact;
    }
    
    public void setContact(ContactDTO contact) {
        this.contact = contact;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    public CompanyDTO getCompany() {
        return company;
    }

}
