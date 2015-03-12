/**
 * TopologyProviderRemoteLdImplTest.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.component.ChannelObj;
import org.o3project.mlo.server.component.ComponentFactory;
import org.o3project.mlo.server.component.FlowObj;
import org.o3project.mlo.server.component.FlowRelayObj;
import org.o3project.mlo.server.component.NetDeviceObj;
import org.o3project.mlo.server.component.NodeObj;
import org.o3project.mlo.server.component.TopologyObj;
import org.o3project.mlo.server.dto.LdTopoDto;
import org.o3project.mlo.server.dto.RyLinkDto;
import org.o3project.mlo.server.dto.RySwitchDto;
import org.o3project.mlo.server.impl.rpc.service.LdServiceImpl;
import org.o3project.mlo.server.logic.HttpRequestInvoker;
import org.o3project.mlo.server.logic.MloException;

/**
 * TopologyProviderRemoteLdImplTest
 *
 */
public class TopologyProviderRemoteLdImplTest {

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
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyProviderRemoteLdImpl#getLdTopo()}.
	 * @throws MloException 
	 */
	@Test
	public void testGetLdTopo() throws MloException {
		TopologyProviderRemoteLdImpl obj = createObj(new HttpRequestInvokerDummyImpl());
		
		LdTopoDto ldTopoDto = null;
		
		ldTopoDto = obj.getLdTopo();
		assertNull(ldTopoDto);
		
		obj.load();
		
		ldTopoDto = obj.getLdTopo();
		assertNotNull(ldTopoDto);
		assertEquals(5, ldTopoDto.hosts.size());
		assertEquals(5, ldTopoDto.switches.size());
		assertEquals(11, ldTopoDto.bridges.size());
		assertEquals(1, ldTopoDto.controllers.size());
		assertEquals(9, ldTopoDto.flows.size());
		assertEquals(5, ldTopoDto.nameHostMap.size());
		assertEquals(5, ldTopoDto.nameSwitchMap.size());
		assertEquals(10, ldTopoDto.nameNodeMap.size());
		assertEquals(11, ldTopoDto.nameBridgeMap.size());
		assertEquals(9, ldTopoDto.nameFlowMap.size());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyProviderRemoteLdImpl#getRySwitches()}.
	 * @throws MloException 
	 */
	@Test
	public void testGetRySwitches() throws MloException {
		TopologyProviderRemoteLdImpl obj = createObj(new HttpRequestInvokerDummyImpl());
		
		List<RySwitchDto> rySwitches = null;
		
		rySwitches = obj.getRySwitches();
		assertNull(rySwitches);
		
		obj.load();
		
		rySwitches = obj.getRySwitches();
		assertNotNull(rySwitches);
		assertEquals(10, rySwitches.size());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyProviderRemoteLdImpl#getRyLinks()}.
	 * @throws MloException 
	 */
	@Test
	public void testGetRyLinks() throws MloException {
		TopologyProviderRemoteLdImpl obj = createObj(new HttpRequestInvokerDummyImpl());
		
		List<RyLinkDto> ryLinks = null;
		
		ryLinks = obj.getRyLinks();
		assertNull(ryLinks);
		
		obj.load();
		
		ryLinks = obj.getRyLinks();
		assertNotNull(ryLinks);
		assertEquals(22, ryLinks.size());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyProviderRemoteLdImpl#geTopologyObj()}.
	 */
	@Test
	public void testGeTopologyObj() {
		TopologyProviderRemoteLdImpl obj = createObj(new HttpRequestInvokerDummyImpl());
		
		TopologyObj topologyObj = null;
		
		topologyObj = obj.getTopologyObj();
		assertNull(topologyObj);
		
		obj.load();
		
		topologyObj = obj.getTopologyObj();
		assertNotNull(topologyObj);
		assertEquals(10, topologyObj.nodeMap.size());
		for (String nodeName : Arrays.asList(
				"s1", "s2", "s3", "s4", "s5", 
				"h1", "h2", "tokyo", "osaka", "akashi")) {
			NodeObj node = topologyObj.nodeMap.get(nodeName);
			_assertNodeObj(nodeName, node);
		}
		assertEquals(11, topologyObj.channelMap.size());
		for (String channelName : Arrays.asList(
				"br1", "br2", 
				"br3", "br4", "br5", "br6", "br7", "br8",
				"br11", "br12", "br21")) {
			ChannelObj channel = topologyObj.channelMap.get(channelName);
			_assertChannelObj(channelName, channel);
		}
	}

	/**
	 * @param nodeName
	 * @param node
	 */
	private static void _assertNodeObj(String nodeName, NodeObj node) {
		assertNotNull(nodeName, node);
		assertTrue(node.netDeviceMap.size() != 0);
		for (NetDeviceObj netDevice : node.netDeviceMap.values()) {
			assertEquals(node, netDevice.node);
			assertNotNull(netDevice.channel);
		}
	}

	/**
	 * @param channelName
	 * @param channel
	 */
	private static void _assertChannelObj(String channelName, ChannelObj channel) {
		assertNotNull(channelName, channel);
		assertTrue(channel.netDeviceSet.size() != 0);
		for (NetDeviceObj netDevice : channel.netDeviceSet) {
			assertEquals(channel, netDevice.channel);
			_assertNodeObj(channelName, netDevice.node);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyProviderRemoteLdImpl#getFlowObjMap()}.
	 */
	@Test
	public void testGetFlowObjMap() {
		TopologyProviderRemoteLdImpl obj = createObj(new HttpRequestInvokerDummyImpl());
		
		Map<String, FlowObj> flowObjMap = null;
		
		flowObjMap = obj.getFlowObjMap();
		assertNull(flowObjMap);
		
		obj.load();
		
		flowObjMap = obj.getFlowObjMap();
		assertNotNull(flowObjMap);
		assertEquals(18, flowObjMap.size());
		for (String flowName : Arrays.asList(
				"of1slow", "of2fast", "of3cutthrough",
				"osaka11slow", "osaka12fast", "osaka13cutthrough",
				"akashi21slow", "akashi22fast", "akashi23cutthrough"
				)) {
			for (String suffix : Arrays.asList("-fw", "-bw")) {
				String flowKey = flowName + suffix;
				FlowObj flow = flowObjMap.get(flowKey);
				assertNotNull(flowKey, flow);
				assertTrue(flow.flowRelays.size() != 0);
				for (FlowRelayObj flowRelay : flow.flowRelays) {
					_assertNodeObj(flowKey, flowRelay.relayNode);
					_assertChannelObj(flowKey, flowRelay.ingressChannel);
					_assertChannelObj(flowKey, flowRelay.egressChannel);
					_assertNodeObj(flowKey, flowRelay.ingressNetDevice.node);
					_assertChannelObj(flowKey, flowRelay.ingressNetDevice.channel);
					_assertNodeObj(flowKey, flowRelay.egressNetDevice.node);
					_assertChannelObj(flowKey, flowRelay.egressNetDevice.channel);
				}
			}
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyProviderRemoteLdImpl#convertLdDpidToRyDpid(java.lang.String)}.
	 */
	@Test
	public void testConvertLdDpidToRyDpid() {
		String ldDpid = null;
		String expected = null;
		String actual = null;
		
		ldDpid = "0.00:00:00:00:00:02";
		expected = "0000000000000002";
		actual = TopologyProviderRemoteLdImpl.convertLdDpidToRyDpid(ldDpid);
		assertEquals(expected, actual);
		
		ldDpid = "0.52:54:00:00:00:2";
		expected = "0000525400000002";
		actual = TopologyProviderRemoteLdImpl.convertLdDpidToRyDpid(ldDpid);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyProviderRemoteLdImpl#formatNumber(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testFormatNumber() {
		String sNum = null;
		sNum = TopologyProviderRemoteLdImpl.formatNumber("a", "%02x");
		assertEquals("0a", sNum);
		sNum = TopologyProviderRemoteLdImpl.formatNumber("52", "%02x");
		assertEquals("52", sNum);
		sNum = TopologyProviderRemoteLdImpl.formatNumber("0b", "%02x");
		assertEquals("0b", sNum);
	}
	
	static TopologyProviderRemoteLdImpl createObj(HttpRequestInvoker httpRequestInvoker) {
		ConfigProviderImpl configProvider = ConfigProviderImplTest.createConfigProviderImpl(null);
		
		LdServiceImpl ldService = new LdServiceImpl();
		ldService.setConfigProvider(configProvider);
		ldService.setHttpRequestInvoker(httpRequestInvoker);
		
		ComponentFactory componentFactory = new ComponentFactory();
		
		TopologyProviderRemoteLdImpl obj = new TopologyProviderRemoteLdImpl();
		obj.setLdService(ldService);
		obj.setComponentFactory(componentFactory);
		
		return obj;
	}
	
	static TopologyRepositoryDefaultImpl createTopologyRepository(TopologyProviderRemoteLdImpl obj) {
		ConfigProviderImpl configProviderImpl = ConfigProviderImplTest.createConfigProviderImpl(null);
		
		ComponentFactory componentFactory = new ComponentFactory();
		
		TopologyProviderLocalImpl topologyProviderLocalImpl = new TopologyProviderLocalImpl();
		topologyProviderLocalImpl.setComponentFactory(componentFactory);
		
		TopologyRepositoryDefaultImpl topologyRepositoryDefaultImpl = new TopologyRepositoryDefaultImpl();
		topologyRepositoryDefaultImpl.setConfigProvider(configProviderImpl);
		topologyRepositoryDefaultImpl.setTopologyProviderLocalImpl(topologyProviderLocalImpl);
		topologyRepositoryDefaultImpl.setTopologyProviderRemoteLdImpl(obj);

		return topologyRepositoryDefaultImpl;

	}

}
