package org.o3project.mlo.server.rpc.servie;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.impl.logic.ConfigProviderImplTest;
import org.o3project.mlo.server.impl.rpc.service.OdenOSTopology;
import org.o3project.mlo.server.logic.ConfigProvider;
import org.o3project.mlo.server.logic.TopologyConfigConstants;
import org.o3project.mlo.server.rpc.entity.PTFlowEntity;
import org.o3project.mlo.server.rpc.entity.PTLinkEntity;

public class OdenOSConstantsTest implements TopologyConfigConstants {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLinkInfo() {
		ConfigProvider configProvider = ConfigProviderImplTest.createConfigProviderImpl(null);
		OdenOSTopology odenOSTopology = new OdenOSTopology();
		
		String linkNameTl1 = String.format("link-%s-%s_%s", AMN6400_1_2_LINK_ID, AMN64001_NAME, AMN64002_NAME);
		String linkNameTl2 = String.format("link-%s-%s_%s", AMN6400_2_3_LINK_ID, AMN64002_NAME, AMN64003_NAME);
		String linkNameTl3 = String.format("link-%s-%s_%s", AMN6400_1_3_LINK_ID, AMN64001_NAME, AMN64003_NAME);
		String linkNameTl4 = String.format("link-%s-%s_%s", AMN6400_2_1_LINK_ID, AMN64002_NAME, AMN64001_NAME);
		String linkNameTl5 = String.format("link-%s-%s_%s", AMN6400_3_2_LINK_ID, AMN64003_NAME, AMN64002_NAME);
		String linkNameTl6 = String.format("link-%s-%s_%s", AMN6400_3_1_LINK_ID, AMN64003_NAME, AMN64001_NAME);
		PTLinkEntity linkEntityTl1 = odenOSTopology.createPTLinkEntity(linkNameTl1, 10000, 9999, configProvider);
		PTLinkEntity linkEntityTl2 = odenOSTopology.createPTLinkEntity(linkNameTl2, 10000, 9999, configProvider);
		PTLinkEntity linkEntityTl3 = odenOSTopology.createPTLinkEntity(linkNameTl3, 10000, 8, configProvider);
		PTLinkEntity linkEntityTl4 = odenOSTopology.createPTLinkEntity(linkNameTl4, 10000, 9999, configProvider);
		PTLinkEntity linkEntityTl5 = odenOSTopology.createPTLinkEntity(linkNameTl5, 10000, 9999, configProvider);
		PTLinkEntity linkEntityTl6 = odenOSTopology.createPTLinkEntity(linkNameTl6, 10000, 8, configProvider);
		
		PTLinkEntity linkEntity = null;
		
		linkEntity = linkEntityTl1;
		assertEquals(linkEntity.linkId, "NW=SDN,Layer=Ether,TL=1");
		assertEquals(linkEntity.srcNode, "NW=SDN,NE=PT1");
		assertEquals(linkEntity.srcPort, "NW=SDN,NE=PT1,Layer=Ether,TTP=2");
		assertEquals(linkEntity.dstNode, "NW=SDN,NE=PT2");
		assertEquals(linkEntity.dstPort, "NW=SDN,NE=PT2,Layer=Ether,TTP=1");
		assertEquals(linkEntity.operStatus, "DOWN");
		assertEquals(linkEntity.reqLatency, "9999");
		assertEquals(linkEntity.reqBandwidth, "10000");
		assertEquals(linkEntity.establishmentStatus, "establishing");
		
		linkEntity = linkEntityTl2;
		assertEquals(linkEntity.linkId, "NW=SDN,Layer=Ether,TL=2");
		assertEquals(linkEntity.srcNode, "NW=SDN,NE=PT2");
		assertEquals(linkEntity.srcPort, "NW=SDN,NE=PT2,Layer=Ether,TTP=2");
		assertEquals(linkEntity.dstNode, "NW=SDN,NE=PT3");
		assertEquals(linkEntity.dstPort, "NW=SDN,NE=PT3,Layer=Ether,TTP=1");
		assertEquals(linkEntity.operStatus, "DOWN");
		assertEquals(linkEntity.reqLatency, "9999");
		assertEquals(linkEntity.reqBandwidth, "10000");
		assertEquals(linkEntity.establishmentStatus, "establishing");

