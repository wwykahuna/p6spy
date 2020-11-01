package com.p6spy.engine.spy;

import java.util.Map;

public interface P6LoadableOptions {

	void load(Map<String, String> options);

	Map<String, String> getDefaults();
}
