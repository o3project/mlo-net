/**
 * OrchestratorImplTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.dto.RestifCommonDto;
import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.impl.logic.DispatcherImpl;
import org.o3project.mlo.server.impl.logic.OrchestratorImpl;
import org.o3project.mlo.server.logic.ApiCallException;
import org.o3project.mlo.server.logic.Dispatcher;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.NbiConstants;
import org.o3project.mlo.server.logic.SliceOperationTask;

/**
 * OrchestratorImplTest
 *
 */
public class OrchestratorImplTest implements NbiConstants {

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
	 * Test method for {@link org.o3project.mlo.server.impl.logic.OrchestratorImpl#setDispatcher(org.o3project.mlo.server.logic.Dispatcher)}.
	 */
	@Test
	public void testSetDispatcher() {
		OrchestratorImpl obj = new OrchestratorImpl();
		Dispatcher disp = new DispatcherImpl();
		try {
			obj.setDispatcher(disp);
		} catch (Throwable th) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.OrchestratorImpl#init()}.
	 */
	@Test
	public void testInit() {
		OrchestratorImpl obj = new OrchestratorImpl();
		try {
			obj.init();
		} catch (Throwable th) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.OrchestratorImpl#dispose()}.
	 */
	@Test
	public void testDispose() {
		OrchestratorImpl obj = new OrchestratorImpl();
		try {
			obj.init();
			obj.dispose();
		} catch (Throwable th) {
			fail();
		}
	}

	/**
	 * Test method for {@link OrchestratorImpl#handle(SliceOperationTask, Map<String, String>)}.
	 */
	@Test
	public void testHandle_normal() {
		OrchestratorImpl obj = new OrchestratorImpl();
		Dispatcher disp = new DispatcherImpl();
		RestifRequestDto reqDto = new RestifRequestDto();
		reqDto.common = RestifCommonDto.createInstance(1, "scrCmp", "dstCmp", "Ope");
		SliceOperationTask sliceOpTask = new EmptySliceOperationTask(null, null);
		RestifResponseDto resDto = null;
		
		obj.setDispatcher(disp);
		resDto = obj.handle(sliceOpTask, reqDto);
		
		assertNull(resDto);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.OrchestratorImpl#handle(org.o3project.mlo.server.logic.SliceOperationTask, org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testHandle_paramMap_normal() {
		OrchestratorImpl obj = new OrchestratorImpl();
		Dispatcher disp = new DispatcherImpl();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put(REQPARAM_KEY_OWNER, "debugClient");
		SliceOperationTask sliceOpTask = new EmptySliceOperationTask(null, null);
		RestifResponseDto resDto = null;
		
		obj.setDispatcher(disp);
		resDto = obj.handle(sliceOpTask, paramMap);
		
		assertNull(resDto);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.OrchestratorImpl#handle(org.o3project.mlo.server.logic.SliceOperationTask, org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testHandle_mloexception_occurs() {
		OrchestratorImpl obj = new OrchestratorImpl();
		Dispatcher disp = new DispatcherImpl();
		RestifRequestDto reqDto = new RestifRequestDto();
		reqDto.common = RestifCommonDto.createInstance(1, "srcCmp", "dstCmp", "Ope");
		MloException sampleMloException = new ApiCallException("sample");
		SliceOperationTask sliceOpTask = new EmptySliceOperationTask(sampleMloException, null);
		RestifResponseDto resDto = null;
		
		obj.setDispatcher(disp);
		resDto = obj.handle(sliceOpTask, reqDto);
		
		assertNotNull(resDto);
		assertEquals(new Integer(1), resDto.common.version);
		assertEquals("dstCmp", resDto.common.srcComponent.name);
		assertEquals("srcCmp", resDto.common.dstComponent.name);
		assertEquals("Response", resDto.common.operation);
		assertNotNull(resDto.error);
		assertEquals("APICallError", resDto.error.cause);
		assertEquals("sample", resDto.error.detail);
	}

	/**
	 * Test method for {@link OrchestratorImpl#handle(SliceOperationTask, Map<String, String>)}.
	 */
	@Test
	public void testHandle_paramMap_mloexception_occurs() {
		OrchestratorImpl obj = new OrchestratorImpl();
		Dispatcher disp = new DispatcherImpl();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put(REQPARAM_KEY_OWNER, "debugClient");
		MloException sampleMloException = new ApiCallException("sample");
		SliceOperationTask sliceOpTask = new EmptySliceOperationTask(sampleMloException, null);
		RestifResponseDto resDto = null;
		
		obj.setDispatcher(disp);
		resDto = obj.handle(sliceOpTask, paramMap);
		
		assertNotNull(resDto);
		assertEquals(new Integer(1), resDto.common.version);
		assertEquals("mlo", resDto.common.srcComponent.name);
		assertEquals("debugClient", resDto.common.dstComponent.name);
		assertEquals("Response", resDto.common.operation);
		assertNotNull(resDto.error);
		assertEquals("APICallError", resDto.error.cause);
		assertEquals("sample", resDto.error.detail);
	}

}
