/**
 * 
 */
package org.altemista.cloudfwk.test.mybatis;

/*
 * #%L
 * altemista-cloud persistence: MyBatis CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.apache.ibatis.binding.BindingException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.altemista.cloudfwk.test.AbstractPersistenceTest;

import com.mycompany.application.module.repository.DemoTableMapper;

/**
 * Tests custom operations on a MyBatis repository backed by mapped statements.
 * 
 * @author NTT DATA
 */
@ContextConfiguration("classpath:spring/altemista-cloudfwk-test-mybatis.xml")
@Transactional
@Rollback
public class CustomDemoTableMapperTest extends AbstractPersistenceTest {

	/** The repository DemoTableMapper */
	@Autowired
	private DemoTableMapper repository;
	
	@Test
	public void testCheckDaoConfig() {
		
		// Just asserts the bean has been created
		Assert.assertNotNull(this.repository);
	}
	
	@Test
	public void testFindUsingCustom() {
		
		Assert.assertNotNull(this.repository.findUsingCustomQuery());
	}
	
	@Test(expected = BindingException.class)
	public void testFindUsingUndefined() {
		
		Assert.assertNotNull(this.repository.findUsingUndefinedQuery());
	}
}
