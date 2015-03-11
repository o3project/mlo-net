package org.o3project.mlo.server.impl.rpc.service;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.impl.logic.ConfigProviderImpl;
import org.o3project.mlo.server.impl.rpc.service.SdtncConfigImpl;
import org.o3project.mlo.server.impl.rpc.service.SdtncPostMethodImpl;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostCreatePwDto;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;
import org.o3project.mlo.server.rpc.service.SdtncSerdes;

public class SdtncBaseMethodTest {

	private static final String DATA_PATH = "src/test/resources/org/o3project/mlo/server/rpc/service/data";
	private static final String DATA_FILE_001 = "sdtnc.config.001.properties";

	private SdtncPostMethodImpl obj;
	private SdtncSerdesStub sdtncSerdes = null;

	@Before
	public void setUp() throws Exception {
		File propFile = new File(DATA_PATH, DATA_FILE_001);
		ConfigProviderImpl configProvider = new ConfigProviderImpl("default.mlo-srv.properties", propFile.getAbsolutePath());
		SdtncConfigImpl sdtncConfig = new SdtncConfigImpl();
		sdtncConfig.setConfigProvider(configProvider);
		
		sdtncSerdes = new SdtncSerdesStub();
		
		obj = new SdtncPostMethodImpl();
		obj.setSdtncConfig(sdtncConfig);
		obj.setSdtncSerdes(sdtncSerdes);
	}

	@After
	public void tearDown() throws Exception {
		obj = null;
		sdtncSerdes = null;
	}

	@Test
	public void testHandleReqOutput() {
		SdtncRequestDto reqDto = new SdtncReqPostCreatePwDto();
		OutputStream ostream = null;
		obj.handleReqOutput(reqDto, ostream);
		assertEquals(Boolean.TRUE, sdtncSerdes.isCalled_serializeToXml);
		assertEquals(reqDto, sdtncSerdes.reqDto);
	}

	@Test
	public void testHandleResInput() {
		SdtncResponseDto resDto = new SdtncResponseDto();
		sdtncSerdes.resDto = resDto;
		InputStream istream = null;
		
		SdtncResponseDto actual = obj.handleResInput(istream);
		assertEquals(resDto, actual);
	}

	@Test
	public void testGetConnectionTimeoutSec() {
		Integer expected = 123;
		Integer actual = obj.getConnectionTimeoutSec();
		assertEquals(expected, actual);
	}

	@Test
	public void testGetReadTimeoutSec() {
		Integer expected = 456;
		Integer actual = obj.getReadTimeoutSec();
		assertEquals(expected, actual);
	}

	@Test
	public void testConstructUrl() {
		String path = "mySubPath";
		Map<String, String> paramMap = new LinkedHashMap<String, String>();
		paramMap.put("abc", "123");
		paramMap.put("def", "456");
		
		String expected = "http://myHost/myPath/mySubPath?abc=123&def=456";
		String actual = obj.constructUrl(path, paramMap);
		assertEquals(expected, actual);
	}

	@Test
	public void testConstructUrl_nullParamMap() {
		String path = "mySubPath";
		Map<String, String> paramMap = null;
		
		String expected = "http://myHost/myPath/mySubPath";
		String actual = obj.constructUrl(path, paramMap);
		assertEquals(expected, actual);
	}

	@Test
	public void testConstructUrl_zeroSizeParamMap() {
		String path = "mySubPath";
		Map<String, String> paramMap = new LinkedHashMap<String, String>();
		
		String expected = "http://myHost/myPath/mySubPath";
		String actual = obj.constructUrl(path, paramMap);
		assertEquals(expected, actual);
	}

}


class SdtncSerdesStub implements SdtncSerdes {
	
	public boolean isCalled_serializeToXml = false;
	
	public SdtncRequestDto reqDto = null;
	
	public SdtncResponseDto resDto = null;

	@Override
	public SdtncResponseDto deserializeFromXml(InputStream istream) {
		return resDto;
	}

	@Override
	public void serializeToXml(SdtncRequestDto reqDto, OutputStream ostream) {
		isCalled_serializeToXml = true;
		this.reqDto = reqDto;
	}
	
}
