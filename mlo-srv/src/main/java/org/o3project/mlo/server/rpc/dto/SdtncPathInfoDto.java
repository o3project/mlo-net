/**
 * SdtncPathInfoDto.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * This class is the path information DTO class.
 */
public class SdtncPathInfoDto {

	/** Grade of the path. */
	@XmlElement(name = "vGrade")
	public Integer vGrade;

}

