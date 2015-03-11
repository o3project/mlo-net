/**
 * SdtncUniDto.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * This class is the UNI DTO for SDTNC.
 */
public class SdtncUniDto {

	/** VLAN ID */
    @XmlElement(name = "vVlanVLanIdNniClient")
    public Integer vVlanVLanIdNniClient;

	/** Inner VLAN ID */
    @XmlElement(name = "vVlanInnerVlanIdNniClient")
    public Integer vVlanInnerVlanIdNniClient;

	/** Inner CoS. */
    @XmlElement(name = "vVlanInnerCosNniClient")
    public Integer vVlanInnerCosNniClient;

	/** Outer CoS. */
    @XmlElement(name = "vVlanOutterCosNniClient")
    public Integer vVlanOutterCosNniClient;

}

