
package com.googlecode.common.admin.service.impl;

import java.util.ArrayList;
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
import com.googlecode.common.protocol.IntListDTO;
import com.googlecode.common.protocol.admin.AppConfReqDTO;
import com.googlecode.common.protocol.admin.AppConfRespDTO;
import com.googlecode.common.protocol.admin.AppSystemDTO;
import com.googlecode.common.protocol.login.AppMenuDTO;
import com.googlecode.common.service.CommonResponses;
import com.googlecode.common.service.ex.OperationFailedException;
import com.googlecode.common.service.ex.ValidationFailedException;
import com.googlecode.common.util.CollectionsUtil;
import com.googlecode.common.util.SafeDigest;
import com.googlecode.common.util.StringHelpers;
import com.googlecode.common.admin.dao.SystemDao;
import com.googlecode.common.admin.dao.SystemGroupDao;
import com.googlecode.common.admin.dao.SystemUserDao;
import com.googlecode.common.admin.domain.SystemEntity;
import com.googlecode.common.admin.domain.SystemGroup;
import com.googlecode.common.admin.domain.SystemUser;
import com.googlecode.common.admin.domain.SystemUserPK;
import com.googlecode.common.admin.domain.User;
import com.googlecode.common.admin.protocol.system.SystemDTO;
import com.googlecode.common.admin.protocol.system.SystemGroupDTO;
import com.googlecode.common.admin.service.AdminResponses;
import com.googlecode.common.admin.service.PermissionManagementService;
import com.googlecode.common.admin.service.SystemService;
import com.googlecode.common.admin.service.UserManagementService;


/**
 * Default implementation for {@link SystemService} interface.
 */
@Service
@Singleton
public class SystemServiceImpl implements SystemService {
    
    private static final int    ADMIN_SYSTEM_ID = 1;
    
    private final Logger        log = LoggerFactory.getLogger(getClass());

    @Autowired
    private SystemDao           systemsDao;
    
    @Autowired
    private SystemGroupDao      groupsDao;
    
    @Autowired
    private SystemUserDao       systemUsersDao;
    
    @Autowired
    private UserManagementService usersService;
    
    @Autowired
    private PermissionManagementService permissionsService;
    

    @Override
    public boolean isAdminSystem(int systemId) {
        return (systemId == ADMIN_SYSTEM_ID);
    }
    
    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public AppMenuDTO getAppMenu(int systemId, int userId) {
        SystemEntity system = null;
        List<SystemEntity> rootList = new ArrayList<SystemEntity>();
        Map<SystemGroup, List<SystemEntity>> listByGroups = 
            new HashMap<SystemGroup, List<SystemEntity>>();
        
        for (SystemUser su : systemUsersDao.getByUser(
                usersService.getUserById(userId))) {
            
            SystemEntity s = su.getPk().getSystem();
            if (s.getId() == systemId) {
                system = s;
            } else {
                SystemGroup g = s.getParent();
                if (g == null) {
                    rootList.add(s);
                } else {
                    List<SystemEntity> list = listByGroups.get(g);
                    list = CollectionsUtil.addToList(list, s);
                    listByGroups.put(g, list);
                }
            }
        }
        
        AppMenuDTO menu = new AppMenuDTO();
        
        if (system != null) {
            menu.setTitle(getSystemTitle(system));
            menu.setUrl(system.getUrl());
        }
        
        List<AppMenuDTO> apps = getSubMenu(new ArrayList<AppMenuDTO>(), 
                rootList);
        menu.setSubMenu(apps);
        
        for (Map.Entry<SystemGroup, List<SystemEntity>> entry 
                : listByGroups.entrySet()) {
            
            List<AppMenuDTO> subMenu = getSubMenu(null, entry.getValue());
            if (subMenu != null && !subMenu.isEmpty()) {
                AppMenuDTO env = new AppMenuDTO();
                env.setTitle(entry.getKey().getName());
                env.setSubMenu(subMenu);
                
                apps.add(env);
            }
        }
        
        return menu;
    }
    
    private String getSystemTitle(SystemEntity s) {
        String title = s.getTitle();
        return (title != null ? title : s.getName());
    }
    
