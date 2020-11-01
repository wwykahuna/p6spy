package com.p6spy.engine.spy.option;

import com.p6spy.engine.common.P6Util;
import com.p6spy.engine.spy.P6ModuleManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;

public class SpyDotProperties implements P6OptionsSource {

	public static final String OPTIONS_FILE_PROPERTY = "spy.properties";
	public static final String OPTIONS_FILE_CHARSET_PROPERTY = OPTIONS_FILE_PROPERTY.concat(".charset");
	public static final String DEFAULT_OPTIONS_FILE = OPTIONS_FILE_PROPERTY;

	private final long lastModified;

	private SpyDotPropertiesReloader reloader;

	private final Map<String, String> options;

	public SpyDotProperties() throws IOException {
		URL url = locate();
		System.out.println("SpyDotProperties.SpyDotProperties :url = "+url);
		if (null == url) {
			lastModified = -1;
			options = null;
			return;
		}

		lastModified = lastModified();
		InputStream in = null;
		try {
			in = url.openStream();
			final Properties properties = new Properties();
			String charsetName = System.getProperty(OPTIONS_FILE_CHARSET_PROPERTY, Charset.defaultCharset().name());
			properties.load(new InputStreamReader(in, charsetName));
			options = P6Util.getPropertiesMap(properties);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * Determines if the file has been modified since it was loaded
	 *
	 * @return true if modified, false otherwise
	 */
	public boolean isModified() {
		return lastModified() != lastModified;
	}

	private long lastModified() {
		long lastMod = -1;
		URLConnection con = null;
		URL url = locate();
		if (url != null) {
			try {
				con = url.openConnection();
				lastMod = con.getLastModified();
			} catch (IOException e) {
				// ignore
			} finally {
				if (con != null) {
					// getLastModified opens an input stream if it is a file
					// the inputStream must be closed manually!
					InputStream in = null;
					try {
						in = con.getInputStream();
					} catch (IOException e) {
					}
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
						}
					}

				}
			}
		}
		return lastMod;
	}

	private URL locate() {
		String propsFileName = System.getProperty(OPTIONS_FILE_PROPERTY, DEFAULT_OPTIONS_FILE);
		if (null == propsFileName || propsFileName.isEmpty()) {
			propsFileName = DEFAULT_OPTIONS_FILE;
		}
		return P6Util.locateFile(propsFileName);
	}

	@Override
	public Map<String, String> getOptions() {
		return options;
	}

	@Override
	public void preDestroy(P6ModuleManager p6moduleManager) {
		if (reloader != null) {
			reloader.kill(p6moduleManager);
		}
	}

	@Override
	public void postInit(P6ModuleManager p6moduleManager) {
		reloader = new SpyDotPropertiesReloader(this, p6moduleManager);
	}
}