
package com.googlecode.common.admin.dao.hibernate;

import org.springframework.stereotype.Repository;
import com.googlecode.common.dao.hibernate.GenericDaoHib;
import com.googlecode.common.admin.dao.UserTokenDao;
import com.googlecode.common.admin.domain.UserToken;


@Repository("usersTokensDao")
public class UserTokenDaoHib extends GenericDaoHib<UserToken, String> 
        implements UserTokenDao {

    public UserTokenDaoHib() {
        super(UserToken.class);
    }

}
