/**
 * 
 */
package org.o3project.mlo.server.impl.rpc.service;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.impl.rpc.service.SdtncPostMethodImpl;

/**
 *
 */
public class SdtncPostMethodImplTest {

	private SdtncPostMethodImpl obj;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		obj = new SdtncPostMethodImpl();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		obj = null;
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncPostMethodImpl#getName()}.
	 */
	@Test
	public void testGetName() {
		assertEquals("POST", obj.getName());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncPostMethodImpl#isSetDoOutput()}.
	 */
	@Test
	public void testIsSetDoOutput() {
		assertEquals(Boolean.TRUE, obj.isSetDoOutput());
	}

}
