package org.must.service.impl;

import java.util.List;

import org.m.datasource.oreg.OregConfig;
import org.must.mapper.oreg.dao.S_SortDao;
import org.must.service.OregService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("oregService")
public class OregServiceImpl implements OregService {

	@Autowired
	private S_SortDao S_SortDao;
	
	@Autowired
	private OregConfig oregConfig;
	
	@Override
	public List findAll() {
		return S_SortDao.getAll();
	}

}
