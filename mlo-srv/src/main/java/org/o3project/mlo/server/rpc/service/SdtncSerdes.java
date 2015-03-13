/**
 * SdtncSerdes.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.service;

import java.io.InputStream;
import java.io.OutputStream;

import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;


/**
 * This interface designates serializer-deserializer feature for SDTNC VRM NBI.
 */
public interface SdtncSerdes {

	/**
	 * Reads response XML from stream and then creates response DTO.
	 * @param istream the input stream including response XML.
	 * @return SDTNC VRM NBI response DTO. 
	 */
	SdtncResponseDto deserializeFromXml(InputStream istream);

	/**
	 * Writes response XML to stream with request DTO.
	 * This method may throw RuntimeException.
	 * @param reqDto SDTNC VRM NBI request DTO.
	 * @param ostream XML the output stream.
	 */
	void serializeToXml(SdtncRequestDto reqDto, OutputStream ostream);

}
