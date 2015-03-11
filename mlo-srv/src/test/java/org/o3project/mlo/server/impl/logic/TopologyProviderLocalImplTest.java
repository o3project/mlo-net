/**
 * TopologyProviderLocalImplTest.java
 * (C) 2015, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.component.ChannelObj;
import org.o3project.mlo.server.component.ComponentFactory;
import org.o3project.mlo.server.component.FlowObj;
import org.o3project.mlo.server.component.TopologyObj;
import org.o3project.mlo.server.dto.FlowDto;
import org.o3project.mlo.server.dto.LinkInfoDto;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.TopologyProvider;

/**
 * TopologyProviderLocalImplTest
 *
 */
public class TopologyProviderLocalImplTest {

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
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyProviderLocalImpl#geTopologyObj()}.
	 */
	@Test
	public void testGeTopologyObj() {
		TopologyProviderLocalImpl obj = new TopologyProviderLocalImpl();
		
		ComponentFactory componentFactory = new ComponentFactory();
		obj.setComponentFactory(componentFactory);
		
		TopologyObj topologyObj = null;
		
		topologyObj = obj.getTopologyObj();
		assertNull(topologyObj);

		boolean actual = obj.load();
		assertEquals(true, actual);
		
		topologyObj = obj.getTopologyObj();
		assertNotNull(topologyObj);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyProviderLocalImpl#getFlowObjMap()}.
	 */
	@Test
	public void testGetFlowObjMap() {
		TopologyProviderLocalImpl obj = new TopologyProviderLocalImpl();
		
		ComponentFactory componentFactory = new ComponentFactory();
		obj.setComponentFactory(componentFactory);
		
		Map<String, FlowObj> flowObjMap = null;
		
		flowObjMap = obj.getFlowObjMap();
		assertNull(flowObjMap);

		boolean actual = obj.load();
		assertEquals(true, actual);
		
		flowObjMap = obj.getFlowObjMap();
		assertNotNull(flowObjMap);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyConfigProviderDefaultImpl#createCePtNodeMap()}.
	 * @throws MloException 
	 */
	@Test
	public void testCreateCePtNodeMap() throws MloException {
		__testQueryCe("tokyo", "00000001");
		__testQueryCe("tokyo", "00000002");
		__testQueryCe("nagoya", "00000003");
		__testQueryCe("osaka", "00000004");
		__testQueryCe("akashi", "00000005");
		__testQueryCe("tokyo123", null);
		__testQueryCe("nagoya123", null);
		__testQueryCe("nagoya234", null);
		__testQueryCe("nagoya345", null);
		__testQueryCe("osaka123", null);
		__testQueryCe("osaka555", null);
		__testQueryCe("osaka666", null);
		__testQueryCe("osaka777", null);
		__testQueryCe("osaka999", null);
		__testQueryCe_anomaly("yokohama", "00000010");
		__testQueryCe("tokyo", null);
		__testQueryCe("osaka", null);

	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyConfigProviderDefaultImpl#createCeVlanIdMap()}.
	 */
	@Test
	public void testCreateCeVlanIdMap() {
		__testGetVlanId("tokyo", "00000001", "421");
		__testGetVlanId("tokyo", "00000002", "422");
		__testGetVlanId("osaka", "00000004", "421");
		__testGetVlanId("akashi", "00000005", "422");
		__testGetVlanId("tokyo", "00000100", "430");
		__testGetVlanId("tokyo", "00000199", "529");
		__testGetVlanId_anomaly("tokyo", "00000050");
		__testGetVlanId("osaka", "00000100", "430");
		__testGetVlanId("osaka", "00000199", "529");
		__testGetVlanId_anomaly("osaka", "00000050");
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyConfigProviderDefaultImpl#createFlowTemplates()}.
	 */
	@Test
	public void testCreateFlowTemplates() {
		TopologyRepositoryDefaultImpl topologyRepositoryDefaultImpl = TopologyRepositoryDefaultImplTest.createObj();
		topologyRepositoryDefaultImpl.setTopologyProviderRemoteLdImpl(null);
		topologyRepositoryDefaultImpl.loadData();
		List<FlowDto> flows = topologyRepositoryDefaultImpl.getFlowDtoTemplates();
		assertEquals(24, flows.size());
		
		FlowDto flow = null;
		
		// flowTemplates.add(createConcreteFlow(Arrays.asList(link31), B_WIDTH, FAST_DELAY));
		flow = flows.get(15);
		assertEquals(null, flow.type);
		assertEquals("flow2", flow.name);
		assertEquals(null, flow.id);
		__assertRequestMemberIsNull(flow);
		__assertPTInfo(flow, "AMN64003", 3, "AMN64001", 1);
		assertEquals(new Integer(10000), flow.usedBandWidth);
		assertEquals(new Integer(9999), flow.delayTime);
		assertEquals("00000003_00000001", flow.overlayLogicalList);
		assertEquals("00000003_00000002_00000001", flow.underlayLogicalList);
		assertEquals(2, flow.linkInfoList.size());
		__assertEquals(FlowDtoUtil.createLinkInfo(50000032, "AMN64003", 3, "AMN64002", 2), flow.linkInfoList.get(0));
		__assertEquals(FlowDtoUtil.createLinkInfo(50000021, "AMN64002", 2, "AMN64001", 1), flow.linkInfoList.get(1));

		
		// flowTemplates.add(createConcreteFlow(Arrays.asList(link12, link23), B_WIDTH, MAX_DELAY));
		flow = flows.get(21);
		assertEquals(null, flow.type);
		assertEquals("flow3", flow.name);
		assertEquals(null, flow.id);
		__assertRequestMemberIsNull(flow);
		__assertPTInfo(flow, "AMN64001", 1, "AMN64003", 3);
		assertEquals(new Integer(10000), flow.usedBandWidth);
		assertEquals(new Integer(8), flow.delayTime);
		assertEquals("00000001_00000003", flow.overlayLogicalList);
		assertEquals("00000001_00000003", flow.underlayLogicalList);
		assertEquals(1, flow.linkInfoList.size());
		__assertEquals(FlowDtoUtil.createLinkInfo(50000013, "AMN64001", 1, "AMN64003", 3), flow.linkInfoList.get(0));
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
	
	private void __testQueryCe(String ceNodeName, String ceNodePortNo) throws MloException {
		TopologyRepositoryDefaultImpl topologyRepository = createTopologyRepository();
		TopologyProvider obj = topologyRepository.getTopologyProviderLocalImpl();
		ChannelObj channelObj = topologyRepository.queryCeChannel(ceNodeName, ceNodePortNo, obj.getTopologyObj(), obj.getFlowObjMap());
		assertNotNull(channelObj);
	}
	
	private void __testQueryCe_anomaly(String ceNodeName, String ceNodePortNo) {
		TopologyRepositoryDefaultImpl topologyRepository = createTopologyRepository();
		TopologyProvider obj = topologyRepository.getTopologyProviderLocalImpl();
		ChannelObj channelObj = null;
		try {
			channelObj = topologyRepository.queryCeChannel(ceNodeName, ceNodePortNo, obj.getTopologyObj(), obj.getFlowObjMap());
			fail();
		} catch (MloException e) {
			assertNull(channelObj);
		}
	}
	
	private void __testGetVlanId(String ceNodeName, String ceNodePortNo, String expected) {
		TopologyRepositoryDefaultImpl topologyRepository = createTopologyRepository();
		String actual = topologyRepository.queryVlanId(ceNodeName, ceNodePortNo);
		assertEquals(expected, actual);
	}
	
	private void __testGetVlanId_anomaly(String ceNodeName, String ceNodePortNo) {
		TopologyRepositoryDefaultImpl topologyRepository = createTopologyRepository();
		String actual = topologyRepository.queryVlanId(ceNodeName, ceNodePortNo);
		assertNull(actual);
	}
	
	private TopologyRepositoryDefaultImpl createTopologyRepository() {
		ConfigProviderImpl configProviderImpl = ConfigProviderImplTest.createConfigProviderImpl(null);
		return TopologyRepositoryDefaultImplTest.createObjLoaded(configProviderImpl);
	}

}
