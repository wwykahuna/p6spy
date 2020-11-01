package com.p6spy.engine.common;

import java.sql.Statement;

public class StatementInformation implements Loggable {

	private final ConnectionInformation connectionInformation;
	private Statement statement;
	private String statementQuery;
	private long totalTimeElapsed;

	public StatementInformation(final ConnectionInformation connectionInformation) {
		this.connectionInformation = connectionInformation;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public String getStatementQuery() {
		return statementQuery;
	}

	public void setStatementQuery(final String statementQuery) {
		this.statementQuery = statementQuery;
	}

	/** {@inheritDoc} */
	@Override
	public ConnectionInformation getConnectionInformation() {
		return this.connectionInformation;
	}

	@Override
	public String getSqlWithValues() {
		return getSql();
	}

	@Override
	public String getSql() {
		return getStatementQuery();
	}

	public long getTotalTimeElapsed() {
		return totalTimeElapsed;
	}

	public void incrementTimeElapsed(long timeElapsedNanos) {
		totalTimeElapsed += timeElapsedNanos;
	}
}
