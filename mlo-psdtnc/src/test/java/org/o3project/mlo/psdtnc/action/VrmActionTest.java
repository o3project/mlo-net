/**
 * VrmActionTest.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.psdtnc.action;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.o3project.mlo.psdtnc.dto.SdtncVrmifLoginDto;
import org.o3project.mlo.psdtnc.dto.SdtncVrmifVlinkListDto;
import org.o3project.mlo.psdtnc.dto.SdtncVrmifVpathDto;
import org.o3project.mlo.psdtnc.dto.SdtncVrmifVpathListDto;
import org.o3project.mlo.psdtnc.form.SdtncVrmifForm;
import org.o3project.mlo.psdtnc.impl.logic.LdConfJsonConverterTopoImpl;
import org.o3project.mlo.psdtnc.impl.logic.LdOperationServiceDummyLocalImpl;
import org.o3project.mlo.psdtnc.impl.logic.LdOperationServiceImpl;
import org.o3project.mlo.psdtnc.impl.logic.LdTopologyRepositoryImpl;
import org.o3project.mlo.psdtnc.impl.logic.PseudoSdtncVrmServiceImpl;
import org.o3project.mlo.server.impl.logic.ConfigProviderImpl;

/**
 * VrmActionTest
 *
 */
public class VrmActionTest {
	private static final Log LOG = LogFactory.getLog(VrmActionTest.class);
	
	private static final String DATA_DIRECTORY_PATH = "src/test/resources/org/o3project/mlo/psdtnc/action";

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
	
	public VrmAction constructVrmAction () {
		String customPropsFilePath = "src/test/resources/org/o3project/mlo/psdtnc/action/mlo-psdtnc.sample001.properties";
		ConfigProviderImpl configProvider = new ConfigProviderImpl("default.mlo-psdtnc.properties", customPropsFilePath);
		
		LdConfJsonConverterTopoImpl ldConfJsonConverter = new LdConfJsonConverterTopoImpl();
		
		LdOperationServiceImpl ldOperationService = new LdOperationServiceImpl();
		ldOperationService.setConfigProvider(configProvider);
		ldOperationService.setLdConfJsonConverter(ldConfJsonConverter);
		
		LdOperationServiceDummyLocalImpl ldOperationServiceDummyLocalImpl = new LdOperationServiceDummyLocalImpl();
		ldOperationServiceDummyLocalImpl.setLdConfJsonConverter(ldConfJsonConverter);

		LdTopologyRepositoryImpl ldTopologyRepository = new LdTopologyRepositoryImpl();
		ldTopologyRepository.setConfigProvider(configProvider);
		ldTopologyRepository.setLdOperationService(ldOperationService);
		ldTopologyRepository.setLdOperationServiceDummyLocalImpl(ldOperationServiceDummyLocalImpl);
		ldTopologyRepository.init();

		PseudoSdtncVrmServiceImpl pseudoSdtncVrmService = new PseudoSdtncVrmServiceImpl();
		pseudoSdtncVrmService.setConfigProvider(configProvider);
		pseudoSdtncVrmService.setLdTopologyRepository(ldTopologyRepository);
		pseudoSdtncVrmService.setLdOperationServiceDummyLocalImpl(ldOperationServiceDummyLocalImpl);
		
		VrmAction obj = new VrmAction();
		obj.setPseudoSdtncVrmService(pseudoSdtncVrmService);
		return obj;
	}
	
	private File getDataFile(String filename) {
		File dataFile = null;
		dataFile = new File(DATA_DIRECTORY_PATH, filename);
		return dataFile;
	}
	
	private String getDataAsString(String dataFilename) throws IOException {
		InputStream inputStream = null;
		ByteArrayOutputStream outputStream = null;
		
		File dataFile = getDataFile(dataFilename);
		outputStream = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int rlen = -1;
		try {
			inputStream = new FileInputStream(dataFile);
			while (!Thread.currentThread().isInterrupted()) {
				rlen = inputStream.read(buf, 0, buf.length);
				if (rlen < 0) {
					break;
				}
				outputStream.write(buf, 0, rlen);
			}
		} finally {
			outputStream.flush();
			if (inputStream != null) {
				inputStream.close();
				inputStream = null;
			}
		}
		return outputStream.toString("UTF-8");
	}
	
	<DTO> DTO deserializeFromString(VrmAction obj, String dtoString, Class<DTO> dtoClass) throws IOException {
		InputStream inputStream = null;
		DTO dto = null;
		try {
			inputStream = new ByteArrayInputStream(dtoString.getBytes("UTF-8"));
			dto = obj.deserializeFrom(inputStream, dtoClass);
		} finally {
			if (inputStream != null) {
				inputStream.close();
				inputStream = null;
			}
		}
		return dto;
	}
	
