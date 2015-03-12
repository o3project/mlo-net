/**
 * RySwitchDto.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.dto;

import java.util.List;

/**
 * This is a DTO class which indicates a network switch.
 */
public class RySwitchDto {
	/**
	 * The data path ID of this network switch.
	 */
	public String dpid;
	
	/**
	 * The list of ports those are installed in this switch.
	 */
	public List<RyPortDto> ports;

}
