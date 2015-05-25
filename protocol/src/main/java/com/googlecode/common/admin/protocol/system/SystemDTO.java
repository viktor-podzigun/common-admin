
package com.googlecode.common.admin.protocol.system;


/**
 * Part of server requests and responses, containing system's data.
 */
public final class SystemDTO {

    private Integer id;
    private String  name;
    private String  password;
    private String  title;
    private String  url;
    
    
    public SystemDTO() {
    }
    
    public int safeGetId() {
        return (id != null ? id.intValue() : 0);
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see safeGetId()
     */
    @Deprecated
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
}
