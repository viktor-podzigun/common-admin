
package com.googlecode.common.admin.protocol.company;


/**
 * Part of server requests and responses, containing company info.
 */
public final class CompanyDTO {

    private Long    id;
    private String  name;
    
    
    public CompanyDTO() {
    }
    
    public CompanyDTO(String name) {
        this.name = name;
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
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return String.valueOf(name);
    }
}
