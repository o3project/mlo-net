/**
 * SdtncLineDto.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * This class is the line DTO class for SDTNC.
 */
public class SdtncLineDto {
	
	/** Element ID. */
	@XmlAttribute(name = "elementIndex")
	public String elementIndex;

	/** Line sequence number. */
	@XmlElement(name = "lineSequence")
	public Integer lineSequence;

}

