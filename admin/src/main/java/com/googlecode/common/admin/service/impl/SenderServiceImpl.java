
package com.googlecode.common.admin.service.impl;

import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.googlecode.common.http.RequestParams;
import com.googlecode.common.service.JsonRequestService;
import com.googlecode.common.service.SettingsService;
import com.googlecode.common.service.impl.AbstractManageableService;
import com.googlecode.common.admin.service.SenderService;


/**
 * Default implementation for SenderService interface.
 * 
 * @see SenderService
 */
@Service
@Singleton
public final class SenderServiceImpl extends AbstractManageableService 
        implements SenderService {

    private final Logger            log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private SettingsService         settingsService;
    
    @Autowired
    private JsonRequestService      requestClient;
    
    private volatile RequestParams  requestParams;
    
    
    @PostConstruct
    @Override
    public void init() {
        super.init();
        
//        ConfigSettings settings = settingsService.getServerSettings();
//        
//        // get settings
//        ConfigSettings connectionsSettings = settings.getNode(
//                ConnectionsConfig.NODE.getName());
//        
//        // init params
//        URI senderServerUrl = connectionsSettings.getUri(
//                ConnectionsConfig.SENDER_SERVER_URL);
//        log.info("senderServerUrl: " + UriHelpers.hidePassword(senderServerUrl));
//        
//        // parse request params
//        requestParams = new RequestParams(senderServerUrl);
    }
    
    @Override
    public void sendEmail(String subject, String msgText, String toAddress,
            String ccAddress) {
        
        sendEmail(subject, msgText, toAddress, ccAddress, null, null);
    }

    @Override
    public void sendEmailTemplate(String subject, String msgText,
            String toAddress, String ccAddress, String templateName,
            Map<String, String> templateData) {

        sendEmail(subject, msgText, toAddress, ccAddress, templateName,
                templateData);
    }

    private void sendEmail(String subject, String msgText,
            String toAddress, String ccAddress, String templateName,
            Map<String, String> templateData) {

//        EmailMessageDTO dto = new EmailMessageDTO();
//        dto.setToAddress(toAddress);
//        dto.setCcAddress(ccAddress);
//        dto.setSubject(subject);
//        dto.setText(msgText);
//
//        try {
//            SubmitResponse resp = null;
//            if (templateName != null) {
//                dto.setTemplateName(templateName);
//                dto.setTemplateData(templateData);
//
//                // perform submit Email by template request
//                resp = requestClient.create(requestParams,
//                        EmailRequests.SUBMIT_BY_TEMPLATE, dto,
//                        SubmitResponse.class);
//            } else {
//                // perform submit Email by template request
//                resp = requestClient.create(requestParams,
//                        EmailRequests.SUBMIT, dto,
//                        SubmitResponse.class);
//            }
//            
//            if (resp.getStatus() == BaseResponse.OK_STATUS) {
//                SubmitRespDTO respData = resp.getData();
//                if (log.isInfoEnabled()) {
//                    log.info("Submit Email: toAddress: " + toAddress
//                            + ", ccAddress: " + ccAddress
//                            + ", subject: " + subject
//                            + ", messageId: " + respData.getMessageId());
//                }
//                return;
//            }
//            
//            log.error("Failed submit Email: toAddress: " + toAddress
//                    + ", ccAddress: " + ccAddress + ", subject: " + subject
//                    + ", response: " + resp);
//        
//        } catch (IOException x) {
//            log.error("Error submit Email: toAddress: " + toAddress
//                    + ", ccAddress: " + ccAddress + ", subject: " + subject
//                    + ", error: " + x);
//        }
    }
    
}
