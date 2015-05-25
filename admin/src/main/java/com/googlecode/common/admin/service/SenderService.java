
package com.googlecode.common.admin.service;

import java.util.Map;


/**
 * Service for processing sender requests.
 */
public interface SenderService {

    public void sendEmail(String subject, String msgText, String toAddress,
            String ccAddress);
    
    public void sendEmailTemplate(String subject, String msgText,
            String toAddress, String ccAddress, String templateName,
            Map<String, String> templateData);

}