		linkEntity = linkEntityTl3;
		assertEquals(linkEntity.linkId, "NW=SDN,Layer=Ether,TL=3");
		assertEquals(linkEntity.srcNode, "NW=SDN,NE=PT1");
		assertEquals(linkEntity.srcPort, "NW=SDN,NE=PT1,Layer=Ether,TTP=1");
		assertEquals(linkEntity.dstNode, "NW=SDN,NE=PT3");
		assertEquals(linkEntity.dstPort, "NW=SDN,NE=PT3,Layer=Ether,TTP=2");
		assertEquals(linkEntity.operStatus, "DOWN");
		assertEquals(linkEntity.reqLatency, "8");
		assertEquals(linkEntity.reqBandwidth, "10000");
		assertEquals(linkEntity.establishmentStatus, "establishing");
		
		linkEntity = linkEntityTl4;
		assertEquals(linkEntity.linkId, "NW=SDN,Layer=Ether,TL=4");
		assertEquals(linkEntity.srcNode, "NW=SDN,NE=PT2");
		assertEquals(linkEntity.srcPort, "NW=SDN,NE=PT2,Layer=Ether,TTP=1");
		assertEquals(linkEntity.dstNode, "NW=SDN,NE=PT1");
		assertEquals(linkEntity.dstPort, "NW=SDN,NE=PT1,Layer=Ether,TTP=2");
		assertEquals(linkEntity.operStatus, "DOWN");
		assertEquals(linkEntity.reqLatency, "9999");
		assertEquals(linkEntity.reqBandwidth, "10000");
		assertEquals(linkEntity.establishmentStatus, "establishing");
		
		linkEntity = linkEntityTl5;
		assertEquals(linkEntity.linkId, "NW=SDN,Layer=Ether,TL=5");
		assertEquals(linkEntity.srcNode, "NW=SDN,NE=PT3");
		assertEquals(linkEntity.srcPort, "NW=SDN,NE=PT3,Layer=Ether,TTP=1");
		assertEquals(linkEntity.dstNode, "NW=SDN,NE=PT2");
		assertEquals(linkEntity.dstPort, "NW=SDN,NE=PT2,Layer=Ether,TTP=2");
		assertEquals(linkEntity.operStatus, "DOWN");
		assertEquals(linkEntity.reqLatency, "9999");
		assertEquals(linkEntity.reqBandwidth, "10000");
		assertEquals(linkEntity.establishmentStatus, "establishing");
		
		linkEntity = linkEntityTl6;
		assertEquals(linkEntity.linkId, "NW=SDN,Layer=Ether,TL=6");
		assertEquals(linkEntity.srcNode, "NW=SDN,NE=PT3");
		assertEquals(linkEntity.srcPort, "NW=SDN,NE=PT3,Layer=Ether,TTP=2");
		assertEquals(linkEntity.dstNode, "NW=SDN,NE=PT1");
		assertEquals(linkEntity.dstPort, "NW=SDN,NE=PT1,Layer=Ether,TTP=1");
		assertEquals(linkEntity.operStatus, "DOWN");
		assertEquals(linkEntity.reqLatency, "8");
		assertEquals(linkEntity.reqBandwidth, "10000");
		assertEquals(linkEntity.establishmentStatus, "establishing");
	}
	
