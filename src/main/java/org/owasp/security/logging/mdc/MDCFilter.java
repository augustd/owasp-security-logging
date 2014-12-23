package org.owasp.security.logging.mdc;

import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.MDC;

/**
 * J2EE filter to add request information to the logging context. Adding data to
 * the MDC is accomplished through implementations of the IPlugin interface. 
 * 
 * This filter adds the following information to the MDC: 
 * 
 * <li>%X{ipAddress} - The remote IP address of the request (using IPAddressPlugin)
 * <li>%X{session} - A hash of the J2EE session ID (using SessionPlugin)
 * <li>%X{productName} - A product name identifier (specified in web.xml)
 * <li>%X{hostname} - The server hostname (from HttpServletRequest)
 * <li>%X{locale} - The preferred Locale of the client (from HttpServletRequest.getLocale())
 * 
 * @author August Detlefsen <augustd@codemagi.com>
 * @see IPlugin
 */
public class MDCFilter implements Filter {

    public static final String IPADDRESS = "ipAddress";
    public static final String LOGIN_ID = "username";

    private FilterConfig filterConfig;
    private String productName;
    
    private static final Map<String,IPlugin> plugins = new LinkedHashMap();
    static {
        //set some defaults 
        plugins.put("ipAddress", new IPAddressPlugin());
        plugins.put("session", new SessionPlugin());
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        
        //process plugins in filter config
        Enumeration e = filterConfig.getInitParameterNames();
        while (e.hasMoreElements()) {
            String pluginName = (String)e.nextElement();
            if ("ProductName".equals(pluginName)) {
                productName = filterConfig.getInitParameter("ProductName");
            } else {
                //this is a plugin 
                try {
                    IPlugin plugin = (IPlugin)Class.forName(filterConfig.getInitParameter(pluginName)).newInstance();
                    plugin.init(filterConfig);
                    plugins.put(pluginName, plugin);
                } catch (Exception cnfe) {
                    //ClassNotFoundException, InstantiationException, IllegalAccessException
                    cnfe.printStackTrace();
                }
            }
        }
    }

    /**
     * Sample filter that populates the MDC on every request.
     */
    public void doFilter(ServletRequest servletRequest,
            ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        //put values into MDC
        MDC.put("hostname", servletRequest.getServerName());
        
        if (productName != null) {
            MDC.put("productName", productName);
        }
        
        MDC.put("locale", servletRequest.getLocale().getDisplayName());
        
        //process plugins 
        for (IPlugin plugin : plugins.values()) {
            plugin.execute(request);
        }
        
        //forward to the chain for processing
        filterChain.doFilter(servletRequest, servletResponse);
        MDC.clear();
    }

    public void destroy() {
    }
}
