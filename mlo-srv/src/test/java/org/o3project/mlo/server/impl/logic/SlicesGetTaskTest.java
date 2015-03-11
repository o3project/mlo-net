/**
 * SlicesGetTaskTest.java
 * (C) 2014, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.impl.logic.SliceManager;
import org.o3project.mlo.server.impl.logic.SlicesGetTask;


public class SlicesGetTaskTest {

	private SliceManager manager;
	private SlicesGetTask task;
	
	@Before
    public void setUp() throws Exception {
		manager = new SliceManager();
		task = new SlicesGetTask();
		task.setSliceManager(manager);
	}

    @After
    public void tearDown() throws Exception {
	}

	@Test
    public void testCall_normal() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("owner", "test");
		task.setRequestParamMap(params);
		task.setRequestDto(null);
		RestifResponseDto res = null;
		try {
			res = task.call();
			assertEquals(res.common.version.intValue(), 1);
			assertEquals(res.common.srcComponent.name, "mlo");
			assertEquals(res.common.dstComponent.name, "test");
			assertEquals(res.common.operation, "Response");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	

	@Test
    public void testCall_param_nothing() {
		task.setRequestParamMap(null);
		task.setRequestDto(null);
		RestifResponseDto res = null;
		try {
			res = task.call();
			assertEquals(res.common.version.intValue(), 1);
			assertEquals(res.common.srcComponent.name, "mlo");
			assertNull(res.common.dstComponent);
			assertEquals(res.common.operation, "Response");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	
}