    private List<AppMenuDTO> getSubMenu(List<AppMenuDTO> subMenu, 
            List<SystemEntity> list) {
        
        for (SystemEntity s : list) {
            if (s.getUrl() != null) {
                AppMenuDTO app = new AppMenuDTO();
                app.setTitle(getSystemTitle(s));
                app.setUrl(s.getUrl());
                
                subMenu = CollectionsUtil.addToList(subMenu, app);
            }
        }
        
        return subMenu;
    }
    
    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public List<SystemGroupDTO> getSystemGroups() {
        List<SystemGroupDTO> list = null;
        for (SystemGroup c : groupsDao.getAll()) {
            list = CollectionsUtil.addToList(list, convertToSystemGroupDTO(
                    new SystemGroupDTO(), c));
        }
        
        return list;
    }
    
    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public List<SystemDTO> getSystems(long parentId) {
        SystemGroup parent = null;
        if (parentId != 0) {
            parent = getSystemGroup(parentId);
        }
        
        List<SystemDTO> list = null;
        for (SystemEntity c : systemsDao.getByParent(parent)) {
            list = CollectionsUtil.addToList(list, convertToSystemDTO(
                    new SystemDTO(), c, true));
        }
        
        return list;
    }
    
    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public SystemEntity getAdminSystem() {
        SystemEntity system = systemsDao.get(ADMIN_SYSTEM_ID);
        if (system == null) {
            throw new OperationFailedException(
                    AdminResponses.SYSTEM_NOT_FOUND, 
                    "Admin system not found");
        }
        
        return system;
    }
    
    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public SystemEntity getSystemById(int id) {
        SystemEntity system = systemsDao.get(id);
        if (system == null) {
            throw new OperationFailedException(
                    AdminResponses.SYSTEM_NOT_FOUND, 
                    "System with specified id (" + id + ") not found");
        }
        
        return system;
    }
    
    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public SystemEntity getSystemByName(String name) {
        SystemEntity system = systemsDao.getByName(name);
        if (system == null) {
            throw new OperationFailedException(
                    AdminResponses.SYSTEM_NOT_FOUND, 
                    "System with name (" + name + ") not found");
        }
        
        return system;
    }
    
    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public List<SystemDTO> getAllSystems() {
        List<SystemDTO> list = null;
        for (SystemEntity c : systemsDao.getAll()) {
            list = CollectionsUtil.addToList(list, convertToSystemDTO(
                    new SystemDTO(), c, false));
        }
        
        return list;
    }

    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public List<SystemDTO> getUserSystems(int userId) {
        List<SystemUser> sysUsers = systemUsersDao.getByUser(
                usersService.getUserById(userId));
        
        List<SystemDTO> list = null;
        for (SystemUser su : sysUsers) {
            list = CollectionsUtil.addToList(list, convertToSystemDTO(
                    new SystemDTO(), su.getPk().getSystem(), false));
        }
        
        return list;
    }
    
    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public void updateUserSystems(int userId, IntListDTO dto, 
            boolean isAdded, User author) {
        
        User user = usersService.getUserById(userId);
        for (Integer id : dto.safeGetList()) {
            if (id != null) {
                if (isAdded) {
                    SystemUser su = new SystemUser(systemsDao.get(id), user);
                    su.setModifiedBy(author);
                    su.setModifiedDate(new Date());
                    systemUsersDao.save(su);
                } else {
                    SystemUser su = systemUsersDao.get(new SystemUserPK(
                            systemsDao.get(id), user));
                    if (su != null) {
                        systemUsersDao.delete(su);
                    }
                }
            }
        }
    }
    
    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public SystemGroupDTO createSystemGroup(SystemGroupDTO dto, User author) {
        validateSystemGroup(null, dto);
        
        SystemGroup group = new SystemGroup();
        group.setName(dto.getName());
        group.setModifiedBy(author);
        group.setModifiedDate(new Date());
        groupsDao.save(group);
        
        return convertToSystemGroupDTO(dto, group);
    }
    
    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public SystemGroupDTO updateSystemGroup(SystemGroupDTO dto, User author) {
        SystemGroup group = getSystemGroup(dto.safeGetId());
        
        group.setName(dto.getName());
        group.setModifiedBy(author);
        group.setModifiedDate(new Date());
        group = groupsDao.merge(group);
        
        return convertToSystemGroupDTO(dto, group);
    }
    
    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public SystemDTO createSystem(long parentId, SystemDTO dto) {
        validateSystem(null, dto);
        
        SystemGroup parent = null;
        if (parentId != 0) {
            parent = getSystemGroup(parentId);
        }
        
        SystemEntity system = new SystemEntity();
        system.setName(dto.getName());
        system.setPassword(dto.getPassword());
        system.setTitle(dto.getTitle());
        system.setUrl(dto.getUrl());
        system.setParent(parent);
        systemsDao.save(system);
        
        return convertToSystemDTO(dto, system, true);
    }
    
    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public SystemDTO updateSystem(SystemDTO dto) {
        SystemEntity system = getSystemById(dto.safeGetId());
        
        String pass = dto.getPassword();
        if (pass != null) {
            system.setPassword(pass);
        }
        
        system.setTitle(dto.getTitle());
        system.setUrl(dto.getUrl());
        system = systemsDao.merge(system);
        
        return convertToSystemDTO(dto, system, true);
    }
    
    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public SystemEntity getAuthenticateSystem(String name, String password) {
        SystemEntity system = getSystemByName(name);
        if (!system.getPassword().equals(password)) {
            log.warn("System password is invalid");
            throw new OperationFailedException(
                    CommonResponses.AUTHENTICATION_FAILED, 
                    "System authentication failed");
        }
        
        return system;
    }
    
    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public AppConfRespDTO readAppSettings(AppConfReqDTO reqDto, 
            SystemEntity system) {
        
        AppConfRespDTO respDto = new AppConfRespDTO();
        respDto.setPermissions(permissionsService.getSystemPermissions(
                system.getId(), reqDto.getPermissions()));
        
        if (reqDto.safeIsLoadSystemInfo()) {
            List<AppSystemDTO> systems = new ArrayList<AppSystemDTO>();
            for (SystemEntity sys : systemsDao.getAll()) {
                systems.add(new AppSystemDTO(sys.getName(), 
                        SafeDigest.digest(sys.getPassword()), 
                        sys.getTitle()));
            }
            
            respDto.setSystems(systems);
        }
        
        return respDto;
    }
    
