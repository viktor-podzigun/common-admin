
package com.googlecode.common.admin.dao;

import java.util.List;
import com.googlecode.common.dao.GenericDao;
import com.googlecode.common.dao.PageData;
import com.googlecode.common.admin.domain.SystemEntity;
import com.googlecode.common.admin.domain.SystemUser;
import com.googlecode.common.admin.domain.SystemUserPK;
import com.googlecode.common.admin.domain.User;
import com.googlecode.common.admin.domain.UserGroup;


/**
 * SystemUser entity dao interface.
 */
public interface SystemUserDao extends GenericDao<SystemUser, SystemUserPK> {

    /**
     * Returns system users for the given system.
     * 
     * @param system        system
     * @param startIndex    index of the first row, pass <code>null</code> 
     *                      to start from 0 and return total rows count
     * @param limit         number of rows to return, used for paging
     * @return              users for the given system
     */
    public PageData<SystemUser> getBySystem(SystemEntity system, 
            Integer startIndex, int limit);

    /**
     * Returns systems users by the given parent group.
     * 
     * @param parent        users' group
     * @param startIndex    index of the first row, pass <code>null</code> 
     *                      to start from 0 and return total rows count
     * @param limit         number of rows to return, used for paging
     * @return              users by the given parent group
     */
    public PageData<SystemUser> getByParent(UserGroup parent, 
            Integer startIndex, int limit);
    
    /**
     * Returns system users by the given user.
     * 
     * @param user          user entity
     * @return              system users by the given user
     */
    public List<SystemUser> getByUser(User user);

}
