/**
 * ApiCallExceptionTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.logic.ApiCallException;

/**
 * ApiCallExceptionTest
 *
 */
public class ApiCallExceptionTest {

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
	 * Test method for {@link org.o3project.mlo.server.logic.ApiCallException#getErrorName()}.
	 */
	@Test
	public void testGetErrorName() {
		String msg = "message";
		ApiCallException obj = new ApiCallException(msg);
		assertEquals("APICallError", obj.getErrorName());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.ApiCallException#ApiCallException(java.lang.String)}.
	 */
	@Test
	public void testApiCallExceptionString() {
		String msg = "message";
		ApiCallException obj = new ApiCallException(msg);
		assertEquals(msg, obj.getMessage());
		assertEquals(null, obj.getCause());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.ApiCallException#ApiCallException(java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	public void testApiCallExceptionStringThrowable() {
		String msg = "message";
		Throwable cause = new RuntimeException("sample");
		ApiCallException obj = new ApiCallException(msg, cause);
		assertEquals(msg, obj.getMessage());
		assertEquals(cause, obj.getCause());
	}

}
