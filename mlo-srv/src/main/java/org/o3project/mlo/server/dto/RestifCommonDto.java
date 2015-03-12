/**
 * RestifCommonDto.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * This class is the DTO class of MLO API common header.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RestifCommonDto {
	
	/** Software version. */
	@XmlElement(name = "Version")
	public Integer version;
	
	/** Sender component information. */
	@XmlElement(name = "SourceComponent")
	public RestifComponentDto srcComponent;
	
	/** Receiver component information. */
	@XmlElement(name = "DestComponent")
	public RestifComponentDto dstComponent;
	
	/** Operation type. */
	@XmlElement(name = "Operation")
	public String operation;

	/**
	 * Creates an instance.
	 * @param version Software version, should be 1.
	 * @param srcCmpName Sender component name.
	 * @param dstCmpName Receiver component name.
	 * @param operation Operation type (Request/Response)
	 * @return An instance.
	 */
	public static RestifCommonDto createInstance(Integer version, 
			String srcCmpName, String dstCmpName, String operation) {
		RestifCommonDto obj = new RestifCommonDto();
		obj.version = 1;
		if (srcCmpName != null) {
			obj.srcComponent = new RestifComponentDto();
			obj.srcComponent.name = srcCmpName;
		}
		if (dstCmpName != null) {
			obj.dstComponent = new RestifComponentDto();
			obj.dstComponent.name = dstCmpName;
		}
		obj.operation = operation;
		return obj;
	}
}
