package org.owasp.security.logging.util;


/**
 * Stores property key/value pairs.
 * 
 * @author Milton Smith
 * @see org.owasp.security.logging.util.IntervalProperty
 */
public class DefaultIntervalProperty implements IntervalProperty {

	private String name;
	
	protected String value = null;
	
	/**
	 * Constructor
	 * @param name Property key name.  The property key name is also
	 * used when the property is logged to identify the value.
	 */
	public DefaultIntervalProperty( String name ) {
		
		this.name = name;
		
	}
	
	/**
	 * Return the property name.  
	 * @return Name of the property.
	 */
	public String getName() {
		
		return name;
		
	}
	
	/**
	 * Return the value.  
	 * @return Value of the property.
	 */
	public String getValue() {
		
		return value;
	}
	
	/**
	 * Signal to update the property value.  Must be implemented if the
	 * property value changes at runtime.  For example, to develop a property
	 * to update system memory consider overriding with the following,
	 * <code>
	 * public void refresh() {
	 *   value = Long.toString(Runtime.getRuntime().totalMemory());
	 * }
	 * </code>
	 * Specific refresh behavior depends upon the type of property.  As general rule
	 * if your data does not change then you should consider logging the data
	 * outside the status message.
	 */
	public void refresh() {
		
	}
	
}
