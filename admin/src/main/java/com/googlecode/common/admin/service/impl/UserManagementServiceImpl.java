
package com.googlecode.common.admin.service.impl;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.googlecode.common.dao.PageData;
import com.googlecode.common.protocol.RenameDTO;
import com.googlecode.common.service.ex.OperationFailedException;
import com.googlecode.common.service.ex.ValidationFailedException;
import com.googlecode.common.util.Bits;
import com.googlecode.common.util.CollectionsUtil;
import com.googlecode.common.util.NumUtils;
import com.googlecode.common.util.SafeDigest;
import com.googlecode.common.util.StringHelpers;
import com.googlecode.common.admin.dao.SystemUserDao;
import com.googlecode.common.admin.dao.UserDao;
import com.googlecode.common.admin.dao.UserGroupDao;
import com.googlecode.common.admin.domain.Company;
import com.googlecode.common.admin.domain.Contact;
import com.googlecode.common.admin.domain.RolesContainer;
import com.googlecode.common.admin.domain.SystemEntity;
import com.googlecode.common.admin.domain.SystemUser;
import com.googlecode.common.admin.domain.SystemUserPK;
import com.googlecode.common.admin.domain.User;
import com.googlecode.common.admin.domain.UserGroup;
import com.googlecode.common.admin.protocol.company.ContactDTO;
import com.googlecode.common.admin.protocol.profile.ProfileDTO;
import com.googlecode.common.admin.protocol.role.RoleDTO;
import com.googlecode.common.admin.protocol.user.NewUserDTO;
import com.googlecode.common.admin.protocol.user.UpdateUserRolesDTO;
import com.googlecode.common.admin.protocol.user.UserDTO;
import com.googlecode.common.admin.protocol.user.UserGroupDTO;
import com.googlecode.common.admin.protocol.user.UserListReqDTO;
import com.googlecode.common.admin.protocol.user.UserListResponse;
import com.googlecode.common.admin.protocol.user.UserMoveDTO;
import com.googlecode.common.admin.service.AdminResponses;
import com.googlecode.common.admin.service.CompanyService;
import com.googlecode.common.admin.service.PermissionManagementService;
import com.googlecode.common.admin.service.SenderService;
import com.googlecode.common.admin.service.SystemService;
import com.googlecode.common.admin.service.UserManagementService;


/**
 * UserManagementService implementation
 *
 * @see UserManagementService
 */
@Service
@Singleton
public class UserManagementServiceImpl implements UserManagementService {

    private static final String TEMPL_NEW_USER  = "new.user.notification";
    
    private final Logger        log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private UserDao             usersDao;
    
    @Autowired
    private UserGroupDao        userGroupsDao;
    
    @Autowired
    private CompanyService      contactService;
    
    @Autowired
    private SenderService       senderService;
    
    @Autowired
    private SystemService       systemsService;

    @Autowired
    private SystemUserDao       systemUsersDao;

