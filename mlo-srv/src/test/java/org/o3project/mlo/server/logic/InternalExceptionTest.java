/**
 * InternalExceptionTest.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.logic;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.MloException;

/**
 * InternalExceptionTest
 *
 */
public class InternalExceptionTest {

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
	 * Test method for {@link org.o3project.mlo.server.logic.InternalException#getErrorName()}.
	 */
	@Test
	public void testGetErrorName() {
		String msg = "message";
		MloException obj = new InternalException(msg);
		assertEquals("InternalError", obj.getErrorName());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.InternalException#InternalException(java.lang.String)}.
	 */
	@Test
	public void testInternalExceptionString() {
		String msg = "message";
		MloException obj = new InternalException(msg);
		assertEquals(msg, obj.getMessage());
		assertEquals(null, obj.getCause());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.InternalException#InternalException(java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	public void testInternalExceptionStringThrowable() {
		String msg = "message";
		Throwable cause = new RuntimeException("A cause");
		MloException obj = new InternalException(msg, cause);
		assertEquals(msg, obj.getMessage());
		assertEquals(cause, obj.getCause());
	}
}
