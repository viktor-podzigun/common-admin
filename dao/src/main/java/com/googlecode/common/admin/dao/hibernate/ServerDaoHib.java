
package com.googlecode.common.admin.dao.hibernate;

import com.googlecode.common.admin.dao.ServerDao;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.googlecode.common.dao.hibernate.GenericDaoHib;
import com.googlecode.common.admin.domain.ServerEntity;
import com.googlecode.common.admin.domain.SystemEntity;


/**
 * Hibernate implementation for corresponding dao interface.
 */
@Repository("systemServersDao")
public class ServerDaoHib extends GenericDaoHib<ServerEntity, Long> 
        implements ServerDao {
    
    public ServerDaoHib() {
        super(ServerEntity.class);
    }

    @Override
    public ServerEntity getByName(String name, SystemEntity system) {
        return getOne(getEntityManager()
                .createNamedQuery(ServerEntity.GET_BY_NAME, entityClass)
                .setParameter("system", system)
                .setParameter("name", name));
    }
    
    @Override
    public List<ServerEntity> getServersBySystem(SystemEntity system) {
        return getEntityManager()
                .createNamedQuery(ServerEntity.GET_BY_SYSTEM, entityClass)
                .setParameter("system", system)
                .getResultList();
    }
}
