package org.must.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.m.datasource.fds.FdsConfig;
import org.must.mapper.fds.dao.S_AdminDao;
import org.must.mapper.fds.entity.S_Admin;
import org.must.service.FdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service("fdsService")
public class FdsServiceImpl implements FdsService {

	@Autowired
	private S_AdminDao S_AdminDao;

	@Autowired
	private FdsConfig fdsConfig;
	
	@Override
	// @Query(value="SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES ORDER BY
	// TABLE_NAME",nativeQuery = true)
	public List findByAll() {
		return fdsConfig.fdsTemplate().queryForList("s_admin.selectAll");
	}

	/**
	 *  異常手動rollback
	 */
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public String testTransactionUpdataSetData() {
		try {
			List list = S_AdminDao.getAdminOne("test1");
			System.out.println("1--->" + list);
			S_Admin s = new S_Admin();
			s.setAd_user("test1");
			s.setLimits("1");
			S_AdminDao.addAdmin(s);
			list = S_AdminDao.getAdminOne("test1");
			System.out.println("2--->" + list);
			s = new S_Admin();
			s.setAd_user("test2");
			// s.setLimits("1");
			S_AdminDao.addAdmin(s);
			list = S_AdminDao.getAdminOne("test1");
			System.out.println("3--->" + list);

			return list.toString();
		} catch (DataAccessException e) {
			System.out.println("SQL異常 in service method");
			SQLException se = (SQLException) e.getCause();
			String errMsg = se.getLocalizedMessage();
			e.printStackTrace();
			return errMsg;
		} catch (Exception e) {
			System.out.println("異常 in service method");
			e.printStackTrace();
			String errMsg = e.getLocalizedMessage();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// 手動rollbak
			return errMsg;
		}
	}

	/**
	 *  自動rollback，使用rollbackFor 異常須拋出Exception，否則不會自動rollback
	 */
	@Override
	@Transactional(rollbackFor = { Exception.class })
	public String test2TransactionUpdataSetData() {
		List list = S_AdminDao.getAdminOne("test1");
		System.out.println("1--->" + list);
		S_Admin s = new S_Admin();
		s.setAd_user("test1");
		s.setLimits("1");
		S_AdminDao.addAdmin(s);
		list = S_AdminDao.getAdminOne("test1");
		System.out.println("2--->" + list);
		s = new S_Admin();
		s.setAd_user("test2");
		// s.setLimits("1");
		S_AdminDao.addAdmin(s);
		list = S_AdminDao.getAdminOne("test1");
		System.out.println("3--->" + list);
		return list.toString();
	}
}
