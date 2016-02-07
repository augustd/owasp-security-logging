package org.owasp.security.logging.mdc;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;

/**
 * This interface defines a plugin to the MDC filter. Applications can implement
 * this interface to add functionality to the MDCFilter. For example, an
 * implementing class might provide custom behavior for determining an
 * authenticated username.
 * 
 * @author August Detlefsen [augustd@codemagi.com]
 */
public interface IPlugin {

	/**
	 * Initialize the plugin and load any required resources.
	 * 
	 * @param config
	 *            The filter configuration to initialize the plugin
	 */
	public void init(FilterConfig config);

	/**
	 * Execute the plugin's action and place information into the diagnostic
	 * context.
	 * 
	 * @param request
	 *            The HTTP request to execute this plugin for
	 */
	public void execute(HttpServletRequest request);
}
