/**
 * TopologyRepositoryDefaultImpl.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.arnx.jsonic.JSON;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.component.ChannelObj;
import org.o3project.mlo.server.component.FlowObj;
import org.o3project.mlo.server.component.FlowRelayObj;
import org.o3project.mlo.server.component.NetDeviceObj;
import org.o3project.mlo.server.component.NodeObj;
import org.o3project.mlo.server.component.TopologyObj;
import org.o3project.mlo.server.dto.AlarmDto;
import org.o3project.mlo.server.dto.EventDto;
import org.o3project.mlo.server.dto.FlowDto;
import org.o3project.mlo.server.dto.LdTopoDto;
import org.o3project.mlo.server.dto.LinkInfoDto;
import org.o3project.mlo.server.dto.RyLinkDto;
import org.o3project.mlo.server.dto.RySwitchDto;
import org.o3project.mlo.server.logic.ApiCallException;
import org.o3project.mlo.server.logic.ConfigConstants;
import org.o3project.mlo.server.logic.ConfigProvider;
import org.o3project.mlo.server.logic.EventRepository;
import org.o3project.mlo.server.logic.LdTopologyRepository;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.Notification;
import org.o3project.mlo.server.logic.NotificationCenter;
import org.o3project.mlo.server.logic.NotificationObserver;
import org.o3project.mlo.server.logic.TopologyConfigConstants;
import org.o3project.mlo.server.logic.TopologyProvider;
import org.o3project.mlo.server.logic.TopologyRepository;

/**
 * This class is the implementation class of {@link TopologyRepository} interface.
 *
 */
public class TopologyRepositoryDefaultImpl implements TopologyRepository, LdTopologyRepository, TopologyConfigConstants, ConfigConstants {
	private static final Log LOG = LogFactory.getLog(TopologyRepositoryDefaultImpl.class);
	
	private static final Integer START_EXTRA_PORT_ID = 100;
	
	private static final Pattern PTN_INTEGER = Pattern.compile("^[^\\d]*(\\d+).*$");

	private ConfigProvider configProvider;
	
	private TopologyProvider topologyProviderLocalImpl;
	
	private TopologyProviderRemoteLdImpl topologyProviderRemoteLdImpl;
	
	private final CacheData cacheData = new CacheData();
	
	private NotificationCenter notificationCenter;
	
	private EventRepository eventRepository;

	/**
	 * Setter method (for DI setter injection).
	 * @param configProvider the configProvider to set
	 */
	public void setConfigProvider(ConfigProvider configProvider) {
		this.configProvider = configProvider;
	}
	
	/**
	 * @param eventRepository the eventRepository to set
	 */
	public void setEventRepository(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}
	
	/**
	 * Setter method (for DI setter injection).
	 * @param topologyProviderLocalImpl the topologyProviderLocalImpl to set
	 */
	public void setTopologyProviderLocalImpl(TopologyProvider topologyProviderLocalImpl) {
		this.topologyProviderLocalImpl = topologyProviderLocalImpl;
	}
	
	/**
	 * Setter method (for DI setter injection).
	 * @return the topologyProviderLocalImpl
	 */
	public TopologyProvider getTopologyProviderLocalImpl() {
		return topologyProviderLocalImpl;
	}
	
	/**
	 * Setter method (for DI setter injection).
	 * @param topologyProviderRemoteLdImpl the topologyProviderRemoteLdImpl to set
	 */
	public void setTopologyProviderRemoteLdImpl(TopologyProviderRemoteLdImpl topologyProviderRemoteLdImpl) {
		this.topologyProviderRemoteLdImpl = topologyProviderRemoteLdImpl;
	}
	
	/**
	 * Setter method (for DI setter injection).
	 * @param notificationCenter the notificationCenter to set
	 */
	public void setNotificationCenter(NotificationCenter notificationCenter) {
		this.notificationCenter = notificationCenter;
	}
	
