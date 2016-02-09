package org.owasp.security.logging.util;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import org.junit.Test;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.management.UnixOperatingSystemMXBean;

public class ExtendedIntervalLoggingTest {

	private static final Logger logger = LoggerFactory.getLogger(ExtendedIntervalLoggingTest.class);
	
	@Test
	public void doExtendedTest() {
		
		logger.info("extended test started");
		
		IntervalLoggerController wd = SecurityLoggingFactory.getControllerInstance();
		
		// Add support for security markers
		wd.setStatusMessageView( new DefaultIntervalLoggerView() {
			@Override
			public void logMessage( String message ) {
				logger.info( SecurityMarkers.RESTRICTED, message );
			}
		});
		
		// Add a new property to the list of current properties
		DefaultIntervalLoggerModel model = new DefaultIntervalLoggerModel();
		model.addProperty( new DefaultIntervalProperty("OpenFiles") {
			public void refresh() {
				// restricted class, display open file handle count on *nix if we can.
				OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
		        if(os instanceof UnixOperatingSystemMXBean)
		        	value = Long.toString(((UnixOperatingSystemMXBean) os).getOpenFileDescriptorCount());
			}
		}
		);
		
		// Remove default property from middle of the list like ThreadNew
		IntervalProperty[] properties = model.getProperties();
		for ( IntervalProperty i : properties ) {
			if( i.getName().equals("ThreadNew") )
				model.removeProperty(i);
		}
		
		wd.setStatusMessageModel(model);
		
		// Update interval to every 3 seconds
		wd.start(3000);
		
		// wait around.
		long exit_time = System.currentTimeMillis() + (1000*30);
		
		while( exit_time > System.currentTimeMillis() ) {
			
			Thread.yield();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		wd.stop();
		
		logger.info( "extended test finished");
		
		
	}
	
}
