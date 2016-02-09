package org.owasp.security.logging.util;

/**
 * The IntervalLoggerModel framework for managing property information.
 * 
 * @author Milton Smith
 * @see org.owasp.security.logging.util.DefaultIntervalLoggerModel
 */
public interface IntervalLoggerModel {

	/**
	 * Add a new property to be included when printing the status message.
	 * @param action Property to add
	 */
	public void addProperty(IntervalProperty action);
	
	/**
	 * Remove property from status messages.
	 * @param action Property to remove
	 */
	public void removeProperty(IntervalProperty action);
	
	/**
	 * Return all properties.
	 * @return Array of all properties available for printing in status message.
	 */
	public IntervalProperty[] getProperties();
	
	/**
	 * Signal properties to update themselves.
	 */
	public void refresh();
	
}