	@Test
	public void testFlowInfo() {
		ConfigProvider configProvider = ConfigProviderImplTest.createConfigProviderImpl(null);
		OdenOSTopology odenOSTopology = new OdenOSTopology();
		
		PTFlowEntity flowEntityFlow1 = odenOSTopology.createPTFlowEntity("flow1", 10000, 9999, configProvider);
		PTFlowEntity flowEntityFlow2 = odenOSTopology.createPTFlowEntity("flow2", 10000, 9999, configProvider);
		PTFlowEntity flowEntityFlow3 = odenOSTopology.createPTFlowEntity("flow3", 10000, 8, configProvider);
		PTFlowEntity flowEntityFlow4 = odenOSTopology.createPTFlowEntity("flow4", 10000, 8, configProvider);
		
		PTFlowEntity flowEntity = null;
		
		flowEntity = flowEntityFlow1;
		assertEquals(flowEntity.flowId, "flow1");
		assertEquals(flowEntity.flowType, "BASIC_FLOW");
		assertEquals(flowEntity.basicFlowMatchInNode, "NW=SDN,NE=PT1");
		assertEquals(flowEntity.basicFlowMatchInPort, "NW=SDN,NE=PT1,Layer=LSP,TTP=3");
		assertEquals(flowEntity.flowPath.size(), 2);
		assertEquals(flowEntity.flowPath.get(0), "NW=SDN,Layer=Ether,TL=1");
		assertEquals(flowEntity.flowPath.get(1), "NW=SDN,Layer=Ether,TL=2");
		assertEquals(flowEntity.basicFlowActionOutputNode, "NW=SDN,NE=PT3");
		assertEquals(flowEntity.basicFlowActionOutputPort, "NW=SDN,NE=PT3,Layer=LSP,TTP=3");
		
		flowEntity = flowEntityFlow2;
		assertEquals(flowEntity.flowId, "flow2");
		assertEquals(flowEntity.flowType, "BASIC_FLOW");
		assertEquals(flowEntity.basicFlowMatchInNode, "NW=SDN,NE=PT3");
		assertEquals(flowEntity.basicFlowMatchInPort, "NW=SDN,NE=PT3,Layer=LSP,TTP=3");
		assertEquals(flowEntity.flowPath.size(), 2);
		assertEquals(flowEntity.flowPath.get(0), "NW=SDN,Layer=Ether,TL=5");
		assertEquals(flowEntity.flowPath.get(1), "NW=SDN,Layer=Ether,TL=4");
		assertEquals(flowEntity.basicFlowActionOutputNode, "NW=SDN,NE=PT1");
		assertEquals(flowEntity.basicFlowActionOutputPort, "NW=SDN,NE=PT1,Layer=LSP,TTP=3");
		
		flowEntity = flowEntityFlow3;
		assertEquals(flowEntity.flowId, "flow3");
		assertEquals(flowEntity.flowType, "BASIC_FLOW");
		assertEquals(flowEntity.basicFlowMatchInNode, "NW=SDN,NE=PT1");
		assertEquals(flowEntity.basicFlowMatchInPort, "NW=SDN,NE=PT1,Layer=LSP,TTP=3");
		assertEquals(flowEntity.flowPath.size(), 1);
		assertEquals(flowEntity.flowPath.get(0), "NW=SDN,Layer=Ether,TL=3");
		assertEquals(flowEntity.basicFlowActionOutputNode, "NW=SDN,NE=PT3");
		assertEquals(flowEntity.basicFlowActionOutputPort, "NW=SDN,NE=PT3,Layer=LSP,TTP=3");
		
		flowEntity = flowEntityFlow4;
		assertEquals(flowEntity.flowId, "flow4");
		assertEquals(flowEntity.flowType, "BASIC_FLOW");
		assertEquals(flowEntity.basicFlowMatchInNode, "NW=SDN,NE=PT3");
		assertEquals(flowEntity.basicFlowMatchInPort, "NW=SDN,NE=PT3,Layer=LSP,TTP=3");
		assertEquals(flowEntity.flowPath.size(), 1);
		assertEquals(flowEntity.flowPath.get(0), "NW=SDN,Layer=Ether,TL=6");
		assertEquals(flowEntity.basicFlowActionOutputNode, "NW=SDN,NE=PT1");
		assertEquals(flowEntity.basicFlowActionOutputPort, "NW=SDN,NE=PT1,Layer=LSP,TTP=3");
	}
}
