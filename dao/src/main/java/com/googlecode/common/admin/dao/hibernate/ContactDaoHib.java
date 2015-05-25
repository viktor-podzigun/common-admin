
package com.googlecode.common.admin.dao.hibernate;

import com.googlecode.common.admin.dao.ContactDao;
import org.springframework.stereotype.Repository;
import com.googlecode.common.dao.hibernate.GenericDaoHib;
import com.googlecode.common.admin.domain.Contact;


/**
 * Hibernate implementation for corresponding dao interface.
 */
@Repository("contactsDao")
public class ContactDaoHib extends GenericDaoHib<Contact, Long> 
        implements ContactDao {
    
    public ContactDaoHib() {
        super(Contact.class);
    }

    @Override
    public Contact getByEmail(String email) {
        return getOne(getEntityManager()
                .createNamedQuery(Contact.GET_BY_EMAIL, entityClass)
                .setParameter("email", email));
    }
    
}
