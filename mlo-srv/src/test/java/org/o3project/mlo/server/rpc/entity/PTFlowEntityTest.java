package org.o3project.mlo.server.rpc.entity;


import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.rpc.entity.PTFlowEntity;

public class PTFlowEntityTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConstructor() {
		PTFlowEntity flow = createFlow1FlowEntity();
		assertEquals(flow.flowId, "flow1");
		assertEquals(flow.flowType, "BASIC_FLOW");
		assertEquals(flow.basicFlowMatchInNode, "NW=SDN,NE=PT1");
		assertEquals(flow.basicFlowMatchInPort, "NW=SDN,NE=PT1,Layer=LSP,TTP=3");
		assertEquals(flow.flowPath.size(), 2);
		assertEquals(flow.flowPath.get(0), "NW=SDN,Layer=Ether,TL=1");
		assertEquals(flow.flowPath.get(1), "NW=SDN,Layer=Ether,TL=2");
		assertEquals(flow.basicFlowActionOutputNode, "NW=SDN,NE=PT3");
		assertEquals(flow.basicFlowActionOutputPort, "NW=SDN,NE=PT3,Layer=LSP,TTP=3");
		assertEquals(flow.attributes.size(), 2);
		assertEquals(flow.attributes.get("req_bandwidth"), "10000");
		assertEquals(flow.attributes.get("req_latency"), "9999");
	}
	
	@Test
	public void testToString() {
		PTFlowEntity flow = createFlow1FlowEntity();
		assertEquals(flow.toString(), "flow1, BASIC_FLOW, NW=SDN,NE=PT1, NW=SDN,NE=PT1,Layer=LSP,TTP=3, [NW=SDN,Layer=Ether,TL=1, NW=SDN,Layer=Ether,TL=2], NW=SDN,NE=PT3, NW=SDN,NE=PT3,Layer=LSP,TTP=3, [req_latency, 9999][req_bandwidth, 10000]");
	}

	/**
	 * @param list
	 * @return
	 */
	private static PTFlowEntity createFlow1FlowEntity() {
		List<String> list = Arrays.asList("NW=SDN,Layer=Ether,TL=1", "NW=SDN,Layer=Ether,TL=2");
		return new PTFlowEntity("flow1", "BASIC_FLOW", "NW=SDN,NE=PT1", "NW=SDN,NE=PT1,Layer=LSP,TTP=3", list, "NW=SDN,NE=PT3", "NW=SDN,NE=PT3,Layer=LSP,TTP=3", 10000, 9999);
	}
	
}
