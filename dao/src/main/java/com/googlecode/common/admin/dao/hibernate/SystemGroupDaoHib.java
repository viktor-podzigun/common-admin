
package com.googlecode.common.admin.dao.hibernate;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.googlecode.common.dao.hibernate.GenericDaoHib;
import com.googlecode.common.admin.dao.SystemGroupDao;
import com.googlecode.common.admin.domain.SystemGroup;


/**
 * Hibernate implementation for corresponding dao interface.
 */
@Repository("systemGroupsDao")
public class SystemGroupDaoHib extends GenericDaoHib<SystemGroup, Long> 
        implements SystemGroupDao {
    
    public SystemGroupDaoHib() {
        super(SystemGroup.class);
    }

    @Override
    public List<SystemGroup> getAll() {
        return getEntityManager()
                .createNamedQuery(SystemGroup.GET_ALL, entityClass)
                .getResultList();
    }

    @Override
    public SystemGroup getByName(String name) {
        return getOne(getEntityManager()
                .createNamedQuery(SystemGroup.GET_BY_NAME, entityClass)
                .setParameter("name", name));
    }

}
