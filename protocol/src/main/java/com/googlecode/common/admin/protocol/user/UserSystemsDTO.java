
package com.googlecode.common.admin.protocol.user;

import com.googlecode.common.admin.protocol.system.SystemDTO;
import java.util.Collections;
import java.util.List;


/**
 * Part of server responses, containing user systems data.
 */
public final class UserSystemsDTO {

    private List<SystemDTO>     availableSystems;
    private List<SystemDTO>     assignedSystems;
    
    
    public UserSystemsDTO() {
    }
    
    public UserSystemsDTO(List<SystemDTO> availableSystems, 
            List<SystemDTO> assignedSystems) {
    
        this.availableSystems = availableSystems;
        this.assignedSystems  = assignedSystems;
    }
    
    public List<SystemDTO> safeGetAvailableSystems() {
        if (availableSystems == null) {
            return Collections.emptyList();
        }
        
        return availableSystems;
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see #safeGetAvailableSystems()
     */
    @Deprecated
    public List<SystemDTO> getAvailableSystems() {
        return availableSystems;
    }
    
    public void setAvailableSystems(List<SystemDTO> availableSystems) {
        this.availableSystems = availableSystems;
    }
    
    public List<SystemDTO> safeGetAssignedSystems() {
        if (assignedSystems == null) {
            return Collections.emptyList();
        }
        
        return assignedSystems;
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see #safeGetAssignedSystems()
     */
    @Deprecated
    public List<SystemDTO> getAssignedSystems() {
        return assignedSystems;
    }
    
    public void setAssignedSystems(List<SystemDTO> assignedSystems) {
        this.assignedSystems = assignedSystems;
    }
    
    @Override
    public String toString() {
        return getClass().getName() + "{" 
                + (availableSystems != null ? 
                        ", availableCount: " + availableSystems.size() : "")
                + (assignedSystems != null ? 
                        ", assignedCount: " + assignedSystems.size() : "")
                + "}";
    }

}
