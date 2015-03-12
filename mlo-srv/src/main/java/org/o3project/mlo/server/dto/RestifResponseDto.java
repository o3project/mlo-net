/**
 * RestifResponseDto.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is the DTO class of MLO API request.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "rootTag")
public class RestifResponseDto {
	
	/** Common header DTO. */
	@XmlElement(name = "Common")
    public RestifCommonDto common;

	/** Error header DTO. */
    @XmlElement(name = "Error")
	public RestifErrorDto error;
   
    /** Slice DTOs. */
    @XmlElement(name = "Slice")
    public List<SliceDto> slices;
}
