
package com.googlecode.common.admin.dao.hibernate;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.googlecode.common.dao.hibernate.GenericDaoHib;
import com.googlecode.common.admin.dao.SystemDao;
import com.googlecode.common.admin.domain.SystemEntity;
import com.googlecode.common.admin.domain.SystemGroup;


/**
 * Hibernate implementation for corresponding dao interface.
 */
@Repository("systemsDao")
public class SystemDaoHib extends GenericDaoHib<SystemEntity, Integer> 
        implements SystemDao {
    
    public SystemDaoHib() {
        super(SystemEntity.class);
    }

    @Override
    public SystemEntity getByName(String name) {
        return getOne(getEntityManager()
                .createNamedQuery(SystemEntity.GET_BY_NAME, entityClass)
                .setParameter("name", name));
    }
    
    @Override
    public List<SystemEntity> getByParent(SystemGroup parent) {
        if (parent == null) {
            return getEntityManager()
                    .createNamedQuery(SystemEntity.GET_ROOTS, entityClass)
                    .getResultList();
        }
        
        return getEntityManager()
                .createNamedQuery(SystemEntity.GET_BY_PARENT, entityClass)
                .setParameter("parent", parent)
                .getResultList();
    }
}
