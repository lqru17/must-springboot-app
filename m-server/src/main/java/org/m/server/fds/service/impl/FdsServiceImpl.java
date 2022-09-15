package org.m.server.fds.service.impl;

import java.util.List;

import org.m.server.fds.config.FdsConfig;
import org.m.server.fds.service.FdsService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service("fds")
public class FdsServiceImpl implements FdsService {
	public static final FdsConfig fdsConfig = new FdsConfig();
	
	@Override
	public List findAll() {
		return fdsConfig.fdsJdbcTemplate().queryForList("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES ORDER BY TABLE_NAME");
	}

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return fdsConfig.fdsJdbcTemplate();
	};
}
