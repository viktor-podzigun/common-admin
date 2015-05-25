
package com.googlecode.common.admin.service;

import java.util.List;
import com.googlecode.common.protocol.IntListDTO;
import com.googlecode.common.protocol.admin.AppConfReqDTO;
import com.googlecode.common.protocol.admin.AppConfRespDTO;
import com.googlecode.common.protocol.login.AppMenuDTO;
import com.googlecode.common.admin.domain.SystemEntity;
import com.googlecode.common.admin.domain.User;
import com.googlecode.common.admin.protocol.system.SystemDTO;
import com.googlecode.common.admin.protocol.system.SystemGroupDTO;


/**
 * Contains methods for working with systems entities.
 */
public interface SystemService {

    /**
     * Returns application main menu for the given user.
     * 
     * @param systemId  system's id
     * @param userId    user id
     * @return          {@link AppMenuDTO} object
     */
    public AppMenuDTO getAppMenu(int systemId, int userId);
    
    /**
     * Checks whether the given id is the Admin system's id.
     * 
     * @param systemId  system's id
     * @return          <code>true</code> if the given id is the Admin system's 
     *                  id or <code>false</code> otherwise
     */
    public boolean isAdminSystem(int systemId);

    /**
     * Returns systems list by the given parent id.
     * 
     * @param parentId  group id, or <code>0</code> for root systems
     * @return          list of {@link SystemDTO} objects
     */
    public List<SystemDTO> getSystems(long parentId);
    
    /**
     * Returns all systems list.
     * @return list of {@link SystemDTO} objects
     */
    public List<SystemDTO> getAllSystems();
    
    /**
     * Returns systems by the given user.
     * 
     * @param userId    user id
     * @return          systems by the given user
     */
    public List<SystemDTO> getUserSystems(int userId);
    
    /**
     * Updates users systems by the given user id and systems IDs.
     * 
     * @param userId    user id
     * @param dto       systems IDs
     * @param isAdded   indicates whether systems were added 
     *                  (<code>true</code>) or removed (<code>false</code>)
     * @param author    author user entity
     */
    public void updateUserSystems(int userId, IntListDTO dto, 
            boolean isAdded, User author);
    
    /**
     * Returns admin's system entity.
     * @return admin's system entity
     */
    public SystemEntity getAdminSystem();
    
    /**
     * Returns system by the given id.
     * 
     * @param systemId  system's id
     * @return          system by the given id
     */
    public SystemEntity getSystemById(int systemId);
    
    /**
     * Returns system by the given name.
     * 
     * @param name      system's name
     * @return          system entity
     */
    public SystemEntity getSystemByName(String name);
    
    /**
     * Creates new system with the given details.
     * 
     * @param parentId  group id, or <code>0</code> for root system
     * @param dto       system details
     * @return          created system details
     */
    public SystemDTO createSystem(long parentId, SystemDTO dto);
    
    /**
     * Updates system with the given details.
     * 
     * @param dto       system details
     * @return          updated system details
     */
    public SystemDTO updateSystem(SystemDTO dto);

    /**
     * Returns system groups list.
     * @return list of {@link SystemGroupDTO} objects
     */
    public List<SystemGroupDTO> getSystemGroups();
    
    /**
     * Creates new system group with the given details.
     * 
     * @param dto       system group details
     * @param author    author user entity
     * @return          created system group details
     */
    public SystemGroupDTO createSystemGroup(SystemGroupDTO dto, User author);
    
    /**
     * Updates system group with the given details.
     * 
     * @param dto       system group details
     * @param author    author user entity
     * @return          updated system group details
     */
    public SystemGroupDTO updateSystemGroup(SystemGroupDTO dto, User author);

    /**
     * Authenticates and returns system entity.
     * 
     * @param name      system's name
     * @param password  system's password
     * @return          system entity
     */
    public SystemEntity getAuthenticateSystem(String name, String password);
    
    /**
     * Reads/Creates application settings for the given system.
     * 
     * @param appDto    default settings
     * @param system    system entity
     * @return          system's application settings
     */
    public AppConfRespDTO readAppSettings(AppConfReqDTO appDto, 
            SystemEntity system);

}
