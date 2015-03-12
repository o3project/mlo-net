/**
 * TopologyConfigProviderRemoteImpl.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.component.ChannelObj;
import org.o3project.mlo.server.component.ComponentFactory;
import org.o3project.mlo.server.component.FlowObj;
import org.o3project.mlo.server.component.FlowRelayObj;
import org.o3project.mlo.server.component.NetDeviceObj;
import org.o3project.mlo.server.component.NodeObj;
import org.o3project.mlo.server.component.TopologyObj;
import org.o3project.mlo.server.dto.LdBridgeDto;
import org.o3project.mlo.server.dto.LdFlowDto;
import org.o3project.mlo.server.dto.LdNodeDto;
import org.o3project.mlo.server.dto.LdTopoDto;
import org.o3project.mlo.server.dto.RyLinkDto;
import org.o3project.mlo.server.dto.RySwitchDto;
import org.o3project.mlo.server.logic.LdTopologyRepository;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.TopologyConfigConstants;
import org.o3project.mlo.server.logic.TopologyProvider;
import org.o3project.mlo.server.rpc.service.LdService;

/**
 * This class the implementation class of {@link TopologyProvider} interface.
 * This class provides a topology from remote LD.
 */
public class TopologyProviderRemoteLdImpl implements TopologyProvider, LdTopologyRepository, TopologyConfigConstants {
	private static final Log LOG = LogFactory.getLog(TopologyProviderRemoteLdImpl.class);

	private static final int RADIX_16 = 16;

	private static final int PT_NODE_ID_THRESHOLD = 100000000;

	private static final int PT_NODE_ID_OFFSET    =  40000001;
	
	private static final int WIDTH_MBPS_MAX = 99999999;
	
	private static final int DELAY_MSEC_MAX = 9999;
	
	LdService ldService;
	
	ComponentFactory componentFactory;
	
	private final CacheData cacheData = new CacheData(); 
	
	/**
	 * Setter method (for DI setter injection).
	 * @param ldService the ldService to set
	 */
	public void setLdService(LdService ldService) {
		this.ldService = ldService;
	}
	
