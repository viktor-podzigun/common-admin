
package com.googlecode.common.admin.dao.hibernate;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.googlecode.common.dao.hibernate.GenericDaoHib;
import com.googlecode.common.admin.dao.CompanyDao;
import com.googlecode.common.admin.domain.Company;


/**
 * Hibernate implementation for corresponding dao interface.
 */
@Repository("companiesDao")
public class CompanyDaoHib extends GenericDaoHib<Company, Long> implements CompanyDao {
    
    public CompanyDaoHib() {
        super(Company.class);
    }

    @Override
    public Company getByName(String name) {
        return getOne(getEntityManager()
                .createNamedQuery(Company.GET_BY_NAME, entityClass)
                .setParameter("name", name));
    }

    @Override
    public List<Company> getAllCompanies() {
        return getEntityManager()
                .createNamedQuery(Company.GET_ALL, entityClass).getResultList();
    }
}
