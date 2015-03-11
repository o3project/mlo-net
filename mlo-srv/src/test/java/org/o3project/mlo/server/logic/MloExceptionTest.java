/**
 * MloExceptionTest.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.logic;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.logic.MloException;

/**
 * MloExceptionTest
 *
 */
public class MloExceptionTest {

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
	 * Test method for {@link org.o3project.mlo.server.logic.MloException#MloException()}.
	 */
	@Test
	public void testMloException() {
		MloException obj = new ExtendedMloException();
		assertEquals(null, obj.getMessage());
		assertEquals(null, obj.getCause());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.MloException#MloException(java.lang.String)}.
	 */
	@Test
	public void testMloExceptionString() {
		String msg = "message";
		MloException obj = new ExtendedMloException(msg);
		assertEquals(msg, obj.getMessage());
		assertEquals(null, obj.getCause());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.MloException#MloException(java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	public void testMloExceptionStringThrowable() {
		String msg = "message";
		Throwable cause = new RuntimeException("A cause");
		MloException obj = new ExtendedMloException(msg, cause);
		assertEquals(msg, obj.getMessage());
		assertEquals(cause, obj.getCause());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.MloException#MloException(java.lang.Throwable)}.
	 */
	@Test
	public void testMloExceptionThrowable() {
		Throwable cause = new RuntimeException("A cause");
		MloException obj = new ExtendedMloException(cause);
		assertEquals(cause, obj.getCause());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.MloException#getSliceName()}.
	 */
	@Test
	public void testGetSliceName() {
		String msg = "message";
		Throwable cause = new RuntimeException("A cause");
		String name = "A name";
		String id = "An id";
		MloException obj = new ExtendedMloException(msg, cause);
		obj.setSliceInfo(name, id);
		assertEquals("A name", obj.getSliceName());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.MloException#getSliceId()}.
	 */
	@Test
	public void testGetSliceId() {
		String msg = "message";
		Throwable cause = new RuntimeException("A cause");
		String name = "A name";
		String id = "An id";
		MloException obj = new ExtendedMloException(msg, cause);
		obj.setSliceInfo(name, id);
		assertEquals("An id", obj.getSliceId());
	}
}

class ExtendedMloException extends MloException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1627953259743578624L;
	
	ExtendedMloException() {
		super();
	}
	
	ExtendedMloException(String msg) {
		super(msg);
	}
	
	ExtendedMloException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	ExtendedMloException(Throwable cause) {
		super(cause);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.MloException#getErrorName()
	 */
	@Override
	public String getErrorName() {
		return "ErrorName";
	}
}


