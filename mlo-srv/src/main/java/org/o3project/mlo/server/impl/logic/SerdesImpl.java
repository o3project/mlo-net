/**
 * SerdesImpl.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXB;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.logic.ApiCallException;
import org.o3project.mlo.server.logic.Serdes;


/**
 * This class is the implementation class of {@link Serdes} interface.
 */
public class SerdesImpl implements Serdes {
	
	private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.Restif#unmarshal(java.io.InputStream)
	 */
	@Override
	public RestifRequestDto unmarshal(InputStream istream) throws ApiCallException {
		return deserialize(istream, null);
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.Restif#marshal(org.o3project.mlo.server.dto.RestifResponseDto, java.io.OutputStream)
	 */
	@Override
	public void marshal(RestifResponseDto dto, OutputStream ostream) {
		serialize(dto, ostream, null);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.Serdes#deserialize(java.io.InputStream, java.lang.String)
	 */
	@Override
	public RestifRequestDto deserialize(InputStream istream, String contentType) throws ApiCallException {
		RestifRequestDto reqDto = null;
		if (isJson(contentType)) {
			try {
				reqDto = JSON.decode(istream, RestifRequestDto.class);
			} catch (JSONException | IOException e) {
				String msg = "Failed to deserialize from json.";
				throw new ApiCallException(msg, e);
			}
		} else {
			reqDto = JAXB.unmarshal(istream, RestifRequestDto.class);
		}
		return reqDto;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.Serdes#serialize(org.o3project.mlo.server.dto.RestifResponseDto, java.io.OutputStream, java.lang.String)
	 */
	@Override
	public void serialize(RestifResponseDto dto, OutputStream ostream, String contentType) {
		if (isJson(contentType)) {
			try {
				JSON.encode(dto, ostream);
			} catch (JSONException | IOException e) {
				String msg = "Failed to serialize to json.";
				throw new RuntimeException(msg, e);
			}
		} else {
			JAXB.marshal(dto, ostream);
		}
	}
	
	private static boolean isJson(String contentType) {
		return (contentType != null && contentType.startsWith(CONTENT_TYPE_APPLICATION_JSON));
	}
}
