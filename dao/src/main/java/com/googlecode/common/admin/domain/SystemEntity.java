
package com.googlecode.common.admin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * Represents system entity.
 */
@Entity
@Table(name = "systems")
@NamedQueries({
    @NamedQuery(name  = SystemEntity.GET_ROOTS, 
                query = "SELECT c FROM SystemEntity c WHERE c.parent IS NULL ORDER BY c.name"),
    @NamedQuery(name  = SystemEntity.GET_BY_PARENT, 
                query = "SELECT c FROM SystemEntity c WHERE c.parent=:parent ORDER BY c.name"),
    @NamedQuery(name  = SystemEntity.GET_BY_NAME, 
                query = "SELECT c FROM SystemEntity c WHERE c.name=:name")
})
public class SystemEntity {
    
    public static final String GET_ROOTS        = "SystemEntity.getRoots";
    public static final String GET_BY_PARENT    = "SystemEntity.getByParent";
    public static final String GET_BY_NAME      = "SystemEntity.getByName";

    @Id
    @SequenceGenerator(name = "systems_id_gen", sequenceName = "systems_id_seq")
    @GeneratedValue(generator = "systems_id_gen")
    @Column(name = "id")
    private int id;

    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "name", unique = true, updatable = false)
    private String name;
    
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "password")
    private String password;
    
    @Size(min = 1, max = 32)
    @Column(name = "title")
    private String title;
    
    @Size(min = 1, max = 128)
    @Column(name = "url")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private SystemGroup parent;
    
    
    public SystemEntity() {
    }
    
    public int getId() {
        return id;
    }
    
    /**
     * Identifier is set by JPA provider so this method should not be used
     * directly, to get entity with some id use DAO methods instead.
     * 
     * @param id the id to set
     * 
     * @deprecated  You don't want to use it. This method was left for 
     *              JPA provider.
     */
    @Deprecated
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public SystemGroup getParent() {
        return parent;
    }
    
    public void setParent(SystemGroup parent) {
        this.parent = parent;
    }
    
}
