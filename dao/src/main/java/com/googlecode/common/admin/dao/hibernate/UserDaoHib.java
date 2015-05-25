
package com.googlecode.common.admin.dao.hibernate;

import com.googlecode.common.admin.dao.UserDao;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import com.googlecode.common.dao.PageData;
import com.googlecode.common.dao.hibernate.GenericDaoHib;
import com.googlecode.common.admin.domain.Company;
import com.googlecode.common.admin.domain.User;


@Repository("usersDao")
public class UserDaoHib extends GenericDaoHib<User, Integer> implements UserDao {
	
	public UserDaoHib() {
		super(User.class);
	}

	@Override
	public User getByLogin(String login) {
		return getOne(getEntityManager()
		        .createNamedQuery(User.GET_BY_LOGIN, entityClass)
		        .setParameter("login", login));
	}
	
	@Override
	public PageData<User> getByCompany(Company company, Integer startIndex, 
	        int limit) {
	    
	    Long totalCount = null;
	    TypedQuery<User> query;
	    
	    if (company == null) {
	        if (startIndex == null) {
	            totalCount = ((Number)getEntityManager()
	                    .createNamedQuery(User.GET_ALL_COUNT)
	                    .getSingleResult()).longValue();
	        }
	        
	        query = getEntityManager()
                    .createNamedQuery(User.GET_ALL, entityClass);
	        
	    } else {
            if (startIndex == null) {
                totalCount = ((Number)getEntityManager()
                        .createNamedQuery(User.GET_BY_COMPANY_COUNT)
                        .setParameter("company", company)
                        .getSingleResult()).longValue();
            }
            
            query = getEntityManager()
                    .createNamedQuery(User.GET_BY_COMPANY, entityClass)
                    .setParameter("company", company);
	    }
	
        query.setFirstResult(startIndex != null ? startIndex.intValue() : 0);
        query.setMaxResults(limit);
        return new PageData<User>(query.getResultList(), totalCount);
	}
	
}
