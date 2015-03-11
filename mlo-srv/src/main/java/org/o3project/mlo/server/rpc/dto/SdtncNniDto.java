/**
 * SdtncNniDto.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * This class is the NNI DTO class for SDTNC.
 */
public class SdtncNniDto {
	
	/** VLAN ID. */
	@XmlElement(name = "vVlanVlanIdNniLine")
	public Integer vVlanVlanIdNniLine;

	/** Outer CoS. */
	@XmlElement(name = "vVlanOutterCosNniLine")
	public Integer vVlanOutterCosNniLine;

}

