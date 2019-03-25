/**
 * @Info:      ManualManagedTransactionFactory.java
 * @Copyright: 2019
 */
package com.biticcf.ocean.sea.domain.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;

/**
 * @Author: Daniel.Cao
 * @Date:   2019年3月25日
 * @Time:   下午4:32:35
 *
 */
public class ManualManagedTransactionFactory extends SpringManagedTransactionFactory {
	@Override
	public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
		return new ManualManagedTransaction(dataSource);
	}

}
