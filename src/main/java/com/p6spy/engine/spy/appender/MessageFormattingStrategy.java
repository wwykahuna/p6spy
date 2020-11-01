package com.p6spy.engine.spy.appender;

public interface MessageFormattingStrategy {

	String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql,
			String url);

}