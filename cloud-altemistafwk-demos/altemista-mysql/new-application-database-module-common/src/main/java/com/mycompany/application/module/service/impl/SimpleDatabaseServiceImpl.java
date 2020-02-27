package com.mycompany.application.module.service.impl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mycompany.application.module.service.SimpleDatabaseService;

@Service
public class SimpleDatabaseServiceImpl implements SimpleDatabaseService {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public SimpleDatabaseServiceImpl(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public Integer countTasks() {
		Integer res = jdbcTemplate.queryForObject("select count(*) from tasks ", Integer.class);
		return res;
	}

}
