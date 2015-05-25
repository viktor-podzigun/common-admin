
package com.googlecode.common.admin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * Represents system's server entity.
 */
@Entity
@Table(name = "systems_servers", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "system_id"})
})
@NamedQueries({
    @NamedQuery(name = ServerEntity.GET_BY_SYSTEM, 
                query= "SELECT c FROM ServerEntity c WHERE c.system=:system ORDER BY c.name"),
    @NamedQuery(name = ServerEntity.GET_BY_NAME, 
                query= "SELECT c FROM ServerEntity c WHERE c.system=:system AND c.name=:name")
})
public class ServerEntity {

    public static final String GET_BY_SYSTEM    = "ServerEntity.getBySystem";
    public static final String GET_BY_NAME      = "ServerEntity.getByName";
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "name")
    private String name;
    
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "url")
    private String url;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private SystemEntity system;

    
    public ServerEntity() {
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
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public SystemEntity getSystem() {
        return system;
    }
    
    public void setSystem(SystemEntity system) {
        this.system = system;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{id: " + id
                + (name != null ? ", name: " + name : "")
                + (url != null ? ", url: " + url : "")
                + (system != null ? ", systemName: " + system.getName() : "") 
                + "}";
    }

}
