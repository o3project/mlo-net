/**
 * SdtncVlanDto.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * This class is the VLAN DTO.
 */
public class SdtncVlanDto {

	/** VLAN tag number. */
	@XmlElement(name = "vVlanTagNum")
	public Integer vVlanTagNum;

	/** VLAN tag mode. */
	@XmlElement(name = "vVlanTagMode")
	public Integer vVlanTagMode;

	/** UNI. */
	@XmlElement(name = "uni")
	public SdtncUniDto uni;

	/** NNI. */
	@XmlElement(name = "nni")
	public SdtncNniDto nni;

}

