
package com.googlecode.common.admin.protocol.role;

import java.util.List;


/**
 * Part of server requests and responses, containing permissions list.
 */
public final class PermissionListDTO {

    private List<Long>  allowList;
    private List<Long>  disallowList;
    
    
    public PermissionListDTO() {
    }
    
    public List<Long> getAllowList() {
        return allowList;
    }
    
    public void setAllowList(List<Long> allowList) {
        this.allowList = allowList;
    }
    
    public List<Long> getDisallowList() {
        return disallowList;
    }
    
    public void setDisallowList(List<Long> disallowList) {
        this.disallowList = disallowList;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{allowList: " + allowList
                + (disallowList != null ? ", disallowList: " + disallowList : "") 
                + "}";
    }

}
