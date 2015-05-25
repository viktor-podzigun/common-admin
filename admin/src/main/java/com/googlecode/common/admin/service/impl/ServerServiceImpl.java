
package com.googlecode.common.admin.service.impl;

import com.googlecode.common.admin.service.AdminResponses;
import com.googlecode.common.admin.service.ServerService;
import java.io.IOException;
import java.util.List;
import javax.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.googlecode.common.http.RequestParams;
import com.googlecode.common.protocol.admin.AdminRequests;
import com.googlecode.common.protocol.admin.ServerModulesResponse;
import com.googlecode.common.protocol.admin.ServerStatusResponse;
import com.googlecode.common.service.CommonResponses;
import com.googlecode.common.service.JsonRequestService;
import com.googlecode.common.service.ex.OperationFailedException;
import com.googlecode.common.service.ex.ValidationFailedException;
import com.googlecode.common.util.CollectionsUtil;
import com.googlecode.common.util.StringHelpers;
import com.googlecode.common.admin.dao.ServerDao;
import com.googlecode.common.admin.domain.ServerEntity;
import com.googlecode.common.admin.domain.SystemEntity;
import com.googlecode.common.admin.protocol.server.ServerDTO;
import com.googlecode.common.admin.service.SystemService;


/**
 * Default implementation for {@link com.googlecode.common.admin.service.ServerService} interface.
 */
@Service
@Singleton
public class ServerServiceImpl implements ServerService {

    @Autowired
    private JsonRequestService  requestService;
    
    @Autowired
    private SystemService       systemsService;

    @Autowired
    private ServerDao           serversDao;


    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public ServerDTO getServer(long serverId) {
        return convertToServerDTO(new ServerDTO(), getServerById(serverId));
    }
    
    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public ServerDTO createServer(int systemId, ServerDTO dto) {
        SystemEntity system = systemsService.getSystemById(systemId);
        validateServerDTO(null, dto, system);
        
        ServerEntity server = convertToServerEntity(new ServerEntity(), dto);
        server.setSystem(system);
        serversDao.save(server);
        
        return convertToServerDTO(dto, server);
    }
    
    @Override
    @Transactional(readOnly     = false, 
                   propagation  = Propagation.REQUIRED, 
                   rollbackFor  = {Exception.class})
    public ServerDTO updateServer(ServerDTO dto) {
        ServerEntity server = getServerById(dto.safeGetId());
        
        validateServerDTO(server, dto, server.getSystem());
        
        convertToServerEntity(server, dto);
        server = serversDao.merge(server);
        
        return convertToServerDTO(dto, server);
    }
    
    @Override
    @Transactional(readOnly     = true, 
                   propagation  = Propagation.SUPPORTS, 
                   rollbackFor  = {Exception.class})
    public List<ServerDTO> getAppServers(int systemId) {
        List<ServerEntity> servers = serversDao.getServersBySystem(
                systemsService.getSystemById(systemId));
        
        List<ServerDTO> list = null;
        for (ServerEntity s : servers) {
            list = CollectionsUtil.addToList(list, 
                    convertToServerDTO(new ServerDTO(), s));
        }
        
        return list;
    }
    
    @Override
    public ServerStatusResponse readServerStatus(ServerDTO dto, 
            String user, String password) {
        
        // read remote server status, using current user login/password
        RequestParams requestParams = new RequestParams();
        requestParams.setUserCredentials(user, password);

        try {
            return requestService.read(requestParams, 
                    dto.getUrl() + AdminRequests.GET_STATUS,
                    ServerStatusResponse.class);
        
        } catch (IOException x) {
            throw new OperationFailedException(
                    CommonResponses.INTERNAL_SERVER_ERROR, 
                    "Failed to read server status, serverName: " 
                        + dto.getName() + ", serverUrl: " + dto.getUrl(), 
                    x);
        }
    }
    
    @Override
    public ServerModulesResponse readServerModules(ServerDTO dto, 
            String user, String password) {
        
        // read remote server status, using current user login/password
        RequestParams requestParams = new RequestParams();
        requestParams.setUserCredentials(user, password);

        try {
            return requestService.read(requestParams, 
                    dto.getUrl() + AdminRequests.GET_MODULES,
                    ServerModulesResponse.class);
        
        } catch (IOException x) {
            throw new OperationFailedException(
                    CommonResponses.INTERNAL_SERVER_ERROR, 
                    "Failed to read server modules, serverName: " 
                        + dto.getName() + ", serverUrl: " + dto.getUrl(), 
                    x);
        }
    }
    
    @Override
    public ServerStatusResponse reloadServer(ServerDTO dto, 
            String user, String password) {
        
        // restart remote server, using current user login/password
        RequestParams requestParams = new RequestParams();
        requestParams.setUserCredentials(user, password);

        try {
            return requestService.read(requestParams, 
                    dto.getUrl() + AdminRequests.RESTART,
                    ServerStatusResponse.class);
        
        } catch (IOException x) {
            throw new OperationFailedException(
                    CommonResponses.INTERNAL_SERVER_ERROR, 
                    "Failed to restart server, serverName: " + dto.getName() 
                        + ", serverUrl: " + dto.getUrl(), 
                    x);
        }
    }
    
    private ServerEntity getServerById(long id) {
        ServerEntity server = serversDao.get(id);
        if (server == null) {
            throw new OperationFailedException(
                    CommonResponses.ENTITY_NOT_FOUND, 
                    "Server with the given id (" + id + ") not found");
        }
        
        return server;
    }
    
    private ServerDTO convertToServerDTO(ServerDTO dto, ServerEntity o) {
        dto.setId(o.getId());
        dto.setName(o.getName());
        dto.setUrl(o.getUrl());
        return dto;
    }
    
    private ServerEntity convertToServerEntity(ServerEntity o, ServerDTO dto) {
        o.setName(dto.getName());
        o.setUrl(dto.getUrl());
        return o;
    }
    
    private void validateServerDTO(ServerEntity curr, ServerDTO dto, 
            SystemEntity system) {
        
        String name = StringHelpers.trim(dto.getName());
        if (name == null || name.length() < 1 || name.length() > 32) {
            throw new ValidationFailedException(
                    AdminResponses.ADMIN_INVALID_SERVER_NAME,
                    "Server name should be from 1 to 32 symbols long");
        }
        
        String url = StringHelpers.trim(dto.getUrl());
        if (url == null || url.length() < 1 || url.length() > 128) {
            throw new ValidationFailedException(
                    AdminResponses.ADMIN_INVALID_SERVER_URL, 
                    "Server URL should be from 1 to 128 symbols long");
        }
        
        if ((curr == null || !curr.getName().equals(name)) 
                && serversDao.getByName(name, system) != null) {
            
            throw new ValidationFailedException(
                    CommonResponses.ENTITY_ALREADY_EXISTS, 
                    "Server with name (" + name + ") already exists");
        }
    }
    
}