    @Autowired
    private PermissionManagementService permissionService;
    
    
    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public UserListResponse getSystemUsers(int systemId, long groupId, 
            UserListReqDTO dto) {
        
        PageData<SystemUser> data;
        if (groupId > 0) {
            data = systemUsersDao.getByParent(getGroupById(groupId), 
                    dto.getStartIndex(), dto.safeGetLimit());
        } else {
            data = systemUsersDao.getBySystem(systemsService.getSystemById(
                    systemId), dto.getStartIndex(), dto.safeGetLimit());
        }

        List<UserDTO> list = null;
        for (SystemUser u : data.getEntities()) {
            list = CollectionsUtil.addToList(list, convertToUserDTO(
                    new UserDTO(), u.getPk().getUser(), u.getParent()));
        }

        UserListResponse resp = new UserListResponse(list);
        resp.setTotalCount(data.getTotalCount());
        return resp;
    }
    
    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public UserListResponse getCompanyUsers(long companyId, 
            UserListReqDTO dto) {
        
        Company company = null;
        if (companyId > 0) {
            company = contactService.getCompanyById(companyId);
        }
        
        PageData<User> data = usersDao.getByCompany(company, 
                dto.getStartIndex(), dto.safeGetLimit());
        
        List<UserDTO> list = null;
        for (User u : data.getEntities()) {
            list = CollectionsUtil.addToList(list, convertToUserDTO(
                    new UserDTO(), u, null));
        }

        UserListResponse resp = new UserListResponse(list);
        resp.setTotalCount(data.getTotalCount());
        return resp;
    }
    
    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public SystemUser getSystemUserById(SystemEntity system, User user) {
        if (system == null) {
            throw new NullPointerException("system");
        }
        if (user == null) {
            throw new NullPointerException("user");
        }
        
        SystemUser su = systemUsersDao.get(new SystemUserPK(system, user));
        if (su == null) {
            throw new OperationFailedException(AdminResponses.USER_NOT_FOUND, 
                    "User with id (" + user.getId() 
                        + ") not mapped to the system (" 
                        + system.getName() + ")");
        }

        return su;
    }
    
    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public UserGroup getGroupById(long groupId) {
        UserGroup group = userGroupsDao.get(groupId);
        if (group == null) {
            throw new OperationFailedException(
                    AdminResponses.USER_GROUP_NOT_FOUND, 
                    "Group with the specified id (" + groupId + ") not found");
        }
        
        return group;
    }
    
    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public User getUserById(int userId) {
        User user = usersDao.get(userId);
        if (user == null) {
            throw new OperationFailedException(AdminResponses.USER_NOT_FOUND, 
                    "User with id (" + userId + ") not found");
        }

        return user;
    }
    
    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public UserDTO getUserDTOById(int userId) {
        return convertToUserDTO(new UserDTO(), getUserById(userId), null);
    }
    
    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public ProfileDTO getProfile(int userId) {
        User user = getUserById(userId);
        
        ContactDTO contact = contactService.getContactDTO(
                user.getContact().getId());
        contact.setCompanyName(user.getCompany().getName());
        
        ProfileDTO dto = new ProfileDTO();
        dto.setContact(contact);
        return dto;
    }
    
    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public void updateProfile(int userId, ProfileDTO dto) {
        User user = getUserById(userId);
        
        contactService.updateContact(dto.getContact());
        
        String newPass = StringHelpers.trim(dto.getNewPass());
        if (newPass != null) {
            user = changeUserPassword(user, dto.getOldPass(), 
                    dto.getNewPass());
        }
    
        user.setModifiedDate(new Date());
        user.setModifiedBy(user);
        usersDao.merge(user);
        
        log.info("User profile updated: " 
                + "login: " + user.getLogin());
    }

    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public UserDTO createNewUser(int systemId, long parentId, 
            NewUserDTO details, User author) {
        
        User user = createUserInternal(details, author);
        
        if (log.isTraceEnabled()) {
            log.trace("User creation completed: " + details);
        
        } else if (log.isInfoEnabled()) {
            log.info("User creation completed: " 
                    + "login: " + details.getLogin());
        }
        
        UserGroup parent = null;
        if (systemId != 0L) {
            SystemEntity system = systemsService.getSystemById(systemId);
            SystemUser su = new SystemUser(system, user);
            su.setModifiedDate(new Date());
            su.setModifiedBy(author);
            
            parent = getParentById(parentId);
            if (parent != null) {
                su.setParent(parent);
                setRoles(su, getRoles(parent, RolesType.ALL), 
                        RolesType.INHERITED);
            }
            
            systemUsersDao.save(su);
        }
        
        return convertToUserDTO(new UserDTO(), user, parent);
    }

    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public UserGroupDTO createNewGroup(int systemId, long parentId, 
            UserGroupDTO dto, User author) {
        
        SystemEntity system = systemsService.getSystemById(systemId);
        UserGroup group = createUserGroupInternal(system, parentId, dto, 
                author);
        
        if (log.isTraceEnabled()) {
            log.trace("UserGroup creation completed: " + dto);
        
        } else if (log.isInfoEnabled()) {
            log.info("UserGroup creation completed: " 
                    + "name: " + dto.getName());
        }
        
        return convertToUserGroupDTO(dto, group);
    }
    
    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public void moveUsers(UserMoveDTO dto, User author) {
        SystemEntity system = systemsService.getSystemById(dto.getSystemId());
        UserGroup parent = getParentById(dto.getDstParentId());
        
        long[] groups = dto.getGroups();
        if (groups != null) {
            moveGroupsInternal(parent, groups, author);
        }

        int[] users = dto.getUsers();
        if (users != null) {
            moveUsersInternal(system, parent, users, author);
        }
    }
    
    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public void renameUserGroup(RenameDTO dto, User author) {
        long userId = dto.getId();
        UserGroup group = userGroupsDao.get(userId);
        if (group == null) {
            throw new OperationFailedException(
                    AdminResponses.USER_GROUP_NOT_FOUND, 
                    "User Group with specified id (" + userId + ") not found");
        }
        
        String name = dto.getName();
        if (StringHelpers.isNullOrEmpty(name)) {
            throw new ValidationFailedException(
                    AdminResponses.USER_GROUP_INVALID_NAME,
                    "User Group name (" + name + ") is invalid");
        }
        
        if (userGroupsDao.getByName(group.getSystem(), name) != null) {
            throw new OperationFailedException(
                    AdminResponses.USER_GROUP_ALREADY_EXISTS, 
                    "User group with the specified name (" 
                    + name + ") already exists");
        }

        group.setName(name);
        group.setModifiedBy(author);
        group.setModifiedDate(new Date());

        userGroupsDao.merge(group);
        
        if (log.isInfoEnabled()) {
            log.info("Updated User Group info: userId: \"" 
                    + userId + "\", details: " + dto);
        }
    }

    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public BitSet updateUserRoles(UpdateUserRolesDTO dto, SystemUser author) {
        SystemUser su = getSystemUserById(systemsService.getSystemById(
                dto.getSystemId()), getUserById((int)dto.getId()));

        updateRolesInternal(su, dto, author);
        
        su.setModifiedBy(author.getPk().getUser());
        su.setModifiedDate(new Date());
        su = systemUsersDao.merge(su);
        
        return getRoles(su, RolesType.ALL);
    }
    
    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public BitSet updateGroupRoles(UpdateUserRolesDTO dto, SystemUser author) {
        Date date = new Date();
        UserGroup g = getGroupById(dto.getId());
        
        updateRolesInternal(g, dto, author);
        
        g.setModifiedBy(author.getPk().getUser());
        g.setModifiedDate(date);
        g = userGroupsDao.merge(g);
        
        updateInheritedRoles(g, author.getPk().getUser(), date);
        return getRoles(g, RolesType.ALL);
    }

    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public void deleteUserGroup(long groupId) {
        UserGroup group = getGroupById(groupId);
        
        List<UserGroup> groupsList = userGroupsDao.getByParent(group);
        if (!groupsList.isEmpty()) {
            throw new OperationFailedException(
                    AdminResponses.USER_GROUP_NOT_EMPTY,
                    "User Group (" + group.getName() + ") is not empty");
        }
        
        PageData<SystemUser> usersList = systemUsersDao.getByParent(group, 
                0, 1);
        if (!usersList.getEntities().isEmpty()) {
            throw new OperationFailedException(
                    AdminResponses.USER_GROUP_NOT_EMPTY,
                    "User Group (" + group.getName() + ") is not empty");
        }
        
        userGroupsDao.delete(group);
    }

    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public List<UserGroupDTO> getAllGroups(int systemId) {
        SystemEntity system = systemsService.getSystemById(systemId);
        
        List<UserGroup> roots = new ArrayList<UserGroup>();
        Map<Long, List<UserGroup>> children = 
            new HashMap<Long, List<UserGroup>>();
        
        for (UserGroup g : userGroupsDao.getAll(system)) {
            Long parentId = g.getParentId();
            if (parentId == null) {
                roots.add(g);
            } else {
                List<UserGroup> childList = children.get(parentId);
                childList = CollectionsUtil.addToList(childList, g);
                children.put(parentId, childList);
            }
        }

        List<UserGroupDTO> groups = null;
        for (UserGroup g : roots) {
            UserGroupDTO groupDto = convertToUserGroupDTO(
                    new UserGroupDTO(), g);
            groups = CollectionsUtil.addToList(groups, groupDto);
            
            addChildrenToUserGroupDTO(groupDto, children);
        }
        
        return groups;
    }

    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public void setLastLoginDate(int userId) {
        User user = getUserById(userId);
        
        user.setLastLoginDate(new Date());
        usersDao.merge(user);
    }
    
