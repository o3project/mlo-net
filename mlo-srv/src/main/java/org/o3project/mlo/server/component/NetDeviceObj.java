/**
 * NetDevice.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.component;

/**
 * This class is the abstraction class of network device.
 */
public class NetDeviceObj {

	/**
	 * Meta information.
	 */
	public MetaObj meta;
	
	/**
	 * The node which this network device is installed to.
	 */
	public NodeObj node;
	
	/**
	 * The channel which this network device is connected to.
	 */
	public ChannelObj channel;
	
	/**
	 * Connects this network device to a channel.
	 * @param channelObj A channel.
	 * @return The instance.
	 */
	public NetDeviceObj connectTo(ChannelObj channelObj) {
		channel = channelObj;
		channelObj.netDeviceSet.add(this);
		return this;
	}
}
