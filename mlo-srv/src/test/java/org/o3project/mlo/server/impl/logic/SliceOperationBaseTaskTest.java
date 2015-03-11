/**
 * SliceOperationBaseTaskTest.java
 * (C) 2014, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.impl.logic.SliceOperationBaseTask;
import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.NbiConstants;

/**
 * SliceOperationBaseTaskTest
 *
 */
public class SliceOperationBaseTaskTest implements NbiConstants {
	
	private SliceOperationDummyTask obj;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		obj = new SliceOperationDummyTask();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		obj = null;
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceOperationBaseTask#checkPreparation()}.
	 */
	@Test
	public void testCheckPreparation() {
		try{
			obj.setSliceManager(null);
			RestifRequestDto requestDto = new RestifRequestDto();
			obj.setRequestDto(requestDto);
			obj.callCheckPreparation();
			fail();
		}catch(Exception e){
			assertTrue(e instanceof InternalException);
			assertEquals(e.getMessage(), "SliceManager is null.");
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceOperationBaseTask#call()}.
	 */
	@Test
	public void testCall() {
		try{
			obj.call();
			fail();
		}catch(Exception e){
			assertTrue(e instanceof InternalException);
			assertEquals(e.getMessage(), "NotImplemented");
		}
	}
	
	class SliceOperationDummyTask extends SliceOperationBaseTask{
		
		public void callCheckPreparation() throws MloException{
			super.checkPreparation();
		}
	}

}