    @Override
    public BitSet getRoles(RolesContainer entity, RolesType type) {
        long[] roles = new long[RolesContainer.LONG_ROLE_WORDS];
        switch (type) {
        case INHERITED:
            roles[0] = NumUtils.ensureNotNull(entity.getInheritedRoles());
            break;
        
        case ROLES:
            roles[0] = NumUtils.ensureNotNull(entity.getRoles());
            break;
        
        case ALL:
            roles[0] = Bits.add(
                    NumUtils.ensureNotNull(entity.getInheritedRoles()), 
                    NumUtils.ensureNotNull(entity.getRoles()));
            break;
        
        default:
            throw new RuntimeException("Unknown rolesType: " + type);
        }
        
        return Bits.fromLongArray(roles);
    }
    
    @Override
    public void setRoles(RolesContainer entity, BitSet bits, RolesType type) {
        long[] roles;
        if (bits != null) {
            roles = Bits.toLongArray(bits, RolesContainer.LONG_ROLE_WORDS);
        } else {
            roles = new long[RolesContainer.LONG_ROLE_WORDS];
        }
        
        if (type == RolesType.INHERITED) {
            entity.setInheritedRoles(NumUtils.ensureNull(roles[0]));
        } else {
            entity.setRoles(NumUtils.ensureNull(roles[0]));
        }
    }
    