	<DTO> String serializeToString(VrmAction obj, DTO dto) throws IOException {
		String dtoString = null;
		ByteArrayOutputStream outputStream = null;
		try {
			outputStream = new ByteArrayOutputStream();
			obj.serializeTo(outputStream, dto);
			dtoString = outputStream.toString("UTF-8");
		} finally {
			if (outputStream != null) {
				outputStream.close();
				outputStream = null;
			}
		}
		return dtoString;
	}
	
	@Test
	public void testVrmAction() {
		VrmAction obj = null;
		
		obj = constructVrmAction();
		
		assertNotNull(obj);
	}

	/**
	 * Test method for {@link org.o3project.mlo.psdtnc.action.VrmAction#postLoginLocal(java.io.InputStream, java.io.OutputStream)}.
	 * @throws IOException 
	 */
	@Test
	public void testPostLoginLocal_Normal() throws IOException {
		String reqData = null;
		String resData = null;
		ByteArrayInputStream inputStream = null;
		ByteArrayOutputStream outputStream = null;
		SdtncVrmifLoginDto resDto = null;
		
		VrmAction obj = constructVrmAction();
		
		reqData = getDataAsString("post.login.req.000.xml");
		inputStream = new ByteArrayInputStream(reqData.getBytes("UTF-8"));
		outputStream = new ByteArrayOutputStream();
		obj.postLoginLocal(inputStream, outputStream);
		resData = outputStream.toString("UTF-8");
		LOG.info("post login res");
		LOG.info(resData);
		resDto = deserializeFromString(obj, resData, SdtncVrmifLoginDto.class);
		
		assertNotNull(resDto);
		assertNotNull(resDto.head);
		assertNotNull(resDto.auth);
		assertNotNull(resDto.auth.token);
		assertNotNull(resDto.login);
	}

	/**
	 * Test method for {@link org.o3project.mlo.psdtnc.action.VrmAction#postLogoutLocal(java.io.InputStream, java.io.OutputStream)}.
	 */
	@Test
	public void testPostLogoutLocal_Normal() throws IOException {
		String reqData = null;
		String resData = null;
		ByteArrayInputStream inputStream = null;
		ByteArrayOutputStream outputStream = null;
		SdtncVrmifLoginDto resDto = null;
		
		VrmAction obj = constructVrmAction();
		
		reqData = getDataAsString("post.login.req.000.xml");
		inputStream = new ByteArrayInputStream(reqData.getBytes("UTF-8"));
		outputStream = new ByteArrayOutputStream();
		obj.postLoginLocal(inputStream, outputStream);
		
		reqData = getDataAsString("post.logout.req.000.xml");
		inputStream = new ByteArrayInputStream(reqData.getBytes("UTF-8"));
		outputStream = new ByteArrayOutputStream();
		obj.postLogoutLocal(inputStream, outputStream);
		
		resData = outputStream.toString("UTF-8");
		LOG.info("post logout res");
		LOG.info(resData);
		resDto = deserializeFromString(obj, resData, SdtncVrmifLoginDto.class);
		
		assertNotNull(resDto);
		assertNotNull(resDto.head);
		assertNull(resDto.auth);
		assertNotNull(resDto.login);
		assertNotNull(resDto.login.loginId);
	}

	/**
	 * Test method for {@link org.o3project.mlo.psdtnc.action.VrmAction#getLinkLocal(org.o3project.mlo.psdtnc.form.SdtncVrmifForm, java.io.OutputStream)}.
	 */
	@Test
	public void testGetLinkLocal_Normal() throws IOException{
		String reqData = null;
		String resData = null;
		SdtncVrmifForm reqParams = null;
		ByteArrayInputStream inputStream = null;
		ByteArrayOutputStream outputStream = null;
		SdtncVrmifLoginDto resVrmifLoginDto = null;
		SdtncVrmifVlinkListDto resVrmifVlinkListDto = null;
		
		VrmAction obj = constructVrmAction();
		
		reqData = getDataAsString("post.login.req.000.xml");
		inputStream = new ByteArrayInputStream(reqData.getBytes("UTF-8"));
		outputStream = new ByteArrayOutputStream();
		obj.postLoginLocal(inputStream, outputStream);
		resData = outputStream.toString("UTF-8");
		resVrmifLoginDto = deserializeFromString(obj, resData, SdtncVrmifLoginDto.class);
		
		reqParams = new SdtncVrmifForm();
		reqParams.token = resVrmifLoginDto.auth.token;
		reqParams.groupIndex = "2";
		reqParams.vObjectIndex = "of1slow";
		outputStream = new ByteArrayOutputStream();
		obj.getLinkLocal(reqParams, outputStream);
		resData = outputStream.toString("UTF-8");
		LOG.info("get link res");
		LOG.info(resData);
		resVrmifVlinkListDto = deserializeFromString(obj, resData, SdtncVrmifVlinkListDto.class);
		
		assertNotNull(resVrmifVlinkListDto);
		assertNotNull(resVrmifVlinkListDto.head);
		assertNotNull(resVrmifVlinkListDto.vlink);
		assertEquals(1, resVrmifVlinkListDto.vlink.size());
	}

