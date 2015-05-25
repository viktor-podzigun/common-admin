
package com.googlecode.common.admin.domain;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * Represents system group entity.
 */
@Entity
@Table(name = "systems_groups")
@NamedQueries({
    @NamedQuery(name  = SystemGroup.GET_ALL, 
                query = "SELECT c FROM SystemGroup c ORDER BY c.name"),
    @NamedQuery(name  = SystemGroup.GET_BY_NAME, 
                query = "SELECT c FROM SystemGroup c WHERE c.name=:name")
})
public class SystemGroup {
    
    public static final String GET_ALL      = "SystemGroup.getAll";
    public static final String GET_BY_NAME  = "SystemGroup.getByName";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "name", unique = true)
    private String name;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User modifiedBy;
    
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    @Column(name = "modified_date")
    private Date modifiedDate;
    
    
    public SystemGroup() {
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
     * @deprecated  You don't want to use it. This method was left for 
     *              JPA provider.
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
    
}
