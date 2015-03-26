/**
 * Serdes.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

import java.io.InputStream;
import java.io.OutputStream;

import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;


/**
 * This interface designates the serializer-deserializer feature of MLO web API.
 * This feature connects the MLO web API with MLO internal processes.
 * 
 */
public interface Serdes {

	/**
	 * Deserializes the input stream including request XML of MLO web API as the request DTO.
	 * @param istream the stream including request XML.
	 * @return the request DTO.
	 * @throws ApiCallException Failed in deserializing.
	 */
	RestifRequestDto unmarshal(InputStream istream) throws ApiCallException;

	/**
	 * Serializes the response DTO to the output stream of MLO web API.
	 * This method may throw the RuntimeException.
	 * @param dto the response DTO.
	 * @param ostream the output stream response XML is written to.
	 */
	void marshal(RestifResponseDto dto, OutputStream ostream);

	/**
	 * Deserializes the input stream including request XML of MLO web API as the request DTO.
	 * @param istream the stream including request XML.
	 * @param contentType the content type.
	 * @return the request DTO.
	 * @throws ApiCallException Failed in deserializing.
	 */
	RestifRequestDto deserialize(InputStream istream, String contentType) throws ApiCallException;

	/**
	 * Serializes the response DTO to the output stream of MLO web API.
	 * This method may throw the RuntimeException.
	 * @param dto the response DTO.
	 * @param ostream the output stream response XML is written to.
	 * @param contentType the content type.
	 */
	void serialize(RestifResponseDto dto, OutputStream ostream, String contentType);
	
	

}
