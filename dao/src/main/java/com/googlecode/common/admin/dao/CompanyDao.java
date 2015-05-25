
package com.googlecode.common.admin.dao;

import java.util.List;
import com.googlecode.common.dao.GenericDao;
import com.googlecode.common.admin.domain.Company;


/**
 * Company entity dao interface.
 */
public interface CompanyDao extends GenericDao<Company, Long> {

    /**
     * Returns Company by name.
     *
     * @param name  company's name
     * @return      Company or <tt>null</tt> if no such found
     */
    public Company getByName(String name);

    public List<Company> getAllCompanies();
}
