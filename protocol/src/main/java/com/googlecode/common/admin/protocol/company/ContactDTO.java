
package com.googlecode.common.admin.protocol.company;


/**
 * Part of server requests and responses, containing contact info.
 */
public final class ContactDTO {

    private Long    id;
    
    private String  firstName;
    private String  middleName;
    
    private String  secondName;
    
    private String  email;
    private Long    phone;
    
    private String  companyName;
    
    
    public ContactDTO() {
    }
    
    public long safeGetId() {
        return (id != null ? id.longValue() : 0L);
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see #safeGetId()
     */
    @Deprecated
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFullName() {
        return getFullName(secondName, firstName, middleName);
    }
    
    public static String getFullName(String second, String first, 
            String middle) {
        
        if (middle != null) {
            return second + " " + first + " " + middle;
        }
        
        return second + " " + first;
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
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{firstName: " + firstName
                + (middleName != null ? ", middleName: " + middleName : "")
                + (secondName != null ? ", secondName: " + secondName : "")
                + (email != null ? ", email: " + email : "")
                + (phone != null ? ", phone: " + phone : "")
                + (id != null ? ", id: " + id : "")
                + (companyName != null ? ", companyName: " + companyName : "")
                + "}";
    }

}
