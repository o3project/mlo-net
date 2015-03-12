/**
 * ComponentFactory.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * This class is a factory class of component instances.
 */
public class ComponentFactory {

	/**
	 * Creates a node instance.
	 * @param id Component ID.
	 * @return An instance.
	 */
	public NodeObj createNode(String id) {
		NodeObj obj = new NodeObj();
		obj.meta = createMeta(id);
		obj.netDeviceMap = new LinkedHashMap<>();
		return obj;
	}

	/**
	 * Creates a network device instance.
	 * @param id Component ID in a node.
	 * @return An instance.
	 */
	public NetDeviceObj createNetDevice(String id) {
		NetDeviceObj obj = new NetDeviceObj();
		obj.meta = createMeta(id);
		obj.node = null;
		obj.channel = null;
		return obj;
	}
	
	/**
	 * Creates a channel instance.
	 * @param id Component ID.
	 * @return An instance.
	 */
	public ChannelObj createChannel(String id) {
		ChannelObj obj = new ChannelObj();
		obj.meta = createMeta(id);
		obj.netDeviceSet = new LinkedHashSet<>();
		return obj;
	}
	
	/**
	 * Creates a flow relay instance.
	 * @param ingressNetDevice An ingress network device.
	 * @param egressNetDevice An egress network device.
	 * @return An instance.
	 */
	public FlowRelayObj createFlowRelay(NetDeviceObj ingressNetDevice, NetDeviceObj egressNetDevice) {
		FlowRelayObj obj = new FlowRelayObj();
		obj.ingressChannel = ingressNetDevice.channel;
		obj.ingressNetDevice = ingressNetDevice;
		obj.egressChannel = egressNetDevice.channel;
		obj.egressNetDevice = egressNetDevice;
		if(!ingressNetDevice.node.meta.id.equals(egressNetDevice.node.meta.id)) {
			throw new IllegalArgumentException("Node of ingress netdevice must equal to Node of egress netdevice.");
		}
		obj.relayNode = ingressNetDevice.node;
		return obj;
	}
	
	/**
	 * Creates a reversed flow relay instance.
	 * @param flowRelay An original flow relay instance.
	 * @param reversedChannelObjMap Reversed channel map.
	 * @return An instance.
	 */
	public FlowRelayObj createReversedFlowRelay(FlowRelayObj flowRelay, Map<ChannelObj, ChannelObj> reversedChannelObjMap) {
		FlowRelayObj obj = new FlowRelayObj();
		obj.ingressChannel = getReversedChannel(flowRelay.egressChannel, reversedChannelObjMap);
		obj.ingressNetDevice = flowRelay.egressNetDevice;
		obj.egressChannel = getReversedChannel(flowRelay.ingressChannel, reversedChannelObjMap);
		obj.egressNetDevice = flowRelay.ingressNetDevice;
		obj.relayNode = flowRelay.relayNode;
		return obj;
	}
	
	private ChannelObj getReversedChannel(ChannelObj origChannelObj, Map<ChannelObj, ChannelObj> reversedChannelObjMap) {
		if (reversedChannelObjMap != null && reversedChannelObjMap.containsKey(origChannelObj)) {
			return reversedChannelObjMap.get(origChannelObj);
		} else {
			return origChannelObj;
		}
	}

	/**
	 * Creates a flow instance.
	 * @param id Component ID.
	 * @param flowRelays Flow relays.
	 * @return An instance.
	 */
	public FlowObj createFlow(String id, FlowRelayObj... flowRelays) {
		FlowObj obj = new FlowObj();
		obj.meta = createMeta(id);
		obj.flowRelays = Arrays.asList(flowRelays);
		return obj;
	}
	
	/**
	 * Creates a reversed flow instance.
	 * @param name Flow name.
	 * @param odenosFlowName Odenos flow name.
	 * @param flowObj Original flow instance.
	 * @param reversedChannelObjMap Reversed channel map.
	 * @return An instance.
	 */
	public FlowObj createReversedFlow(String name, String odenosFlowName, FlowObj flowObj, Map<ChannelObj, ChannelObj> reversedChannelObjMap) {
		List<FlowRelayObj> newRelays = new ArrayList<>();
		for (FlowRelayObj flowRelay : flowObj.flowRelays) {
			newRelays.add(createReversedFlowRelay(flowRelay, reversedChannelObjMap));
		}
		Collections.reverse(newRelays);
		FlowObj obj = createFlow(name);
		obj.flowRelays = newRelays;
		obj.meta.attributes.putAll(flowObj.meta.attributes);
		obj.meta.attributes.put("odenosFlowName", odenosFlowName);

		obj.meta.attributes.put("reverse", flowObj);
		flowObj.meta.attributes.put("reverse", obj);
		
		return obj;
	}
	
