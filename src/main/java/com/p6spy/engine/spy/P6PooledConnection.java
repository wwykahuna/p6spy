package com.p6spy.engine.spy;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;
import javax.sql.StatementEventListener;

import com.p6spy.engine.common.ConnectionInformation;
import com.p6spy.engine.event.JdbcEventListener;
import com.p6spy.engine.wrapper.ConnectionWrapper;

public class P6PooledConnection implements PooledConnection {

	protected final PooledConnection passthru;
	protected final JdbcEventListenerFactory jdbcEventListenerFactory;

	public P6PooledConnection(PooledConnection connection, JdbcEventListenerFactory jdbcEventListenerFactory) {
		this.passthru = connection;
		this.jdbcEventListenerFactory = jdbcEventListenerFactory;
	}

	@Override
	public Connection getConnection() throws SQLException {
		final long start = System.nanoTime();

		final Connection conn;
		final JdbcEventListener jdbcEventListener = this.jdbcEventListenerFactory.createJdbcEventListener();
		final ConnectionInformation connectionInformation = ConnectionInformation.fromPooledConnection(passthru);
		jdbcEventListener.onBeforeGetConnection(connectionInformation);
		try {
			conn = passthru.getConnection();
			connectionInformation.setConnection(conn);
			connectionInformation.setTimeToGetConnectionNs(System.nanoTime() - start);
			jdbcEventListener.onAfterGetConnection(connectionInformation, null);
		} catch (SQLException e) {
			connectionInformation.setTimeToGetConnectionNs(System.nanoTime() - start);
			jdbcEventListener.onAfterGetConnection(connectionInformation, e);
			throw e;
		}

		return ConnectionWrapper.wrap(conn, jdbcEventListener, connectionInformation);
	}

	@Override
	public void close() throws SQLException {
		passthru.close();
	}

	@Override
	public void addConnectionEventListener(ConnectionEventListener eventTarget) {
		passthru.addConnectionEventListener(eventTarget);
	}

	@Override
	public void removeConnectionEventListener(ConnectionEventListener eventTarget) {
		passthru.removeConnectionEventListener(eventTarget);
	}

	@Override
	public void addStatementEventListener(StatementEventListener listener) {
		passthru.addStatementEventListener(listener);
	}

	@Override
	public void removeStatementEventListener(StatementEventListener listener) {
		passthru.removeStatementEventListener(listener);
	}

}