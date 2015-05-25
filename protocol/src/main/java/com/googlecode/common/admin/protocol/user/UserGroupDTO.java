
package com.googlecode.common.admin.protocol.user;

import java.util.Collections;
import java.util.List;


/**
 * Part of server requests and responses, containing users group info.
 */
public final class UserGroupDTO {

    private Long                    id;
    
    private String                  name;
    private List<UserGroupDTO>      groups;
    
    
    public UserGroupDTO() {
    }

    public UserGroupDTO(String name) {
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
    
    public List<UserGroupDTO> safeGetGroups() {
        if (groups == null) {
            return Collections.emptyList();
        }
        
        return groups;
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see safeGetGroups()
     */
    @Deprecated
    public List<UserGroupDTO> getGroups() {
        return groups;
    }
    
    public void setGroups(List<UserGroupDTO> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{id: " + id
                + (name != null ? ", name: " + name : "")
                + (groups != null ? ", groupsCount: " + groups.size() : "")
                + "}";
    }
}
