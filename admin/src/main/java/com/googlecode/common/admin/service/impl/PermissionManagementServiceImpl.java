
package com.googlecode.common.admin.service.impl;

import com.googlecode.common.admin.service.AdminResponses;
import com.googlecode.common.admin.service.PermissionManagementService;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import com.googlecode.common.protocol.Permission;
import com.googlecode.common.protocol.PermissionNode;
import com.googlecode.common.protocol.RenameDTO;
import com.googlecode.common.protocol.perm.PermissionDTO;
import com.googlecode.common.protocol.perm.PermissionNodeDTO;
import com.googlecode.common.service.ManageableService;
import com.googlecode.common.service.ServiceManager;
import com.googlecode.common.service.ex.OperationFailedException;
import com.googlecode.common.service.ex.ValidationFailedException;
import com.googlecode.common.util.Bits;
import com.googlecode.common.util.CollectionsUtil;
import com.googlecode.common.util.StringHelpers;
import com.googlecode.common.admin.dao.PermissionDao;
import com.googlecode.common.admin.dao.RoleDao;
import com.googlecode.common.admin.domain.PermissionEntity;
import com.googlecode.common.admin.domain.Role;
import com.googlecode.common.admin.domain.RolesContainer;
import com.googlecode.common.admin.domain.SystemEntity;
import com.googlecode.common.admin.protocol.AdminPerm;
import com.googlecode.common.admin.protocol.role.PermissionListDTO;
import com.googlecode.common.admin.protocol.role.RoleDTO;
import com.googlecode.common.admin.service.SystemService;


/**
 * Default implementation for {@link com.googlecode.common.admin.service.PermissionManagementService} interface.
 */
@Service("permissionManagementService")
@Singleton
public class PermissionManagementServiceImpl implements ManageableService, PermissionManagementService {

    private final Logger    log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected ServiceManager            serviceManager;
    
    @Autowired
    private PlatformTransactionManager  txManager;
    
    @Autowired
    private SystemService               systemsService;

    @Autowired
    private PermissionDao               permissionsDao;
    
    @Autowired
    private RoleDao                     rolesDao;
    
    private volatile Map<Permission, BitSet>    permissionToRoles;

    
    @PostConstruct
    public void init() {
        serviceManager.registerService(this);
        
        SystemEntity system = systemsService.getAdminSystem();
        initPermissions(system, AdminPerm.ROOT);
        
        // cache permissions to roles relations
        permissionToRoles = loadPermissions(system, AdminPerm.ROOT);
    }
    
    @Override
    public void restart() {
        log.info("Restarting...");
    
        // reload permissions to roles cache
        permissionToRoles = loadPermissions(systemsService.getAdminSystem(), 
                AdminPerm.ROOT);
    }
        
    @Override
    public boolean isSuperUserRole(BitSet roles) {
        return roles.get(RoleDTO.SUPERUSER);
    }
    
