/**
 * 
 */
package org.o3project.mlo.server.action;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.action.ActionUtil;
import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.logic.ApiCallException;
import org.o3project.mlo.server.logic.Orchestrator;
import org.o3project.mlo.server.logic.Serdes;
import org.o3project.mlo.server.logic.SliceOperationTask;
import org.o3project.mlo.server.logic.Validator;

/**
 *
 */
public class ActionUtilTest {

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
	 * Test method for {@link org.o3project.mlo.server.action.ActionUtil#isSupportingHttpMethod(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testIsSupportingHttpMethodStringString_n_matchGET() {
		boolean expected = true;
		boolean actual = ActionUtil.isSupportingHttpMethod("GET", "GET");
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.action.ActionUtil#isSupportingHttpMethod(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testIsSupportingHttpMethodStringString_a_notMatchGET() {
		boolean expected = false;
		boolean actual = ActionUtil.isSupportingHttpMethod("GET", "POST");
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.action.ActionUtil#isSupportingHttpMethod(java.lang.String, java.lang.String[])}.
	 */
	@Test
	public void testIsSupportingHttpMethodStringStringArray_n_matchGET() {
		boolean expected = true;
		boolean actual = ActionUtil.isSupportingHttpMethod("GET", 
				new String[]{"HEAD", "POST", "GET", "DELETE", "PUT"});
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.action.ActionUtil#isSupportingHttpMethod(java.lang.String, java.lang.String[])}.
	 */
	@Test
	public void testIsSupportingHttpMethodStringStringArray_a_notMatchGET() {
		boolean expected = false;
		boolean actual = ActionUtil.isSupportingHttpMethod("GET", 
				new String[]{"HEAD", "POST", "DELETE", "PUT"});
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.action.ActionUtil#doAction(org.o3project.mlo.server.logic.Orchestrator, org.o3project.mlo.server.logic.Serdes, org.o3project.mlo.server.logic.Validator, org.o3project.mlo.server.logic.SliceOperationTask, java.io.InputStream, java.io.OutputStream)}.
	 */
	@Test
	public void testDoAction_n() {
		RestifResponseDto resDto = new RestifResponseDto();
		OrchestratorStub orchestrator = new OrchestratorStub();
		orchestrator.resDto = resDto;
		SerdesStub serdes = new SerdesStub();
		ValidatorStub validator = new ValidatorStub();
		SliceOperationTaskStub sliceOpTask = new SliceOperationTaskStub();
		InputStream istream = null;
		OutputStream ostream = null;

		String expected = null;
		String actual = ActionUtil.doAction(orchestrator, serdes, validator, sliceOpTask, istream, ostream);
		assertEquals(expected, actual);
		assertEquals(resDto, serdes.resDto);
		assertEquals(null, serdes.resDto.error);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.action.ActionUtil#doAction(org.o3project.mlo.server.logic.Orchestrator, org.o3project.mlo.server.logic.Serdes, org.o3project.mlo.server.logic.Validator, org.o3project.mlo.server.logic.SliceOperationTask, java.io.InputStream, java.io.OutputStream)}.
	 */
	@Test
	public void testDoAction_a_resDtoNull() {
		RestifResponseDto resDto = null;
		OrchestratorStub orchestrator = new OrchestratorStub();
		orchestrator.resDto = resDto;
		SerdesStub serdes = new SerdesStub();
		ValidatorStub validator = new ValidatorStub();
		SliceOperationTaskStub sliceOpTask = new SliceOperationTaskStub();
		InputStream istream = null;
		OutputStream ostream = null;

		String expected = null;
		String actual = ActionUtil.doAction(orchestrator, serdes, validator, sliceOpTask, istream, ostream);
		assertEquals(expected, actual);
		assertEquals(resDto, serdes.resDto);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.action.ActionUtil#doAction(org.o3project.mlo.server.logic.Orchestrator, org.o3project.mlo.server.logic.Serdes, org.o3project.mlo.server.logic.Validator, org.o3project.mlo.server.logic.SliceOperationTask, java.io.InputStream, java.io.OutputStream)}.
	 */
	@Test
	public void testDoAction_a_apiCallException() {
		RestifResponseDto resDto = new RestifResponseDto();
		ApiCallException apiCallException = new ApiCallException("sampleError");
		OrchestratorStub orchestrator = new OrchestratorStub();
		orchestrator.resDto = resDto;
		SerdesStub serdes = new SerdesStub();
		ValidatorStub validator = new ValidatorStub();
		validator.apiCallException = apiCallException;
		SliceOperationTaskStub sliceOpTask = new SliceOperationTaskStub();
		InputStream istream = null;
		OutputStream ostream = null;

		String expected = null;
		String actual = ActionUtil.doAction(orchestrator, serdes, validator, sliceOpTask, istream, ostream);
		assertEquals(expected, actual);
		assertNotSame(resDto, serdes.resDto);
		assertEquals("APICallError", serdes.resDto.error.cause);
		assertEquals("sampleError", serdes.resDto.error.detail);
	}
}

class OrchestratorStub implements Orchestrator {
	
	public RestifResponseDto resDto = null;

	@Override
	public void init() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public RestifResponseDto handle(SliceOperationTask sliceOpTask,
			RestifRequestDto reqDto) {
		return resDto;
	}

	@Override
	public RestifResponseDto handle(SliceOperationTask sliceOpTask,
			Map<String, String> paramMap) {
		return null;
	}	
}

class SerdesStub implements Serdes {
	
	public RestifResponseDto resDto = null;

	@Override
	public RestifRequestDto unmarshal(InputStream istream)
			throws ApiCallException {
		return null;
	}

	@Override
	public void marshal(RestifResponseDto dto, OutputStream ostream) {
		resDto = dto;
	}
}

class ValidatorStub implements Validator {
	
	public ApiCallException apiCallException = null;

	@Override
	public void validate(RestifRequestDto dto) throws ApiCallException {
		if (apiCallException != null) {
			throw apiCallException;
		}
	}	
}

class SliceOperationTaskStub implements SliceOperationTask {

	@Override
	public RestifResponseDto call() throws Exception {
		return null;
	}

	@Override
	public void setRequestDto(RestifRequestDto reqDto) {
	}

	@Override
	public void setRequestParamMap(Map<String, String> reqParamMap) {
	}
}