    private UserGroup getParentById(long parentId) {
        if (parentId == 0) {
            return null;
        }
        
        return getGroupById(parentId);
    }
    
    private void updateRolesInternal(RolesContainer entity, 
            UpdateUserRolesDTO dto, SystemUser author) {
        
        BitSet  authorRoles = getRoles(author, RolesType.ROLES);
        BitSet  roles       = new BitSet();
        boolean isGroup     = (entity instanceof UserGroup);
        
        for (Integer val : dto.safeGetRoles()) {
            int bitIndex = NumUtils.ensureNotNull(val);
            if (RoleDTO.isSuperUser(bitIndex)) {
                if (!permissionService.isSuperUserRole(authorRoles)) {
                    throw new OperationFailedException(
                            AdminResponses.PERMISSION_INVALID_ROLE,
                            "Regular user cannot change SUPERUSER role");
                }
            
                if (isGroup) {
                    throw new OperationFailedException(
                            AdminResponses.PERMISSION_INVALID_ROLE,
                            "SUPERUSER role cannot be assigned to group");
                }
            }
            
            roles.set(bitIndex);
        }
        
        BitSet currRoles = getRoles(entity, RolesType.ROLES);
        if (dto.isAdded()) {
            currRoles.or(roles);
        } else {
            currRoles.andNot(roles);
        }
        
        setRoles(entity, currRoles, RolesType.ROLES);
    }
    
    private void updateInheritedRoles(UserGroup parent, User author, 
            Date date) {
        
        BitSet roles = getRoles(parent, RolesType.ALL);
        
        for (UserGroup g : userGroupsDao.getByParent(parent)) {
            setRoles(g, roles, RolesType.INHERITED);
            g.setModifiedBy(author);
            g.setModifiedDate(date);
            g = userGroupsDao.merge(g);
            
            updateInheritedRoles(g, author, date);
        }
        
        PageData<SystemUser> data = systemUsersDao.getByParent(parent, 
                0, Integer.MAX_VALUE);
        for (SystemUser su : data.getEntities()) {
            setRoles(su, roles, RolesType.INHERITED);
            su.setModifiedBy(author);
            su.setModifiedDate(date);
            systemUsersDao.merge(su);
        }
    }
    