	/**
	 * Test method for {@link org.o3project.mlo.psdtnc.action.VrmAction#postPathLocal(java.io.InputStream, java.io.OutputStream)}.
	 */
	@Test
	public void testPostPathLocal() throws IOException {
		String reqData = null;
		String resData = null;
		ByteArrayInputStream inputStream = null;
		ByteArrayOutputStream outputStream = null;
		SdtncVrmifVpathDto reqVrmifVpathDto = null;
		SdtncVrmifLoginDto resVrmifLoginDto = null;
		SdtncVrmifVpathDto resVrmifVpathDto = null;
		
		VrmAction obj = constructVrmAction();
		
		reqData = getDataAsString("post.login.req.000.xml");
		inputStream = new ByteArrayInputStream(reqData.getBytes("UTF-8"));
		outputStream = new ByteArrayOutputStream();
		obj.postLoginLocal(inputStream, outputStream);
		resData = outputStream.toString("UTF-8");
		resVrmifLoginDto = deserializeFromString(obj, resData, SdtncVrmifLoginDto.class);
		
		reqData = getDataAsString("post.path.req.000.xml");
		reqVrmifVpathDto = deserializeFromString(obj, reqData, SdtncVrmifVpathDto.class);
		reqVrmifVpathDto.auth.token = resVrmifLoginDto.auth.token;
		reqData = serializeToString(obj, reqVrmifVpathDto);
		inputStream = new ByteArrayInputStream(reqData.getBytes("UTF-8"));
		outputStream = new ByteArrayOutputStream();
		obj.postPathLocal(inputStream, outputStream);
		resData = outputStream.toString("UTF-8");
		LOG.info("get path res");
		LOG.info(resData);
		resVrmifVpathDto = deserializeFromString(obj, resData, SdtncVrmifVpathDto.class);
		
		assertNotNull(resVrmifVpathDto);
		assertNotNull(resVrmifVpathDto.head);
		assertNotNull(resVrmifVpathDto.vpath);
		assertEquals(1, resVrmifVpathDto.vpath.size());
		assertNotNull(resVrmifVpathDto.vpath.get(0));
		assertNotNull(resVrmifVpathDto.vpath.get(0).vObjectIndex);
	}

	/**
	 * Test method for {@link org.o3project.mlo.psdtnc.action.VrmAction#deletePathLocal(org.o3project.mlo.psdtnc.form.SdtncVrmifForm, java.io.OutputStream)}.
	 * @throws IOException 
	 */
	@Test
	public void testDeletePathLocal() throws IOException {
		String reqData = null;
		String resData = null;
		ByteArrayInputStream inputStream = null;
		ByteArrayOutputStream outputStream = null;
		SdtncVrmifForm reqParams = null;
		SdtncVrmifVpathDto reqVrmifVpathDto = null;
		SdtncVrmifLoginDto resVrmifLoginDto = null;
		SdtncVrmifVpathDto resVrmifVpathDto = null;
		
		VrmAction obj = constructVrmAction();
		
		reqData = getDataAsString("post.login.req.000.xml");
		inputStream = new ByteArrayInputStream(reqData.getBytes("UTF-8"));
		outputStream = new ByteArrayOutputStream();
		obj.postLoginLocal(inputStream, outputStream);
		resData = outputStream.toString("UTF-8");
		resVrmifLoginDto = deserializeFromString(obj, resData, SdtncVrmifLoginDto.class);
		
		reqData = getDataAsString("post.path.req.000.xml");
		reqVrmifVpathDto = deserializeFromString(obj, reqData, SdtncVrmifVpathDto.class);
		reqVrmifVpathDto.auth.token = resVrmifLoginDto.auth.token;
		reqData = serializeToString(obj, reqVrmifVpathDto);
		inputStream = new ByteArrayInputStream(reqData.getBytes("UTF-8"));
		outputStream = new ByteArrayOutputStream();
		obj.postPathLocal(inputStream, outputStream);
		resData = outputStream.toString("UTF-8");
		resVrmifVpathDto = deserializeFromString(obj, resData, SdtncVrmifVpathDto.class);
		
		String vObjectIndex = resVrmifVpathDto.vpath.get(0).vObjectIndex;
		String resourceIndex = resVrmifVpathDto.vpath.get(0).resourceIndex;
		
		reqParams = new SdtncVrmifForm();
		reqParams.token = resVrmifLoginDto.auth.token;
		reqParams.groupIndex = "2";
		reqParams.vObjectIndex = vObjectIndex;
		outputStream = new ByteArrayOutputStream();
		obj.deletePathLocal(reqParams, outputStream);
		resData = outputStream.toString("UTF-8");
		LOG.info("delete path res");
		LOG.info(resData);
		resVrmifVpathDto = deserializeFromString(obj, resData, SdtncVrmifVpathDto.class);
		
		assertNotNull(resVrmifVpathDto);
		assertNotNull(resVrmifVpathDto.head);
		assertNotNull(resVrmifVpathDto.vpath);
		assertEquals(1, resVrmifVpathDto.vpath.size());
		assertNotNull(resVrmifVpathDto.vpath.get(0));
		assertEquals(vObjectIndex, resVrmifVpathDto.vpath.get(0).vObjectIndex);
		assertEquals(resourceIndex, resVrmifVpathDto.vpath.get(0).resourceIndex);
	}

