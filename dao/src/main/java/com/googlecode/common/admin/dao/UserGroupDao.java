
package com.googlecode.common.admin.dao;

import java.util.List;
import com.googlecode.common.dao.GenericDao;
import com.googlecode.common.admin.domain.SystemEntity;
import com.googlecode.common.admin.domain.UserGroup;


/**
 * UserGroup entity dao interface.
 */
public interface UserGroupDao extends GenericDao<UserGroup, Long> {
    
    /**
     * Returns all user groups for the given system.
     * 
     * @param system    system entity
     * @return          all user groups list for the given system
     */
    public List<UserGroup> getAll(SystemEntity system);

    /**
     * Returns user group with the given name and within the given system.
     * 
     * @param system    system entity
     * @param name      group's name
     * @return          user group or <code>null<code> if no such found
     */
    public UserGroup getByName(SystemEntity system, String name);

    /**
     * Returns user groups with the given parent.
     * 
     * @param parent    group's parent, cannot be <code>null<code>
     * @return          user group or <code>null<code> if no such found
     */
    public List<UserGroup> getByParent(UserGroup parent);

}
