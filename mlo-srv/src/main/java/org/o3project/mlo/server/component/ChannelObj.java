/**
 * Channel.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.component;

import java.util.Set;

/**
 * This class is the abstraction class of network channel.
 */
public class ChannelObj {
	/**
	 * Meta information.
	 */
	public MetaObj meta;
	
	/**
	 * Network devices connected to this channel.
	 */
	public Set<NetDeviceObj> netDeviceSet;
}
