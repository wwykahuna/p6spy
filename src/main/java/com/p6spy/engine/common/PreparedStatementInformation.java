package com.p6spy.engine.common;

import java.util.HashMap;
import java.util.Map;

public class PreparedStatementInformation extends StatementInformation implements Loggable {

	private final Map<Integer, Value> parameterValues = new HashMap<Integer, Value>();

	public PreparedStatementInformation(final ConnectionInformation connectionInformation, String query) {
		super(connectionInformation);
		setStatementQuery(query);
	}

	@Override
	public String getSqlWithValues() {
		final StringBuilder sb = new StringBuilder();
		final String statementQuery = getStatementQuery();

		int currentParameter = 0;
		for (int pos = 0; pos < statementQuery.length(); pos++) {
			char character = statementQuery.charAt(pos);
			if (statementQuery.charAt(pos) == '?' && currentParameter <= parameterValues.size()) {
				Value value = parameterValues.get(currentParameter);
				sb.append(value != null ? value.toString() : new Value().toString());
				currentParameter++;
			} else {
				sb.append(character);
			}
		}

		return sb.toString();
	}

	public void setParameterValue(final int position, final Object value) {
		parameterValues.put(position - 1, new Value(value));
	}

	protected Map<Integer, Value> getParameterValues() {
		return parameterValues;
	}
}
