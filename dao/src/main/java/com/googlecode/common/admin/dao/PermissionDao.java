
package com.googlecode.common.admin.dao;

import java.util.List;
import com.googlecode.common.dao.GenericDao;
import com.googlecode.common.admin.domain.PermissionEntity;
import com.googlecode.common.admin.domain.SystemEntity;


/**
 * Permission entity dao interface.
 */
public interface PermissionDao extends GenericDao<PermissionEntity, Long> {

    /**
     * Refreshes the given entity and lock pessimistic.
     * 
     * @param entity    entity to lock
     * @return          refreshed entity
     */
    public PermissionEntity refreshAndLock(PermissionEntity entity);
    
    /**
     * Returns all permissions entities for the given system.
     * 
     * @param system    system entity
     * @return          all permissions entities for the given system
     */
    public List<PermissionEntity> getAll(SystemEntity system);
    
    /**
     * Returns permission node by the given name and for the given parent.
     * 
     * @param system    system entity
     * @param name      node name
     * @param parent    parent node, can be <tt>null</tt> for root node
     * @return          permission node for the given name and parent
     */
    public PermissionEntity getNodeByName(SystemEntity system, String name, 
            PermissionEntity parent);
    
    /**
     * Returns permission by the given name and parent.
     * 
     * @param name      parameter name
     * @param parent    parent node, cannot be <tt>null</tt>
     * @return          permission for the given name and parent
     */
    public PermissionEntity getPermissionByName(String name, 
            PermissionEntity parent);

    /**
     * Returns permissions entities list by the given list of IDs.
     * 
     * @param system    system entity
     * @param idList    list of IDs
     * @return          permissions entities list by the given list of IDs
     */
    public List<PermissionEntity> getPermissions(SystemEntity system, 
            List<Long> idList);

}
