
package com.googlecode.common.admin.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * Represents user group entity.
 */
@Entity
@Table(name = "users_groups", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "system_id"})
})
@NamedQueries({
    @NamedQuery(name = UserGroup.GET_ALL, 
                query= "SELECT c FROM UserGroup c WHERE c.system=:system ORDER BY c.parent, c.name"),
    @NamedQuery(name = UserGroup.GET_BY_NAME, 
                query= "SELECT c FROM UserGroup c WHERE c.system=:system AND c.name=:name"),
    @NamedQuery(name = UserGroup.GET_BY_PARENT, 
                query= "SELECT c FROM UserGroup c WHERE c.parent=:parent")
})
public class UserGroup implements RolesContainer {
    
    public static final String GET_ALL          = "UserGroup.getAll";
    public static final String GET_BY_NAME      = "UserGroup.getByName";
    public static final String GET_BY_PARENT    = "UserGroup.getByParent";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "name")
    private String name;
    
    @Column(name = "roles")
    private Long roles;

    @Column(name = "inherited_roles")
    private Long inheritedRoles;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User modifiedBy;
    
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    @Column(name = "modified_date")
    private Date modifiedDate;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private SystemEntity system;
    
    @Column(name = "parent_id", insertable = false, updatable = false)
    private Long parentId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="parent_id")
    private UserGroup parent;

    
    public UserGroup() {
    }
    
    public long getId() {
        return id;
    }
    
    /**
     * Identifier is set by JPA provider so this method should not be used
     * directly, to get entity with some id use DAO methods instead.
     * 
     * @param id the id to set
     * 
     * @deprecated You don't want to use it. This method was left for JPA provider.
     */
    public void setId(long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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
    
    public SystemEntity getSystem() {
        return system;
    }
    
    public void setSystem(SystemEntity system) {
        this.system = system;
    }

    public Long getParentId() {
        return parentId;
    }
    
    /**
     * Identifier is set by JPA provider so this method should not be used
     * directly, to get entity with some id use DAO methods instead.
     * 
     * @param id the id to set
     * 
     * @deprecated You don't want to use it. This method was left for JPA provider.
     */
    @Deprecated
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public UserGroup getParent() {
        return parent;
    }
    
    public void setParent(UserGroup parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{id: " + id
                + (name != null ? ", name: " + name : "")
                + (modifiedBy != null ? ", modifiedBy: " + modifiedBy : "")
                + (modifiedDate != null ? ", modifiedDate: " + modifiedDate : "")
                + (system != null ? ", systemName: " + system.getName() : "")
                + (parent != null ? ", parentName: " + parent.getName() : "") 
                + "}";
    }

}
