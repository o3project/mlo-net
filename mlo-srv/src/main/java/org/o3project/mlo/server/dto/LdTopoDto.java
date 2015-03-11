/**
 * LdTopoDto.java
 * (C) 2015, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSONHint;

/**
 * This is a DTO class which indicates a network topology.
 */
public class LdTopoDto {
	/**
	 * The switch nodes in the network topology.
	 */
	public List<LdNodeDto> switches;
	
	/**
	 * The host nodes in the network topology.
	 */
	public List<LdNodeDto> hosts;
	
	/**
	 * The names of the network controllers in the network topology.
	 */
	public List<String> controllers;
	
	/**
	 * The flows in the network topology.
	 */
	public List<LdFlowDto> flows;

	/**
	 * The network bridges in the network topology.
	 */
	public List<LdBridgeDto> bridges;

	/**
	 * The map of nodes.
	 * Keys of the map entries are names of nodes.
	 * A key is a name of the node.
	 */
	@JSONHint(ignore = true)
	public Map<String, LdNodeDto> nameNodeMap = new HashMap<String, LdNodeDto>();
	
	/**
	 * The map of switches.
	 * Keys of the map entries are names of switches.
	 */
	@JSONHint(ignore = true)
	public Map<String, LdNodeDto> nameSwitchMap = new HashMap<String, LdNodeDto>();
	
	/**
	 * The map of hosts.
	 * Keys of the map entries are names of hosts.
	 */
	@JSONHint(ignore = true)
	public Map<String, LdNodeDto> nameHostMap = new HashMap<String, LdNodeDto>();
	
	/**
	 * The map of flows.
	 * Keys of the map entries are names of flows.
	 */
	@JSONHint(ignore = true)
	public Map<String, LdFlowDto> nameFlowMap = new HashMap<String, LdFlowDto>();
	
	/**
	 * The map of network bridges.
	 * Keys of the map entries are names of network bridges.
	 */
	@JSONHint(ignore = true)
	public Map<String, LdBridgeDto> nameBridgeMap = new HashMap<String, LdBridgeDto>();
	
	/**
	 * Constructs an empty object.
	 * @return an object.
	 */
	public static LdTopoDto constructEmptyObject() {
		LdTopoDto obj = new LdTopoDto();
		obj.switches = new ArrayList<LdNodeDto>();
		obj.hosts = new ArrayList<LdNodeDto>();
		obj.controllers = new ArrayList<String>();
		obj.flows = new ArrayList<LdFlowDto>();
		obj.bridges = new ArrayList<LdBridgeDto>();
		return obj;
	}
}
