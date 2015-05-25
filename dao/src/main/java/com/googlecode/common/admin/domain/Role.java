
package com.googlecode.common.admin.domain;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
 * Represents Role entity.
 */
@Entity
@Table(name = "roles", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"bit_index", "system_id"})
})
@NamedQueries({
    @NamedQuery(name = Role.GET_ALL, 
                query= "SELECT c FROM Role c WHERE c.system=:system ORDER BY c.title"),
    @NamedQuery(name = Role.GET_LAST, 
                query= "SELECT c FROM Role c WHERE c.system=:system ORDER BY c.bitIndex DESC"),
    @NamedQuery(name = Role.GET_BY_TITLE, 
                query= "SELECT c FROM Role c WHERE c.system=:system AND c.title=:title")
})
public class Role {
    
    public static final String GET_ALL      = "Role.getAll";
    public static final String GET_LAST     = "Role.getLast";
    public static final String GET_BY_TITLE = "Role.getByTitle";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @NotNull
    @Size(min = 2, max = 64)
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "bit_index")
    private int bitIndex;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Index(name="idx_roles_system_id") // hibernate specific annotation
    private SystemEntity system;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "roles_permissions",
               joinColumns = @JoinColumn(name = "role_id"),
               inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<PermissionEntity> permissions;
    
    
    public Role() {
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
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public int getBitIndex() {
        return bitIndex;
    }
    
    public void setBitIndex(int bitIndex) {
        this.bitIndex = bitIndex;
    }

    public SystemEntity getSystem() {
        return system;
    }

    public void setSystem(SystemEntity system) {
        this.system = system;
    }
    
    public Set<PermissionEntity> getPermissions() {
        return permissions;
    }
    
    public void setPermissions(Set<PermissionEntity> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{id: " + id
                + ", title: " + title
                + ", bitIndex: " + bitIndex
                + (system != null ? ", system: " + system.getName() : "")
                + (permissions != null ? ", permissionsCount: " + permissions.size() : "")
                + "}";
    }

}
