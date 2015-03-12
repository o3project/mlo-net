/**
 * LdNodeDto.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.dto;

import java.util.List;

/**
 * This is a DTO class which indicates a network node.
 */
public class LdNodeDto {
	/**
	 * The name of the node.
	 */
	public String name;
	
	/**
	 * The data path ID of the node.
	 */
	public String dpid;
	
	/**
	 * The type of the node.
	 * The alternatives are as follows:
	 * <dl>
	 * <dt>host</dt><dd></dd>
	 * <dt>edge</dt><dd></dd>
	 * <dt>metro</dt><dd></dd>
	 * <dt>core</dt><dd></dd>
	 * </dl>
	 */
	public String type;
	
	/**
	 * The IP address of the node.
	 * If no IP address is assigned, the value should be null.
	 */
	public String ip;

	/**
	 * The MAC address of the node.
	 * If no MAC address is assigned, the value should be null.
	 */
	public String mac;

	/**
	 * The list of port names, those are connected to network channels.
	 * An element port included in this list is connected to a network bridge 
	 * which has the same index in the brNames field.
	 */
	public List<String> portNames;
	
	/**
	 * The list of network bridge names.
	 */
	public List<String> brNames;
}
