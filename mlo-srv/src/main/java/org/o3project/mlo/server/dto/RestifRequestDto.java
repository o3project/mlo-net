/**
 * RestifRequestDto.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is the DTO class of MLO API request.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "rootTag")
public class RestifRequestDto {
	
	/** Header DTO. */
	@XmlElement(name = "Common")
	public RestifCommonDto common;
	
	/** Slice DTO. */
	@XmlElement(name = "Slice")
	public SliceDto slice;

}