    private void moveGroupsInternal(UserGroup parent, long[] groups, 
            User author) {
        
        Date currDate = new Date();
        for (long id : groups) {
            UserGroup g = getGroupById(id);
            g.setParent(parent);
            g.setModifiedBy(author);
            g.setModifiedDate(currDate);

            if (parent != null) {
                setRoles(g, getRoles(parent, RolesType.ALL), 
                        RolesType.INHERITED);
            } else {
                setRoles(g, null, RolesType.INHERITED);
            }
            
            g = userGroupsDao.merge(g);
            
            updateInheritedRoles(g, author, currDate);
        }
    }

    private void moveUsersInternal(SystemEntity system, UserGroup parent, 
            int[] users, User author) {
        
        Date currDate = new Date();
        for (long id : users) {
            SystemUser su = getSystemUserById(system, getUserById((int)id));
            su.setParent(parent);
            
            if (parent != null) {
                setRoles(su, getRoles(parent, RolesType.ALL), 
                        RolesType.INHERITED);
            } else {
                setRoles(su, null, RolesType.INHERITED);
            }
            
            su.setModifiedBy(author);
            su.setModifiedDate(currDate);
            systemUsersDao.merge(su);
        }
    }

    private User changeUserPassword(User user, String oldPass, 
            String newPass) {
        
        oldPass = StringHelpers.trim(oldPass);
        if (StringHelpers.isNullOrEmpty(oldPass)) {
            throw new ValidationFailedException(
                    AdminResponses.USER_OLD_PASSWORD_WRONG, 
                    "Old password is not specified");
        }
        
        if (!user.getPassHash().equals(SafeDigest.digest(oldPass))) {
            throw new ValidationFailedException(
                    AdminResponses.USER_OLD_PASSWORD_WRONG, 
                    "Old password is invalid");
        }
        
        if (newPass == null || newPass.length() < 6
                || newPass.length() > 20) {
            
            throw new ValidationFailedException(
                    AdminResponses.USER_NEW_PASSWORD_INVALID, 
                    "New password should be from 6 to 20 characters long");
        }
        
        user.setPassHash(SafeDigest.digest(newPass));
        return usersDao.merge(user);
    }
    
    /**
     * Creates new user from the specified details.
     * 
     * @param details  user parameters
     * @return         newly created user
     * 
     * @throws ValidationFailedException if some fields aren't valid.
     */
    private User createUserInternal(NewUserDTO details, User author) {
        String login = details.getLogin();
        if (login == null || login.length() < 2 || login.length() > 64) {
            throw new ValidationFailedException(
                    AdminResponses.USER_INVALID_LOGIN, 
                    "User login should be from 2 to 64 symbols long");
        }
        
        String password = details.getPassword();
        if (password == null || password.length() < 6
                || password.length() > 20) {
            
            throw new ValidationFailedException(
                    AdminResponses.USER_INVALID_PASSWORD, 
                    "User password should be from 6 to 20 symbols long");
        }
        
        if (usersDao.getByLogin(login) != null) {
            throw new OperationFailedException(
                    AdminResponses.USER_ALREADY_EXISTS, 
                    "User with the specified login (" + login 
                    + ") already exists");
        }
        
        Company company = contactService.getCreateCompay(details.getCompany());
        ContactDTO contactDto = contactService.createNewContact(
                company, details.getContact());
        
        User u = new User();
        u.setLogin(login);
        u.setContact(contactService.getContactById(contactDto.safeGetId()));
        u.setCompany(company);
        
        Date now = new Date();
        u.setActive(true);
        u.setCreated(now);

//        String password = StringHelpers.generatePassword(6);
        sendNewUserMailNotification(login, password, contactDto.getEmail());
        u.setPassHash(SafeDigest.digest(password));
        
//        String ldapDomain = details.getLdapDomain();
//        if (ldapDomain != null
//                && (ldapDomain.length() < 4 || ldapDomain.length() > 64)) {
//            throw new ValidationFailedException(
//                    AdminResponses.USER_LDAP_DAMAIN_INVALID,
//                    "User LDAP domain should be from 4 to 64 symbols long");
//        }
//        
//        if (ldapDomain == null) {
//            String password = StringHelpers.generatePassword(6);
//            sendNewUserMailNotification(login, password, contactDto.getEmail());
//            u.setPassHash(SafeDigest.digest(password));
//        } else {
//            u.setLdapDomain(ldapDomain);
//        }
        
        u.setModifiedBy(author);
        u.setModifiedDate(now);
        usersDao.save(u);
        
        return u;
    }
    
