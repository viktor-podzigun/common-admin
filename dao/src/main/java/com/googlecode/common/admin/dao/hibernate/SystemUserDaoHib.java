
package com.googlecode.common.admin.dao.hibernate;

import java.util.List;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import com.googlecode.common.dao.PageData;
import com.googlecode.common.dao.hibernate.GenericDaoHib;
import com.googlecode.common.admin.dao.SystemUserDao;
import com.googlecode.common.admin.domain.SystemEntity;
import com.googlecode.common.admin.domain.SystemUser;
import com.googlecode.common.admin.domain.SystemUserPK;
import com.googlecode.common.admin.domain.User;
import com.googlecode.common.admin.domain.UserGroup;


/**
 * Hibernate implementation for corresponding dao interface.
 */
@Repository("systemsUsersDao")
public class SystemUserDaoHib extends GenericDaoHib<SystemUser, SystemUserPK> 
        implements SystemUserDao {
    
    public SystemUserDaoHib() {
        super(SystemUser.class);
    }
    
    @Override
    public List<SystemUser> getAll() {
        return getEntityManager()
                .createNamedQuery(SystemUser.GET_ALL, entityClass)
                .getResultList();
    }
    
    @Override
    public PageData<SystemUser> getBySystem(SystemEntity system, 
            Integer startIndex, int limit) {
        
        Long totalCount = null;
        if (startIndex == null) {
            totalCount = ((Number)getEntityManager()
                    .createNamedQuery(SystemUser.GET_BY_SYSTEM_COUNT)
                    .setParameter("system", system)
                    .getSingleResult()).longValue();
        }
        
        TypedQuery<SystemUser> query = getEntityManager()
                .createNamedQuery(SystemUser.GET_BY_SYSTEM, entityClass)
                .setParameter("system", system);
    
        query.setFirstResult(startIndex != null ? startIndex.intValue() : 0);
        query.setMaxResults(limit);
        return new PageData<SystemUser>(query.getResultList(), totalCount);
    }
    
    @Override
    public PageData<SystemUser> getByParent(UserGroup parent, 
            Integer startIndex, int limit) {
        
        Long totalCount = null;
        if (startIndex == null) {
            totalCount = ((Number)getEntityManager()
                    .createNamedQuery(SystemUser.GET_BY_PARENT_COUNT)
                    .setParameter("parent", parent)
                    .getSingleResult()).longValue();
        }
        
        TypedQuery<SystemUser> query = getEntityManager()
                .createNamedQuery(SystemUser.GET_BY_PARENT, entityClass)
                .setParameter("parent", parent);
        
        query.setFirstResult(startIndex != null ? startIndex.intValue() : 0);
        query.setMaxResults(limit);
        return new PageData<SystemUser>(query.getResultList(), totalCount);
    }

    @Override
    public List<SystemUser> getByUser(User user) {
        return getEntityManager()
                .createNamedQuery(SystemUser.GET_BY_USER, entityClass)
                .setParameter("user", user)
                .getResultList();
    }

}
