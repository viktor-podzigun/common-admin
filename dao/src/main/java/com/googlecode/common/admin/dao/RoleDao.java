
package com.googlecode.common.admin.dao;

import java.util.List;
import com.googlecode.common.dao.GenericDao;
import com.googlecode.common.admin.domain.Role;
import com.googlecode.common.admin.domain.SystemEntity;


/**
 * Role entity dao interface.
 */
public interface RoleDao extends GenericDao<Role, Long> {

    /**
     * Refreshes the given entity and lock pessimistic.
     * 
     * @param entity    entity to lock
     * @return          refreshed entity
     */
    public Role refreshAndLock(Role entity);
    
    /**
     * Returns all roles for the given system.
     * 
     * @param system    system entity
     * @return          all roles for the given system
     */
    public List<Role> getAll(SystemEntity system);
    
    /**
     * Returns last role in the given system.
     * 
     * @param system    system entity
     * @return          last role in the given system
     */
    public Role getLast(SystemEntity system);
    
    /**
     * Returns role with the given name and within the given system.
     * 
     * @param system    system entity
     * @param title     role's name
     * @return          role with the given name and within the given system
     */
    public Role getByTitle(SystemEntity system, String title);

}
