
package com.googlecode.common.admin.dao.hibernate;

import java.util.List;
import javax.persistence.LockModeType;
import org.springframework.stereotype.Repository;
import com.googlecode.common.dao.hibernate.GenericDaoHib;
import com.googlecode.common.admin.dao.PermissionDao;
import com.googlecode.common.admin.domain.PermissionEntity;
import com.googlecode.common.admin.domain.SystemEntity;


/**
 * Hibernate implementation for corresponding dao interface.
 */
@Repository("permissionsDao")
public class PermissionDaoHib extends GenericDaoHib<PermissionEntity, Long> 
        implements PermissionDao {
    
    public PermissionDaoHib() {
        super(PermissionEntity.class);
    }

    @Override
    public PermissionEntity refreshAndLock(PermissionEntity entity) {
        return super.refresh(entity, LockModeType.PESSIMISTIC_WRITE);
    }
    
    @Override
    public List<PermissionEntity> getAll(SystemEntity system) {
        return getEntityManager()
                .createNamedQuery(PermissionEntity.GET_ALL, entityClass)
                .setParameter("system", system)
                .getResultList();
    }
    
    @Override
    public PermissionEntity getNodeByName(SystemEntity system, String name, 
            PermissionEntity parent) {
        
        if (parent == null) {
            return getOne(getEntityManager()
                    .createNamedQuery(PermissionEntity.GET_ROOT_BY_NAME, entityClass)
                    .setParameter("system", system)
                    .setParameter("name", name));
        }

        return getOne(getEntityManager()
                .createNamedQuery(PermissionEntity.GET_BY_NAME, entityClass)
                .setParameter("isNode", true)
                .setParameter("name", name)
                .setParameter("parent", parent));
    }

    @Override
    public PermissionEntity getPermissionByName(String name, 
            PermissionEntity parent) {
        
        if (parent == null) {
            // permission always has parent node
            throw new NullPointerException("parent");
        }
        
        return getOne(getEntityManager()
                .createNamedQuery(PermissionEntity.GET_BY_NAME, entityClass)
                .setParameter("isNode", false)
                .setParameter("name", name)
                .setParameter("parent", parent));
    }
    
    @Override
    public List<PermissionEntity> getPermissions(SystemEntity system, 
            List<Long> idList) {
        
        return getEntityManager()
                .createNamedQuery(PermissionEntity.GET_PERMISSIONS, entityClass)
                .setParameter("system", system)
                .setParameter("idList", idList)
                .getResultList();
    }
}
