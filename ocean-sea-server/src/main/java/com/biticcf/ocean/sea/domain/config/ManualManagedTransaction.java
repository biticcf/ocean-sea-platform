/**
 * @Info:      ManualManagedTransaction.java
 * @Copyright: 2019
 */
package com.biticcf.ocean.sea.domain.config;

import static org.springframework.util.Assert.notNull;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.transaction.Transaction;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.beyonds.phoenix.mountain.core.common.service.TransStatusHolder;

/**
 * @Author: Daniel.Cao
 * @Date:   2019年3月25日
 * @Time:   下午3:24:44
 *
 */
public class ManualManagedTransaction implements Transaction {
	private static final Logger LOGGER = LoggerFactory.getLogger(ManualManagedTransaction.class);
	
	private final DataSource dataSource;
	
	private Connection connection;
	private boolean isConnectionTransactional;
	private boolean autoCommit;
	
	public ManualManagedTransaction(DataSource dataSource) {
		notNull(dataSource, "No DataSource specified");
		
		this.dataSource = dataSource;
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		if (this.connection == null) {
			openConnection();
		}
		
		boolean withTrans = TransStatusHolder.getTransStatus();
		if (!withTrans) {
			this.connection.setReadOnly(true);
		} else {
			this.connection.setReadOnly(false);
		}
		
		return this.connection;
	}
	
	/**
	   * Gets a connection from Spring transaction manager and discovers if this
	   * {@code Transaction} should manage connection or let it to Spring.
	   * <p>
	   * It also reads autocommit setting because when using Spring Transaction MyBatis
	   * thinks that autocommit is always false and will always call commit/rollback
	   * so we need to no-op that calls.
	   */
	private void openConnection() throws SQLException {
		this.connection = DataSourceUtils.getConnection(this.dataSource);
		this.autoCommit = this.connection.getAutoCommit();
		this.isConnectionTransactional = DataSourceUtils.isConnectionTransactional(this.connection, this.dataSource);
		
		LOGGER.debug(() ->
		        "JDBC Connection ["
		        + this.connection
		        + "] will"
		        + (this.isConnectionTransactional ? " " : " not ")
		        + "be managed by Spring");
	}
	
	@Override
	public void commit() throws SQLException {
		boolean withTrans = TransStatusHolder.getTransStatus();
		if (!withTrans) {
			return;
		}
		
		if (this.connection != null && !this.isConnectionTransactional && !this.autoCommit) {
		    LOGGER.debug(() -> "Committing JDBC Connection [" + this.connection + "]");
		    
		    this.connection.commit();
		}
	}

	@Override
	public void rollback() throws SQLException {
		boolean withTrans = TransStatusHolder.getTransStatus();
		if (!withTrans) {
			return;
		}
		
		if (this.connection != null && !this.isConnectionTransactional && !this.autoCommit) {
		    LOGGER.debug(() -> "Rolling back JDBC Connection [" + this.connection + "]");
		    
		    this.connection.rollback();
		}
	}

	@Override
	public void close() {
		DataSourceUtils.releaseConnection(this.connection, this.dataSource);
	}

	@Override
	public Integer getTimeout() {
		ConnectionHolder holder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
		if (holder != null && holder.hasTimeout()) {
			return holder.getTimeToLiveInSeconds();
	    }
		
	    return null;
	}

}
