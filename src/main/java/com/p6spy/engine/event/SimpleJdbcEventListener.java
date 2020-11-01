package com.p6spy.engine.event;

import com.p6spy.engine.common.PreparedStatementInformation;
import com.p6spy.engine.common.StatementInformation;

import java.sql.SQLException;

public abstract class SimpleJdbcEventListener extends JdbcEventListener {

	public void onBeforeAnyExecute(StatementInformation statementInformation) {
	}

	public void onAfterAnyExecute(StatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
	}

	public void onBeforeAnyAddBatch(StatementInformation statementInformation) {
	}

	public void onAfterAnyAddBatch(StatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
	}

	@Override
	public void onBeforeExecute(PreparedStatementInformation statementInformation) {
		onBeforeAnyExecute(statementInformation);
	}

	@Override
	public void onBeforeExecute(StatementInformation statementInformation, String sql) {
		onBeforeAnyExecute(statementInformation);
	}

	@Override
	public void onBeforeExecuteBatch(StatementInformation statementInformation) {
		onBeforeAnyExecute(statementInformation);
	}

	@Override
	public void onBeforeExecuteUpdate(PreparedStatementInformation statementInformation) {
		onBeforeAnyExecute(statementInformation);
	}

	@Override
	public void onBeforeExecuteUpdate(StatementInformation statementInformation, String sql) {
		onBeforeAnyExecute(statementInformation);
	}

	@Override
	public void onBeforeExecuteQuery(PreparedStatementInformation statementInformation) {
		onBeforeAnyExecute(statementInformation);
	}

	@Override
	public void onBeforeExecuteQuery(StatementInformation statementInformation, String sql) {
		onBeforeAnyExecute(statementInformation);
	}

	@Override
	public void onAfterExecute(PreparedStatementInformation statementInformation, long timeElapsedNanos,
			SQLException e) {
		onAfterAnyExecute(statementInformation, timeElapsedNanos, e);
	}

	@Override
	public void onAfterExecute(StatementInformation statementInformation, long timeElapsedNanos, String sql,
			SQLException e) {
		onAfterAnyExecute(statementInformation, timeElapsedNanos, e);
	}

	@Override
	public void onAfterExecuteBatch(StatementInformation statementInformation, long timeElapsedNanos,
			int[] updateCounts, SQLException e) {
		onAfterAnyExecute(statementInformation, timeElapsedNanos, e);
	}

	@Override
	public void onAfterExecuteUpdate(PreparedStatementInformation statementInformation, long timeElapsedNanos,
			int rowCount, SQLException e) {
		onAfterAnyExecute(statementInformation, timeElapsedNanos, e);
	}

	@Override
	public void onAfterExecuteUpdate(StatementInformation statementInformation, long timeElapsedNanos, String sql,
			int rowCount, SQLException e) {
		onAfterAnyExecute(statementInformation, timeElapsedNanos, e);
	}

	@Override
	public void onAfterExecuteQuery(PreparedStatementInformation statementInformation, long timeElapsedNanos,
			SQLException e) {
		onAfterAnyExecute(statementInformation, timeElapsedNanos, e);
	}

	@Override
	public void onAfterExecuteQuery(StatementInformation statementInformation, long timeElapsedNanos, String sql,
			SQLException e) {
		onAfterAnyExecute(statementInformation, timeElapsedNanos, e);
	}

	@Override
	public void onBeforeAddBatch(PreparedStatementInformation statementInformation) {
		onBeforeAnyAddBatch(statementInformation);
	}

	@Override
	public void onBeforeAddBatch(StatementInformation statementInformation, String sql) {
		onBeforeAnyAddBatch(statementInformation);
	}

	@Override
	public void onAfterAddBatch(PreparedStatementInformation statementInformation, long timeElapsedNanos,
			SQLException e) {
		onAfterAnyAddBatch(statementInformation, timeElapsedNanos, e);
	}

	@Override
	public void onAfterAddBatch(StatementInformation statementInformation, long timeElapsedNanos, String sql,
			SQLException e) {
		onAfterAnyAddBatch(statementInformation, timeElapsedNanos, e);
	}
}