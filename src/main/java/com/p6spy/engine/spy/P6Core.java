package com.p6spy.engine.spy;

import java.sql.Connection;

import com.p6spy.engine.common.ConnectionInformation;
import com.p6spy.engine.event.JdbcEventListener;
import com.p6spy.engine.wrapper.ConnectionWrapper;

/**
 * @author Quinton McCombs
 * @since 09/2013
 * @deprecated use {@link ConnectionWrapper} instead. Should be removed with
 *             next major release.
 */
public class P6Core {

	@SuppressWarnings("resource")
	public static Connection wrapConnection(Connection realConnection, ConnectionInformation connectionInformation) {
		if (realConnection == null) {
			return null;
		}
		return ConnectionWrapper.wrap(realConnection, JdbcEventListenerFactoryLoader.load().createJdbcEventListener(),
				connectionInformation);
	}

	public static JdbcEventListener getJdbcEventListener() {
		return JdbcEventListenerFactoryLoader.load().createJdbcEventListener();
	}

}