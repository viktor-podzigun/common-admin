
package com.googlecode.common.admin.service;

import java.util.BitSet;
import java.util.List;
import com.googlecode.common.protocol.RenameDTO;
import com.googlecode.common.admin.domain.RolesContainer;
import com.googlecode.common.admin.domain.SystemEntity;
import com.googlecode.common.admin.domain.SystemUser;
import com.googlecode.common.admin.domain.User;
import com.googlecode.common.admin.domain.UserGroup;
import com.googlecode.common.admin.protocol.profile.ProfileDTO;
import com.googlecode.common.admin.protocol.user.NewUserDTO;
import com.googlecode.common.admin.protocol.user.UpdateUserRolesDTO;
import com.googlecode.common.admin.protocol.user.UserDTO;
import com.googlecode.common.admin.protocol.user.UserGroupDTO;
import com.googlecode.common.admin.protocol.user.UserListReqDTO;
import com.googlecode.common.admin.protocol.user.UserListResponse;
import com.googlecode.common.admin.protocol.user.UserMoveDTO;


/**
 * This service responsible for users creation, retrieving, and updating.
 */
public interface UserManagementService {

    public static enum RolesType {
        INHERITED,
        ROLES,
        ALL,
    }
    
    /**
     * Returns system users in group.
     * 
     * @param systemId  system's ID
     * @param groupId   group's ID, or <code>0</code> for all users
     * @param dto       request info
     * @return          users response
     */
    public UserListResponse getSystemUsers(int systemId, long groupId, 
            UserListReqDTO dto);
    
    /**
     * Returns company users.
     * 
     * @param companyId company's id
     * @param dto       request info
     * @return          users response
     */
    public UserListResponse getCompanyUsers(long companyId, UserListReqDTO dto);
    
    /**
     * Returns system user by the given ID and within the given system.
     * 
     * @param system    system entity
     * @param user      user entity
     * @return          user for the given ID
     */
    public SystemUser getSystemUserById(SystemEntity system, User user);
    
    /**
     * Returns user group by the given ID.
     * 
     * @param groupId   group's ID
     * @return          group user for the given ID
     */
    public UserGroup getGroupById(long groupId);
    
    /**
     * Returns user entity by the given id.
     * 
     * @param userId    user id
     * @return          user entity by the given id
     */
    public User getUserById(int userId);
    
    /**
     * Returns user's profile info by the given id.
     * 
     * @param userId    user id
     * @return          {@link ProfileDTO} object
     */
    public ProfileDTO getProfile(int userId);
    
    /**
     * Updates user's profile info by the given id.
     * 
     * @param userId    user id
     * @param dto       {@link ProfileDTO} object containing user profile info
     */
    public void updateProfile(int userId, ProfileDTO dto);
    
    /**
     * Returns user info by the given id.
     * 
     * @param userId    user id
     * @return          user info by the given id
     */
    public UserDTO getUserDTOById(int userId);
    
    /**
     * Returns all user groups.
     * 
     * @param systemId  system's ID
     * @return all user groups list
     */
    public List<UserGroupDTO> getAllGroups(int systemId);

    /**
     * Creates new user with the specified details.
     * 
     * @param systemId  system's ID
     * @param parentId  parent group's ID, can be <code>0</code> for root group
     * @param details   user's details
     * @param author    current user
     * @return          update status response
     */
    public UserDTO createNewUser(int systemId, long parentId, 
            NewUserDTO details, User author);

    /**
     * Creates new user group.
     * 
     * @param systemId  system's ID
     * @param parentId  parent group's ID, can be <code>0</code> for root group
     * @param dto       user group dto
     * @param author    current user
     * @return          update status response
     */
    public UserGroupDTO createNewGroup(int systemId, long parentId, 
            UserGroupDTO dto, User author);

    /**
     * Move users and groups to specific group
     * 
     * @param dto       UserMoveDTO
     * @param author    current user
     * @return          move status response
     */
    public void moveUsers(UserMoveDTO dto, User author);

    public void renameUserGroup(RenameDTO dto, User author);

    /**
     * Updates user's roles with the given data.
     * 
     * @param dto       update roles data
     * @param author    current user
     * @return          all user's roles (including inherited roles)
     */
    public BitSet updateUserRoles(UpdateUserRolesDTO dto, SystemUser author);
    
    /**
     * Updates group's roles with the given data.
     * 
     * @param dto       update roles data
     * @param author    current user
     * @return          all group's roles (including inherited roles)
     */
    public BitSet updateGroupRoles(UpdateUserRolesDTO dto, SystemUser author);
    
    public void deleteUserGroup(long groupId);
    
    public void setLastLoginDate(int userId);
    
    public BitSet getRoles(RolesContainer entity, RolesType type);
    
    public void setRoles(RolesContainer entity, BitSet roles, RolesType type);
    
}
