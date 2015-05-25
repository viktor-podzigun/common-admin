
package com.googlecode.common.admin.dao.hibernate;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.googlecode.common.dao.hibernate.GenericDaoHib;
import com.googlecode.common.admin.dao.UserGroupDao;
import com.googlecode.common.admin.domain.SystemEntity;
import com.googlecode.common.admin.domain.UserGroup;


/**
 * Hibernate implementation for corresponding dao interface.
 */
@Repository("userGroupsDao")
public class UserGroupDaoHib extends GenericDaoHib<UserGroup, Long> 
        implements UserGroupDao {
    
    public UserGroupDaoHib() {
        super(UserGroup.class);
    }

    @Override
    public List<UserGroup> getAll(SystemEntity system) {
        return getEntityManager()
                .createNamedQuery(UserGroup.GET_ALL, entityClass)
                .setParameter("system", system)
                .getResultList();
    }

    @Override
    public UserGroup getByName(SystemEntity system, String name) {
        return getOne(getEntityManager()
                .createNamedQuery(UserGroup.GET_BY_NAME, entityClass)
                .setParameter("system", system)
                .setParameter("name", name));
    }

    @Override
    public List<UserGroup> getByParent(UserGroup parent) {
        return getEntityManager()
                .createNamedQuery(UserGroup.GET_BY_PARENT, entityClass)
                .setParameter("parent", parent)
                .getResultList();
    }
    
}