    @Override
    public boolean hasRolePermission(BitSet roles, Permission p) {
        if (isSuperUserRole(roles)) {
            // super user has all permissions
            return true;
        }
        
        BitSet permissionRoles = permissionToRoles.get(p);
        if (permissionRoles == null) {
            throw new OperationFailedException(
                    AdminResponses.PERMISSION_NOT_FOUND,
                    "Permission (" + p.getName() + ") not found");
        }
        
        return permissionRoles.intersects(roles);
    }
    
    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public List<RoleDTO> getRoles(int systemId) {
        SystemEntity system = systemsService.getSystemById(systemId);
        
        List<RoleDTO> roles = new ArrayList<RoleDTO>();
        for (Role role : rolesDao.getAll(system)) {
            roles.add(convertToRoleDTO(new RoleDTO(), role));
        }
        
        return roles;
    }
    
    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public PermissionNodeDTO getRolePermissions(int systemId, BitSet roles, 
            Set<IncludeFlags> includeFlags) {
        
        SystemEntity system = systemsService.getSystemById(systemId);
        
        PermissionEntity root = null;
        Map<Long, List<PermissionEntity>> children = 
            new HashMap<Long, List<PermissionEntity>>();
        
        for (PermissionEntity entity : permissionsDao.getAll(system)) {
            Long parentId = entity.getParentId();
            if (parentId == null) {
                root = entity;
            } else {
                List<PermissionEntity> childList = children.get(parentId);
                childList = CollectionsUtil.addToList(childList, entity);
                children.put(parentId, childList);
            }
        }

        if (root == null) {
            return null;
        }
        
        PermissionNodeDTO dto = convertToPermissionNodeDTO(root, children, 
                roles, isSuperUserRole(roles), includeFlags);
        if (dto == null) {
            dto = new PermissionNodeDTO();
            dto.setName(root.getName());
            
            if (includeFlags.contains(IncludeFlags.TITLES)) {
                dto.setTitle(root.getTitle());
            }
        }
        
        return dto;
    }

    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public PermissionNodeDTO getSystemPermissions(int systemId, 
            PermissionNodeDTO root) {
        
        if (root != null) {
            initPermissions(systemsService.getSystemById(systemId), 
                    new DefPermissionNode(root));
        }
        
        return getRolePermissions(systemId, new BitSet(), 
                EnumSet.of(IncludeFlags.ALL_PERMISSIONS, IncludeFlags.ROLES));
    }

    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public void updateRolePermissions(long roleId, 
            PermissionListDTO dto) {
        
        Role role = rolesDao.get(roleId);
        if (role == null) {
            throw new OperationFailedException(
                    AdminResponses.PERMISSION_ROLE_NOT_FOUND,
                    "Permission role (" + roleId + ") not found");
        }
        
        SystemEntity system = role.getSystem();
        Set<PermissionEntity> permissions = role.getPermissions();
        
        // disallow given list of permissions
        List<Long> disallowList = dto.getDisallowList();
        if (disallowList != null && !disallowList.isEmpty()) {
            permissions.removeAll(permissionsDao.getPermissions(system, 
                    disallowList));
        }
        
        // allow given list of permissions
        List<Long> allowList = dto.getAllowList();
        if (allowList != null && !allowList.isEmpty()) {
            permissions.addAll(permissionsDao.getPermissions(system, 
                    allowList));
        }
        
        // update changes
        role.setPermissions(permissions);
        rolesDao.merge(role);
    }
    
    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public RoleDTO createRole(int systemId, RoleDTO dto) {
        String title = dto.getTitle();
        if (StringHelpers.isNullOrEmpty(title)) {
            throw new ValidationFailedException(
                    AdminResponses.PERMISSION_INVALID_ROLE,
                    "Role title (" + title + ") is invalid");
        }

        SystemEntity system = systemsService.getSystemById(systemId);
        if (rolesDao.getByTitle(system, title) != null) {
            throw new ValidationFailedException(
                    AdminResponses.PERMISSION_INVALID_ROLE,
                    "Role with title (" + title + ") already exists");
        }
        
        int  bitIndex = 1; // start from 1; 0 is for SUPERUSER role
        Role lastRole = rolesDao.getLast(system);
        
        if (lastRole != null) {
            lastRole = rolesDao.refreshAndLock(lastRole);
            
            bitIndex = lastRole.getBitIndex() + 1;
            if (bitIndex > RolesContainer.MAX_ROLE_BIT_INDEX) {
                throw new OperationFailedException(
                        AdminResponses.PERMISSION_ROLES_MAX_COUNT_ACHIEVED,
                        "Maximum count of roles has been achieved");
            }
        }
        
        Role r = new Role();
        r.setBitIndex(bitIndex);
        r.setTitle(title);
        r.setSystem(system);
        rolesDao.save(r);

        return convertToRoleDTO(new RoleDTO(), r);
    }
    
    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public void renameRole(RenameDTO reqData) {
        long id = reqData.getId();
        Role role = rolesDao.get(id);
        if (role == null) {
            throw new OperationFailedException(
                    AdminResponses.PERMISSION_ROLE_NOT_FOUND,
                    "Role not found, id: " + id);
        }
        
        String name = reqData.getName();
        if (StringHelpers.isNullOrEmpty(name)) {
            throw new ValidationFailedException(
                    AdminResponses.PERMISSION_INVALID_ROLE,
                    "Role name (" + name + ") is invalid");
        }
        
        if (rolesDao.getByTitle(role.getSystem(), name) != null) {
            throw new ValidationFailedException(
                    AdminResponses.PERMISSION_INVALID_ROLE,
                    "Role with name (" + name + ") already exists");
        }
        
        role.setTitle(name);
        rolesDao.merge(role);
    }

    private BitSet getRolesBits(Collection<Role> rolesList) {
        BitSet roles = new BitSet();
        for (Role r : rolesList) {
            roles.set(r.getBitIndex());
        }
        
        return roles;
    }