	/**
	 * Creates a topology instance.
	 * @param id Component ID.
	 * @return An instance.
	 */
	public TopologyObj createTopology(String id) {
		TopologyObj obj = new TopologyObj();
		obj.meta = createMeta(id);
		obj.nodeMap = new LinkedHashMap<>();
		obj.channelMap = new LinkedHashMap<>();
		return obj;
	}
	
	/**
	 * Creates an MPLS node.
	 * @param name MPLS name.
	 * @param ptId Packet transport equipment ID.
	 * @return An instance.
	 */
	public NodeObj createMplsNode(String name, Integer ptId) {
		NodeObj obj = createNode(name, "mpls");
		obj.meta.attributes.put("mplsNodeId", ptId);
		obj.meta.attributes.put("mplsNodeName", name);
		return obj;
	}
	
	/**
	 * Creates a node instance.
	 * @param name Node name.
	 * @param nodeType Node type.
	 * @return An instance.
	 */
	public NodeObj createNode(String name, String nodeType) {
		NodeObj obj = createNode(name);
		obj.meta.attributes.put("nodeType", nodeType);
		obj.meta.attributes.put("nodeName", name);
		return obj;
	}
	
	/**
	 * Creates a channel instance.
	 * @param name Name.
	 * @param channelType Channel type.
	 * @return An instance.
	 */
	public ChannelObj createChannel(String name, String channelType) {
		ChannelObj obj = createChannel(name);
		obj.meta.attributes.put("channelType", channelType);
		return obj;
	}

	/**
	 * Creates a channel instance.
	 * @param name Channel name.
	 * @param channelType Channel type.
	 * @param linkInfoDtoId Link info DTO ID.
	 * @param widthMbps Band width.
	 * @param delayMsec Delay.
	 * @return An instance.
	 */
	public ChannelObj createChannel(String name, String channelType, Integer linkInfoDtoId, Integer widthMbps, Integer delayMsec) {
		ChannelObj obj = createChannel(name, channelType);
		if (linkInfoDtoId != null) {
			obj.meta.attributes.put("linkInfoDtoId", linkInfoDtoId);
		}
		if (widthMbps != null) {
			obj.meta.attributes.put("widthMbps", widthMbps);
		}
		if (delayMsec != null) {
			obj.meta.attributes.put("delayMsec", delayMsec);
		}
		return obj;
	}
	
	/**
	 * Creates an MPLS channel instance.
	 * @param name Name.
	 * @param linkInfoDtoId Link info DTO ID.
	 * @param widthMbps Band width.
	 * @param delayMsec Delay.
	 * @return An instance.
	 */
	public ChannelObj createMplsChannel(String name, Integer linkInfoDtoId, Integer widthMbps, Integer delayMsec) {
		ChannelObj obj = createChannel(name, "mpls", linkInfoDtoId, widthMbps, delayMsec);
		return obj;
	}
	
	/**
	 * Creates a flow instance.
	 * @param name Name.
	 * @param odenosFlowName Odenos flow name.
	 * @param mplsLspId MPLS LSP ID.
	 * @param widthMbps Band width.
	 * @param delayMsec Delay.
	 * @param flowRelays Flow relays.
	 * @return An instance.
	 */
	public FlowObj createFlow(String name, String odenosFlowName, String mplsLspId, Integer widthMbps, Integer delayMsec, FlowRelayObj... flowRelays) {
		FlowObj obj = createFlow(name, flowRelays);
		obj.meta.attributes.put("widthMbps", widthMbps);
		obj.meta.attributes.put("delayMsec", delayMsec);
		obj.meta.attributes.put("odenosFlowName", odenosFlowName);
		obj.meta.attributes.put("mplsLspId", mplsLspId);
		return obj;
	}

	private static MetaObj createMeta(String id) {
		MetaObj obj = new MetaObj();
		obj.id = id;
		obj.attributes = new LinkedHashMap<>();
		return obj;
	}

}
