
package com.googlecode.common.admin.dao.hibernate;

import java.util.List;
import javax.persistence.LockModeType;
import org.springframework.stereotype.Repository;
import com.googlecode.common.dao.hibernate.GenericDaoHib;
import com.googlecode.common.admin.dao.RoleDao;
import com.googlecode.common.admin.domain.Role;
import com.googlecode.common.admin.domain.SystemEntity;


/**
 * Hibernate implementation for corresponding dao interface.
 */
@Repository("rolesDao")
public class RoleDaoHib extends GenericDaoHib<Role, Long> implements RoleDao {

    public RoleDaoHib() {
        super(Role.class);
    }

    @Override
    public Role refreshAndLock(Role entity) {
        return super.refresh(entity, LockModeType.PESSIMISTIC_WRITE);
    }
    
    @Override
    public List<Role> getAll(SystemEntity system) {
        return getEntityManager()
                .createNamedQuery(Role.GET_ALL, entityClass)
                .setParameter("system", system)
                .getResultList();
    }

    @Override
    public Role getLast(SystemEntity system) {
        return getOne(getEntityManager()
                .createNamedQuery(Role.GET_LAST, entityClass)
                .setParameter("system", system)
                .setMaxResults(1));
    }

    @Override
    public Role getByTitle(SystemEntity system, String title) {
        return getOne(getEntityManager()
                .createNamedQuery(Role.GET_BY_TITLE, entityClass)
                .setParameter("system", system)
                .setParameter("title", title));
    }
    
}
