/**
 * ETL support Spring beans for the batch processing of module.
 * (i.e.: {@code ItemReader}, {@code ItemProcessor}, and {@code ItemWriter} {@code @Component}s)
 */
package com.mycompany.application.module.amazon;

import java.sql.Timestamp;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SimpleDatabaseService {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public SimpleDatabaseService(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/***
	 * write your database operations here using jdbcTemplate
	 */
	public void insert(String description) {

		jdbcTemplate.update("insert into tasks(description,dateEvent)values(' " 
								+ description + "','"+new Timestamp(System.currentTimeMillis())+"' );");

	}

}
