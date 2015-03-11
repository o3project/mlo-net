/**
 * LdFlowDto.java
 * (C) 2015, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.dto;

import java.util.List;

/**
 * This is a DTO class which indicates a network flow.
 */
public class LdFlowDto {
	/**
	 * The name of the flow.
	 */
	public String name;
	
	/**
	 * The list of the bridge name.
	 * It means that this flow is composed by the array of those bridges.
	 */
	public List<String> brNames;
}
