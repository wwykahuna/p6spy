package com.p6spy.engine.spy;

import com.p6spy.engine.common.P6LogQuery;
import com.p6spy.engine.common.P6Util;
import com.p6spy.engine.spy.option.EnvironmentVariables;
import com.p6spy.engine.spy.option.P6OptionChangedListener;
import com.p6spy.engine.spy.option.P6OptionsRepository;
import com.p6spy.engine.spy.option.P6OptionsSource;
import com.p6spy.engine.spy.option.SpyDotProperties;
import com.p6spy.engine.spy.option.SystemProperties;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;

public class P6ModuleManager {

	private final P6OptionsSource[] optionsSources = new P6OptionsSource[] { new SpyDotProperties(),
			new EnvironmentVariables(), new SystemProperties() };
	private final Map<Class<? extends P6LoadableOptions>, P6LoadableOptions> allOptions = new HashMap<Class<? extends P6LoadableOptions>, P6LoadableOptions>();
	private final List<P6Factory> factories = new CopyOnWriteArrayList<P6Factory>();
	private final P6MBeansRegistry mBeansRegistry = new P6MBeansRegistry();

	private final P6OptionsRepository optionsRepository = new P6OptionsRepository();

	private static P6ModuleManager instance;

	static {
		System.out.println("P6ModuleManager.static : initMe() -B");
		initMe();
		System.out.println("P6ModuleManager.static : initMe() -E");
	}

	private static synchronized void initMe() {
		try {
			System.out.println("P6ModuleManager.initMe : cleanUp() -B");
			cleanUp();
			System.out.println("P6ModuleManager.initMe : cleanUp() -E");
			
			System.out.println("P6ModuleManager.initMe : new P6ModuleManager()-B");
			instance = new P6ModuleManager();
			System.out.println("P6ModuleManager.initMe : new P6ModuleManager()-E");

			System.out.println("P6ModuleManager.initMe : P6LogQuery.initialize()-B");
			P6LogQuery.initialize();
			System.out.println("P6ModuleManager.initMe : P6LogQuery.initialize()-E");
		} catch (IOException e) {
			handleInitEx(e);
		} catch (MBeanRegistrationException e) {
			handleInitEx(e);
		} catch (InstanceNotFoundException e) {
			handleInitEx(e);
		} catch (MalformedObjectNameException e) {
			handleInitEx(e);
		} catch (NotCompliantMBeanException e) {
			handleInitEx(e);
		}
	}

	private static void cleanUp()
			throws MBeanRegistrationException, InstanceNotFoundException, MalformedObjectNameException {
		System.out.println("P6ModuleManager.cleanUp :instance="+instance);
		if (instance == null) {
			return;
		}

		for (P6OptionsSource optionsSource : instance.optionsSources) {
			optionsSource.preDestroy(instance);
		}

		if (P6SpyOptions.getActiveInstance().getJmx() && instance.mBeansRegistry != null) {
			instance.mBeansRegistry.unregisterAllMBeans(P6SpyOptions.getActiveInstance().getJmxPrefix());
		}
		new DefaultJdbcEventListenerFactory().clearCache();
	}

	private P6ModuleManager() throws IOException, MBeanRegistrationException, NotCompliantMBeanException,
			MalformedObjectNameException, InstanceNotFoundException {
		debug(this.getClass().getName() + " re/initiating modules started");
		
		System.out.println("P6ModuleManager.P6ModuleManager :registerOptionChangedListener-B");
		registerOptionChangedListener(new P6LogQuery());
		System.out.println("P6ModuleManager.P6ModuleManager :registerOptionChangedListener-E");
		System.out.println();

		System.out.println("P6ModuleManager.P6ModuleManager :registerModule-B");
		final P6SpyLoadableOptions spyOptions = (P6SpyLoadableOptions) registerModule(new P6SpyFactory());
		System.out.println("P6ModuleManager.P6ModuleManager :registerModule-E");
		loadDriversExplicitly(spyOptions);

		final Set<P6Factory> moduleFactories = spyOptions.getModuleFactories();
		System.out.println("P6ModuleManager.P6ModuleManager :moduleFactories = "+moduleFactories);
		if (null != moduleFactories) {
			for (P6Factory factory : spyOptions.getModuleFactories()) {
				registerModule(factory);
			}
		}
		
		System.out.println("P6ModuleManager.P6ModuleManager : optionsRepository.initCompleted() - B");
		optionsRepository.initCompleted();
		System.out.println("P6ModuleManager.P6ModuleManager : optionsRepository.initCompleted() - E");
		
		mBeansRegistry.registerMBeans(allOptions.values());
		
		System.out.println("P6ModuleManager.P6ModuleManager :allOptions = "+allOptions.size());
		for (P6OptionsSource optionsSource : optionsSources) {
			System.out.println("P6ModuleManager.P6ModuleManager :optionsSource = "+optionsSource);
			optionsSource.postInit(this);
		}
		debug(this.getClass().getName() + " re/initiating modules done");
		System.out.println();
	}

