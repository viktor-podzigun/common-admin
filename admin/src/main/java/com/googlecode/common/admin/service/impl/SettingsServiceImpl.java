
package com.googlecode.common.admin.service.impl;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.googlecode.common.service.SettingsService;
import com.googlecode.common.admin.service.SettingsManagementService;


/**
 * Default implementation for {@link SettingsService} interface.
 */
@Service
@Singleton
public class SettingsServiceImpl implements SettingsService, 
        SettingsManagementService {

    private final Logger    log = LoggerFactory.getLogger(getClass());
//    
//    @Autowired
//    private SystemService   systemsService;
//    
//    @Autowired
//    private ApplicationContext  appContext;

    
    @PostConstruct
    public void init() {
//        Properties properties = appContext.getBean("configurationProperties", 
//                Properties.class);
//        serverName = properties.getProperty("app.server.name");
//        log.info("ServerProperties:" 
//                + "\n\tapp.server.name: " + serverName);
        
        log.info("Services initialization completed");
    }

    @Override
    public void reloadSettings() throws IOException {
        log.info("Services initialization completed");
    }

}
