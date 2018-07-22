package com.fdu201.jobmanager.manager;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyManager {
	private static Map<Object, Object> propMap = new HashMap<>();
	private final static Logger logger = Logger.getLogger(PropertyManager.class);
	
	public static Object getProperty(Object key) {
		if (propMap.isEmpty())
			readProperties();
		return propMap.get(key);
	}
	
	private static void readProperties() {
	    logger.info("Start reading properties.");
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream("/etc/opt/jobmanager/config.properties");
			prop.load(input);
			for (Map.Entry<Object, Object> entry : prop.entrySet())
				propMap.put(entry.getKey(), entry.getValue());
		}
		catch (IOException ioe) {
			logger.error("Failed to read properties.", ioe);
		}
		finally {
			if (null != input) {
				try {
					input.close();
				}
				catch (IOException ioe) {
					logger.error("Failed to close property file.", ioe);
				}
			}
            logger.info("Finish reading properties.");
		}
	}
}
