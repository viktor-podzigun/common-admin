
package com.googlecode.common.admin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * Represents Company entity.
 */
@Entity
@Table(name = "companies")
@NamedQueries({
    @NamedQuery(name = Company.GET_BY_NAME, 
                query= "SELECT c FROM Company c WHERE c.name=:name"),
    @NamedQuery(name = Company.GET_ALL, 
                query= "SELECT c FROM Company c ORDER BY c.name")
})
public class Company {
    
    public static final String GET_BY_NAME = "Company.getByName";
    public static final String GET_ALL     = "Company.getAll";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "name", unique = true)
    private String name;
    
    
    public Company() {
    }

    public long getId() {
        return id;
    }

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
    public String toString() {
        return getClass().getName() + "{id: " + id 
                + (name != null ? ", name: " + name : "")
                + "}";
    }

}
