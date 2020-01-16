package org.altemista.cloudfwk.it.service.impl;

/*
 * #%L
 * altemista-cloud performance module integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.altemista.cloudfwk.it.model.DemoTable;
import org.altemista.cloudfwk.it.model.DemoTableExample;
import org.altemista.cloudfwk.it.model.DemoTableWithBLOBs;
import org.altemista.cloudfwk.it.repository.DemoTableMapper;
import org.altemista.cloudfwk.it.service.AnotherExampleService;
import org.altemista.cloudfwk.it.service.ExampleService;
import org.altemista.cloudfwk.it.util.PerformanceUtil;

/**
 * The implementation of the service to illustrate the performance module
 * @author NTT DATA
 */
@Service
public class ExampleServiceImpl implements ExampleService {
	
	/** Another service to illustrate the performance module */
	@Autowired
	private AnotherExampleService anotherService;
	
	/** The repository DemoTableMapper */
	@Autowired
	private DemoTableMapper repository;

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.service.ExampleService#simpleMethod()
	 */
	@Override
	public String simpleMethod() {
		
		PerformanceUtil.randomSleep(10, 110);
		return "Hello, World!";
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.service.ExampleService#simpleMethodWithArguments(java.lang.String, int)
	 */
	@Override
	public String simpleMethodWithArguments(String argument, int anotherArgument) {
		
		PerformanceUtil.randomSleep(50, 250);
		return StringUtils.repeat(argument, anotherArgument);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.service.ExampleService#hierarchicalMethodWithArguments(java.lang.String, int)
	 */
	@Override
	public String hierarchicalMethodWithArguments(String argument, int anotherArgument) {

		PerformanceUtil.randomSleep(10, 110);
		String ret = argument;
		for (int i = 0; i < anotherArgument; i++) {
			ret = this.anotherService.simpleMethodWithArgument(ret); 
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.service.ExampleService#methodWithDatabaseInsert()
	 */
	@Override
	public DemoTable methodWithDatabaseInsert() {
		
		PerformanceUtil.randomSleep(10, 110);
		DemoTableWithBLOBs entity = this.newEntity();
		this.repository.insert(entity);
		return entity;
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.service.ExampleService#methodWithDatabaseSelect()
	 */
	@Override
	public List<DemoTable> methodWithDatabaseSelect() {
		
		DemoTableExample query = new DemoTableExample();
		
		PerformanceUtil.randomSleep(10, 110);
		return this.repository.selectByExample(query);
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.service.ExampleService#methodWithDatabaseSelect(org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<DemoTable> methodWithDatabaseSelect(Pageable pageable) {
		
		DemoTableExample query = new DemoTableExample();
		RowBounds rowBounds = new RowBounds((int) pageable.getOffset(), pageable.getPageSize());
		int count = this.repository.countByExample(query);
		
		PerformanceUtil.randomSleep(10, 110);
		List<DemoTable> list = this.repository.selectByExampleWithRowbounds(query, rowBounds);
		PerformanceUtil.randomSleep(10, 110);
		
		return new PageImpl<DemoTable>(list, pageable, count);
	}
	
	/**
	 * Convenience method
	 * @return a DemoTable
	 */
	private DemoTableWithBLOBs newEntity() {
		
		DemoTableWithBLOBs entity = new DemoTableWithBLOBs();
		
		entity.setIntegerField(RandomUtils.nextInt(0, 1000));
		entity.setIntField(RandomUtils.nextInt(0, 1000));
		entity.setSmallintField((short) RandomUtils.nextInt(0, 1000));
		entity.setTinyintField(RandomUtils.nextBytes(1)[0]);
		entity.setBigintField(RandomUtils.nextLong(0, 1000000));
		
		entity.setDecimalField(BigDecimal.valueOf(RandomUtils.nextDouble(0, 1000000.0d)));
		entity.setNumericField(BigDecimal.valueOf(RandomUtils.nextDouble(0, 1000000.0d)));
		entity.setFloatField(RandomUtils.nextDouble(0, 1000000.0d));
		entity.setDoubleField(RandomUtils.nextDouble(0, 1000000.0d));
		
		entity.setDateField(new Date(RandomUtils.nextLong(0, 1000000)));
		entity.setDatetimeField(new Date(RandomUtils.nextLong(0, 1000000)));
		entity.setTimestampField(new Date(RandomUtils.nextLong(0, 1000000)));
		entity.setTimeField(new Date(RandomUtils.nextLong(0, 1000000)));
		
		entity.setCharField(RandomStringUtils.randomAscii(100));
		entity.setVarcharField(RandomStringUtils.randomAscii(100));
		entity.setBinaryField(RandomUtils.nextBytes(100));
		entity.setVarbinaryField(RandomUtils.nextBytes(100));

		entity.setBlobField(RandomUtils.nextBytes(1000));
		
		return entity;
	}

}
