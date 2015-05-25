
package com.googlecode.common.admin.protocol.role;


/**
 * Part of server requests and responses, containing role info.
 */
public final class RoleDTO {
    
    public static final int SUPERUSER = 0;
    
    private Long        id;
    private String      title;
    private Integer     bitIndex;
    
    
    public RoleDTO() {
    }

    public RoleDTO(String title) {
        this.title  = title;
    }

    public static RoleDTO createSuperUser() {
        RoleDTO r = new RoleDTO("SUPERUSER");
        r.setBitIndex(SUPERUSER);
        return r;
    }

    public static boolean isSuperUser(int roleBitIndex) {
        return (roleBitIndex == SUPERUSER);
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
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public int safeGetBitIndex() {
        return (bitIndex != null ? bitIndex.intValue() : 0);
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see #safeGetBitIndex()
     */
    @Deprecated
    public Integer getBitIndex() {
        return bitIndex;
    }
    
    public void setBitIndex(Integer bitIndex) {
        this.bitIndex = bitIndex;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{title: " + title 
                + (bitIndex != null ? ", bitIndex: " + bitIndex : "")
                + (id != null ? ", id: " + id : "")
                + "}";
    }

}
