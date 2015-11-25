package com.example.util;

import java.util.ResourceBundle;

public class ResourceHelper {

	public static String getConfig(String propertyKey) {
		return ResourceBundle.getBundle("cfg_general").getString(propertyKey);
	}
}
