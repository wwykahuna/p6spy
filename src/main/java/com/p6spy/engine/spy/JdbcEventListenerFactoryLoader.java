package com.p6spy.engine.spy;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Loads the {@link JdbcEventListenerFactory} from a {@link ServiceLoader} or
 * gets the {@link DefaultJdbcEventListenerFactory}.
 *
 * @since 3.7.0
 */
class JdbcEventListenerFactoryLoader {

	private static final JdbcEventListenerFactory jdbcEventListenerFactory;

	static {
		final Iterator<JdbcEventListenerFactory> iterator = ServiceLoader
				.load(JdbcEventListenerFactory.class, JdbcEventListenerFactory.class.getClassLoader()).iterator();
		if (iterator.hasNext()) {
			jdbcEventListenerFactory = iterator.next();
		} else {
			jdbcEventListenerFactory = new DefaultJdbcEventListenerFactory();
		}
	}

	private JdbcEventListenerFactoryLoader() {
	}

	static JdbcEventListenerFactory load() {
		return jdbcEventListenerFactory;
	}

}