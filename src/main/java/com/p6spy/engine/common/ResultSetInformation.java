package com.p6spy.engine.common;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.p6spy.engine.logging.Category;

public class ResultSetInformation implements Loggable {

	private final StatementInformation statementInformation;
	private ResultSet resultSet;
	private String query;
	private final Map<String, Value> resultMap = new LinkedHashMap<String, Value>();
	private int currRow = -1;
	private int lastRowLogged = -1;

	public ResultSetInformation(final StatementInformation statementInformation) {
		this.statementInformation = statementInformation;
		this.query = statementInformation.getStatementQuery();
	}

	public void generateLogMessage() {
		if (lastRowLogged != currRow) {
			P6LogQuery.log(Category.RESULTSET, this);
			lastRowLogged = currRow;
		}
	}

	public int getCurrRow() {
		return currRow;
	}

	public void incrementCurrRow() {
		this.currRow++;
		this.resultMap.clear();
	}

	public void setColumnValue(String columnName, Object value) {
		resultMap.put(columnName, new Value(value));
	}

	@Override
	public String getSql() {
		return query;
	}

	@Override
	public String getSqlWithValues() {
		final StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Value> entry : resultMap.entrySet()) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(entry.getKey());
			sb.append(" = ");
			sb.append(entry.getValue() != null ? entry.getValue().toString() : new Value().toString());
		}

		return sb.toString();
	}

	public Map<String, Value> getResultMap() {
		return Collections.unmodifiableMap(resultMap);
	}

	public StatementInformation getStatementInformation() {
		return statementInformation;
	}

	/** {@inheritDoc} */
	@Override
	public ConnectionInformation getConnectionInformation() {
		return this.statementInformation.getConnectionInformation();
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}
}
