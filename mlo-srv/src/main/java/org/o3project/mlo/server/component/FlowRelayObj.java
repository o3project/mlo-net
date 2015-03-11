/**
 * FlowRelay.java
 * (C) 2015, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.component;

/**
 *  This class is the abstraction class of network flow relay.
 */
public class FlowRelayObj {
	/**
	 * An ingress network channel.
	 */
	public ChannelObj ingressChannel;
	
	/**
	 * An ingress network device connected to an ingress channel.
	 */
	public NetDeviceObj ingressNetDevice;
	
	/**
	 * A relay node.
	 */
	public NodeObj relayNode;

	/**
	 * An egress network device connected to an egress channel.
	 */
	public NetDeviceObj egressNetDevice;
	
	/**
	 * An egress network channel.
	 */
	public ChannelObj egressChannel;
}
