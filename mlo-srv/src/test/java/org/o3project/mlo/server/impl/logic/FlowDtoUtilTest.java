/**
 * FlowDtoUtilTest.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.dto.FlowDto;
import org.o3project.mlo.server.dto.LinkInfoDto;
import org.o3project.mlo.server.impl.logic.FlowDtoUtil;

/**
 * FlowDtoUtilTest
 *
 */
public class FlowDtoUtilTest {

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
	 * Test method for {@link org.o3project.mlo.server.impl.logic.FlowDtoUtil#createLinkInfo(java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.String, java.lang.Integer)}.
	 */
	@Test
	public void testCreateLinkInfo() {
		Integer id = new Integer(5);
		String srcNodeName = "src1";
		Integer srcNodeId = new Integer(10);
		String dstNodeName = "dst1";
		Integer dstNodeId = new Integer(20);
		LinkInfoDto lid = FlowDtoUtil.createLinkInfo(id, srcNodeName, srcNodeId, dstNodeName, dstNodeId);
		assertEquals(id, lid.id);
		assertEquals(srcNodeName, lid.srcPTNodeName);
		assertEquals(srcNodeId, lid.srcPTNodeId);
		assertEquals(dstNodeName, lid.dstPTNodeName);
		assertEquals(dstNodeId, lid.dstPTNodeId);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.FlowDtoUtil#copyConcreteFlow(org.o3project.mlo.server.dto.FlowDto)}.
	 */
	@Test
	public void testCopyConcreteFlow() {
		FlowDto base = FlowDtoUtil.createConcreteFlow(
				Arrays.asList(
						FlowDtoUtil.createLinkInfo(20000000, "src21", 22, "dst23", 24),
						FlowDtoUtil.createLinkInfo(20000001, "src23", 24, "dst25", 26)
						), 
				200, 
				201);
		base.type = "type210";
		base.id = 210;
		base.name = "flow211";
		base.srcCENodeName = "srcCE212";
		base.reqBandWidth = 213;
		FlowDto flow = FlowDtoUtil.copyConcreteFlow(base);
		assertEquals(null, flow.type);
		assertEquals("flow211", flow.name);
		assertEquals(new Integer(210), flow.id);
		__assertRequestMemberIsNull(flow);
		__assertPTInfo(flow, "src21", 22, "dst25", 26);
		assertEquals("00000022_00000026", flow.overlayLogicalList);
		assertEquals("00000022_00000024_00000026", flow.underlayLogicalList);
		assertEquals(2, flow.linkInfoList.size());
		__assertEquals(FlowDtoUtil.createLinkInfo(20000000, "src21", 22, "dst23", 24), flow.linkInfoList.get(0));
		__assertEquals(FlowDtoUtil.createLinkInfo(20000001, "src23", 24, "dst25", 26), flow.linkInfoList.get(1));
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.FlowDtoUtil#createConcreteFlow(java.util.List, int, int)}.
	 */
	@Test
	public void testCreateConcreteFlow_1_link() {
		FlowDto flow = FlowDtoUtil.createConcreteFlow(
				Arrays.asList(
						FlowDtoUtil.createLinkInfo(10, "src11", 12, "dst13", 14)
						), 
				100, 
				102);
		assertEquals(null, flow.type);
		assertEquals(null, flow.name);
		assertEquals(null, flow.id);
		__assertRequestMemberIsNull(flow);
		__assertPTInfo(flow, "src11", 12, "dst13", 14);
		assertEquals("00000012_00000014", flow.overlayLogicalList);
		assertEquals("00000012_00000014", flow.underlayLogicalList);
		assertEquals(1, flow.linkInfoList.size());
		__assertEquals(FlowDtoUtil.createLinkInfo(10, "src11", 12, "dst13", 14), flow.linkInfoList.get(0));
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.FlowDtoUtil#createConcreteFlow(java.util.List, int, int)}.
	 */
	@Test
	public void testCreateConcreteFlow_0_link() {
		FlowDto flow = FlowDtoUtil.createConcreteFlow(
				new ArrayList<LinkInfoDto>(), 
				200, 
				201);
		assertEquals(null, flow);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.FlowDtoUtil#createConcreteFlow(java.util.List, int, int)}.
	 */
	@Test
	public void testCreateConcreteFlow_2_link() {
		FlowDto flow = FlowDtoUtil.createConcreteFlow(
				Arrays.asList(
						FlowDtoUtil.createLinkInfo(20000000, "src21", 22, "dst23", 24),
						FlowDtoUtil.createLinkInfo(20000001, "src23", 24, "dst25", 26)
						), 
				200, 
				201);
		assertEquals(null, flow.type);
		assertEquals(null, flow.name);
		assertEquals(null, flow.id);
		__assertRequestMemberIsNull(flow);
		__assertPTInfo(flow, "src21", 22, "dst25", 26);
		assertEquals("00000022_00000026", flow.overlayLogicalList);
		assertEquals("00000022_00000024_00000026", flow.underlayLogicalList);
		assertEquals(2, flow.linkInfoList.size());
		__assertEquals(FlowDtoUtil.createLinkInfo(20000000, "src21", 22, "dst23", 24), flow.linkInfoList.get(0));
		__assertEquals(FlowDtoUtil.createLinkInfo(20000001, "src23", 24, "dst25", 26), flow.linkInfoList.get(1));
	}
	
	private void __assertRequestMemberIsNull(FlowDto flow) {
		assertEquals(null, flow.srcCENodeName);
		assertEquals(null, flow.srcCEPortNo);
		
		assertEquals(null, flow.dstCENodeName);
		assertEquals(null, flow.dstCEPortNo);
		
		assertEquals(null, flow.reqBandWidth);
		assertEquals(null, flow.reqDelay);
		assertEquals(null, flow.protectionLevel);
	}
	
	private void __assertPTInfo(FlowDto flow, String srcPtNodeName, Integer srcPtNodeId, String dstPtNodeName, Integer dstPtNodeId) {
		assertEquals(srcPtNodeName, flow.srcPTNodeName);
		assertEquals(srcPtNodeId, flow.srcPTNodeId);
		assertEquals(dstPtNodeName, flow.dstPTNodeName);
		assertEquals(dstPtNodeId, flow.dstPTNodeId);
	}
	
	private void __assertEquals(LinkInfoDto expected, LinkInfoDto actual) {
		assertEquals(expected.id, actual.id);
		assertEquals(expected.srcPTNodeName, actual.srcPTNodeName);
		assertEquals(expected.srcPTNodeId, actual.srcPTNodeId);
		assertEquals(expected.dstPTNodeName, actual.dstPTNodeName);
		assertEquals(expected.dstPTNodeId, actual.dstPTNodeId);
	}

}
