package com.vibrant.jobmanager.medicareeligibility.manager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyManager {
	private static Map<Object, Object> propMap = new HashMap<>();
	
	public static Object getProperty(Object key) {
		if (propMap.isEmpty())
			readProperties();
		return propMap.get(key);
	}
	
	private static void readProperties() {
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream("xxx.properties");
			prop.load(input);
			for (Map.Entry<Object, Object> entry : prop.entrySet())
				propMap.put(entry.getKey(), entry.getValue());
		}
		catch (IOException ioe) {
			// TODO
		}
		finally {
			if (null != input) {
				try {
					input.close();
				}
				catch (IOException ioe) {
					// TODO
				}
			}
		}
	}
}
