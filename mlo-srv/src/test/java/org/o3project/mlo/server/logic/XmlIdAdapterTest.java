/**
 * XmlIdAdapterTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.logic.XmlIdAdapter;

/**
 * XmlIdAdapterTest
 *
 */
public class XmlIdAdapterTest {
	
	private XmlIdAdapter obj = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		obj = new XmlIdAdapter();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		obj = null;
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.XmlIdAdapter#marshal(java.lang.Integer)}.
	 * @throws Exception 
	 */
	@Test
	public void testMarshalInteger_normal() throws Exception {
		Integer val = null;
		String sVal = null;
		
		// [Test]
		val = 1;
		sVal = obj.marshal(val);
		assertEquals("00000001", sVal);
		
		// [Test]
		val = 2;
		sVal = obj.marshal(val);
		assertEquals("00000002", sVal);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.XmlIdAdapter#marshal(java.lang.Integer)}.
	 * @throws Exception 
	 */
	@Test
	public void testMarshalInteger_null() throws Exception {
		Integer val = null;
		String sVal = null;
		
		// [Test]
		sVal = obj.marshal(val);
		assertNull(sVal);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.XmlIdAdapter#unmarshal(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testUnmarshalString_normal() throws Exception {
		Integer val = null;
		String sVal = null;

		// [Test]
		sVal = "00000001";
		val = obj.unmarshal(sVal);
		assertEquals(new Integer(1), val);

		// [Test]
		sVal = "00000002";
		val = obj.unmarshal(sVal);
		assertEquals(new Integer(2), val);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.XmlIdAdapter#unmarshal(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testUnmarshalString_null() throws Exception {
		Integer val = null;
		String sVal = null;

		// [Test]
		val = obj.unmarshal(sVal);
		assertNull(val);
	}

}
