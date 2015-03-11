/**
 * RyLinkDto.java
 * (C) 2015, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.dto;

/**
 * This is a DTO class which indicates a network link.
 */
public class RyLinkDto {
	/**
	 * Source port of the network link.
	 */
	public RyPortDto src;
	
	/**
	 * Destination port of the network link.
	 */
	public RyPortDto dst;
}