    private void initPermissions(SystemEntity system, PermissionNode root) {
        log.info("Initializing permissions...");
        
        // Manually create write transaction
        TransactionStatus txStatus = txManager.getTransaction(
                new DefaultTransactionDefinition(
                        TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        
        try {
            initNodeInternal(system, root, null, "");
    
            txManager.commit(txStatus);
            
        } catch (Exception x) {
            if (!txStatus.isCompleted()) {
                txManager.rollback(txStatus);
            }
            
            throw new RuntimeException(x);
        }
    }
    
    private void initNodeInternal(SystemEntity system, PermissionNode node, 
            PermissionEntity parent, String absolutePath) {

        String nodeName = node.getName();
        absolutePath += "/" + nodeName;
        
        PermissionEntity entity = permissionsDao.getNodeByName(system, 
                nodeName, parent);
        
        if (entity == null) {
            if (log.isInfoEnabled()) {
                log.info("Creating perm node:  " + absolutePath);
            }
            
            entity = new PermissionEntity();
            entity.setSystem(system);
            entity.setParent(parent);
            entity.setName(node.getName());
            entity.setTitle(node.getTitle());
            entity.setNode(true);
            
            permissionsDao.save(entity);
        
        } else {
            // lock for concurrent access
            permissionsDao.refreshAndLock(entity);
        
            String title = node.getTitle();
            if (!StringHelpers.isEqual(entity.getTitle(), title)) {
                if (log.isInfoEnabled()) {
                    log.info("Updating perm node:  " + absolutePath);
                }
                
                entity.setTitle(title);
                entity = permissionsDao.merge(entity);
            }
        }
        
        for (Permission p : node.getPermissions()) {
            initPermissionInternal(system, p, entity, absolutePath);
        }
        
        for (PermissionNode n : node.getNodes()) {
            initNodeInternal(system, n, entity, absolutePath);
        }
    }
    
    private PermissionEntity initPermissionInternal(SystemEntity system, 
            Permission p, PermissionEntity node, String absolutePath) {
        
        String name = p.getName();
        absolutePath += "/" + name;
        
        PermissionEntity entity = permissionsDao.getPermissionByName(name, node);
        if (entity == null) {
            if (log.isInfoEnabled()) {
                log.info("Creating permission: " + absolutePath);
            }
            
            entity = new PermissionEntity();
            entity.setSystem(system);
            entity.setParent(node);
            entity.setName(p.getName());
            entity.setTitle(p.getTitle());
            
            permissionsDao.save(entity);
        
        } else {
            String title = p.getTitle();
            if (!StringHelpers.isEqual(entity.getTitle(), title)) {
                if (log.isInfoEnabled()) {
                    log.info("Updating permission: " + absolutePath);
                }
                
                entity.setTitle(title);
                entity = permissionsDao.merge(entity);
            }
        }
        
        return entity;
    }

    private Map<Permission, BitSet> loadPermissions(SystemEntity system, 
            PermissionNode root) {
        
        log.info("Loading permissions...");
        
        // Manually create read transaction
        DefaultTransactionDefinition txDef = new DefaultTransactionDefinition(
                TransactionDefinition.PROPAGATION_SUPPORTS);
        txDef.setReadOnly(true);
        TransactionStatus txStatus = txManager.getTransaction(txDef);
        
        try {
            Map<Permission, BitSet> permissionsMap = 
                new HashMap<Permission, BitSet>();
            
            loadNodeInternal(system, permissionsMap, root, null, "");
    
            txManager.commit(txStatus);
            return permissionsMap;
            
        } catch (Exception x) {
            if (!txStatus.isCompleted()) {
                txManager.rollback(txStatus);
            }
            
            throw new RuntimeException(x);
        }
    }
    
    private void loadNodeInternal(SystemEntity system, 
            Map<Permission, BitSet> permissionsMap, PermissionNode node, 
            PermissionEntity parent, String absolutePath) {

        String nodeName = node.getName();
        absolutePath += "/" + nodeName;
        
        PermissionEntity entity = permissionsDao.getNodeByName(system, 
                nodeName, parent);
        
        if (entity == null) {
            throw new IllegalArgumentException("Permission node not found: " 
                    + absolutePath);
        }
        
        for (PermissionNode n : node.getNodes()) {
            loadNodeInternal(system, permissionsMap, n, entity, absolutePath);
        }
        
        for (Permission p : node.getPermissions()) {
            permissionsMap.put(p, loadPermissionRoles(p, entity, absolutePath));
        }
    }
    
    private BitSet loadPermissionRoles(Permission p, PermissionEntity node, 
            String absolutePath) {
        
        String name = p.getName();
        absolutePath += "/" + name;
        
        PermissionEntity entity = permissionsDao.getPermissionByName(name, node);
        if (entity == null) {
            throw new IllegalArgumentException("Permission not found: " 
                    + absolutePath);
        }
        
        return getRolesBits(entity.getRoles());
    }
    
    private RoleDTO convertToRoleDTO(RoleDTO dto, Role role) {
        dto.setId(role.getId());
        dto.setTitle(role.getTitle());
        dto.setBitIndex(role.getBitIndex());
        return dto;
    }

    private PermissionNodeDTO convertToPermissionNodeDTO(
            PermissionEntity nodeEntity, 
            Map<Long, List<PermissionEntity>> children, BitSet roles, 
            boolean isSuperUser, Set<IncludeFlags> includeFlags) {
        
        boolean allPermissions = includeFlags.contains(
                IncludeFlags.ALL_PERMISSIONS);
        
        List<PermissionNodeDTO> nodes       = null;
        List<PermissionDTO>     permissions = null;
        
        List<PermissionEntity> childList = children.get(nodeEntity.getId());
        if (childList != null) {
            for (PermissionEntity entity : childList) {
                if (entity.isNode()) {
                    PermissionNodeDTO dto = convertToPermissionNodeDTO(entity, 
                            children, roles, isSuperUser, includeFlags);
                    
                    if (dto != null) {
                        nodes = CollectionsUtil.addToList(nodes, dto);
                    }
                } else {
                    BitSet permRoles = getRolesBits(entity.getRoles());
                    boolean isAllowed = isSuperUser 
                            || permRoles.intersects(roles);
                    
                    if (allPermissions || isAllowed) {
                        permissions = CollectionsUtil.addToList(permissions, 
                                convertToPermissionDTO(entity, isAllowed, 
                                        includeFlags));
                    }
                }
            }
        }
        
        if (allPermissions || nodes != null || permissions != null) {
            PermissionNodeDTO dto = new PermissionNodeDTO();
            dto.setName(nodeEntity.getName());
            dto.setNodes(nodes);
            dto.setPermissions(permissions);
            
            if (includeFlags.contains(IncludeFlags.TITLES)) {
                dto.setTitle(nodeEntity.getTitle());
            }
            return dto;
        }
        
        return null;
    }
    
    private PermissionDTO convertToPermissionDTO(PermissionEntity permEntity, 
            boolean isAllowed, Set<IncludeFlags> includeFlags) {

        PermissionDTO dto = new PermissionDTO();
        dto.setName(permEntity.getName());
        
        if (includeFlags.contains(IncludeFlags.TITLES)) {
            dto.setTitle(permEntity.getTitle());
        }
        
        if (includeFlags.contains(IncludeFlags.IDS)) {
            dto.setId(permEntity.getId());
        }
        
        if (includeFlags.contains(IncludeFlags.ALLOWS)) {
            dto.setAllowed(isAllowed);
        }
        
        if (includeFlags.contains(IncludeFlags.ROLES)) {
            BitSet roles = getRolesBits(permEntity.getRoles());
            dto.setRoles(CollectionsUtil.toIntList(Bits.toIntArray(roles)));
        }
        
        return dto;
    }

    
    private static class DefPermissionNode extends PermissionNode {

        public DefPermissionNode(PermissionNodeDTO dto) {
            super(dto.getName(), dto.getTitle(), addPermissions(dto),
                    addNodes(dto));
        }

        private static List<Permission> addPermissions(PermissionNodeDTO dto) {
            List<Permission> list = null;
            for (PermissionDTO p : dto.safeGetPermissions()) {
                list = CollectionsUtil.addToList(list, Permission.newPerm(
                        p.getName(), p.getTitle()));
            }

            return list;
        }

        private static PermissionNode[] addNodes(PermissionNodeDTO dto) {
            List<PermissionNode> nodes = null;
            for (PermissionNodeDTO n : dto.safeGetNodes()) {
                nodes = CollectionsUtil.addToList(nodes, 
                        new DefPermissionNode(n));
            }

            if (nodes == null) {
                return null;
            }
            
            return nodes.toArray(new PermissionNode[nodes.size()]);
        }
    }

}