    private UserGroup createUserGroupInternal(SystemEntity system, 
            long parentId, UserGroupDTO dto, User author) {
        
        String name = dto.getName();
        if (StringHelpers.isNullOrEmpty(name)) {
            throw new ValidationFailedException(
                    AdminResponses.USER_GROUP_INVALID_NAME, 
                    "User group name is null or empty");
        }
    
        // check name for new group
        if (userGroupsDao.getByName(system, name) != null) {
            throw new OperationFailedException(
                    AdminResponses.USER_GROUP_ALREADY_EXISTS, 
                    "User group with the specified name (" 
                    + name + ") already exists");
        }
        
        UserGroup g = new UserGroup();
        g.setSystem(system);
        g.setName(name);
        g.setModifiedBy(author);
        g.setModifiedDate(new Date());
        
        UserGroup parent = getParentById(parentId);
        if (parent != null) {
            g.setParent(parent);
            setRoles(g, getRoles(parent, RolesType.ALL), RolesType.INHERITED);
        }
        
        userGroupsDao.save(g);
        return g;
    }
    
    private UserDTO convertToUserDTO(UserDTO dto, User u, UserGroup parent) {
        dto.setId(u.getId());
        dto.setLogin(u.getLogin());
        dto.setActive(u.isActive());
        dto.setCreated(u.getCreated());
        dto.setModified(u.getModifiedDate());
        dto.setLastLogin(u.getLastLoginDate());
        dto.setCompanyName(u.getCompany().getName());
        dto.setLdapDomain(u.getLdapDomain());
        
        if (parent != null) {
            dto.setParentId(parent.getId());
            dto.setParentName(parent.getName());
        }
        
        Contact contact = u.getContact();
        if (contact != null) {
            dto.setContactId(contact.getId());
        }
        
        return dto;
    }

    private UserGroupDTO convertToUserGroupDTO(UserGroupDTO dto, UserGroup g) {
        dto.setId(g.getId());
        dto.setName(g.getName());
        return dto;
    }
    
    private UserGroupDTO addChildrenToUserGroupDTO(UserGroupDTO dto, 
            Map<Long, List<UserGroup>> children) {
        
        List<UserGroupDTO> groups = null;
        
        List<UserGroup> childList = children.get(dto.safeGetId());
        if (childList != null) {
            for (UserGroup g : childList) {
                UserGroupDTO groupDto = convertToUserGroupDTO(
                        new UserGroupDTO(), g);
                groups = CollectionsUtil.addToList(groups, groupDto);
                
                addChildrenToUserGroupDTO(groupDto, children);
            }
        }
        
        dto.setGroups(groups);
        return dto;
    }
    
    private void sendNewUserMailNotification(String login, String password,
            String address) {

        if (log.isInfoEnabled()) {
            log.info("Sending notification for new user: " + login);
        }

        Map<String, String> vars = new HashMap<String, String>();
        vars.put("login", login);
        vars.put("password", password);

        senderService.sendEmailTemplate(null, "", address, null,
                TEMPL_NEW_USER, vars);
    }
    
}
