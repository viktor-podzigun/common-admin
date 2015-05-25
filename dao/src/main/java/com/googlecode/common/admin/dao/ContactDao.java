
package com.googlecode.common.admin.dao;

import com.googlecode.common.dao.GenericDao;
import com.googlecode.common.admin.domain.Contact;


/**
 * Contact entity dao interface.
 */
public interface ContactDao extends GenericDao<Contact, Long> {

    /**
     * Returns contact by email.
     *
     * @param email contact email
     * @return      Contact or null if no such found
     */
    public Contact getByEmail(String email);

}
