package org.owasp.security.logging.util;

/**
 * Framework for storing property data.
 * @author Milton Smith
 * @see org.owasp.security.logging.util.DefaultIntervalProperty
 * @see org.owasp.security.logging.util.ByteIntervalProperty
 */
public interface IntervalProperty {
	
	/**
	 * Return the property name.  
	 * @return Name of the property.
	 */
	public String getName();
	
	/**
	 * Return the value.  
	 * @return Value of the property.
	 */
	public String getValue();
	
	/**
	 * Signal to update the property value.  Must be implemented if the
	 * property value changes at runtime.  
	 * @see org.owasp.security.logging.util.DefaultIntervalProperty#refresh()
	 */
	public void refresh();
	
}
