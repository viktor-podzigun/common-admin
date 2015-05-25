
package com.googlecode.common.admin.dao;

import com.googlecode.common.dao.GenericDao;
import com.googlecode.common.dao.PageData;
import com.googlecode.common.admin.domain.Company;
import com.googlecode.common.admin.domain.User;


public interface UserDao extends GenericDao<User, Integer> {
    
    /**
     * Returns user by login.
     *
     * @param name          user's login
     * @return              return user or null if no such found.
     */
    public User getByLogin(String login);
    
    /**
     * Returns users for the given company.
     * 
     * @param company       company, or <code>null</code> for all users
     * @param startIndex    index of the first row, pass <code>null</code> 
     *                      to start from 0 and return total rows count
     * @param limit         number of rows to return, used for paging
     * @return              users for the given company
     */
    public PageData<User> getByCompany(Company company, Integer startIndex, 
            int limit);

}