    private SystemGroup getSystemGroup(long id) {
        SystemGroup g = groupsDao.get(id);
        if (g == null) {
            throw new OperationFailedException(
                    CommonResponses.ENTITY_NOT_FOUND, 
                    "SystemGroup with id(" + id + ") not found");
        }
        
        return g;
    }
    
    private SystemGroupDTO convertToSystemGroupDTO(SystemGroupDTO dto, 
            SystemGroup c) {
        
        dto.setId(c.getId());
        dto.setName(c.getName());
        return dto;
    }
    
    private SystemDTO convertToSystemDTO(SystemDTO dto, SystemEntity c, 
            boolean fullInfo) {
        
        dto.setId(c.getId());
        dto.setName(c.getName());
        
        if (fullInfo) {
            dto.setTitle(c.getTitle());
            dto.setUrl(c.getUrl());
        }
        
        return dto;
    }
    
    private void validateSystemGroup(SystemGroup curr, SystemGroupDTO dto) {
        String name = StringHelpers.trim(dto.getName());
        dto.setName(name);
        if (name == null || name.length() < 1 || name.length() > 64) {
            throw new ValidationFailedException(
                    AdminResponses.SYSTEM_GROUP_INVALID_NAME, 
                    "SystemGroup name should be from 1 to 64 symbols long");
        }
        
        // check name is unique for SystemGroup
        if ((curr == null || !curr.getName().equals(name)) 
                && groupsDao.getByName(name) != null) {
            
            throw new OperationFailedException(
                    CommonResponses.ENTITY_ALREADY_EXISTS, 
                    "SystemGroup with name(" + name + ") already exists");
        }
    }

    private void validateSystem(SystemEntity curr, SystemDTO dto) {
        String name = StringHelpers.trim(dto.getName());
        dto.setName(name);
        // validate name for new system
        if (curr == null && (name == null 
                || name.length() < 1 || name.length() > 32)) {
            
            throw new ValidationFailedException(
                    AdminResponses.SYSTEM_INVALID_NAME, 
                    "System name should be from 1 to 32 symbols long");
        }
        
        String pass = StringHelpers.trim(dto.getPassword());
        dto.setPassword(pass);
        // validate password for new system or when it explicitly set
        if ((curr == null || pass != null) && (pass == null 
                || pass.length() < 1 || pass.length() > 32)) {
            
            throw new ValidationFailedException(
                    AdminResponses.SYSTEM_INVALID_PASSWORD, 
                    "System password should be from 1 to 32 symbols long");
        }
        
        String title = StringHelpers.trim(dto.getTitle());
        dto.setTitle(title);
        if (title != null && (title.length() < 1 || title.length() > 32)) {
            throw new ValidationFailedException(
                    AdminResponses.SYSTEM_INVALID_TITLE, 
                    "System title should be from 1 to 32 symbols long");
        }
        
        String url = StringHelpers.trim(dto.getUrl());
        dto.setUrl(url);
        if (url != null && (url.length() < 1 || url.length() > 128)) {
            throw new ValidationFailedException(
                    AdminResponses.SYSTEM_INVALID_URL, 
                    "System URL should be from 1 to 128 symbols long");
        }
        
        // check name is unique for new System
        if (curr == null && systemsDao.getByName(name) != null) {
            throw new OperationFailedException(
                    AdminResponses.SYSTEM_ALREADY_EXISTS, 
                    "System with name(" + name + ") already exists");
        }
    }

}
