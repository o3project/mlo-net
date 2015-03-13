/**
 * SdtncHeadDtoTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl;
import org.o3project.mlo.server.impl.rpc.service.SdtncTestUtil;
import org.o3project.mlo.server.rpc.dto.SdtncHeadDto;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostLoginDto;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;

/**
 * SdtncHeadDtoTest
 *
 */
public class SdtncHeadDtoTest {
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
	public void testSerializeToXml_head_req() throws Throwable {
		SdtncReqPostLoginDto reqDto = new SdtncReqPostLoginDto();
		SdtncHeadDto headDto = new SdtncHeadDto();
		headDto.errorDetail = "errorDetail";
		headDto.majorResponseCode = new Integer(123);
		headDto.minorResponseCode = new Integer(124);
		headDto.sequenceNumber = new Integer(125);
		headDto.time = "2013/12/11 12:56:34+0900";
		reqDto.head = headDto;
		assertTrue(SdtncTestUtil.isSameXmlAs(serdes, reqDto, DATA_PATH, "sdtnc.head.req.xml"));
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl#deserializeFromXml(java.io.InputStream)}.
	 * @throws Throwable 
	 */
	@Test
	public void testDeserializeFromXml_head_res() throws Throwable {
		SdtncResponseDto resDto = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "sdtnc.head.res.xml");
		assertEquals(new Integer(125), resDto.head.sequenceNumber);
		assertEquals("2013/12/11 12:56:34+0900", resDto.head.time);
		assertEquals(new Integer(123), resDto.head.majorResponseCode);
		assertEquals(new Integer(124), resDto.head.minorResponseCode);
		assertEquals("errorDetail", resDto.head.errorDetail);
	}
}
