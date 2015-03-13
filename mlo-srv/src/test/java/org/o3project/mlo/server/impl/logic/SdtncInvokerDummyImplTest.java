/**
 * SdtncInvokerDummyImplTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.impl.rpc.service.SdtncDtoUtil;
import org.o3project.mlo.server.impl.rpc.service.SdtncInvokerDummyImpl;
import org.o3project.mlo.server.impl.rpc.service.SdtncDeleteMethodImpl;
import org.o3project.mlo.server.impl.rpc.service.SdtncGetMethodImpl;
import org.o3project.mlo.server.impl.rpc.service.SdtncPostMethodImpl;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;

/**
 * SdtncInvokerDummyImplTest
 *
 */
public class SdtncInvokerDummyImplTest {

	private SdtncInvokerDummyImpl obj;
	private static final String PATH_LOGIN = "login";
	private static final String PATH_LOGOUT = "logout";
	private static final String PATH_LINK = "link";
	private static final String PATH_PATH = "path";
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		obj = new SdtncInvokerDummyImpl();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		obj = null;
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.rpc.service.SdtncInvokerDummyImpl#invoke(org.o3project.mlo.server.rpc.service.SdtncMethod, org.o3project.mlo.server.rpc.dto.SdtncRequestDto, java.lang.String, java.util.Map)}.
	 */
	@Test
	public void test_invoke_login() {
		try {
			SdtncResponseDto resDto = obj.invoke(new SdtncPostMethodImpl(), null, PATH_LOGIN, null);
			assertNotNull(resDto);
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.rpc.service.SdtncInvokerDummyImpl#invoke(org.o3project.mlo.server.rpc.service.SdtncMethod, org.o3project.mlo.server.rpc.dto.SdtncRequestDto, java.lang.String, java.util.Map)}.
	 */
	@Test
	public void test_invoke_logout() {
		try {
			SdtncResponseDto resDto = obj.invoke(new SdtncPostMethodImpl(), null, PATH_LOGOUT, null);
			assertNotNull(resDto);
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.rpc.service.SdtncInvokerDummyImpl#invoke(org.o3project.mlo.server.rpc.service.SdtncMethod, org.o3project.mlo.server.rpc.dto.SdtncRequestDto, java.lang.String, java.util.Map)}.
	 */
	@Test
	public void test_invoke_createPw() {
		try {
			SdtncResponseDto resDto = obj.invoke(new SdtncPostMethodImpl(), null, PATH_PATH, null);
			assertNotNull(resDto);
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.rpc.service.SdtncInvokerDummyImpl#invoke(org.o3project.mlo.server.rpc.service.SdtncMethod, org.o3project.mlo.server.rpc.dto.SdtncRequestDto, java.lang.String, java.util.Map)}.
	 */
	@Test
	public void test_invoke_getLspResource() {
		try {
			SdtncResponseDto resDto = obj.invoke(new SdtncGetMethodImpl(), null, PATH_LINK, null);
			assertNotNull(resDto);
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.rpc.service.SdtncInvokerDummyImpl#invoke(org.o3project.mlo.server.rpc.service.SdtncMethod, org.o3project.mlo.server.rpc.dto.SdtncRequestDto, java.lang.String, java.util.Map)}.
	 */
	@Test
	public void test_invoke_getPw() {
		try {
			Map<String, String> params = new HashMap<String,String>();
			params.put(SdtncDtoUtil.DTO_PRM_KEY_PATHID, "aaa");
			SdtncResponseDto resDto = obj.invoke(new SdtncGetMethodImpl(), null, PATH_PATH, params);
			assertNotNull(resDto);
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.rpc.service.SdtncInvokerDummyImpl#invoke(org.o3project.mlo.server.rpc.service.SdtncMethod, org.o3project.mlo.server.rpc.dto.SdtncRequestDto, java.lang.String, java.util.Map)}.
	 */
	@Test
	public void test_invoke_getPw2() {
		try {
			Map<String, String> params = new HashMap<String,String>();
			params.put(SdtncDtoUtil.DTO_PRM_KEY_PATHID, "testHBHPathId");
			SdtncResponseDto resDto = obj.invoke(new SdtncGetMethodImpl(), null, PATH_PATH, params);
			assertNotNull(resDto);
			assertTrue(resDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == 425);
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.rpc.service.SdtncInvokerDummyImpl#invoke(org.o3project.mlo.server.rpc.service.SdtncMethod, org.o3project.mlo.server.rpc.dto.SdtncRequestDto, java.lang.String, java.util.Map)}.
	 */
	@Test
	public void test_invoke_getPw3() {
		try {
			Map<String, String> params = new HashMap<String,String>();
			params.put(SdtncDtoUtil.DTO_PRM_KEY_PATHID, "testCThPathId");
			SdtncResponseDto resDto = obj.invoke(new SdtncGetMethodImpl(), null, PATH_PATH, params);
			assertNotNull(resDto);
			assertTrue(resDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == 426);
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.rpc.service.SdtncInvokerDummyImpl#invoke(org.o3project.mlo.server.rpc.service.SdtncMethod, org.o3project.mlo.server.rpc.dto.SdtncRequestDto, java.lang.String, java.util.Map)}.
	 */
	@Test
	public void test_invoke_deletePw() {
		try {
			SdtncResponseDto resDto = obj.invoke(new SdtncDeleteMethodImpl(), null, PATH_PATH, null);
			assertNotNull(resDto);
		} catch (MloException e) {
			fail();
		}
	}

}
