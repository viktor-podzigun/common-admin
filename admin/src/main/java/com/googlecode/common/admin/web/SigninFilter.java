
package com.googlecode.common.admin.web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.googlecode.common.service.AdminService;
import com.googlecode.common.service.CommonResponses;
import com.googlecode.common.service.ex.OperationFailedException;
import com.googlecode.common.web.ServletHelpers;
import com.googlecode.common.admin.service.AuthorizationService;


/**
 * Servlet Filter used to redirect to signin page.
 */
public final class SigninFilter implements Filter {

    private final Logger    log = LoggerFactory.getLogger(getClass());
    
    private AuthorizationService    authService;
    private AdminService            adminService;
    
    
    public SigninFilter() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        try {
            ServletContext context = config.getServletContext();
            WebApplicationContext applicationContext = 
                WebApplicationContextUtils.getWebApplicationContext(context);
        
            authService = applicationContext.getBean(
                    AuthorizationService.class);
            adminService = applicationContext.getBean(AdminService.class);
        
        } catch (Exception x) {
            log.error("init", x);
            
            throw new RuntimeException(x);
        }
    }
    
    @Override
	public void doFilter(ServletRequest request, ServletResponse response, 
	        FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest)request;
        
        // redirect only if in the same domain
        if (req.getServerName().endsWith(ServletHelpers.getAppDomain())) {
            try {
                // try to authenticate user by token
                authService.authUserToken(req);
                
            } catch (OperationFailedException x) {
                if (x.getResponseMessage() 
                        != CommonResponses.AUTHENTICATION_FAILED) {
                    
                    throw x;
                }
                
                // if authentication failed, then redirect to login page
                StringBuilder targetUrl = new StringBuilder(
                        ServletHelpers.getRequestUrl(req));
                
                String query = req.getQueryString();
                if (query != null) {
                    targetUrl.append('?').append(query);
                }
                
                ((HttpServletResponse)response).sendRedirect(
                        adminService.getLoginRedirectUrl(req, 
                                targetUrl.toString()));
                return;
            }
        }
        
        chain.doFilter(request, response);
    }

}