	protected synchronized P6LoadableOptions registerModule(P6Factory factory) {
		for (P6Factory registeredFactory : factories) {
			if (registeredFactory.getClass().equals(factory.getClass())) {
				return null;
			}
		}
		
		System.out.println("P6ModuleManager.P6ModuleManager :factory = "+factory);
		System.out.println("P6ModuleManager.P6ModuleManager :optionsRepository = "+optionsRepository);
		final P6LoadableOptions options = factory.getOptions(optionsRepository);
		System.out.println("P6ModuleManager.P6ModuleManager :options = "+options);
		
		System.out.println("P6ModuleManager.P6ModuleManager :loadOptions-B ");
		loadOptions(options);
		System.out.println("P6ModuleManager.P6ModuleManager :loadOptions-E ");
		factories.add(factory);
		debug("Registered factory: " + factory.getClass().getName() + " with options: " + options.getClass().getName());
		return options;
	}

	private void loadOptions(final P6LoadableOptions options) {
		System.out.println("P6ModuleManager.loadOptions :options = "+options);
		options.load(options.getDefaults());
		System.out.println("P6ModuleManager.loadOptions :optionsSources = "+optionsSources);
		for (P6OptionsSource optionsSource : optionsSources) {
			Map<String, String> toLoad = optionsSource.getOptions();
			if (null != toLoad) {
				options.load(toLoad);
			}
		}
		System.out.println("P6ModuleManager.loadOptions :allOptions = "+allOptions);
		allOptions.put(options.getClass(), options);
	}

	public static P6ModuleManager getInstance() {
		System.out.println("P6ModuleManager.getInstance : instance = "+instance);
		return instance;
	}

	private static void handleInitEx(Exception e) {
		e.printStackTrace(System.err);
	}

	private void loadDriversExplicitly(P6SpyLoadableOptions spyOptions) {
		final Collection<String> driverNames = spyOptions.getDriverNames();
		System.out.println("P6ModuleManager.loadDriversExplicitly :driverNames = "+driverNames);
		if (null != driverNames) {
			for (String driverName : driverNames) {
				try {
					P6Util.forName(driverName).newInstance();
				} catch (Exception e) {
					String err = "Error registering driver names: " + driverNames + " \nCaused By: " + e.toString();
					P6LogQuery.error(err);
					throw new P6DriverNotFoundError(err);
				}
			}
		}
	}

	private void debug(String msg) {
		if (instance == null || factories.isEmpty()) {
			return;
		}
		P6LogQuery.debug(msg);
	}

	@SuppressWarnings("unchecked")
	public <T extends P6LoadableOptions> T getOptions(Class<T> optionsClass) {
		return (T) allOptions.get(optionsClass);
	}

	public void reload() {
		initMe();
	}

	public List<P6Factory> getFactories() {
		return factories;
	}

	public void registerOptionChangedListener(P6OptionChangedListener listener) {
		System.out.println("P6ModuleManager.registerOptionChangedListener :optionsRepository.registerOptionChangedListener-B");
		System.out.println("P6ModuleManager.registerOptionChangedListener :optionsRepository = "+optionsRepository);
		System.out.println("P6ModuleManager.registerOptionChangedListener :listener = "+listener);
		optionsRepository.registerOptionChangedListener(listener);
		System.out.println("P6ModuleManager.registerOptionChangedListener :optionsRepository.registerOptionChangedListener-E");
	}

	public void unregisterOptionChangedListener(P6OptionChangedListener listener) {
		optionsRepository.unregisterOptionChangedListener(listener);
	}
}
