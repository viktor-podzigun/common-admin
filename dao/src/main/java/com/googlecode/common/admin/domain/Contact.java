
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.Email;


/**
 * Represents company contact entity.
 */
@Entity
@Table(name = "contacts")
@NamedQueries({
    @NamedQuery(name = Contact.GET_BY_EMAIL, 
                query= "SELECT c FROM Contact c WHERE c.email=:email")
})
public class Contact {
    
    public static final String GET_BY_EMAIL = "Contact.getByEmail";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @NotNull
    @Size(min = 2, max = 32)
    private String firstName;

    @Size(min = 2, max = 32)
    private String middleName;
    
    @NotNull
    @Size(min = 2, max = 32)
    private String secondName;
    
    private Long phone;
    
    @NotNull
    @Size(min = 4, max = 255)
    @Column(name = "email", unique = true)
    @Email
    private String email;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Index(name="idx_contacts_company_id") // hibernate specific annotation
    private Company company;

    
    public Contact() {
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
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getMiddleName() {
        return middleName;
    }
    
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    
    public String getSecondName() {
        return secondName;
    }
    
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
    
    public Long getPhone() {
        return phone;
    }
    
    public void setPhone(Long phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Company getCompany() {
        return company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{id: " + id
                + (firstName != null ? ", firstName: " + firstName : "")
                + (middleName != null ? ", middleName: " + middleName : "")
                + (secondName != null ? ", secondName: " + secondName : "")
                + (phone != null ? ", phone: " + phone : "")
                + (email != null ? ", email: " + email : "")
                + "}";
    }

}
