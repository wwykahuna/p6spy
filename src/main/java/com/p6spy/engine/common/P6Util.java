package com.p6spy.engine.common;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Pattern;

public class P6Util {
	static Pattern lineBreakPattern = Pattern.compile("(\\r?\\n)+");

	public static String singleLine(String str) {
		return lineBreakPattern.matcher(str).replaceAll(" ");
	}

	private P6Util() {
		// preventing instantiation of the util class
	}

	public static int parseInt(String i, int defaultValue) {
		if (i == null || i.isEmpty()) {
			return defaultValue;
		}
		try {
			return (Integer.parseInt(i));
		} catch (NumberFormatException nfe) {
			P6LogQuery.error("NumberFormatException occured parsing value " + i);
			return defaultValue;
		}
	}

	public static boolean isTrue(String s, boolean defaultValue) {
		if (s == null) {
			return defaultValue;
		}
		return ("1".equals(s) || "true".equalsIgnoreCase(s.trim()));
	}

	public static URL locateFile(String file) {
		File fp;
		URL result = null;

		try {
			// try to find relative to current working directory first
			fp = new File(file);
			if (fp.exists()) {
				result = fp.toURI().toURL();
			}

			// next try to load from context class loader
			if (result == null) {
				result = locateOnClassPath(file);
			}
		} catch (Exception e) {
		}
		return result;
	}

	public static URL locateOnClassPath(String filename) {
		URL result = null;
		// first try to load from context class loader
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		if (contextClassLoader != null) {
			result = contextClassLoader.getResource(filename);
		}

		// next try the current class loader which loaded p6spy
		if (result == null) {
			result = P6Util.class.getClassLoader().getResource(filename);
		}

		// finally try the system class loader
		if (result == null) {
			result = ClassLoader.getSystemResource(filename);
		}

		return result;
	}

	public static Class<?> forName(String name) throws ClassNotFoundException {
		ClassLoader ctxLoader = null;
		try {
			ctxLoader = Thread.currentThread().getContextClassLoader();
			return Class.forName(name, true, ctxLoader);

		} catch (ClassNotFoundException ex) {
			// try to fall through and use the default
			// Class.forName
			// if(ctxLoader == null) { throw ex; }
		} catch (SecurityException ex) {
		}
		return Class.forName(name);
	}

	public static String getPath(URL theURL) {
		String file = theURL.getFile();
		String path = null;
		if (file != null) {
			int q = file.lastIndexOf('?');
			if (q != -1) {
				path = file.substring(0, q);
			} else {
				path = file;
			}
		}
		return path;
	}

	public static Map<String, String> getPropertiesMap(final Properties properties) {
		if (null == properties) {
			return null;
		}

		final Map<String, String> map = new HashMap<String, String>();
		for (Entry<Object, Object> entry : properties.entrySet()) {
			map.put((String) entry.getKey(), (String) entry.getValue());
		}
		return map;
	}

	public static List<String> parseCSVList(String csv) {
		if (csv == null || csv.isEmpty()) {
			return Collections.emptyList();
		}

		return new ArrayList<String>(Arrays.asList(csv.split(",")));
	}

	public static Properties getProperties(Map<String, String> map) {
		if (map == null) {
			return null;
		}

		final Properties properties = new Properties();
		properties.putAll(map);
		return properties;
	}

	public static String joinNullSafe(Collection<String> collection, String separator) {
		if (null == collection || collection.isEmpty()) {
			return "";
		}

		if (null == separator) {
			separator = "";
		}

		final StringBuilder sb = new StringBuilder();
		for (String str : collection) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			sb.append(str);
		}
		return sb.toString();
	}

}
