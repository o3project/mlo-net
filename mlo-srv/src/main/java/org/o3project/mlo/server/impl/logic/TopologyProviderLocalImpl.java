/**
 * TopologyProviderDefaultImpl.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.o3project.mlo.server.component.ChannelObj;
import org.o3project.mlo.server.component.ComponentFactory;
import org.o3project.mlo.server.component.FlowObj;
import org.o3project.mlo.server.component.FlowRelayObj;
import org.o3project.mlo.server.component.NetDeviceObj;
import org.o3project.mlo.server.component.NodeObj;
import org.o3project.mlo.server.component.TopologyObj;
import org.o3project.mlo.server.logic.TopologyConfigConstants;
import org.o3project.mlo.server.logic.TopologyProvider;

/**
 * This class the implementation class of {@link TopologyProvider} interface.
 * This class locally provides a specific topology.
 */
public class TopologyProviderLocalImpl implements TopologyProvider, TopologyConfigConstants {
	
	private static final int MAX_DELAY = 9999;
	private static final int FAST_DELAY = 8;
	private static final int MIN_DELAY = 0;
	private static final int B_WIDTH = 10000;
	private static final int N_WIDTH = 10;
	
	ComponentFactory componentFactory;
	
	TopologyObj cachedTopoA;
	
	Map<String, FlowObj> cachedTopoAFlowMap;
	
	TopologyObj cachedTopoB;
	
	Map<String, FlowObj> cachedTopoBFlowMap;
	
	boolean isLoaded = false;
	
