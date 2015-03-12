/**
 * Topology.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.component;

import java.util.Map;

/**
 * This class is the abstraction class of network topology.
 */
public class TopologyObj {

	/**
	 * Meta information.
	 */
	public MetaObj meta;
	
	/**
	 * Nodes.
	 */
	public Map<String, NodeObj> nodeMap;
	
	/**
	 * Channels.
	 */
	public Map<String, ChannelObj> channelMap;
}
