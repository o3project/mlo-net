/**
 * TimeOutExceptionTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.TimeOutException;

/**
 * TimeOutExceptionTest
 *
 */
public class TimeOutExceptionTest {

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
	 * Test method for {@link org.o3project.mlo.server.logic.TimeOutException#getErrorName()}.
	 */
	@Test
	public void testGetErrorName() {
		String msg = "message";
		MloException obj = new TimeOutException(msg);
		assertEquals("TimeOutError", obj.getErrorName());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.TimeOutException#TimeOutException(java.lang.String)}.
	 */
	@Test
	public void testTimeOutExceptionString() {
		String msg = "message";
		MloException obj = new TimeOutException(msg);
		assertEquals(msg, obj.getMessage());
		assertEquals(null, obj.getCause());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.TimeOutException#TimeOutException(java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	public void testTimeOutExceptionStringThrowable() {
		String msg = "message";
		Throwable cause = new RuntimeException("A cause");
		MloException obj = new TimeOutException(msg, cause);
		assertEquals(msg, obj.getMessage());
		assertEquals(cause, obj.getCause());
	}

}