	/**
	 * Setter method (for DI setter injection).
	 * @param componentFactory the componentFactory to set
	 */
	public void setComponentFactory(ComponentFactory componentFactory) {
		this.componentFactory = componentFactory;
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.TopologyProvider#load()
	 */
	@Override
	public boolean load() {
		initTopoA();
		initTopoB();
		isLoaded = true;
		return isLoaded;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.TopologyProvider#getTopologyObj()
	 */
	@Override
	public TopologyObj getTopologyObj() {
		TopologyObj obj = null;
		if (isLoaded) {
			obj = componentFactory.createTopology(TopologyProviderLocalImpl.class.getSimpleName());
			obj.nodeMap.putAll(cachedTopoA.nodeMap);
			obj.nodeMap.putAll(cachedTopoB.nodeMap);
			obj.channelMap.putAll(cachedTopoA.channelMap);
			obj.channelMap.putAll(cachedTopoB.channelMap);
		}
		return obj;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.TopologyProvider#getFlowObjMap()
	 */
	@Override
	public Map<String, FlowObj> getFlowObjMap() {
		Map<String, FlowObj> flowObjMap = null;
		if (isLoaded) {
			flowObjMap = new LinkedHashMap<>();
			flowObjMap.putAll(cachedTopoAFlowMap);
			flowObjMap.putAll(cachedTopoBFlowMap);
		}
		return flowObjMap;
	}

	/**
	 * 
	 */
	private void initTopoA() {
		/*
		 * Nodes
		 */
		NodeObj nodeAmn64001 = createMplsNode(AMN64001_NAME, AMN64001_ID);
		NodeObj nodeAmn64002 = createMplsNode(AMN64002_NAME, AMN64002_ID);
		NodeObj nodeAmn64003 = createMplsNode(AMN64003_NAME, AMN64003_ID);
		NodeObj nodeTokyo = createCeNode("tokyo");
		NodeObj nodeNagoya = createCeNode("nagoya");
		NodeObj nodeOsaka = createCeNode("osaka");
		NodeObj nodeAkashi = createCeNode("akashi");
		
		/*
		 * Access channels
		 */
		ChannelObj ipKanto = createAccessChannel("ipKanto");
		ChannelObj ipKansai = createAccessChannel("ipKansai");
		ChannelObj ipChubu = createAccessChannel("ipChubu");

		/*
		 * Installs net devices and connects CE to access channels.
		 */
		NetDeviceObj portTokyo1 = createNetDevice("00000001");
		nodeTokyo.install(portTokyo1).connectTo(ipKanto);
		portTokyo1.meta.attributes.put("vlanId", "421");
		
		NetDeviceObj portTokyo2 = createNetDevice("00000002");
		nodeTokyo.install(portTokyo2).connectTo(ipKanto);
		portTokyo2.meta.attributes.put("vlanId", "422");
		
		NetDeviceObj portTokyoAny = createNetDevice("*");
		nodeTokyo.install(portTokyoAny).connectTo(ipKanto);
		
		NetDeviceObj portNagoya3 = createNetDevice("00000003");
		nodeNagoya.install(portNagoya3).connectTo(ipChubu);
		
		NetDeviceObj portOsaka4 = createNetDevice("00000004");
		nodeOsaka.install(portOsaka4).connectTo(ipKansai);
		portOsaka4.meta.attributes.put("vlanId", "421");
		
		NetDeviceObj portOsakaAny = createNetDevice("*");
		nodeOsaka.install(portOsakaAny).connectTo(ipKansai);
		
		NetDeviceObj portAkashi5 = createNetDevice("00000005");
		nodeAkashi.install(portAkashi5).connectTo(ipKansai);
		portAkashi5.meta.attributes.put("vlanId", "422");
	
		/*
		 * mpls channels
		 */
		ChannelObj mplsAmn12 = createMplsChannel("" + AMN6400_1_2_LINK_ID, AMN6400_1_2_LINK_ID, B_WIDTH, MAX_DELAY);
		ChannelObj mplsAmn23 = createMplsChannel("" + AMN6400_2_3_LINK_ID, AMN6400_2_3_LINK_ID, B_WIDTH, MAX_DELAY);
		ChannelObj mplsAmn13 = createMplsChannel("" + AMN6400_1_3_LINK_ID, AMN6400_1_3_LINK_ID, B_WIDTH, FAST_DELAY);
		ChannelObj mplsAmn21 = createMplsChannel("" + AMN6400_2_1_LINK_ID, AMN6400_2_1_LINK_ID, B_WIDTH, MAX_DELAY);
		ChannelObj mplsAmn32 = createMplsChannel("" + AMN6400_3_2_LINK_ID, AMN6400_3_2_LINK_ID, B_WIDTH, MAX_DELAY);
		ChannelObj mplsAmn31 = createMplsChannel("" + AMN6400_3_1_LINK_ID, AMN6400_3_1_LINK_ID, B_WIDTH, FAST_DELAY);
		
		Map<ChannelObj, ChannelObj> reversedChannelObjMap = new LinkedHashMap<>();
		reversedChannelObjMap.put(mplsAmn12, mplsAmn21);
		reversedChannelObjMap.put(mplsAmn23, mplsAmn32);
		reversedChannelObjMap.put(mplsAmn31, mplsAmn13);
		reversedChannelObjMap.put(mplsAmn21, mplsAmn12);
		reversedChannelObjMap.put(mplsAmn13, mplsAmn31);
		reversedChannelObjMap.put(mplsAmn32, mplsAmn23);

		/*
		 * Installs net devices and connnects PT to channels.
		 */
		NetDeviceObj portAmn64001Access = createNetDevice("amn64001Access");
		NetDeviceObj portAmn64001to64002 = createNetDevice("amn64001to64002");
		NetDeviceObj portAmn64001to64003 = createNetDevice("amn64001to64003");
		nodeAmn64001.install(portAmn64001Access).connectTo(ipKanto);
		nodeAmn64001.install(portAmn64001to64002).connectTo(mplsAmn12);
		nodeAmn64001.install(portAmn64001to64003).connectTo(mplsAmn31);
		
		NetDeviceObj portAmn64002Access = createNetDevice("amn64002Access");
		NetDeviceObj portAmn64002to64003 = createNetDevice("amn64002to64003");
		NetDeviceObj portAmn64002to64001 = createNetDevice("amn64002to64001");
		nodeAmn64002.install(portAmn64002Access).connectTo(ipChubu);
		nodeAmn64002.install(portAmn64002to64003).connectTo(mplsAmn23);
		nodeAmn64002.install(portAmn64002to64001).connectTo(mplsAmn12);
		
		NetDeviceObj portAmn64003Access = createNetDevice("amn64003Access");
		NetDeviceObj portAmn64003to64002 = createNetDevice("amn64003to64002");
		NetDeviceObj portAmn64003to64001 = createNetDevice("amn64003to64001");
		nodeAmn64003.install(portAmn64003Access).connectTo(ipKansai);
		nodeAmn64003.install(portAmn64003to64002).connectTo(mplsAmn23);
		nodeAmn64003.install(portAmn64003to64001).connectTo(mplsAmn31);
		
		/*
		 * Defines flows
		 */
		FlowObj flow12_23 = createFlow("flow1", B_WIDTH, MAX_DELAY,
				createFlowRelay(portAmn64001Access, portAmn64001to64002),
				createFlowRelay(portAmn64002to64001, portAmn64002to64003),
				createFlowRelay(portAmn64003to64002, portAmn64003Access));
		FlowObj flow23_31 = createFlow("flow23_31", B_WIDTH, MAX_DELAY, 
				createFlowRelay(portAmn64002Access, portAmn64002to64003),
				createFlowRelay(portAmn64003to64002, portAmn64003to64001),
				createFlowRelay(portAmn64001to64003, portAmn64001Access));
		FlowObj flow31_12 = createFlow("flow31_12", B_WIDTH, MAX_DELAY, 
				createFlowRelay(portAmn64003Access, portAmn64003to64001),
				createFlowRelay(portAmn64001to64003, portAmn64001to64002),
				createFlowRelay(portAmn64002to64001, portAmn64002Access));
		FlowObj flow21_13 = createReversedFlow("flow21_13", flow31_12, reversedChannelObjMap);
		FlowObj flow13_32 = createReversedFlow("flow13_32", flow23_31, reversedChannelObjMap);
		FlowObj flow32_21 = createReversedFlow("flow2", flow12_23, reversedChannelObjMap);
		
		FlowObj flow12 = createFlow("flow12", B_WIDTH, MAX_DELAY,
				createFlowRelay(portAmn64001Access, portAmn64001to64002),
				createFlowRelay(portAmn64002to64001, portAmn64002Access));
		FlowObj flow23 = createFlow("flow23", B_WIDTH, MAX_DELAY,
				createFlowRelay(portAmn64002Access, portAmn64002to64003),
				createFlowRelay(portAmn64003to64002, portAmn64003Access));
		FlowObj flow31 = createFlow("flow4", B_WIDTH, FAST_DELAY,
				createFlowRelay(portAmn64003Access, portAmn64003to64001),
				createFlowRelay(portAmn64001to64003, portAmn64001Access));
		FlowObj flow21 = createReversedFlow("flow21", flow12, reversedChannelObjMap);
		FlowObj flow13 = createReversedFlow("flow3", flow31, reversedChannelObjMap);
		FlowObj flow32 = createReversedFlow("flow32", flow23, reversedChannelObjMap);
		
		/*
		 * Sets up fields.
		 */
		TopologyObj topo = componentFactory.createTopology("topo-a");
		for (NodeObj node : Arrays.asList(
				nodeTokyo, nodeNagoya, nodeOsaka, nodeAkashi, 
				nodeAmn64001, nodeAmn64002, nodeAmn64003)) {
			topo.nodeMap.put(node.meta.id, node);
		}
		for (ChannelObj channel : Arrays.asList(
				ipKanto, ipChubu, ipKansai, 
				mplsAmn12, mplsAmn23, mplsAmn31, mplsAmn21, mplsAmn13, mplsAmn31)) {
			topo.channelMap.put(channel.meta.id, channel);
		}
		
		Map<String, FlowObj> flowMap = new LinkedHashMap<>();
		for (FlowObj flow : Arrays.asList(
				flow12_23, flow23_31, flow31_12, flow21_13, flow13_32, flow32_21,
				flow12, flow23, flow31, flow21, flow13, flow32)) {
			flowMap.put(flow.meta.id, flow);
		}
		
		cachedTopoA = topo;
		cachedTopoAFlowMap = flowMap;
	}

	/**
	 * 
	 */
	private void initTopoB() {
		/*
		 * Defines nodes.
		 */
		NodeObj nodeA = createMplsNode(PT_NODEA_NAME, PT_NODEA_ID);
		NodeObj nodeC = createMplsNode(PT_NODEC_NAME, PT_NODEC_ID);
		NodeObj nodeD = createMplsNode(PT_NODED_NAME, PT_NODED_ID);
		NodeObj tokyo123  = createCeNode("tokyo123");
		NodeObj nagoya123 = createCeNode("nagoya123");
		NodeObj nagoya234 = createCeNode("nagoya234");
		NodeObj nagoya345 = createCeNode("nagoya345");
		NodeObj osaka123  = createCeNode("osaka123");
		NodeObj osaka555  = createCeNode("osaka555");
		NodeObj osaka666  = createCeNode("osaka666");
		NodeObj osaka777  = createCeNode("osaka777");
		NodeObj osaka999  = createCeNode("osaka999");
		
		/*
		 * Defines channels.
		 */
		ChannelObj chIpKanto  = createAccessChannel("chIpKanto");
		ChannelObj chIpChubu  = createAccessChannel("chIpChubu");
		ChannelObj chIpKansai = createAccessChannel("chIpKansai");
		ChannelObj chMplsAD = createMplsChannel("PT_NODE_AD_LINK", PT_NODE_A_D_LINK_ID, null, null);
		ChannelObj chMplsDC = createMplsChannel("PT_NODE_DC_LINK", PT_NODE_D_C_LINK_ID, null, null);
		ChannelObj chMplsCA = createMplsChannel("PT_NODE_CA_LINK", PT_NODE_C_A_LINK_ID, null, null);
		ChannelObj chMplsDA = createMplsChannel("PT_NODE_DA_LINK", PT_NODE_D_A_LINK_ID, null, null);
		ChannelObj chMplsAC = createMplsChannel("PT_NODE_AC_LINK", PT_NODE_A_C_LINK_ID, null, null);
		ChannelObj chMplsCD = createMplsChannel("PT_NODE_CD_LINK", PT_NODE_C_D_LINK_ID, null, null);
		
		Map<ChannelObj, ChannelObj> reversedChannelObjMap = new LinkedHashMap<>();
		reversedChannelObjMap.put(chMplsAD, chMplsDA);
		reversedChannelObjMap.put(chMplsDC, chMplsCD);
		reversedChannelObjMap.put(chMplsCA, chMplsAC);
		reversedChannelObjMap.put(chMplsDA, chMplsAD);
		reversedChannelObjMap.put(chMplsAC, chMplsCA);
		reversedChannelObjMap.put(chMplsCD, chMplsDC);

		/*
		 * Defines ports, installs them and connects them to channels.
		 */
		NetDeviceObj portNodeAAccess = createNetDevice("portNodeAAccess");
		NetDeviceObj portNodeAtoD = createNetDevice("portNodeAtoD");
		NetDeviceObj portNodeAtoC = createNetDevice("portNodeAtoC");
		nodeA.install(portNodeAAccess).connectTo(chIpKanto);
		nodeA.install(portNodeAtoD).connectTo(chMplsAD);
		nodeA.install(portNodeAtoC).connectTo(chMplsCA);
		
		NetDeviceObj portNodeCAccess = createNetDevice("portNodeCAccess");
		NetDeviceObj portNodeCtoD = createNetDevice("portNodeCtoD");
		NetDeviceObj portNodeCtoA = createNetDevice("portNodeCtoA");
		nodeC.install(portNodeCAccess).connectTo(chIpKansai);
		nodeC.install(portNodeCtoD).connectTo(chMplsDC);
		nodeC.install(portNodeCtoA).connectTo(chMplsCA);
		
		NetDeviceObj portNodeDAccess = createNetDevice("portNodeDAccess");
		NetDeviceObj portNodeDtoC = createNetDevice("portNodeDtoC");
		NetDeviceObj portNodeDtoA = createNetDevice("portNodeDtoA");
		nodeD.install(portNodeDAccess).connectTo(chIpChubu);
		nodeD.install(portNodeDtoC).connectTo(chMplsDC);
		nodeD.install(portNodeDtoA).connectTo(chMplsAD);
		
		NetDeviceObj portTokyo123  = createNetDevice("*");
		NetDeviceObj portNagoya123 = createNetDevice("*");
		NetDeviceObj portNagoya234 = createNetDevice("*");
		NetDeviceObj portNagoya345 = createNetDevice("*");
		NetDeviceObj portOsaka123  = createNetDevice("*");
		NetDeviceObj portOsaka555  = createNetDevice("*");
		NetDeviceObj portOsaka666  = createNetDevice("*");
		NetDeviceObj portOsaka777  = createNetDevice("*");
		NetDeviceObj portOsaka999  = createNetDevice("*");
		tokyo123.install(portTokyo123).connectTo(chIpKanto);
		nagoya123.install(portNagoya123).connectTo(chIpChubu);
		nagoya234.install(portNagoya234).connectTo(chIpChubu);
		nagoya345.install(portNagoya345).connectTo(chIpChubu);
		osaka123.install(portOsaka123).connectTo(chIpKansai);
		osaka555.install(portOsaka555).connectTo(chIpKansai);
		osaka666.install(portOsaka666).connectTo(chIpKansai);
		osaka777.install(portOsaka777).connectTo(chIpKansai);
		osaka999.install(portOsaka999).connectTo(chIpKansai);
		
		/*
		 * Define flows.
		 */
		FlowObj flowAD_DC = createFlow("flowAD_DC", N_WIDTH, MIN_DELAY, 
				createFlowRelay(portNodeAAccess, portNodeAtoD),
				createFlowRelay(portNodeDtoA, portNodeDtoC),
				createFlowRelay(portNodeCtoD, portNodeCAccess));
		FlowObj flowDC_CA = createFlow("flowDC_CA", N_WIDTH, MIN_DELAY, 
				createFlowRelay(portNodeDAccess, portNodeDtoC),
				createFlowRelay(portNodeCtoD, portNodeCtoA),
				createFlowRelay(portNodeAtoC, portNodeAAccess));
		FlowObj flowCA_AD = createFlow("flowCA_AD", N_WIDTH, MIN_DELAY, 
				createFlowRelay(portNodeCAccess, portNodeCtoA),
				createFlowRelay(portNodeAtoC, portNodeAtoD),
				createFlowRelay(portNodeDtoA, portNodeDAccess));
		FlowObj flowCD_DA = createReversedFlow("flowCD_DA", flowAD_DC, reversedChannelObjMap);
		FlowObj flowAC_CD = createReversedFlow("flowAC_CD", flowDC_CA, reversedChannelObjMap);
		FlowObj flowDA_AC = createReversedFlow("flowDA_AC", flowCA_AD, reversedChannelObjMap);
		
		FlowObj flowAD = createFlow("flowAD", N_WIDTH, MIN_DELAY,
				createFlowRelay(portNodeAAccess, portNodeAtoD),
				createFlowRelay(portNodeDtoA, portNodeDAccess));
		FlowObj flowDC = createFlow("flowDC", N_WIDTH, MIN_DELAY,
				createFlowRelay(portNodeDAccess, portNodeDtoC),
				createFlowRelay(portNodeCtoD, portNodeCAccess));
		FlowObj flowCA = createFlow("flowCA", B_WIDTH, MIN_DELAY,
				createFlowRelay(portNodeCAccess, portNodeCtoA),
				createFlowRelay(portNodeAtoC, portNodeAAccess));
		FlowObj flowDA = createReversedFlow("flowDA", flowAD, reversedChannelObjMap);
		FlowObj flowCD = createReversedFlow("flowCD", flowDC, reversedChannelObjMap);
		FlowObj flowAC = createReversedFlow("flowAC", flowCA, reversedChannelObjMap);
		
		/*
		 * Sets up fields.
		 */
		TopologyObj topo = componentFactory.createTopology("topo-b");
		for (NodeObj node : Arrays.asList(nodeA, nodeC, nodeD, 
				tokyo123, nagoya123, nagoya234, nagoya345,
				osaka123, osaka555, osaka666, osaka777, osaka999)) {
			topo.nodeMap.put(node.meta.id, node);
		}
		for (ChannelObj channel : Arrays.asList(chIpKanto, chIpChubu, chIpKansai, 
				chMplsAC, chMplsCD, chMplsDA, chMplsCA, chMplsAD, chMplsDC)) {
			topo.channelMap.put(channel.meta.id, channel);
		}
		
		Map<String, FlowObj> flowMap = new LinkedHashMap<>();
		for (FlowObj flow : Arrays.asList(
				flowAD_DC, flowDC_CA, flowCA_AD, flowDA_AC, flowAC_CD, flowCD_DA,
				flowAD, flowDC, flowCA, flowDA, flowAC, flowCD)) {
			flowMap.put(flow.meta.id, flow);
		}
		
		cachedTopoB = topo;
		cachedTopoBFlowMap = flowMap;
	}

	private NodeObj createMplsNode(String name, Integer ptId) {
		return componentFactory.createMplsNode(name, ptId);
	}
	
	private NodeObj createCeNode(String name) {
		NodeObj obj = componentFactory.createNode(name, "ce");
		obj.meta.attributes.put("ceName", name);
		return obj;
	}
	
	private ChannelObj createAccessChannel(String name) {
		return componentFactory.createChannel(name, "access");
	}
	
	private ChannelObj createMplsChannel(String name, Integer linkId, Integer widthMbps, Integer delayMsec) {
		ChannelObj obj = componentFactory.createMplsChannel(name, linkId, widthMbps, delayMsec);
		return obj;
	}
	
	private NetDeviceObj createNetDevice(String name) {
		NetDeviceObj obj = componentFactory.createNetDevice(name);
		return obj;
	}
	
	private FlowObj createFlow(String name, Integer widthMbps, Integer delayMsec, FlowRelayObj... flowRelays) {
		FlowObj obj = componentFactory.createFlow(name, name, name, widthMbps, delayMsec, flowRelays);
		return obj;
	}
	
	private FlowRelayObj createFlowRelay(NetDeviceObj ingressNetDevice, NetDeviceObj egressNetDevice) {
		FlowRelayObj obj = componentFactory.createFlowRelay(ingressNetDevice, egressNetDevice);
		return obj;
	}
	
	private FlowObj createReversedFlow(String name, FlowObj flow, Map<ChannelObj, ChannelObj> reversedChannelObjMap) {
		FlowObj obj = componentFactory.createReversedFlow(name, name, flow, reversedChannelObjMap);
		return obj;
	}
}
