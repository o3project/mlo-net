/**
 * SdtncSliceDtoTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl;
import org.o3project.mlo.server.impl.rpc.service.SdtncTestUtil;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostCreatePwDto;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;
import org.o3project.mlo.server.rpc.dto.SdtncSliceDto;

/**
 * SdtncSliceDtoTest
 *
 */
public class SdtncSliceDtoTest {
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
	public void testSerializeToXml_slice_req() throws Throwable {
		SdtncReqPostCreatePwDto reqDto = new SdtncReqPostCreatePwDto();
		SdtncSliceDto sliceDto = new SdtncSliceDto();
		sliceDto.groupIndex = "groupIndex";
		reqDto.slice = sliceDto;
		assertTrue(SdtncTestUtil.isSameXmlAs(serdes, reqDto, DATA_PATH, "sdtnc.slice.req.xml"));
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl#deserializeFromXml(java.io.InputStream)}.
	 * @throws Throwable 
	 */
	@Test
	public void testDeserializeFromXml_slice_res() throws Throwable {
		SdtncResponseDto resDto = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "sdtnc.slice.res.xml");
		assertEquals("groupIndex", resDto.slice.groupIndex);
	}
	
}
