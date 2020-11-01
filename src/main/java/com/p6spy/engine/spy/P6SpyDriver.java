package com.p6spy.engine.spy;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import com.p6spy.engine.common.ConnectionInformation;
import com.p6spy.engine.common.P6LogQuery;
import com.p6spy.engine.event.JdbcEventListener;
import com.p6spy.engine.wrapper.ConnectionWrapper;

/**
 * JDBC driver for P6Spy
 */
public class P6SpyDriver implements Driver {
	private static Driver instance = new P6SpyDriver();
	private static JdbcEventListenerFactory jdbcEventListenerFactory;

	static {
		try {
			DriverManager.registerDriver(P6SpyDriver.instance);
		} catch (SQLException e) {
			throw new IllegalStateException("Could not register P6SpyDriver with DriverManager", e);
		}
	}

	@Override
	public boolean acceptsURL(final String url) {
		System.out.println("P6SpyDriver.acceptsURL: url="+url);
		return url != null && url.startsWith("jdbc:p6spy:");
	}

	private String extractRealUrl(String url) {
		return acceptsURL(url) ? url.replace("p6spy:", "") : url;
	}

	static List<Driver> registeredDrivers() {
		List<Driver> result = new ArrayList<Driver>();
		for (Enumeration<Driver> driverEnumeration = DriverManager.getDrivers(); driverEnumeration.hasMoreElements();) {
			result.add(driverEnumeration.nextElement());
		}
		return result;
	}

	@Override
	public Connection connect(String url, Properties properties) throws SQLException {
		System.out.println("P6SpyDriver.connect: url="+url+",properties="+properties);
		if (url == null) {
			throw new SQLException("url is required");
		}
		if (!acceptsURL(url)) {
			return null;
		}
		
		System.out.println("P6SpyDriver.connect: findPassthru()-B ");
		Driver passThru = findPassthru(url);
		System.out.println("P6SpyDriver.connect: findPassthru()-E ");
		
		P6LogQuery.debug("this is " + this + " and passthru is " + passThru);
		final long start = System.nanoTime();
		if (P6SpyDriver.jdbcEventListenerFactory == null) {
			P6SpyDriver.jdbcEventListenerFactory = JdbcEventListenerFactoryLoader.load();
		}
		final Connection conn;
		final JdbcEventListener jdbcEventListener = P6SpyDriver.jdbcEventListenerFactory.createJdbcEventListener();
		final ConnectionInformation connectionInformation = ConnectionInformation.fromDriver(passThru);
		connectionInformation.setUrl(url);
		jdbcEventListener.onBeforeGetConnection(connectionInformation);
		try {
			conn = passThru.connect(extractRealUrl(url), properties);
			connectionInformation.setConnection(conn);
			connectionInformation.setTimeToGetConnectionNs(System.nanoTime() - start);
			jdbcEventListener.onAfterGetConnection(connectionInformation, null);
		} catch (SQLException e) {
			connectionInformation.setTimeToGetConnectionNs(System.nanoTime() - start);
			jdbcEventListener.onAfterGetConnection(connectionInformation, e);
			throw e;
		}
		return ConnectionWrapper.wrap(conn, jdbcEventListener, connectionInformation);
	}

	protected Driver findPassthru(String url) throws SQLException {
		System.out.println("P6SpyDriver.findPassthru: url= "+url);
		
		System.out.println("P6SpyDriver.findPassthru: P6ModuleManager.getInstance()-B");
		P6ModuleManager.getInstance();
		System.out.println("P6SpyDriver.findPassthru: P6ModuleManager.getInstance()-E");
		
		String realUrl = extractRealUrl(url);
		Driver passthru = null;
		for (Driver driver : registeredDrivers()) {
			try {
				if (driver.acceptsURL(extractRealUrl(url))) {
					passthru = driver;
					break;
				}
			} catch (SQLException e) {
			}
		}
		if (passthru == null) {
			throw new SQLException("Unable to find a driver that accepts " + realUrl);
		}
		return passthru;
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties properties) throws SQLException {
		return findPassthru(url).getPropertyInfo(url, properties);
	}

	@Override
	public int getMajorVersion() {
		return 2;
	}

	@Override
	public int getMinorVersion() {
		System.out.println("~~getMinorVersion");
		return 0;
	}

	@Override
	public boolean jdbcCompliant() {
		System.out.println("~~jdbcCompliant");
		return true;
	}

	// Note: @Override annotation not added to allow compilation using Java 1.6
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException("Feature not supported");
	}

	public static void setJdbcEventListenerFactory(JdbcEventListenerFactory jdbcEventListenerFactory) {
		System.out.println("~~setJdbcEventListenerFactory");
		P6SpyDriver.jdbcEventListenerFactory = jdbcEventListenerFactory;
	}
}