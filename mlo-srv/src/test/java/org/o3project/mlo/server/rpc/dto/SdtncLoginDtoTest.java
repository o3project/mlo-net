/**
 * SdtncLoginDtoTest.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl;
import org.o3project.mlo.server.impl.rpc.service.SdtncTestUtil;
import org.o3project.mlo.server.rpc.dto.SdtncLoginDto;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostLoginDto;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;

/**
 * SdtncLoginDtoTest
 *
 */
public class SdtncLoginDtoTest {
	private static final String DATA_PATH = "src/test/resources/org/o3project/mlo/server/rpc/service/data";

	private SdtncSerdesImpl serdes;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		serdes = new SdtncSerdesImpl();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		serdes = null;
	}
	

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl#serializeToXml(SdtncRequestDto, java.io.OutputStream)}.
	 * @throws Throwable 
	 */
	@Test
	public void testSerializeToXml_login_req() throws Throwable {
		SdtncReqPostLoginDto reqDto = new SdtncReqPostLoginDto();
		SdtncLoginDto loginDto = new SdtncLoginDto();
		loginDto.loginId = "loginId";
		loginDto.loginPassword = "loginPassword";
		loginDto.ipAddress = "123.145.167.189";
		loginDto.accountName = "accountName";
		loginDto.accountLoginDate = "2013/12/31 12:34:56";
		loginDto.accountTimeZone = "+0900";
		reqDto.login = loginDto;
		assertTrue(SdtncTestUtil.isSameXmlAs(serdes, reqDto, DATA_PATH, "sdtnc.login.req.xml"));
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl#deserializeFromXml(java.io.InputStream)}.
	 * @throws Throwable 
	 */
	@Test
	public void testDeserializeFromXml_login_res() throws Throwable {
		SdtncResponseDto resDto = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "sdtnc.login.res.xml");
		assertEquals("loginId", resDto.login.loginId);
		assertEquals("loginPassword", resDto.login.loginPassword);
		assertEquals("123.145.167.189", resDto.login.ipAddress);
		assertEquals("accountName", resDto.login.accountName);
		assertEquals("2013/12/31 12:34:56", resDto.login.accountLoginDate);
		assertEquals("+0900", resDto.login.accountTimeZone);
	}
}
