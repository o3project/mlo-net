/**
 * LdServiceImplTest.java
 * (C) 2015, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import net.arnx.jsonic.JSON;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.dto.RyLinkDto;
import org.o3project.mlo.server.dto.RyPortDto;
import org.o3project.mlo.server.dto.RySwitchDto;
import org.o3project.mlo.server.impl.logic.ConfigProviderImpl;
import org.o3project.mlo.server.logic.HttpRequestInvoker;
import org.o3project.mlo.server.logic.HttpRequestInvokerException;
import org.o3project.mlo.server.logic.HttpRequestMethod;
import org.o3project.mlo.server.logic.MloException;

/**
 * LdServiceImplTest
 *
 */
public class LdServiceImplTest {

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
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.LdServiceImpl#getLdTopo()}.
	 */
	//@Test
	public void testGetLdTopo() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.LdServiceImpl#getRySwitches()}.
	 * @throws MloException 
	 */
	@Test
	public void testGetRySwitches() throws MloException {
		List<RySwitchDto> rySwitchDtos = Arrays.asList(
				createRySwitchDto("0001", 
						createRyPortDto("name-0001-00", "port-0001-00", "mac-0001-00", "0001"),
						createRyPortDto("name-0001-01", "port-0001-01", "mac-0001-01", "0001"),
						createRyPortDto("name-0001-02", "port-0001-02", "mac-0001-02", "0001")
						),
				createRySwitchDto("0002", 
						createRyPortDto("name-0002-00", "port-0002-00", "mac-0002-00", "0002"),
						createRyPortDto("name-0002-01", "port-0002-01", "mac-0002-01", "0002")
						)
						);
		String resBody = JSON.encode(rySwitchDtos);
		LdServiceImpl obj = createObj(resBody);

		List<RySwitchDto> resDtos = obj.getRySwitches();
		
		RySwitchDto dto = null;
		assertEquals(2, resDtos.size());
		
		dto = resDtos.get(0);
		assertEquals("0001", dto.dpid);
		assertEquals(3, dto.ports.size());
		assertTrue(rySwitchDtos.get(0).ports.get(0) != dto.ports.get(0));
		assertEquals(rySwitchDtos.get(0).ports.get(0), dto.ports.get(0));
		assertTrue(rySwitchDtos.get(0).ports.get(1) != dto.ports.get(1));
		assertEquals(rySwitchDtos.get(0).ports.get(1), dto.ports.get(1));
		assertTrue(rySwitchDtos.get(0).ports.get(2) != dto.ports.get(2));
		assertEquals(rySwitchDtos.get(0).ports.get(2), dto.ports.get(2));
		
		dto = resDtos.get(1);
		assertEquals("0002", dto.dpid);
		assertEquals(2, dto.ports.size());
		assertTrue(rySwitchDtos.get(1).ports.get(0) != dto.ports.get(0));
		assertEquals(rySwitchDtos.get(1).ports.get(0), dto.ports.get(0));
		assertTrue(rySwitchDtos.get(1).ports.get(1) != dto.ports.get(1));
		assertEquals(rySwitchDtos.get(1).ports.get(1), dto.ports.get(1));
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.LdServiceImpl#getRyLinks()}.
	 * @throws MloException 
	 */
	@Test
	public void testGetRyLinks() throws MloException {
		RyLinkDto[] ryLinkDtos = new RyLinkDto[]{
				createRyLinkDto(
						createRyPortDto("name-1001-00", "port-1001-00", "mac-1001-00", "1001"), 
						createRyPortDto("name-1001-01", "port-1001-01", "mac-1001-01", "1001")),
				createRyLinkDto(
						createRyPortDto("name-1002-00", "port-1002-00", "mac-1002-00", "1002"), 
						createRyPortDto("name-1002-01", "port-1002-01", "mac-1002-01", "1002")),
		};
		String resBody = JSON.encode(ryLinkDtos);
		
		LdServiceImpl obj = createObj(resBody);
		List<RyLinkDto> resDto = obj.getRyLinks();
		
		RyLinkDto dto = null;
		assertEquals(2, resDto.size());
		dto = resDto.get(0);
		assertTrue(ryLinkDtos[0].src != dto.src);
		assertEquals(ryLinkDtos[0].src, dto.src);
		assertTrue(ryLinkDtos[0].dst != dto.dst);
		assertEquals(ryLinkDtos[0].dst, dto.dst);
		dto = resDto.get(1);
		assertTrue(ryLinkDtos[1].src != dto.src);
		assertEquals(ryLinkDtos[1].src, dto.src);
		assertTrue(ryLinkDtos[1].dst != dto.dst);
		assertEquals(ryLinkDtos[1].dst, dto.dst);
		
	}
	
	static RySwitchDto createRySwitchDto(String dpid, RyPortDto... ports) {
		RySwitchDto dto = new RySwitchDto();
		dto.dpid = dpid;
		dto.ports = Arrays.asList(ports);
		return dto;
	}
	
	static RyLinkDto createRyLinkDto(RyPortDto src, RyPortDto dst) {
		RyLinkDto dto = new RyLinkDto();
		dto.src = src;
		dto.dst = dst;
		return dto;
	}
	
	static RyPortDto createRyPortDto(String name, String portNo, String hwAddr, String dpid) {
		RyPortDto dto = new RyPortDto();
		dto.name = name;
		dto.portNo = portNo;
		dto.hwAddr = hwAddr;
		dto.dpid = dpid;
		return dto;
	}

	/**
	 * @param resBody
	 * @return
	 */
	private LdServiceImpl createObj(String resBody) {
		HttpRequestInvokerDummyImpl invoker = new HttpRequestInvokerDummyImpl();
		invoker.statusCode = 200;
		invoker.resBody = resBody;
		System.out.println(invoker.resBody);
		ConfigProviderImpl configProvider = new ConfigProviderImpl("default.mlo-srv.properties", null);
		
		LdServiceImpl obj = new LdServiceImpl();
		obj.setConfigProvider(configProvider);
		obj.setHttpRequestInvoker(invoker);
		return obj;
	}

	class HttpRequestInvokerDummyImpl implements HttpRequestInvoker {
		int statusCode;
		String resBody;

		/* (non-Javadoc)
		 * @see org.o3project.mlo.server.logic.HttpRequestInvoker#invoke(java.lang.String, org.o3project.mlo.server.logic.HttpRequestMethod, java.lang.Object, java.lang.Class)
		 */
		@Override
		public <REQUEST_DTO, RESPONSE_DTO> RESPONSE_DTO invoke(String uri, HttpRequestMethod method, REQUEST_DTO reqObj, Class<RESPONSE_DTO> resClazz)
				throws HttpRequestInvokerException, IOException {
			InputStream istream = null;
			RESPONSE_DTO resDto = null;
			try {
				istream = new ByteArrayInputStream(resBody.getBytes("UTF-8"));
				resDto = method.handleResInput(statusCode, istream, resClazz);
			} finally {
				if (istream != null) {
					istream.close();
					istream = null;
				}
			}
			return resDto;
		}
	}
}