	/**
	 * Test method for {@link org.o3project.mlo.psdtnc.action.VrmAction#getPathLocal(org.o3project.mlo.psdtnc.form.SdtncVrmifForm, java.io.OutputStream)}.
	 */
	@Test
	public void testGetPathLocal() throws IOException {
		String reqData = null;
		String resData = null;
		ByteArrayInputStream inputStream = null;
		ByteArrayOutputStream outputStream = null;
		SdtncVrmifForm reqParams = null;
		SdtncVrmifVpathDto reqVrmifVpathDto = null;
		SdtncVrmifLoginDto resVrmifLoginDto = null;
		SdtncVrmifVpathDto resVrmifVpathDto = null;
		SdtncVrmifVpathListDto resVrmifVpathListDto = null;
		
		VrmAction obj = constructVrmAction();
		
		reqData = getDataAsString("post.login.req.000.xml");
		inputStream = new ByteArrayInputStream(reqData.getBytes("UTF-8"));
		outputStream = new ByteArrayOutputStream();
		obj.postLoginLocal(inputStream, outputStream);
		resData = outputStream.toString("UTF-8");
		resVrmifLoginDto = deserializeFromString(obj, resData, SdtncVrmifLoginDto.class);
		
		reqData = getDataAsString("post.path.req.000.xml");
		reqVrmifVpathDto = deserializeFromString(obj, reqData, SdtncVrmifVpathDto.class);
		reqVrmifVpathDto.auth.token = resVrmifLoginDto.auth.token;
		reqData = serializeToString(obj, reqVrmifVpathDto);
		inputStream = new ByteArrayInputStream(reqData.getBytes("UTF-8"));
		outputStream = new ByteArrayOutputStream();
		obj.postPathLocal(inputStream, outputStream);
		resData = outputStream.toString("UTF-8");
		resVrmifVpathDto = deserializeFromString(obj, resData, SdtncVrmifVpathDto.class);
		
		String vObjectIndex = resVrmifVpathDto.vpath.get(0).vObjectIndex;
		String resourceIndex = resVrmifVpathDto.vpath.get(0).resourceIndex;
		
		reqParams = new SdtncVrmifForm();
		reqParams.token = resVrmifLoginDto.auth.token;
		reqParams.groupIndex = "2";
		reqParams.vObjectIndex = vObjectIndex;
		outputStream = new ByteArrayOutputStream();
		obj.getPathLocal(reqParams, outputStream);
		resData = outputStream.toString("UTF-8");
		LOG.info("get path res");
		LOG.info(resData);
		resVrmifVpathListDto = deserializeFromString(obj, resData, SdtncVrmifVpathListDto.class);
		
		assertNotNull(resVrmifVpathListDto);
		assertNotNull(resVrmifVpathListDto.head);
		assertNotNull(resVrmifVpathListDto.vpath);
		assertEquals(1, resVrmifVpathListDto.vpath.size());
		assertNotNull(resVrmifVpathListDto.vpath.get(0));
		assertEquals(vObjectIndex, resVrmifVpathListDto.vpath.get(0).vObjectIndex);
		assertEquals(resourceIndex, resVrmifVpathListDto.vpath.get(0).resourceIndex);
	}

	/**
	 * Test method for {@link org.o3project.mlo.psdtnc.action.VrmAction#handleNotImplementedMethod()}.
	 */
	@Ignore
	@Test
	public void testHandleNotImplementedMethod() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.o3project.mlo.psdtnc.action.VrmAction#deserializeFrom(java.io.InputStream, java.lang.Class)}.
	 */
	@Ignore
	@Test
	public void testDeserializeFrom() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.o3project.mlo.psdtnc.action.VrmAction#serializeTo(java.io.OutputStream, java.lang.Object)}.
	 */
	@Ignore
	@Test
	public void testSerializeTo() {
		fail("Not yet implemented");
	}

}
