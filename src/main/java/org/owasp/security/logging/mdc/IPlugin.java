package org.owasp.security.logging.mdc;

import javax.servlet.http.HttpServletRequest;

/**
 * This interface defines a plugin to the MDC filter. Applications can implement 
 * this interface to add functionality to the MDCFilter. For example, an implementing
 * class might provide custom behavior for determining an authenticated username. 
 * 
 * @author August Detlefsen <augustd@codemagi.com>
 */
public interface IPlugin {
    public void execute(HttpServletRequest request);
}
