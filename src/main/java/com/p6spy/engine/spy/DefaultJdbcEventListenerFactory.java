package com.p6spy.engine.spy;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import com.p6spy.engine.event.CompoundJdbcEventListener;
import com.p6spy.engine.event.DefaultEventListener;
import com.p6spy.engine.event.JdbcEventListener;

public class DefaultJdbcEventListenerFactory implements JdbcEventListenerFactory {

	private static ServiceLoader<JdbcEventListener> jdbcEventListenerServiceLoader = //
			ServiceLoader.load(JdbcEventListener.class, DefaultJdbcEventListenerFactory.class.getClassLoader());

	private static volatile JdbcEventListener jdbcEventListener;

	@Override
	public JdbcEventListener createJdbcEventListener() {
		if (jdbcEventListener == null) {
			synchronized (DefaultJdbcEventListenerFactory.class) {
				if (jdbcEventListener == null) {
					CompoundJdbcEventListener compoundEventListener = new CompoundJdbcEventListener();
					compoundEventListener.addListener(DefaultEventListener.INSTANCE);
					registerEventListenersFromFactories(compoundEventListener);
					registerEventListenersFromServiceLoader(compoundEventListener);
					jdbcEventListener = compoundEventListener;
				}
			}
		}

		return jdbcEventListener;
	}

	public static void clearCache() {
		jdbcEventListener = null;
	}

	protected void registerEventListenersFromFactories(CompoundJdbcEventListener compoundEventListener) {
		List<P6Factory> factories = P6ModuleManager.getInstance().getFactories();
		if (factories != null) {
			for (P6Factory factory : factories) {
				final JdbcEventListener eventListener = factory.getJdbcEventListener();
				if (eventListener != null) {
					compoundEventListener.addListener(eventListener);
				}
			}
		}
	}

	protected void registerEventListenersFromServiceLoader(CompoundJdbcEventListener compoundEventListener) {
		for (Iterator<JdbcEventListener> iterator = jdbcEventListenerServiceLoader.iterator(); iterator.hasNext();) {
			compoundEventListener.addListener(iterator.next());
		}
	}

}