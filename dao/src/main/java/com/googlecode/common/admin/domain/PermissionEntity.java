
package com.googlecode.common.admin.domain;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Index;


/**
 * Represents permission entity.
 */
@Entity
@Table(name = "permissions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"is_node", "name", "parent_id"})
})
@NamedQueries({
    @NamedQuery(name = PermissionEntity.GET_ALL, 
                query= "SELECT c FROM PermissionEntity c WHERE c.system=:system ORDER BY c.parent, c.isNode DESC, c.title"),
    @NamedQuery(name = PermissionEntity.GET_ROOT_BY_NAME, 
                query= "SELECT c FROM PermissionEntity c WHERE c.system=:system AND c.isNode=true AND c.parent IS NULL AND c.name=:name"),
    @NamedQuery(name = PermissionEntity.GET_BY_NAME, 
                query= "SELECT c FROM PermissionEntity c WHERE c.isNode=:isNode AND c.parent=:parent AND c.name=:name"),
    @NamedQuery(name = PermissionEntity.GET_PERMISSIONS, 
                query= "SELECT c FROM PermissionEntity c WHERE c.system=:system AND c.isNode=false AND c.id IN (:idList)")
})
public class PermissionEntity {
    
    public static final String GET_ALL          = "PermissionEntity.getAll";
    public static final String GET_ROOT_BY_NAME = "PermissionEntity.getRootByName";
    public static final String GET_BY_NAME      = "PermissionEntity.getByName";
    public static final String GET_PERMISSIONS  = "PermissionEntity.getPermissions";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    
    @NotNull
    @Column(name = "is_node", updatable = false)
    private boolean isNode;
    
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "name")
    @Index(name="idx_permissions_name") // hibernate specific annotation
    private String name;
    
    @Size(min = 1, max = 256)
    @Column(name = "title")
    private String title;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Index(name="idx_permissions_system_id") // hibernate specific annotation
    private SystemEntity system;
    
    @Column(name = "parent_id", insertable = false, updatable = false)
    private Long parentId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private PermissionEntity parent;
    
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    private Set<Role> roles;
    
    
    public PermissionEntity() {
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
    @Deprecated
    public void setId(long id) {
        this.id = id;
    }
    
    public boolean isNode() {
        return isNode;
    }
    
    public void setNode(boolean isNode) {
        this.isNode = isNode;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
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

    public PermissionEntity getParent() {
        return parent;
    }
    
    public void setParent(PermissionEntity parent) {
        this.parent = parent;
    }
    
    public Set<Role> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{id: " + id 
                + ", isNode: " + isNode
                + (name != null ? ", name: " + name : "")
                + (title != null ? ", title: " + title : "")
                + (system != null ? ", system: " + system.getName() : "")
                + (parent != null ? ", parentId: " + parent.getId() : "")
                + (roles != null ? ", rolesCount: " + roles.size() : "")
                + "}";
    }

}
