
package com.googlecode.common.admin.protocol.system;



/**
 * Part of server requests and responses, containing system group info.
 */
public final class SystemGroupDTO {

    private Long    id;
    private String  name;
    
    
    public SystemGroupDTO() {
    }

    public SystemGroupDTO(Long id, String name) {
        this.id   = id;
        this.name = name;
    }

    public long safeGetId() {
        return (id != null ? id.longValue() : 0L);
    }

    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see safeGetId()
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
    
}
