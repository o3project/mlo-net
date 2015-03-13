/**
 * LdBridgeDto.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.dto;

/**
 * This is a DTO class which indicates a network bridge.
 */
public class LdBridgeDto {
	/**
	 * The default value of delay.
	 */
	public static final String DEFAULT_DELAY = "0ms";

	/**
	 * The default value of band width.
	 */
	public static final String DEFAULT_BW = "1000000kbps";

	/**
	 * The name of the network bridge, e.g. br1.
	 */
	public String name;

	/**
	 * The delay time of the network bridge.
	 * This value must end with "ms".
	 * For example, "100ms" means 100 milliseconds delay. 
	 */
	public String delay;
	
	/**
	 * The band width of the network bridge.
	 * This value must end with "kbps";
	 * For example, "1000kbps" means 1 Mbps band width.
	 */
	public String bw;
}
