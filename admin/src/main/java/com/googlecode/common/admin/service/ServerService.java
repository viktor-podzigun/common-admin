
package com.googlecode.common.admin.service;

import java.util.List;
import com.googlecode.common.protocol.admin.ServerModulesResponse;
import com.googlecode.common.protocol.admin.ServerStatusResponse;
import com.googlecode.common.admin.protocol.server.ServerDTO;


/**
 * Contains server management functionality.
 */
public interface ServerService {

    /**
     * Returns server info by the given id.
     * 
     * @param serverId  server's id
     * @return          server info
     */
    public ServerDTO getServer(long serverId);

    /**
     * Creates new server with the given info.
     * 
     * @param systemId  system's id
     * @param dto       server info
     * @return          created server info
     */
    public ServerDTO createServer(int systemId, ServerDTO dto);
    
    /**
     * Updates server with the given info.
     * 
     * @param dto   server info
     * @return      updated server info
     */
    public ServerDTO updateServer(ServerDTO dto);
    
    /**
     * Returns system's application servers list.
     * 
     * @param path  system's path
     * @return system's application servers list
     */
    public List<ServerDTO> getAppServers(int systemId);
    
    public ServerModulesResponse readServerModules(ServerDTO dto, 
            String user, String password);

    public ServerStatusResponse readServerStatus(ServerDTO dto, 
            String user, String password);

    public ServerStatusResponse reloadServer(ServerDTO dto, 
            String user, String password);
    
}
