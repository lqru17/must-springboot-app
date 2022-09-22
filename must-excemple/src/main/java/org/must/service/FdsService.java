package org.must.service;

import java.util.List;

public interface FdsService {
	List findByAll();
	/**
	 *  異常手動rollback
	 */
	String testTransactionUpdataSetData();
	/**
	 *  自動rollback，使用rollbackFor 異常須拋出Exception，否則不會自動rollback
	 */
	String test2TransactionUpdataSetData();
}
