/**
 * PTFlowEntity.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.rpc.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is the data class which designates flow information for packet transport nodes.
 */
public class PTFlowEntity {
	
	private static final String ATTRIBUTE_REQ_BANDWIDTH = "req_bandwidth";
	private static final String ATTRIBUTE_REQ_LATENCY = "req_latency";

	/**
	 * Flow ID. 
	 */
	public String flowId;
	
	/**
	 * Flow type.
	 */
	public String flowType;
	
	/**
	 * Start point node.
	 */
	public String basicFlowMatchInNode;
	
	/**
	 * Ingress port.
	 */
	public String basicFlowMatchInPort;
	
	/**
	 * The list of flow path.
	 */
	public List<String> flowPath;
	
	/**
	 * End point node.
	 */
	public String basicFlowActionOutputNode;
	
	/**
	 * Egress port.
	 */
	public String basicFlowActionOutputPort;

	/**
	 * Attributes.
	 */
	public Map<String, String> attributes = new HashMap<String, String>();

	/**
	 * Creates an instance.
	 * @param flowId Flow ID. 
	 * @param flowType Flow type. 
	 * @param basicFlowMatchInNode Start point node. 
	 * @param basicFlowMatchInPort Ingress port.
	 * @param flowPath The list of flow path.
	 * @param basicFlowActionOutputNode End point node.
	 * @param basicFlowActionOutputPort Egress port.
	 * @param bandWidth the band width attribute.
	 * @param delay the delay time attribute.
	 */
	public PTFlowEntity(String flowId, String flowType, String basicFlowMatchInNode,
			String basicFlowMatchInPort, List<String> flowPath, 
			String basicFlowActionOutputNode, String basicFlowActionOutputPort, int bandWidth, int delay){
		this.flowId = flowId;
		this.flowType = flowType;
		this.basicFlowMatchInNode = basicFlowMatchInNode;
		this.basicFlowMatchInPort = basicFlowMatchInPort;
		this.flowPath = flowPath;
		this.basicFlowActionOutputNode = basicFlowActionOutputNode;
		this.basicFlowActionOutputPort = basicFlowActionOutputPort;
		this.attributes.put(ATTRIBUTE_REQ_BANDWIDTH, String.valueOf(bandWidth));
		this.attributes.put(ATTRIBUTE_REQ_LATENCY, String.valueOf(delay));
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String attrStr = "";
		for(Map.Entry<String, String> e : attributes.entrySet()) {
			attrStr = String.format("%s[%s, %s]", attrStr, e.getKey(), e.getValue());
		}
		return String.format("%s, %s, %s, %s, %s, %s, %s, %s", 
				flowId, flowType, basicFlowMatchInNode, basicFlowMatchInPort, 
				flowPath, basicFlowActionOutputNode, basicFlowActionOutputPort, attrStr);
	}
}
