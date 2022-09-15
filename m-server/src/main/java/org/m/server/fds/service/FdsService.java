package org.m.server.fds.service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public interface FdsService {
	public JdbcTemplate getJdbcTemplate();
	public List findAll();
}
