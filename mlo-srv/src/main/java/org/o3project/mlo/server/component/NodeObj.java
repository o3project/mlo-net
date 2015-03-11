/**
 * Node.java
 * (C) 2015, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.component;

import java.util.Map;

/**
 * This class is the abstraction class of network node.
 */
public class NodeObj {
	/**
	 * Meta information.
	 */
	public MetaObj meta;
	
	/**
	 * Network devices those are installed to this instance.
	 */
	public Map<String, NetDeviceObj> netDeviceMap;
	
	/**
	 * Installs a network device.
	 * @param netDeviceObj A network device.
	 * @return A network device.
	 */
	public NetDeviceObj install(NetDeviceObj netDeviceObj) {
		if (netDeviceObj.node != null && netDeviceObj.node.netDeviceMap.containsValue(netDeviceObj)) {
			String oldKey = null;
			for (Map.Entry<String, NetDeviceObj> entry : netDeviceObj.node.netDeviceMap.entrySet()) {
				if (entry.getValue().equals(netDeviceObj)) {
					oldKey = entry.getKey();
					break;
				}
			}
			netDeviceObj.node.netDeviceMap.remove(oldKey);
		}
		String key = netDeviceObj.meta.id;
		netDeviceMap.put(key, netDeviceObj);
		netDeviceObj.node = this;
		return netDeviceObj;
	}
}
