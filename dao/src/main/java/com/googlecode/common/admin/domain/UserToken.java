
package com.googlecode.common.admin.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * Represents user token entity.
 */
@Entity
@Table(name = "users_tokens")
public class UserToken {

    @Id
    @Size(max = 40)
    private String id;
    
    /**
     * Indicates the time when this token will expire.
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    @Column(name = "expire_date")
    private Date expireDate;
    
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Column(name = "remember_me")
    private Boolean rememberMe;
    

    public UserToken() {
    }
    
    public UserToken(String token, Date expireDate) {
        this.id         = token;
        this.expireDate = expireDate;
    }

    /**
     * @return the token
     */
    public String getId() {
        return id;
    }
    
    /**
     * @param token the token to set
     */
    public void setId(String token) {
        this.id = token;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public Boolean getRememberMe() {
        return rememberMe;
    }
    
    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{id: " + id
                + (expireDate != null ? ", expireDate: " + expireDate : "")
                + (user != null ? ", userLogin: " + user.getLogin() : "") 
                + (rememberMe != null ? ", rememberMe: " + rememberMe : "")
                + "}";
    }

}
