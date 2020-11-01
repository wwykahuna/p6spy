package com.p6spy.engine.spy.appender;

import com.p6spy.engine.logging.Category;

public interface P6Logger {

	public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared, String sql,
			String url);

	public void logException(Exception e);

	public void logText(String text);

	public boolean isCategoryEnabled(Category category);
}