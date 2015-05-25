
package com.googlecode.common.admin.domain;


/**
 * Provides access to roles bit sets.
 */
public interface RolesContainer {

    public static final int LONG_ROLE_WORDS     = 1;
    public static final int MAX_ROLE_BIT_INDEX  = (LONG_ROLE_WORDS * 64) - 1;
    
    
    /**
     * Returns regular roles bit map.
     * @return regular roles map
     */
    public Long getRoles();

    /**
     * Sets regular roles bit map.
     * 
     * @param roles regular roles bit map to set
     */
    public void setRoles(Long roles);
    
    /**
     * Returns inherited roles bit map.
     * @return inherited roles map
     */
    public Long getInheritedRoles();
    
    /**
     * Sets inherited roles bit map.
     * 
     * @param roles inherited roles bit map to set
     */
    public void setInheritedRoles(Long inheritedRoles);

}
