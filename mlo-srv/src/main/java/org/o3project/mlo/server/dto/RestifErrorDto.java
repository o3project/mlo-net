/**
 * RestifErrorDto.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * This is the DTO class of error information. 
 */
public class RestifErrorDto {
	
	/** A cause of the error. */
	@XmlElement(name = "Cause")
	public String cause;
	
	/** A detail description. */
	@XmlElement(name = "Detail")
	public String detail;
	
	/** Slice name. */
	@XmlElement(name = "SliceName")
	public String sliceName;
	
	/** Slice ID. */
	@XmlElement(name = "SliceId")
	public String sliceId;
}
