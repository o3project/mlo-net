/**
 * SdtncLspDto.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * This class is the LSP DTO class for SDTNC. 
 */
public class SdtncLspDto {
	
	/** Downstream LSP label value. */
	@XmlElement(name = "vLspDownLabel")
	public String vLspDownLabel;

	/** Upstream LSP label value. */
	@XmlElement(name = "vLspUpLabel")
	public String vLspUpLabel;

}

