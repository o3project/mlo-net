/**
 * DbAccessExceptionTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.logic.DbAccessException;

/**
 * DbAccessExceptionTest
 *
 */
public class DbAccessExceptionTest {

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
	 * Test method for {@link org.o3project.mlo.server.logic.DbAccessException#getErrorName()}.
	 */
	@Test
	public void testGetErrorName() {
		String msg = "message";
		DbAccessException obj = new DbAccessException(msg);
		assertEquals("DBAccessError", obj.getErrorName());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.DbAccessException#DbAccessException(java.lang.String)}.
	 */
	@Test
	public void testDbAccessExceptionString() {
		String msg = "message";
		DbAccessException obj = new DbAccessException(msg);
		assertEquals(msg, obj.getMessage());
		assertEquals(null, obj.getCause());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.DbAccessException#DbAccessException(java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	public void testDbAccessExceptionStringThrowable() {
		String msg = "message";
		Throwable cause = new RuntimeException("A cause");
		DbAccessException obj = new DbAccessException(msg, cause);
		assertEquals(msg, obj.getMessage());
		assertEquals(cause, obj.getCause());
	}

}
