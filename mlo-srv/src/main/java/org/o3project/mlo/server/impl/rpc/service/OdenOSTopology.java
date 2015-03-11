/**
 * OdenOSTopology.java
 * (C) 2015, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.o3project.mlo.server.logic.ConfigConstants;
import org.o3project.mlo.server.logic.ConfigProvider;
import org.o3project.mlo.server.logic.TopologyConfigConstants;
import org.o3project.mlo.server.rpc.entity.PTFlowEntity;
import org.o3project.mlo.server.rpc.entity.PTLinkEntity;

/**
 * This class is the Odenos topology utility class.
 */
public class OdenOSTopology implements ConfigConstants, TopologyConfigConstants {

	/**
	 * Creates PT link entity.
	 * @param linkName Link name.
	 * @param bandWidth Band width.
	 * @param delay delay time.
	 * @param configProvider Configuration provider.
	 * @return PT link entity.
	 */
	public PTLinkEntity createPTLinkEntity(Object linkName, Object bandWidth, Object delay, ConfigProvider configProvider) {
		PTLinkEntity linkEntity = null;
		Map<String, String> linkPropMap = getLinkPropMapByLinkName(linkName, configProvider); 
		if (linkPropMap != null && linkPropMap.size() > 0) {
			String linkId = linkPropMap.get(PROP_SUBKEY_ODENOS_ID);
			String srcNode = linkPropMap.get(PROP_SUBKEY_SRC_NODE);
			String srcPort = linkPropMap.get(PROP_SUBKEY_SRC_PORT);
			String dstNode = linkPropMap.get(PROP_SUBKEY_DST_NODE);
			String dstPort = linkPropMap.get(PROP_SUBKEY_DST_PORT);
			String operStatus = "DOWN";
			String reqBandwidth = ("" + bandWidth);
			String reqLatency = ("" + delay);
			String establishmentStatus = "establishing";
			linkEntity = new PTLinkEntity(linkId, 
					srcNode, srcPort, dstNode, dstPort, 
					operStatus, reqLatency, reqBandwidth, establishmentStatus);
		}
		return linkEntity;
	}

	/**
	 * Creates PT flow entity.
	 * @param flowName Flow name.
	 * @param reqBandWidth Requested band width.
	 * @param reqDelay Requested delay.
	 * @param configProvider Configuration provider.
	 * @return PT flow entity.
	 */
	public PTFlowEntity createPTFlowEntity(Object flowName, Integer reqBandWidth, Integer reqDelay, ConfigProvider configProvider) {
		PTFlowEntity flowEntity = null;
		Map<String, String> flowPropMap = getFlowPropMapByFlowName(flowName, configProvider);
		if (flowPropMap != null && flowPropMap.size() > 0) {
			String flowId = flowName.toString();
			String flowType = "BASIC_FLOW";
			String basicFlowMatchInNode = flowPropMap.get(PROP_SUBKEY_BASIC_FLOW_MATCH_NODE);
			String basicFlowMatchInPort = flowPropMap.get(PROP_SUBKEY_BASIC_FLOW_MATCH_PORT);
			List<String> flowPath = createFlowPath(flowPropMap.get(PROP_SUBKEY_LINK_NAMES), configProvider);
			String basicFlowActionOutputNode = flowPropMap.get(PROP_SUBKEY_BASIC_FLOW_ACTION_NODE);
			String basicFlowActionOutputPort = flowPropMap.get(PROP_SUBKEY_BASIC_FLOW_ACTION_PORT);
			int bandWidth = reqBandWidth;
			int delay = reqDelay;
			flowEntity = new PTFlowEntity(flowId, flowType, 
					basicFlowMatchInNode, basicFlowMatchInPort, flowPath, 
					basicFlowActionOutputNode, basicFlowActionOutputPort, 
					bandWidth, delay);
		}
		return flowEntity;
	}
	
	/**
	 * Obtains link property map by link name.
	 * @param linkName Link name.
	 * @param configProvider Configuration provider.
	 * @return link property map.
	 */
	Map<String, String> getLinkPropMapByLinkName(Object linkName, ConfigProvider configProvider) {
		Map<String, String> linkPropMap = null;
		if (linkName != null) {
			String linkPropKey = PROP_KEY_ODENOS_TOPOLOGY_LINKS_PREFIX_ + linkName + ".";
			linkPropMap = configProvider.getSubProperties(linkPropKey);
		}
		return linkPropMap;
	}

	/**
	 * Obtains flow property map by flow name.
	 * @param flowName Flow name.
	 * @param configProvider Configuration provider.
	 * @return flow property map.
	 */
	Map<String, String> getFlowPropMapByFlowName(Object flowName, ConfigProvider configProvider) {
		Map<String, String> flowPropMap = null;
		if (flowName != null) {
			String flowPropKey = PROP_KEY_ODENOS_TOPOLOGY_FLOWS_PREFIX_ + flowName + ".";
			flowPropMap = configProvider.getSubProperties(flowPropKey);
		}
		return flowPropMap;
	}
	
	List<String> createFlowPath(String commaSeparatedLinkNames, ConfigProvider configProvider) {
		List<String> flowPath = new ArrayList<>();
		String[] linkNames = commaSeparatedLinkNames.split(",");
		for (String linkName : linkNames) {
			Map<String, String> linkPropMap = getLinkPropMapByLinkName(linkName, configProvider);
			if (linkPropMap != null) {
				String flowPathLink = linkPropMap.get(PROP_SUBKEY_ODENOS_ID);
				flowPath.add(flowPathLink);
			}
		}
		return flowPath;
	}
}