	/**
	 * Setter method (for DI setter injection).
	 * @param componentFactory the componentFactory to set
	 */
	public void setComponentFactory(ComponentFactory componentFactory) {
		this.componentFactory = componentFactory;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.LdTopologyRepository#getLdTopo()
	 */
	@Override
	public LdTopoDto getLdTopo() throws MloException {
		return cacheData.ldTopoDto;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.LdTopologyRepository#getRySwitches()
	 */
	@Override
	public List<RySwitchDto> getRySwitches() throws MloException {
		return cacheData.rySwitches;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.LdTopologyRepository#getRyLinks()
	 */
	@Override
	public List<RyLinkDto> getRyLinks() throws MloException {
		return cacheData.ryLinks;
	}

	/**
	 * @throws MloException
	 */
	private void loadRemoteTopology(CacheData cache) throws MloException {
		
		// Load rySwitches, ryPorts, ldTopoDto fields.
		List<RySwitchDto> rySwitches = ldService.getRySwitches();
		List<RyLinkDto> ryLinks = ldService.getRyLinks();
		LdTopoDto ldTopoDto = ldService.getLdTopo();
		
		ldTopoDto.nameNodeMap = createNameNodeMap(ldTopoDto.hosts);
		ldTopoDto.nameNodeMap = putNameNodeMap(ldTopoDto.switches, ldTopoDto.nameNodeMap);
		ldTopoDto.nameHostMap = createNameNodeMap(ldTopoDto.hosts);
		ldTopoDto.nameSwitchMap = createNameNodeMap(ldTopoDto.switches);
		ldTopoDto.nameFlowMap = createNameFlowMap(ldTopoDto.flows);
		ldTopoDto.nameBridgeMap = createNameBridgeMap(ldTopoDto.bridges);
		
		Map<String, Integer> ldDpidPtNodeIdMap = createLdDpidPtNodeIdMap(ldTopoDto);
		
		cache.rySwitches = rySwitches;
		cache.ryLinks = ryLinks;
		cache.ldTopoDto = ldTopoDto;
		cache.ldDpidPtNodeIdMap = ldDpidPtNodeIdMap;
	}

	/**
	 * @param ldTopoDto
	 * @return
	 */
	private Map<String, Integer> createLdDpidPtNodeIdMap(LdTopoDto ldTopoDto) {
		Map<String, Integer> ldDpidPtNodeIdMap = new LinkedHashMap<>();
		int ptNodeId = PT_NODE_ID_OFFSET;
		List<LdNodeDto> nodes = null;
		nodes = ldTopoDto.hosts;
		for (LdNodeDto node : nodes) {
			ldDpidPtNodeIdMap.put(node.dpid, ptNodeId);
			LOG.info("(ldDpid, ptNodeId) = (" + node.dpid + ", " + ptNodeId + ")");
			ptNodeId = (ptNodeId + 1) % PT_NODE_ID_THRESHOLD;
		}
		nodes = ldTopoDto.switches;
		for (LdNodeDto node : nodes) {
			ldDpidPtNodeIdMap.put(node.dpid, ptNodeId);
			LOG.info("(ldDpid, ptNodeId) = (" + node.dpid + ", " + ptNodeId + ")");
			ptNodeId = (ptNodeId + 1) % PT_NODE_ID_THRESHOLD;
		}
		return ldDpidPtNodeIdMap;
	}
	
	private static int calcWidthMbps(int value, int prevValue) {
		return Math.min(value, prevValue);
	}
	
	private static int calcDelayMsec(int value, int prevValue) {
		long candidate = ((long) value) + prevValue;
		if (candidate > (long) DELAY_MSEC_MAX) {
			return DELAY_MSEC_MAX;
		} else {
			return (value + prevValue);
		}
	}
	
	private static int convertToWidthMbps(String ldWidth) {
		if (ldWidth == null) {
			ldWidth = LdBridgeDto.DEFAULT_BW;
		}
		String widthMbpsSuffix = "000kbps";
		return Integer.parseInt(ldWidth.replace(widthMbpsSuffix, ""));
	}
	
	private static int convertToDelayMsec(String ldDelay) {
		if (ldDelay == null) {
			ldDelay = LdBridgeDto.DEFAULT_DELAY;
		}
		String delayMsecSuffix = "ms";
		return Integer.parseInt(ldDelay.replace(delayMsecSuffix, ""));
	}
	
	private static Map<String, LdNodeDto> createNameNodeMap(List<LdNodeDto> dtos) {
		Map<String, LdNodeDto> nameObjMap = new HashMap<String, LdNodeDto>();
		return putNameNodeMap(dtos, nameObjMap);
	}
	
	private static Map<String, LdNodeDto> putNameNodeMap(List<LdNodeDto> dtos, Map<String, LdNodeDto> nameObjMap) {
		for (LdNodeDto dto: dtos) {
			nameObjMap.put(dto.name, dto);
			
			if (dto.portNames == null && dto.brNames != null) {
				dto.portNames = new ArrayList<String>();
				for (int idx = 0; idx < dto.brNames.size(); idx += 1) {
					dto.portNames.add("eth" + (idx + 1));
				}
			}
		}
		return nameObjMap;
	}
	
	private static Map<String, LdFlowDto> createNameFlowMap(List<LdFlowDto> dtos) {
		Map<String, LdFlowDto> nameObjMap = new HashMap<String, LdFlowDto>();
		for (LdFlowDto dto: dtos) {
			nameObjMap.put(dto.name, dto);
		}
		return nameObjMap;
	}
	
	private static Map<String, LdBridgeDto> createNameBridgeMap(List<LdBridgeDto> dtos) {
		Map<String, LdBridgeDto> nameObjMap = new HashMap<String, LdBridgeDto>();
		for (LdBridgeDto dto: dtos) {
			nameObjMap.put(dto.name, dto);
		}
		return nameObjMap;
	}
	
	static String convertLdDpidToRyDpid(String ldDpid) {
		String[] splited = null;
		splited = ldDpid.split("\\.", 2);
		String firstHalf = splited[0];
		String secondHalf = splited[1];
		splited = secondHalf.split(":");
		
		StringBuilder ryDpid = new StringBuilder();
		ryDpid.append(formatNumber(firstHalf, "%04x"));
		for (String part : splited) {
			ryDpid.append(formatNumber(part, "%02x"));
		}
		return ryDpid.toString();
	}
	
	static String formatNumber(String orig, String format) {
		Integer value = Integer.parseInt(orig, RADIX_16);
		return String.format(format, value);	
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.TopologyProvider#load()
	 */
	@Override
	public boolean load() {
		boolean isLoaded = false;
		try {
			loadRemoteTopology(cacheData);
			
			LdTopoDto ldTopoDto = cacheData.ldTopoDto;
			List<RySwitchDto> rySwitches = cacheData.rySwitches;
			List<RyLinkDto> ryLinks = cacheData.ryLinks;
			Map<String, Integer> ldDpidPtNodeIdMap = cacheData.ldDpidPtNodeIdMap;
			
			TopologyObj topologyObj = createTopologyObj(ldTopoDto, rySwitches, ryLinks, ldDpidPtNodeIdMap);
			Map<String, FlowObj> flowObjMap = createFlowObjMap(ldTopoDto, topologyObj);
			
			cacheData.topologyObj = topologyObj;
			cacheData.flowObjMap = flowObjMap;
			
			isLoaded = true;
		} catch (MloException e) {
			LOG.error("Failed to load remote topology.", e);
			isLoaded = false;
		}
		return isLoaded;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.TopologyProvider#getTopologyObj()
	 */
	@Override
	public TopologyObj getTopologyObj() {
		return cacheData.topologyObj;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.TopologyProvider#getFlowObjMap()
	 */
	@Override
	public Map<String, FlowObj> getFlowObjMap() {
		return cacheData.flowObjMap;
	}

	/**
	 * @param ldTopoDto
	 * @param rySwitches
	 * @param ryLinks
	 * @param ldDpidPtNodeIdMap
	 * @return
	 */
	private Map<String, FlowObj> createFlowObjMap(LdTopoDto ldTopoDto, TopologyObj topologyObj) {
		Map<String, FlowObj> flowObjMap = new LinkedHashMap<>();
		Map<String, LdFlowDto> nameFlowMap = ldTopoDto.nameFlowMap;
		for (Map.Entry<String, LdFlowDto> entry : nameFlowMap.entrySet()) {
			String ldFlowName = entry.getKey();
			LdFlowDto ldFlowDto = entry.getValue();
			
			String fwOdenosFlowName = ldFlowName + "-fw";
			String bwOdenosFlowName = ldFlowName + "-bw";
			String mplsLspId = ldFlowName;
			
			List<String> fwBrNames = new ArrayList<>(ldFlowDto.brNames);
			FlowObj fwFlowObj = createFlowObj(fwOdenosFlowName, fwOdenosFlowName, mplsLspId, fwBrNames, topologyObj);
			FlowObj bwFlowObj = componentFactory.createReversedFlow(bwOdenosFlowName, bwOdenosFlowName, fwFlowObj, null);
			
			flowObjMap.put(fwFlowObj.meta.id, fwFlowObj);
			flowObjMap.put(bwFlowObj.meta.id, bwFlowObj);
		}
		return flowObjMap;
	}

	/**
	 * @param odenosFlowName
	 * @param mplsLspId 
	 * @param brNames
	 * @param topologyObj
	 */
	private FlowObj createFlowObj(String flowObjId, String odenosFlowName, String mplsLspId, List<String> brNames, TopologyObj topologyObj) {
		int widthMbps = WIDTH_MBPS_MAX;
		int delayMsec = 0;
		ChannelObj prevChannelObj = null;
		List<FlowRelayObj> flowRelayList = new ArrayList<>();
		for (String brName : brNames) {
			ChannelObj channelObj = topologyObj.channelMap.get(brName);
			
			if (isMplsChannel(channelObj)) {
				int linkWidthMbps = (Integer) channelObj.meta.attributes.get("widthMbps");
				int linkDelayMsec = (Integer) channelObj.meta.attributes.get("delayMsec");
				widthMbps = calcWidthMbps(linkWidthMbps, widthMbps);
				delayMsec = calcDelayMsec(linkDelayMsec, delayMsec);
			}
			
			if (prevChannelObj != null) {
				NodeObj relayNode = searchRelayNodeObj(prevChannelObj, channelObj);
				NetDeviceObj ingressNetDevice = searchConnectedNetDeviceObj(relayNode, prevChannelObj);
				NetDeviceObj egressNetDevice = searchConnectedNetDeviceObj(relayNode, channelObj);
				FlowRelayObj relayObj = componentFactory.createFlowRelay(ingressNetDevice, egressNetDevice);
				flowRelayList.add(relayObj);
			}
			
			prevChannelObj = channelObj;
		}
		
		FlowRelayObj[] flowRelays = flowRelayList.toArray(new FlowRelayObj[]{});
		FlowObj flowObj = componentFactory.createFlow(flowObjId, odenosFlowName, mplsLspId, widthMbps, delayMsec, flowRelays);
		return flowObj;
	}

	/**
	 * @param prevChannelObj
	 * @param channelObj
	 * @return
	 */
	private NodeObj searchRelayNodeObj(ChannelObj prevChannel, ChannelObj nextChannel) {
		NodeObj relayNode = null;
		if (prevChannel != null && nextChannel != null) {
			for (NetDeviceObj ingressPort : prevChannel.netDeviceSet) {
				for (NetDeviceObj egressPort : nextChannel.netDeviceSet) {
					if (ingressPort.node == egressPort.node) {
						relayNode = ingressPort.node;
						break;
					}
				}
				if (relayNode != null) {
					break;
				}
			}
		}
		return relayNode;
	}
	
	private NetDeviceObj searchConnectedNetDeviceObj(NodeObj node, ChannelObj channel) {
		NetDeviceObj port = null;
		if (node != null && channel != null) {
			for (NetDeviceObj netDevice : node.netDeviceMap.values()) {
				if (netDevice.channel == channel) {
					port = netDevice;
					break;
				}
			}
		}
		return port;
	}

	/**
	 * @param channelObj
	 * @return
	 */
	private boolean isMplsChannel(ChannelObj channelObj) {
		boolean isMplsChannel = true;
		for (NetDeviceObj netDevice : channelObj.netDeviceSet) {
			if (netDevice.node != null) {
				isMplsChannel &= "mpls".equals(netDevice.node.meta.attributes.get("nodeType"));
			}
		}
		return isMplsChannel;
	}

	private TopologyObj createTopologyObj(LdTopoDto ldTopoDto, List<RySwitchDto> rySwitches, List<RyLinkDto> ryLinks, Map<String, Integer> ldDpidPtNodeIdMap) {
		Map<String, ChannelObj> channelMap = createChannelMap(ldTopoDto, rySwitches, ryLinks);
		Map<String, NodeObj> nodeMap = createNodeMap(ldTopoDto, rySwitches, channelMap);
		
		TopologyObj obj = componentFactory.createTopology("topo-ld");
		obj.nodeMap = nodeMap ;
		obj.channelMap = channelMap;
		return obj;
	}

	/**
	 * @param ldTopoDto
	 * @param rySwitches
	 * @return
	 */
	private Map<String, NodeObj> createNodeMap(LdTopoDto ldTopoDto, List<RySwitchDto> rySwitches, Map<String, ChannelObj> channelObjMap) {
		Map<String, NodeObj> nodeObjMap = new LinkedHashMap<>();
		Map<String, Integer> ldDpidPtNodeIdMap = createLdDpidPtNodeIdMap(ldTopoDto);
		
		Map<String, LdNodeDto> nameNodeMap = ldTopoDto.nameNodeMap;
		for (Map.Entry<String, LdNodeDto> entry : nameNodeMap.entrySet()) {
			String name = entry.getKey();
			LdNodeDto ldNodeDto = entry.getValue();
			
			NodeObj nodeObj = null;
			if ("mpls".equals(ldNodeDto.type)) {
				Integer ptId = ldDpidPtNodeIdMap.get(ldNodeDto.dpid);
				nodeObj = componentFactory.createMplsNode(name, ptId);
			} else {
				nodeObj = componentFactory.createNode(name, ldNodeDto.type);
			}
			
			List<String> portNames = ldNodeDto.portNames;
			for (int idx = 0; idx < portNames.size(); idx += 1) {
				String portName = portNames.get(idx);
				String brName = ldNodeDto.brNames.get(idx);
				
				ChannelObj channelObj = getChannelObj(brName, ldTopoDto, channelObjMap);
				
				NetDeviceObj netDeviceObj = componentFactory.createNetDevice(portName);
				nodeObj.install(netDeviceObj).connectTo(channelObj);
			}
			
			String ryDpid = convertLdDpidToRyDpid(ldNodeDto.dpid);
			RySwitchDto rySwitchDto = searchRySwitchDtoByDpid(ryDpid, rySwitches);
			
			nodeObj.meta.attributes.put(LdNodeDto.class.getSimpleName(), ldNodeDto);
			nodeObj.meta.attributes.put(RySwitchDto.class.getSimpleName(), rySwitchDto);
			
			nodeObjMap.put(nodeObj.meta.id, nodeObj);
		}
		return nodeObjMap;
	}

	private ChannelObj getChannelObj(String brName, LdTopoDto ldTopoDto, Map<String, ChannelObj> channelObjMap) {
		ChannelObj channelObj = channelObjMap.get(brName);
		if (channelObj == null) {
			LdBridgeDto ldBridgeDto = ldTopoDto.nameBridgeMap.get(brName);
			Integer widthMbps = convertToWidthMbps(ldBridgeDto.bw);
			Integer delayMsec = convertToDelayMsec(ldBridgeDto.delay);
			
			Integer linkInfoDtoId = getLinkInfoDtoId(brName, ldTopoDto);
			channelObj = componentFactory.createChannel(brName, "any", linkInfoDtoId, widthMbps, delayMsec);
			channelObj.meta.attributes.put(ldBridgeDto.getClass().getSimpleName(), ldBridgeDto);
			
			channelObjMap.put(channelObj.meta.id, channelObj);
		}
		return channelObj;
	}

	/**
	 * @param brName
	 * @param ldTopoDto
	 * @return
	 */
	private Integer getLinkInfoDtoId(String brName, LdTopoDto ldTopoDto) {
		return ldTopoDto.bridges.indexOf(ldTopoDto.nameBridgeMap.get(brName));
	}

	private static RySwitchDto searchRySwitchDtoByDpid(String dpid, List<RySwitchDto> rySwitches) {
		RySwitchDto dto = null;
		for (RySwitchDto rySwitch : rySwitches) {
			if (rySwitch.dpid.equals(dpid)) {
				dto = rySwitch;
				break;
			}
		}
		return dto;
	}
	
	
	/**
	 * @param ldTopoDto
	 * @param rySwitches
	 * @param ryLinks
	 * @return
	 */
	private Map<String, ChannelObj> createChannelMap(LdTopoDto ldTopoDto, List<RySwitchDto> rySwitches, List<RyLinkDto> ryLinks) {
		Map<String, ChannelObj> channelObjMap = new LinkedHashMap<>();
		for (Map.Entry<String, LdBridgeDto> entry : ldTopoDto.nameBridgeMap.entrySet()) {
			String name = entry.getKey();
			getChannelObj(name, ldTopoDto, channelObjMap);
		}
		return channelObjMap;
	}

	private class CacheData {
		
		Map<String, Integer> ldDpidPtNodeIdMap = null;

		List<RySwitchDto> rySwitches = null;
		
		List<RyLinkDto> ryLinks = null;
		
		LdTopoDto ldTopoDto = null;
		
		TopologyObj topologyObj = null;
		
		Map<String, FlowObj> flowObjMap = null;
	}
}
