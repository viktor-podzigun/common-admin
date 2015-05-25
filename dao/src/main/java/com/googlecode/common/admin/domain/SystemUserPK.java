
package com.googlecode.common.admin.domain;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;


/**
 * Primary key for <code>SystemUser</code> entity.
 */
@Embeddable
public class SystemUserPK implements Serializable {

    private static final long serialVersionUID = -6306391322578332615L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private SystemEntity system;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    
    
    public SystemUserPK() {
    }
    
    public SystemUserPK(SystemEntity system, User user) {
        this.system = system;
        this.user   = user;
    }
    
    public SystemEntity getSystem() {
        return system;
    }

    public void setSystem(SystemEntity system) {
        this.system = system;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SystemUserPK that = (SystemUserPK) o;
        if (system != null ? !system.equals(that.system) : that.system != null) {
            return false;
        }
        if (user != null ? !user.equals(that.user) : that.user != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (system != null ? system.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "["
                + (system != null ? "system: " + system.getName() : "")
                + (user != null ? ", user: " + user.getLogin() : "") 
                + "]";
    }

}
