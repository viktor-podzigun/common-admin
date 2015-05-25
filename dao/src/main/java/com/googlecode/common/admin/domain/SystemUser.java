
package com.googlecode.common.admin.domain;

import java.util.Date;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * Represents systems to users relationship entity.
 *
 * Got from here:
 * http://sieze.wordpress.com/2009/09/04/mapping-a-many-to-many-join-table-with-extra-column-using-jpa/
 */
@Entity
@Table(name = "systems_users")
@AssociationOverrides({
    @AssociationOverride(name = "pk.system",joinColumns = @JoinColumn(name = "system_id")),
    @AssociationOverride(name = "pk.user",  joinColumns = @JoinColumn(name = "user_id"))
})
@NamedQueries({
    @NamedQuery(name  = SystemUser.GET_ALL, 
                query = "SELECT c FROM SystemUser c ORDER BY c.pk.user.login"),
    @NamedQuery(name  = SystemUser.GET_BY_SYSTEM, 
                query = "SELECT c FROM SystemUser c WHERE c.pk.system=:system ORDER BY c.pk.user.login"),
    @NamedQuery(name  = SystemUser.GET_BY_SYSTEM_COUNT, 
                query = "SELECT count(c) FROM SystemUser c WHERE c.pk.system=:system"),
    @NamedQuery(name  = SystemUser.GET_BY_PARENT, 
                query = "SELECT c FROM SystemUser c WHERE c.parent=:parent ORDER BY c.pk.user.login"),
    @NamedQuery(name  = SystemUser.GET_BY_PARENT_COUNT, 
                query = "SELECT count(c) FROM SystemUser c WHERE c.parent=:parent"),
    @NamedQuery(name  = SystemUser.GET_BY_USER, 
                query = "SELECT c FROM SystemUser c WHERE c.pk.user=:user")
})
public class SystemUser implements RolesContainer {
    
    public static final String GET_ALL              = "SystemUser.getAll";
    public static final String GET_BY_SYSTEM        = "SystemUser.getBySystem";
    public static final String GET_BY_SYSTEM_COUNT  = "SystemUser.getBySystemCount";
    public static final String GET_BY_PARENT        = "SystemUser.getByParent";
    public static final String GET_BY_PARENT_COUNT  = "SystemUser.getByParentCount";
    public static final String GET_BY_USER          = "SystemUser.getByUser";

    @EmbeddedId
    private SystemUserPK pk;

    @Column(name = "roles")
    private Long roles;

    @Column(name = "inherited_roles")
    private Long inheritedRoles;

    @ManyToOne(fetch = FetchType.LAZY)
    private User modifiedBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    @Column(name = "modified_date")
    private Date modifiedDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private UserGroup parent;
    
    
    public SystemUser() {
    }
    
    public SystemUser(SystemEntity system, User user) {
        this.pk = new SystemUserPK(system, user);
    }
    
    public SystemUserPK getPk() {
        return pk;
    }
    
    public void setPk(SystemUserPK pk) {
        this.pk = pk;
    }
    
    @Override
    public Long getRoles() {
        return roles;
    }

    @Override
    public void setRoles(Long roles) {
        this.roles = roles;
    }
    
    @Override
    public Long getInheritedRoles() {
        return inheritedRoles;
    }
    
    @Override
    public void setInheritedRoles(Long inheritedRoles) {
        this.inheritedRoles = inheritedRoles;
    }
    
    public User getModifiedBy() {
        return modifiedBy;
    }
    
    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    
    public Date getModifiedDate() {
        return modifiedDate;
    }
    
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public UserGroup getParent() {
        return parent;
    }
    
    public void setParent(UserGroup parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SystemUser that = (SystemUser)o;
        if (getPk() != null ? !getPk().equals(that.getPk()) 
                : that.getPk() != null) {
            
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }

    @Override
    public String toString() {
        return getClass().getName() + "{pk: " + pk
                + (parent != null ? ", parent: " + parent : "")
                + "}";
    }

}
