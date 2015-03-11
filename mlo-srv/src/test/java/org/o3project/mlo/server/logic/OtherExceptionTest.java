/**
 * OtherExceptionTest.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.logic;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.OtherException;

/**
 * OtherExceptionTest
 *
 */
public class OtherExceptionTest {

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
	 * Test method for {@link org.o3project.mlo.server.logic.OtherException#getErrorName()}.
	 */
	@Test
	public void testGetErrorName() {
		String msg = "message";
		MloException obj = new OtherException(msg);
		assertEquals("OtherError", obj.getErrorName());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.OtherException#OtherException(java.lang.String)}.
	 */
	@Test
	public void testOtherExceptionString() {
		String msg = "message";
		MloException obj = new OtherException(msg);
		assertEquals(msg, obj.getMessage());
		assertEquals(null, obj.getCause());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.OtherException#OtherException(java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	public void testOtherExceptionStringThrowable() {
		String msg = "message";
		Throwable cause = new RuntimeException("A cause");
		MloException obj = new OtherException(msg, cause);
		assertEquals(msg, obj.getMessage());
		assertEquals(cause, obj.getCause());
	}

}
