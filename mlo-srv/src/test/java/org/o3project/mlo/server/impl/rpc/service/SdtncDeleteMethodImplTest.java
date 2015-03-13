/**
 * 
 */
package org.o3project.mlo.server.impl.rpc.service;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.impl.rpc.service.SdtncDeleteMethodImpl;

/**
 *
 */
public class SdtncDeleteMethodImplTest {
	
	private SdtncDeleteMethodImpl obj;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		obj = new SdtncDeleteMethodImpl();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		obj = null;
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDeleteMethodImpl#getName()}.
	 */
	@Test
	public void testGetName() {
		assertEquals("DELETE", obj.getName());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDeleteMethodImpl#isSetDoOutput()}.
	 */
	@Test
	public void testIsSetDoOutput() {
		assertEquals(Boolean.FALSE, obj.isSetDoOutput());
	}

}
