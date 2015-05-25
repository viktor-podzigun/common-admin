
package com.googlecode.common.admin.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * Represents user entity.
 */
@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name  = User.GET_ALL, 
                query = "SELECT c FROM User c ORDER BY c.login"),
    @NamedQuery(name  = User.GET_ALL_COUNT, 
                query = "SELECT count(c) FROM User c"),
    @NamedQuery(name  = User.GET_BY_LOGIN, 
                query = "SELECT c FROM User c WHERE c.login=:login"),
	@NamedQuery(name  = User.GET_BY_COMPANY, 
	            query = "SELECT c FROM User c WHERE c.company=:company ORDER BY c.login"),
    @NamedQuery(name  = User.GET_BY_COMPANY_COUNT, 
                query = "SELECT count(c) FROM User c WHERE c.company=:company")
})
public class User {
    
    public static final String GET_ALL              = "User.getAll";
    public static final String GET_ALL_COUNT        = "User.getAllCount";
    public static final String GET_BY_LOGIN         = "User.getByLogin";
    public static final String GET_BY_COMPANY       = "User.getByCompany";
    public static final String GET_BY_COMPANY_COUNT = "User.getByCompanyCount";

    @Id
    @SequenceGenerator(name = "users_id_gen", sequenceName = "users_id_seq")
    @GeneratedValue(generator = "users_id_gen")
    @Column(name = "id")
	private int id;

	@NotNull
	@Size(min = 2, max = 64)
	@Column(name = "login", unique = true)
	private String login;
	
	@Size(max = 40)
	private String passHash;

	@NotNull
    @OneToOne(fetch = FetchType.LAZY)
    private Contact contact;
    
    @NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Company company;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "S-")
	@Column(name = "created", updatable = false)
	private Date created;
	
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    @Column(name = "last_login_date")
    private Date lastLoginDate;
    
	@NotNull
	@Column(name = "active")
	private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    private User modifiedBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    @Column(name = "modified_date")
    private Date modifiedDate;
    
    @Size(min = 4, max = 64)
    @Column(name = "ldap_domain")
    private String ldapDomain;

	
	public User() {
	}
	
	/**
	 * Creates new user with specified params and inits it's creation date 
	 * and set's it active.
	 * 
	 * @param login    user's login
	 * @param passHash user's password
	 */
	public User(String login, String passHash) {
		this.login    = login;
		this.passHash = passHash;
		this.created  = new Date();
		this.active   = true;
	}
	
	/**
	 * Returns user identifier.
	 * @return the mask unique identifier of the user.
	 */
	public int getId() {
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
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Returns user's login.
	 * @return user's login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Sets user's login.
	 * 
	 * @param name user's login
	 */
	public void setLogin(String login) {
		this.login = login;
	}

    public Contact getContact() {
        return contact;
    }
    
    public void setContact(Contact contact) {
        this.contact = contact;
    }
    
    public Company getCompany() {
        return company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }

    /**
	 * Returns hash of users password.
	 * @return the passHash hash of users password.
	 */
	public String getPassHash() {
		return passHash;
	}

	/**
	 * Sets double SHA-1 of user password.
	 * One SHA-1 is calculated when user enters password on the 
	 * client side, second is for storing on the server calculated from
	 * previous hash. It helps to avoid vocabularies and database obtaining
	 * vulnerabilities.
	 * 
	 * @param passHash the password hash
	 */
	public void setPassHash(String passHash) {
		this.passHash = passHash;
	}
	
	/**
	 * Returns user creation timestamp.
	 * @return the creation timestamp.
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * Sets user creation timestamp.
	 * NOTE: The field is not updatable in database, so cannot be changed after creation. 
	 * @param created user creation time.
	 */
	public void setCreated(Date created) {
		this.created = created;
	}
	
    public Date getLastLoginDate() {
        return lastLoginDate;
    }
    
    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
    
    /**
	 * Returns active state of this user.
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets active or inactive state for this user.
	 * @param active the active state to set.
	 */
	public void setActive(boolean active) {
		this.active = active;
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

    public void setLdapDomain(String ldapDomain) {
        this.ldapDomain = ldapDomain;
    }

    public String getLdapDomain() {
        return ldapDomain;
    }
    
    @Override
    public String toString() {
        return getClass().getName() + "{id: " + id
                + (login != null ? ", login: " + login : "")
                + (lastLoginDate != null ? ", lastLoginDate: " + lastLoginDate : "")
                + ", active: " + active 
                + (contact != null ? ", contactId: " + contact.getId() : "")
                + (company != null ? ", companyName: " + company.getName() : "")
                + (created != null ? ", created: " + created : "")
                + (ldapDomain != null ? ", ldapDomain: " + ldapDomain : "")
                + "}";
    }
}
