/**
 * Flow.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.component;

import java.util.List;

/**
 * This class is the abstraction class of network flow.
 */
public class FlowObj {

	/**
	 * Meta information.
	 */
	public MetaObj meta;
	
	/**
	 * Flow relays.
	 * This means relay nodes in the flow.
	 */
	public List<FlowRelayObj> flowRelays;
}
