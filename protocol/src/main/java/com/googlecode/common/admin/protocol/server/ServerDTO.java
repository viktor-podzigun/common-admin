
package com.googlecode.common.admin.protocol.server;


/**
 * Part of server requests and responses, containing server data.
 */
public final class ServerDTO {

    private Long        id;
    private String      name;
    private String      url;
    
    
    public ServerDTO() {
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
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{name: " + name
                + (id != null ? ", id: " + id : "")
                + ", url: " + url
                + "}";
    }

}
