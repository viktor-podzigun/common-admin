
package com.googlecode.common.admin.dao;

import java.util.List;
import com.googlecode.common.dao.GenericDao;
import com.googlecode.common.admin.domain.SystemEntity;
import com.googlecode.common.admin.domain.SystemGroup;


/**
 * System entity dao interface.
 */
public interface SystemDao extends GenericDao<SystemEntity, Integer> {

    /**
     * Returns system by the given name.
     *
     * @param name      system's name
     * @return          return system or null if no such found
     */
    public SystemEntity getByName(String name);
    
    /**
     * Returns systems list by the given parent.
     * 
     * @param parent    parent group, can be <code>null</code> for root systems
     * @return          systems list by the given parent
     */
    public List<SystemEntity> getByParent(SystemGroup parent);

}
