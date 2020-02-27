/**
 * 
 */
package cloud.altemista.fwk.test.mybatis;

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


import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import cloud.altemista.fwk.test.AbstractPersistenceTest;

import com.mycompany.application.module.model.DemoTable;
import com.mycompany.application.module.model.DemoTableExample;
import com.mycompany.application.module.model.DemoTableWithBLOBs;
import com.mycompany.application.module.repository.DemoTableMapper;

/**
 * Tests the basic CRUD operations on a MyBatis repository backed by mapped statements.
 * 
 * @author NTT DATA
 */
@ContextConfiguration("classpath:spring/cloud-altemistafwk-test-mybatis.xml")
@Transactional
@Rollback
public class DemoTableMapperTest extends AbstractPersistenceTest {

	/** The repository DemoTableMapper */
	@Autowired
	private DemoTableMapper repository;
	
	@Test
	public void testCheckDaoConfig() {
		
		// Just asserts the bean has been created
		Assert.assertNotNull(this.repository);
	}
	
	@Test
	public void testInsert() {
		
		DemoTableWithBLOBs entity = this.newEntity();
		
		Assert.assertNull(entity.getKey());
		Assert.assertEquals(1, this.repository.insert(entity));
		Integer key = entity.getKey();
		Assert.assertNotNull(key);
	}

	@Test
	public void testInsertSelective() {
		
		DemoTableWithBLOBs entity = new DemoTableWithBLOBs();
		entity.setTinyintField((byte) 1);
		
		Assert.assertNull(entity.getKey());
		Assert.assertEquals(1, this.repository.insertSelective(entity));
		Assert.assertNotNull(entity.getKey());
		Assert.assertEquals(Byte.valueOf((byte) 1), entity.getTinyintField());
		Assert.assertNull(entity.getIntegerField());
	}

	@Test
	public void testCount() {
		
		Assert.assertTrue(this.repository.countByExample(null) != 0);
	}

	@Test
	public void testCountByExample() {
		
		DemoTableExample example = new DemoTableExample();
		example.createCriteria().andKeyEqualTo(1001);
		Assert.assertEquals(1, this.repository.countByExample(example));
	}

	@Test
	public void testFindOne() {
		
		// Primary key exists
		Assert.assertNotNull(this.repository.selectByPrimaryKey(1001));
		
		// Primary key does not exist
		Assert.assertNull(this.repository.selectByPrimaryKey(999));
	}

	@Test
	public void testFindAll() {
		
		Iterable<DemoTable> iterable = this.repository.selectByExample(null);
		Assert.assertNotNull(iterable);
		
		Iterator<DemoTable> iterator = iterable.iterator();
		Assert.assertNotNull(iterator);
		Assert.assertTrue(iterable.iterator().hasNext());
	}
	
	@Test
	public void testFindAllByExample() {
		
		DemoTableExample example = new DemoTableExample();
		example.createCriteria().andKeyEqualTo(1001);
		example.or(example.createCriteria().andKeyBetween(1003, 1004));
		
		Iterable<DemoTable> entities = this.repository.selectByExample(example);
		for (DemoTable entity : entities) {
			Assert.assertTrue(ArrayUtils.contains(new int[]{ 1001, 1003, 1004 }, entity.getKey().intValue()));
		}
	}
	
	@Test
	public void testUpdate() {
		
		DemoTable entity = this.repository.selectByPrimaryKey(1001);
		Assert.assertEquals(1, this.repository.updateByPrimaryKey(entity));
		Assert.assertEquals(Integer.valueOf(1001), entity.getKey());
	}

	@Test
	public void testUpdateSelective() {
		
		DemoTableWithBLOBs entity = this.repository.selectByPrimaryKey(1001);
		entity.setIntegerField(33);
		Assert.assertNotNull(this.repository.updateByPrimaryKeySelective(entity));
		
		entity = this.repository.selectByPrimaryKey(1001);
		Assert.assertNotNull(entity.getIntegerField());
	}

	@Test
	public void testUpdateByExample() {
		
		DemoTable entity = new DemoTable();
		entity.setKey(1);
		entity.setIntegerField(99);
		
		DemoTableExample example = new DemoTableExample();
		example.createCriteria().andKeyEqualTo(1001);
		
		this.repository.updateByExample(entity, example);
		
		Assert.assertNull(this.repository.selectByPrimaryKey(1001));
		Assert.assertNotNull(entity = this.repository.selectByPrimaryKey(1));
		Assert.assertNull(entity.getTinyintField());
		Assert.assertEquals(Integer.valueOf(99), entity.getIntegerField());
	}

	@Test
	public void testUpdateByExampleSelective() {
		
		DemoTableWithBLOBs entity = new DemoTableWithBLOBs();
		entity.setKey(1);
		entity.setIntegerField(99);
		
		DemoTableExample example = new DemoTableExample();
		example.createCriteria().andKeyEqualTo(1001);
		
		this.repository.updateByExampleSelective(entity, example);
		
		Assert.assertNull(this.repository.selectByPrimaryKey(1001));
		Assert.assertNotNull(entity = this.repository.selectByPrimaryKey(1));
		Assert.assertNotNull(entity.getIntegerField());
		Assert.assertEquals(Integer.valueOf(99), entity.getIntegerField());
	}

	@Test
	public void testDeleteId() {
		
		Assume.assumeNotNull(this.repository.selectByPrimaryKey(1002));
		
		// Primary key exists
		this.repository.deleteByPrimaryKey(1002);
		Assert.assertNull(this.repository.selectByPrimaryKey(1002));
		
		// Primary key no longer not exist
		this.repository.deleteByPrimaryKey(1002);
		Assert.assertNull(this.repository.selectByPrimaryKey(1002));
	}
	
	@Test
	public void testDeleteByExample() {
		
		DemoTableExample example = new DemoTableExample();
		example.createCriteria().andKeyEqualTo(1001);
		
		long count = this.repository.countByExample(null);
		this.repository.deleteByExample(example);
		Assert.assertNotEquals(count, this.repository.countByExample(null));
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
		
		entity.setCharField(RandomStringUtils.random(100));
		entity.setVarcharField(RandomStringUtils.random(100));
		entity.setBinaryField(RandomUtils.nextBytes(100));
		entity.setVarbinaryField(RandomUtils.nextBytes(100));

		entity.setBlobField(RandomUtils.nextBytes(1000));
		
		return entity;
	}
}