	/**
	 * Initializes the instance.
	 */
	public void init() {
		synchronized (cacheData) {
			cacheData.isLoaded = false;
		}
		
		notificationCenter.addObserver(new NotificationObserver() {
			@Override
			public void notificationObserved(Notification notification) {
				loadData();
				synchronized (cacheData) {
					if (cacheData.isLoaded) {
						AlarmDto alarmDto = (AlarmDto) notification.data;
						
						// Changes state of the node.
						NodeObj targetNodeObj = cacheData.topologyObj.nodeMap.get(alarmDto.targetId);
						String stateKey = getStateKey(targetNodeObj.getClass(), targetNodeObj.meta.id);
						
						if(cacheData.componentStateMap.containsKey(stateKey)){
							if (!cacheData.componentStateMap.get(stateKey).equals(alarmDto.state)){
								cacheData.componentStateMap.put(stateKey, alarmDto.state);
								eventRepository.addEventToAllUsers(createTopologyStateChangedEvent());
							}
						}else{
							cacheData.componentStateMap.put(stateKey, alarmDto.state);
							eventRepository.addEventToAllUsers(createTopologyStateChangedEvent());
						}
						
						// Changes state of flows.
						for (Entry<String, FlowObj> entry : cacheData.flowObjMap.entrySet()) {
							for (FlowRelayObj flowRelayObj : entry.getValue().flowRelays) {
								if (flowRelayObj.relayNode.meta.id.equals(targetNodeObj.meta.id)) {
									entry.getValue().meta.attributes.put("state", alarmDto.state);
									break;
								}
							}
						}
					}
				}
			}
			
			private EventDto createTopologyStateChangedEvent() {
				EventDto eventDto = new EventDto();
				eventDto.timestamp = new Date();
				eventDto.type = "modified";
				eventDto.targetType = "topology-state";
				return eventDto;
			}
		}, AlarmDto.class.getName());
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.TopologyRepository#queryFlowTemplate(org.o3project.mlo.server.dto.FlowDto)
	 */
	@Override
	public FlowDto queryFlowTemplate(FlowDto reqFlowDto) throws MloException {
		loadData();
		
		ChannelObj srcChannel = null;
		ChannelObj dstChannel = null;
		
		TopologyObj topologyObj = cacheData.topologyObj;
		Map<String, FlowObj> flowObjMap = cacheData.flowObjMap;
		List<FlowDto> tmpls = cacheData.flowDtoTemplates;
		
		try {
			srcChannel = queryCeChannel(
					reqFlowDto.srcCENodeName, reqFlowDto.srcCEPortNo, 
					topologyObj, flowObjMap);
		} catch (MloException e) {
			LOG.warn(String.format(
					"Source channel not found. (CENodeName, CEPortNo)=(%s, %s)", 
					reqFlowDto.srcCENodeName, reqFlowDto.srcCEPortNo));
			throw e;
		}
		try {
			dstChannel = queryCeChannel(
					reqFlowDto.dstCENodeName, reqFlowDto.dstCEPortNo, 
					topologyObj, flowObjMap);
		} catch (MloException e) {
			LOG.warn(String.format(
					"Destination channel not found. (CENodeName, CEPortNo)=(%s, %s)", 
					reqFlowDto.dstCENodeName, reqFlowDto.dstCEPortNo));
			throw e;
		}
		
		if (LOG.isInfoEnabled()) {
			String fmt = "%s: CE(Name, PortNo) -> Channel(id): (%s, %s) -> (%s)";
			LOG.info(String.format(
					fmt, "Source", 
					reqFlowDto.srcCENodeName, reqFlowDto.srcCEPortNo,
					srcChannel.meta.id));
			LOG.info(String.format(
					fmt, "Destination",
					reqFlowDto.dstCENodeName, reqFlowDto.dstCEPortNo,
					dstChannel.meta.id));
		}
		
		FlowDto flow = null;
		for (FlowDto tmpl : tmpls) {
			if (isCeChannelMatch(srcChannel, dstChannel, tmpl)
					&& "ok".equals(getState(tmpl))
					&& (reqFlowDto.reqBandWidth <= tmpl.usedBandWidth)
					&& (reqFlowDto.reqDelay >= tmpl.delayTime)) {
				flow = tmpl;
				break;
			}
		}

		if (flow == null) {
			LOG.warn("Adequate flow not found.");
			String reqFmt = "req: (name, srcCENodeName, srcCEPortNo, dstCENodeName, dstCEPortNo, reqBandWidth, reqDelay) = (%s, %s, %s, %s, %s, %d, %d)";
			LOG.warn(String.format(reqFmt, reqFlowDto.name, 
					reqFlowDto.srcCENodeName, reqFlowDto.srcCEPortNo, 
					reqFlowDto.dstCENodeName, reqFlowDto.dstCEPortNo, 
					reqFlowDto.reqBandWidth, reqFlowDto.reqDelay));
			for (FlowDto tmpl : cacheData.flowDtoTemplates) {
				LOG.warn(getFlowTmplLogString(tmpl));
			}
		}
		return flow;
	}

	/**
	 * @param tmpl
	 * @return
	 */
	private String getState(FlowDto tmpl) {
		String state = "ok";
		FlowObj flowObj = (FlowObj) tmpl.attributes.get(FlowObj.class.getSimpleName());
		if (flowObj != null && flowObj.meta.attributes.get("state") != null) {
			state = (String) flowObj.meta.attributes.get("state");
		}
		return state;
	}

	/**
	 * @param tmplFmt
	 * @param tmpl
	 * @return
	 */
	private static String getFlowTmplLogString(FlowDto tmpl) {
		String tmplFmt = "tmpl: (name, srcPTNodeName, srcPTNodeId, dstPTNodeName, dstPTNodeId, usedBandWidth, delayTime)"
				+ " = (%s, %s, %d, %s, %d, %d, %d), bwFlowName = %s";
		String bwName = null;
		Object bwTmpl = tmpl.attributes.get(FLOW_ATTR_KEY_REVERSE);
		if (bwTmpl != null) {
			bwName = ((FlowDto) bwTmpl).name;
		}
		return String.format(tmplFmt, tmpl.name, 
				tmpl.srcPTNodeName, tmpl.srcPTNodeId, 
				tmpl.dstPTNodeName, tmpl.dstPTNodeId, 
				tmpl.usedBandWidth, tmpl.delayTime,
				bwName);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.TopologyRepository#queryVlanId(java.lang.String, java.lang.String)
	 */
	@Override
	public String queryVlanId(String ceNodeName, String ceNodePortNo) {
		loadData();
		
		NodeObj ceNode = cacheData.topologyObj.nodeMap.get(ceNodeName);
		NetDeviceObj cePort = null;
		if (ceNode != null) {
			cePort = ceNode.netDeviceMap.get(ceNodePortNo);
		}
		String vlanId = null;
		if (cePort != null) {
			vlanId = (String) cePort.meta.attributes.get("vlanId");
		}
		
		if(vlanId == null){
			int plusCnt = getIntegerValue(ceNodePortNo) - START_EXTRA_PORT_ID;
			if (plusCnt >= 0) {
				int startVlanId = getExtraVlanIdOffset();
				vlanId = String.valueOf(startVlanId + plusCnt);
			}
		}

		return vlanId;
	}

	/**
	 * @param ceNodePortNo
	 * @return
	 */
	private Integer getIntegerValue(String ceNodePortNo) {
		Matcher m = PTN_INTEGER.matcher(ceNodePortNo);
		boolean found = m.find();
		Integer value = null;
		if (found) {
			value = Integer.valueOf(m.group(1));
		} else {
			value = -1;
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.TopologyRepository#queryVlanId(org.o3project.mlo.server.dto.FlowDto)
	 */
	@Override
	public String queryVlanId(FlowDto flowDto) {
		String vlanId = (String) flowDto.attributes.get(FLOW_ATTR_KEY_VLAN_ID);
		return vlanId;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.TopologyRepository#getExtraVlanIdOffset()
	 */
	@Override
	public Integer getExtraVlanIdOffset() {
		return configProvider.getIntegerProperty(PROP_KEY_EXTRA_VLAN_ID_OFFSET);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.LdTopologyRepository#getLdTopo()
	 */
	@Override
	public LdTopoDto getLdTopo() throws MloException {
		loadData();
		
		LdTopologyRepository repo = getLdTopologyRepository();
		if (repo != null) {
			return repo.getLdTopo();
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.LdTopologyRepository#getRySwitches()
	 */
	@Override
	public List<RySwitchDto> getRySwitches() throws MloException {
		loadData();
		
		LdTopologyRepository repo = getLdTopologyRepository();
		if (repo != null) {
			return repo.getRySwitches();
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.LdTopologyRepository#getRyLinks()
	 */
	@Override
	public List<RyLinkDto> getRyLinks() throws MloException {
		loadData();
		
		LdTopologyRepository repo = getLdTopologyRepository();
		if (repo != null) {
			return repo.getRyLinks();
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.TopologyRepository#getComponentState(java.lang.Class, java.lang.String)
	 */
	@Override
	public String getComponentState(Class<?> componentObjClass, String name) {
		String key = getStateKey(componentObjClass, name);
		String stateValue = null;
		synchronized (cacheData) {
			if (cacheData.isLoaded) {
				stateValue = cacheData.componentStateMap.get(key);
			}
		}
		return stateValue;
	}

	/**
	 * Loads a topology data.
	 */
	public void loadData() {
		synchronized (cacheData) {
			if (!cacheData.isLoaded) {
				cacheData.isLoaded = loadData(cacheData);
			}
		}
	}

	/**
	 * @param cache
	 * @return
	 */
	private boolean loadData(CacheData cache) {
		TopologyProvider topologyProvider = getTopologyProvider();
		return loadData(cache, topologyProvider);
	}

	private boolean loadData(CacheData cache, TopologyProvider topologyProvider) {
		boolean isLoaded = topologyProvider.load();
		if (isLoaded) {
			TopologyObj topologyObj = topologyProvider.getTopologyObj();
			Map<String, FlowObj> flowObjMap = topologyProvider.getFlowObjMap();
			Map<FlowObj, FlowDto> flowObjFlowDtoMap = createFlowObjFlowDtoMap(flowObjMap, topologyObj);

			cache.topologyObj = topologyObj;
			cache.flowObjMap = flowObjMap;
			cache.flowDtoTemplates = createFlowDtoTemplates(flowObjFlowDtoMap);
			cache.componentStateMap = new LinkedHashMap<>();
			
			if (LOG.isDebugEnabled()) {
				for (FlowDto tmpl : cache.flowDtoTemplates) {
					LOG.debug(getFlowTmplLogString(tmpl));
				}
			}
		}
		return isLoaded;
	}
	
	/**
	 * @return
	 */
	private TopologyProvider getTopologyProvider() {
		boolean isRemoteDisabled = configProvider.getBooleanProperty(PROP_KEY_LD_DEBUG_REMOTE_DISABLE);
		if (!isRemoteDisabled && topologyProviderRemoteLdImpl != null) {
			return topologyProviderRemoteLdImpl;
		} else {
			return topologyProviderLocalImpl;
		}
	}
	
	/**
	 * @return
	 */
	private LdTopologyRepository getLdTopologyRepository() {
		boolean isRemoteDisabled = configProvider.getBooleanProperty(PROP_KEY_LD_DEBUG_REMOTE_DISABLE);
		if (!isRemoteDisabled) {
			return topologyProviderRemoteLdImpl;
		} else {
			return null;
		}
	}
	
	private boolean isCeChannelMatch(ChannelObj srcChannel, ChannelObj dstChannel, FlowDto tmplFlowDto) {
		FlowObj flowObj = (FlowObj) tmplFlowDto.attributes.get(FlowObj.class.getSimpleName());
		int firstIdx = 0;
		int lastIdx = flowObj.flowRelays.size() - 1;
		ChannelObj tmplSrcChannel = flowObj.flowRelays.get(firstIdx).ingressChannel;
		ChannelObj tmplDstChannel = flowObj.flowRelays.get(lastIdx).egressChannel;
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("tmpl : (srcChannel, dstChannel)=(%s, %s)", tmplSrcChannel.meta.id, tmplDstChannel.meta.id));
		}
		return ((srcChannel == tmplSrcChannel) && (dstChannel == tmplDstChannel));
	}

	/**
	 * @param ceNodeName
	 * @param cePortNo
	 * @return
	 */
	ChannelObj queryCeChannel(String ceNodeName, String cePortNo, TopologyObj topologyObj, Map<String, FlowObj> flowObjMap) throws MloException {
		NodeObj ceNode = topologyObj.nodeMap.get(ceNodeName);
		if (ceNode == null) {
			throw new ApiCallException("BadRequest/InvalidCENode");
		}
		NetDeviceObj ceNetDevice = ceNode.netDeviceMap.get(cePortNo);
		if (ceNetDevice == null) {
			ceNetDevice = ceNode.netDeviceMap.get("*"); // anonymous
		}
		if (ceNetDevice == null) {
			throw new ApiCallException(String.format("BadRequest/InvalidCENode/(ceNodeName, cePortNo)=(%s, %s)", ceNodeName, cePortNo));
		}
		ChannelObj channel = ceNetDevice.channel;
		if (channel == null) {
			throw new ApiCallException(String.format("BadRequest/InvalidCENode/(ceNodeName, cePortNo)=(%s, %s)", ceNodeName, cePortNo));
		}
		return channel;
	}

	List<FlowDto> createFlowDtoTemplates(Map<FlowObj, FlowDto> flowObjFlowDtoMap) {
		List<FlowDto> flowDtoList = new ArrayList<>();
		int vlanIdIdx = 0;
		for (Map.Entry<FlowObj, FlowDto> entry : flowObjFlowDtoMap.entrySet()) {
			FlowDto flowDto = entry.getValue();
			setDuplexAttribute(flowDto, FLOW_ATTR_KEY_VLAN_ID, getVlanId(vlanIdIdx));
			flowDtoList.add(flowDto);
			vlanIdIdx += 1;
		}
		sortFlowDtos(flowDtoList);
		return flowDtoList;
	}
	
	private static String getVlanId(int vlanIdIdx) {
		final int vlanIdOffset = 600;
		return String.valueOf(vlanIdOffset + vlanIdIdx);
	}
	
	private static void setDuplexAttribute(FlowDto flowDto, String attrKey, Object attrValue) {
		Object fwAttrValue = flowDto.attributes.get(attrKey);
		FlowDto bwFlowDto = (FlowDto) flowDto.attributes.get(FLOW_ATTR_KEY_REVERSE);
		if (fwAttrValue == null) {
			flowDto.attributes.put(attrKey, attrValue);
			fwAttrValue = attrValue;
		}
		if (bwFlowDto != null) {
			bwFlowDto.attributes.put(attrKey, attrValue);
		}
	}
	
	Map<FlowObj, FlowDto> createFlowObjFlowDtoMap(Map<String, FlowObj> flowObjMap, TopologyObj topologyObj) {
		Map<FlowObj, FlowDto> flowObjFlowDtoMap = new LinkedHashMap<>();
		for (Map.Entry<String, FlowObj> entry : flowObjMap.entrySet()) {
			FlowObj fwFlow = entry.getValue();
			extractFlowDtos(fwFlow, topologyObj, flowObjFlowDtoMap);
		}
		return flowObjFlowDtoMap;
	}
	
	void extractFlowDtos(FlowObj fwFlow, TopologyObj topo, Map<FlowObj, FlowDto> flowObjFlowDtoMap) {
		FlowObj bwFlow = (FlowObj) fwFlow.meta.attributes.get("reverse");
		
		// forward direction
		FlowDto fwFlowDto = flowObjFlowDtoMap.get(fwFlow);
		if (fwFlowDto == null) {
			fwFlowDto = convertToFlowDto(fwFlow, topo);
			flowObjFlowDtoMap.put(fwFlow, fwFlowDto);
		}
		
		// backward direction
		FlowDto bwFlowDto = null;
		if (bwFlow != null) {
			bwFlowDto = flowObjFlowDtoMap.get(bwFlow);
		}
		
		if (fwFlowDto != null && bwFlowDto != null) {
			fwFlowDto.attributes.put(FLOW_ATTR_KEY_REVERSE, bwFlowDto);
			bwFlowDto.attributes.put(FLOW_ATTR_KEY_REVERSE, fwFlowDto);
		}
	}
	
	FlowDto convertToFlowDto(FlowObj flow, TopologyObj topo) {
		List<LinkInfoDto> linkInfos = new ArrayList<>();
		List<FlowRelayObj> relays = flow.flowRelays;
		NodeObj prevNode = null;
		for (FlowRelayObj relay : relays) {
			NodeObj currentNode = relay.relayNode;
			ChannelObj channel = relay.ingressChannel;
			if (isMplsNode(prevNode) && isMplsNode(currentNode)) {
				Integer id = (Integer) channel.meta.attributes.get("linkInfoDtoId");
				LinkInfoDto linkInfo = createLinkInfoDto(id, channel, prevNode, currentNode);
				linkInfos.add(linkInfo);
			}
			prevNode = currentNode;
		}
		Integer widthMbps = (Integer) flow.meta.attributes.get("widthMbps");
		Integer delayMsec = (Integer) flow.meta.attributes.get("delayMsec");
		String odenosFlowName = (String) flow.meta.attributes.get("odenosFlowName");
		String mplsLspId = (String) flow.meta.attributes.get("mplsLspId");
		FlowDto flowDto = FlowDtoUtil.createConcreteFlow(linkInfos, widthMbps, delayMsec);
		flowDto.attributes.put(FLOW_ATTR_KEY_FLOW_NAME, odenosFlowName);
		flowDto.attributes.put(FLOW_ATTR_KEY_SDTNC_LINK_ID, mplsLspId);
		flowDto.attributes.put(FlowObj.class.getSimpleName(), flow);
		flowDto.name = flow.meta.id;
		return flowDto;
	}
	
	/**
	 * @param channel
	 * @param prevNode
	 * @param currentNode
	 * @return
	 */
	private LinkInfoDto createLinkInfoDto(Integer id, ChannelObj channel, NodeObj prevNode, NodeObj currentNode) {
		String srcNodeName = prevNode.meta.id;
		Integer srcNodeId = (Integer) prevNode.meta.attributes.get("mplsNodeId");
		String dstNodeName = currentNode.meta.id;
		Integer dstNodeId = (Integer) currentNode.meta.attributes.get("mplsNodeId");
		LinkInfoDto dto = FlowDtoUtil.createLinkInfo(id, srcNodeName, srcNodeId, dstNodeName, dstNodeId);
		
		String linkName = String.format("link-%s-%s_%s", channel.meta.id, prevNode.meta.id, currentNode.meta.id);
		Integer widthMbps = getWidthMbps(channel);
		Integer delayMsec = getDelayMsec(channel);
		dto.attributes.put(LINK_ATTR_KEY_LINK_NAME, linkName);
		if (widthMbps != null) {
			dto.attributes.put(LINK_ATTR_KEY_BAND_WIDTH, widthMbps);
		}
		if (delayMsec != null) {
			dto.attributes.put(LINK_ATTR_KEY_DELAY, delayMsec);
		}
		dto.attributes.put(ChannelObj.class.getSimpleName(), channel);
		return dto;
	}

	/**
	 * @param channel
	 * @return
	 */
	private Integer getWidthMbps(ChannelObj channel) {
		return (Integer) channel.meta.attributes.get("widthMbps");
	}

	/**
	 * @param channel
	 * @return
	 */
	private Integer getDelayMsec(ChannelObj channel) {
		return (Integer) channel.meta.attributes.get("delayMsec");
	}
	
	private String getStateKey(Class<?> clazz, String id) {
		LinkedHashMap<String, Object> keySeed = new LinkedHashMap<>();
		keySeed.put("Type", clazz.getSimpleName());
		keySeed.put("id", id);
		return JSON.encode(keySeed);
	}

	boolean isMplsNode(NodeObj node) {
		boolean isMplsNode = (node != null);
		if (isMplsNode) {
			Object nodeType = node.meta.attributes.get("nodeType");
			isMplsNode = ("mpls".equals(nodeType));
		}
		return isMplsNode;
	}
	
	List<FlowDto> getFlowDtoTemplates() {
		return cacheData.flowDtoTemplates;
	}

	/**
	 * @param flowDtos
	 */
	static void sortFlowDtos(List<FlowDto> flowDtos) {
		Collections.sort(flowDtos, new Comparator<FlowDto>() {
			@Override
			public int compare(FlowDto flow1, FlowDto flow2) {
				int cmp = 0;
				if (!flow1.usedBandWidth.equals(flow2.usedBandWidth)) {
					// Small width is moved to small index.
					cmp = +1 * (flow1.usedBandWidth - flow2.usedBandWidth);
				} else if (!flow1.delayTime.equals(flow2.delayTime)) {
					// Large delay is moved to small index.
					cmp = -1 * (flow1.delayTime - flow2.delayTime);
				} else {
					cmp = 0;
				}
				return cmp;
			}
		});
	}
	
	private class CacheData {
		boolean isLoaded = false;
		
		TopologyObj topologyObj = null;
		
		Map<String, FlowObj> flowObjMap = null;
		
		List<FlowDto> flowDtoTemplates = null;
		
		Map<String, String> componentStateMap = null;
	}
}
