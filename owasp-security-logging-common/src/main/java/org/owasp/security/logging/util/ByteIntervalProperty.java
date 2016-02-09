package org.owasp.security.logging.util;

/**
 * Stores property key/value pairs and appends SI unit names to the value.
 * 
 * @author Milton Smith
 *
 */
public class ByteIntervalProperty extends DefaultIntervalProperty {

	
	/**
	 * Constructor
	 * @param name Property key name.  The property key name is also
	 * used when the property is logged to identify the value.
	 */
	public ByteIntervalProperty(String name) {
		super(name);
	}

	/**
	 * Return the value in bytes with SI unit name includes.  
	 * @return Value in bytes with SI unit name.
	 * @see #addUnits(String)
	 */
	public String getValue() {
		
		String results = super.getValue();
		
		try {
			Long.parseLong(super.getValue());
			results = addUnits(super.getValue());
		}catch(NumberFormatException e) {}
		
		return results;
	
	}
	
	/**
	 * Utility method to include the SI unit name.
	 * @param value The value of a Long in String form.
	 * @return Rounded value with appended SI units.  For example, 45.3MB, 62B, 27.2GB, etc.
	 */
	public String addUnits( String value ) {

		StringBuffer buff = new StringBuffer(100);
		
		long bytes = Long.parseLong(value);

		if( bytes < 1000000 ) {
			
			buff.append(value);
			buff.append("B");
			
		} else {
			
			int unit = 1000;
			int exp = (int) (Math.log(bytes) / Math.log(unit));
			String pre = "kMGTPE".charAt(exp-1)+"";
			String ov = String.format("%.1f%sB", bytes / Math.pow(unit, exp), pre);
			buff.append(ov);
			
		}

		return buff.toString();
		
	}
	
}
