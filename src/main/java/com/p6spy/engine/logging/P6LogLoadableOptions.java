package com.p6spy.engine.logging;

import java.util.regex.Pattern;

import com.p6spy.engine.spy.P6LoadableOptions;

public interface P6LogLoadableOptions extends P6LoadableOptions, P6LogOptionsMBean {

	void setExcludebinary(String excludebinary);

	void setFilter(String filter);

	void setExecutionThreshold(String executionThreshold);

	Pattern getIncludeExcludePattern();

	Pattern getSQLExpressionPattern();
}
