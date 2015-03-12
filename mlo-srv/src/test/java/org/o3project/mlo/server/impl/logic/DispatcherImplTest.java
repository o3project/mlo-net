/**
 * DispatcherImplTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.impl.logic.DispatcherImpl;
import org.o3project.mlo.server.logic.ApiCallException;
import org.o3project.mlo.server.logic.Dispatcher;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.SliceOperationTask;

/**
 * DispatcherImplTest
 *
 */
public class DispatcherImplTest {

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
	 * Test method for {@link org.o3project.mlo.server.impl.logic.DispatcherImpl#init()}.
	 */
	@Test
	public void testInit() {
		Dispatcher obj = new DispatcherImpl();
		try {
			obj.init();
		} catch (MloException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.DispatcherImpl#dispose()}.
	 */
	@Test
	public void testDispose() {
		Dispatcher obj = new DispatcherImpl();
		try {
			obj.init();
			obj.dispose();
		} catch (MloException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.DispatcherImpl#dispatch(org.o3project.mlo.server.logic.SliceOperationTask, org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testDispatch_normal() {
		Dispatcher obj = new DispatcherImpl();
		SliceOperationTask sliceOpTask = new EmptySliceOperationTask(null, null);
		RestifRequestDto reqDto = new RestifRequestDto();
		try {
			obj.dispatch(sliceOpTask, reqDto);
		} catch (MloException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.DispatcherImpl#dispatch(org.o3project.mlo.server.logic.SliceOperationTask, org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testDispatch_MloException_occurs() {
		Dispatcher obj = new DispatcherImpl();
		MloException sampleMloException  = new ApiCallException("sample");
		SliceOperationTask sliceOpTask = new EmptySliceOperationTask(sampleMloException, null);
		RestifRequestDto reqDto = new RestifRequestDto();
		try {
			obj.dispatch(sliceOpTask, reqDto);
			fail();
		} catch (MloException e) {
			assertEquals(sampleMloException, e);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.DispatcherImpl#dispatch(org.o3project.mlo.server.logic.SliceOperationTask, org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testDispatch_Exception_occurs() {
		Dispatcher obj = new DispatcherImpl();
		Exception sampleException = new RuntimeException("sample");
		SliceOperationTask sliceOpTask = new EmptySliceOperationTask(null, sampleException);
		RestifRequestDto reqDto = new RestifRequestDto();
		try {
			obj.dispatch(sliceOpTask, reqDto);
			fail();
		} catch (MloException e) {
			assertEquals(sampleException, e.getCause());
		}
	}

	/**
	 * Test method for {@link DispatcherImpl#dispatch(SliceOperationTask, Map<String, String>)}.
	 */
	@Test
	public void testDispatch_paramMap_normal() {
		Dispatcher obj = new DispatcherImpl();
		SliceOperationTask sliceOpTask = new EmptySliceOperationTask(null, null);
		Map<String, String> paramMap = new HashMap<String, String>();
		try {
			obj.dispatch(sliceOpTask, paramMap);
		} catch (MloException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link DispatcherImpl#dispatch(SliceOperationTask, Map<String, String>)}.
	 */
	@Test
	public void testDispatch_paramMap_MloException_occurs() {
		Dispatcher obj = new DispatcherImpl();
		MloException sampleMloException  = new ApiCallException("sample");
		SliceOperationTask sliceOpTask = new EmptySliceOperationTask(sampleMloException, null);
		Map<String, String> paramMap = new HashMap<String, String>();
		try {
			obj.dispatch(sliceOpTask, paramMap);
			fail();
		} catch (MloException e) {
			assertEquals(sampleMloException, e);
		}
	}

	/**
	 * Test method for {@link DispatcherImpl#dispatch(SliceOperationTask, Map<String, String>)}.
	 */
	@Test
	public void testDispatch_paramMap_Exception_occurs() {
		Dispatcher obj = new DispatcherImpl();
		Exception sampleException = new RuntimeException("sample");
		SliceOperationTask sliceOpTask = new EmptySliceOperationTask(null, sampleException);
		Map<String, String> paramMap = new HashMap<String, String>();
		try {
			obj.dispatch(sliceOpTask, paramMap);
			fail();
		} catch (MloException e) {
			assertEquals(sampleException, e.getCause());
		}
	}

}
