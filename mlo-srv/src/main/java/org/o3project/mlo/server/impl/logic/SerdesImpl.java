/**
 * SerdesImpl.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXB;

import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.logic.ApiCallException;
import org.o3project.mlo.server.logic.Serdes;


/**
 * This class is the implementation class of {@link Serdes} interface.
 */
public class SerdesImpl implements Serdes {
	
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.Restif#unmarshal(java.io.InputStream)
	 */
	@Override
	public RestifRequestDto unmarshal(InputStream istream) throws ApiCallException {
		RestifRequestDto reqDto = null;
		reqDto = JAXB.unmarshal(istream, RestifRequestDto.class);
		return reqDto;
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.Restif#marshal(org.o3project.mlo.server.dto.RestifResponseDto, java.io.OutputStream)
	 */
	@Override
	public void marshal(RestifResponseDto dto, OutputStream ostream) {
		JAXB.marshal(dto, ostream);
	}
}
