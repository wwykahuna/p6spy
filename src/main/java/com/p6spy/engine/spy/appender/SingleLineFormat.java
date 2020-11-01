package com.p6spy.engine.spy.appender;

import com.p6spy.engine.common.P6Util;

public class SingleLineFormat implements MessageFormattingStrategy {

	@Override
	public String formatMessage(final int connectionId, final String now, final long elapsed, final String category,
			final String prepared, final String sql, final String url) {
		return now + "|" + elapsed + "|" + category + "|connection " + connectionId + "|url " + url + "|"
				+ P6Util.singleLine(prepared) + "|" + P6Util.singleLine(sql);
	}
}