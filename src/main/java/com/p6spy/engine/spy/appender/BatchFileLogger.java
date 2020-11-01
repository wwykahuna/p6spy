package com.p6spy.engine.spy.appender;

import com.p6spy.engine.logging.Category;

public class BatchFileLogger extends FileLogger {
	public static final char BATCH_SEPARATOR = ';';
	private boolean endOfStatement = true;

	@Override
	public void logException(Exception e) {
	}

	@Override
	public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared, String sql,
			String url) {
		if (endOfStatement) {
			getStream().println(BATCH_SEPARATOR);
		}
		if (Category.STATEMENT.equals(category)) {
			String actual = null == sql || 0 == sql.length() ? prepared : sql;
			getStream().print(actual);
			endOfStatement = true;
		} else if (Category.COMMIT.equals(category) || Category.ROLLBACK.equals(category)) {
			getStream().print(category);
			endOfStatement = true;
		} else {
			getStream().println("-- " + category);
			endOfStatement = false;
		}
		getStream().flush();
	}

	@Override
	public void logText(String text) {
	}

}