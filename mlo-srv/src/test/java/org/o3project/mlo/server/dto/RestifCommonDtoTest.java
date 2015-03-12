/**
 * RestifCommonDtoTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.dto;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.dto.RestifCommonDto;

/**
 * RestifCommonDtoTest
 *
 */
public class RestifCommonDtoTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.dto.RestifCommonDto#createInstance(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreateInstance_args_are_not_null() {
		RestifCommonDto obj = null;
		Integer version = 1;
		String srcCmpName = "src";
		String dstCmpName = "dst";
		String operation = "Request";
		obj = RestifCommonDto.createInstance(version, srcCmpName, dstCmpName, operation);
		assertEquals(version, obj.version);
		assertEquals(srcCmpName, obj.srcComponent.name);
		assertEquals(dstCmpName, obj.dstComponent.name);
		assertEquals(operation, obj.operation);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.dto.RestifCommonDto#createInstance(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreateInstance_src_is_null() {
		RestifCommonDto obj = null;
		Integer version = 1;
		String srcCmpName = null;
		String dstCmpName = "dst";
		String operation = "Request";
		obj = RestifCommonDto.createInstance(version, srcCmpName, dstCmpName, operation);
		assertEquals(version, obj.version);
		assertNull(obj.srcComponent);
		assertEquals(dstCmpName, obj.dstComponent.name);
		assertEquals(operation, obj.operation);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.dto.RestifCommonDto#createInstance(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreateInstance_dst_is_null() {
		RestifCommonDto obj = null;
		Integer version = 1;
		String srcCmpName = "src";
		String dstCmpName = null;
		String operation = "Request";
		obj = RestifCommonDto.createInstance(version, srcCmpName, dstCmpName, operation);
		assertEquals(version, obj.version);
		assertEquals(srcCmpName, obj.srcComponent.name);
		assertNull(obj.dstComponent);
		assertEquals(operation, obj.operation);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.dto.RestifCommonDto#createInstance(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreateInstance_src_dst_are_null() {
		RestifCommonDto obj = null;
		Integer version = 1;
		String srcCmpName = null;
		String dstCmpName = null;
		String operation = "Request";
		obj = RestifCommonDto.createInstance(version, srcCmpName, dstCmpName, operation);
		assertEquals(version, obj.version);
		assertNull(obj.srcComponent);
		assertNull(obj.dstComponent);
		assertEquals(operation, obj.operation);
	}
}
