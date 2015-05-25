
package com.googlecode.common.admin.dao;

import java.util.List;
import com.googlecode.common.dao.GenericDao;
import com.googlecode.common.admin.domain.ServerEntity;
import com.googlecode.common.admin.domain.SystemEntity;


/**
 * Server entity dao interface.
 */
public interface ServerDao extends GenericDao<ServerEntity, Long> {

    /**
     * Returns server entity by the given name and system entity.
     *
     * @param name      server's name
     * @param system    system entity, cannot be <code>null</code>
     * @return          return server entity or null if no such found
     */
    public ServerEntity getByName(String name, SystemEntity system);
    
    /**
     * Returns servers list by the given system.
     * 
     * @param system    system entity, cannot be <code>null</code>
     * @return          servers list by the given system
     */
    public List<ServerEntity> getServersBySystem(SystemEntity system);

}
