
package com.googlecode.common.admin.dao;

import java.util.List;
import com.googlecode.common.dao.GenericDao;
import com.googlecode.common.admin.domain.SystemGroup;


/**
 * {@link SystemGroup} entity dao interface.
 */
public interface SystemGroupDao extends GenericDao<SystemGroup, Long> {
    
    /**
     * Returns all system groups list.
     * @return list of {@link SystemGroup} objects
     */
    public List<SystemGroup> getAll();

    /**
     * Returns {@link SystemGroup} with the given name.
     * 
     * @param name  group's name
     * @return      {@link SystemGroup} or <code>null<code> if no such found
     */
    public SystemGroup getByName(String name);

}
