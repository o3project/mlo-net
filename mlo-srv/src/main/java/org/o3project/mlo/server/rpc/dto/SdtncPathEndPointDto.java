/**
 * SdtncPathEndPointDto.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * This class is the path end point DTO class for SDTNC.
 */
public class SdtncPathEndPointDto {

	/** Start/end point. */
	@XmlAttribute(name = "startEndPoint")
	public String startEndPoint;

	/** Node ID. */
	@XmlElement(name = "neId")
	public String neId;

	/** Port ID. */
	@XmlElement(name = "portId")
	public String portId;

}

