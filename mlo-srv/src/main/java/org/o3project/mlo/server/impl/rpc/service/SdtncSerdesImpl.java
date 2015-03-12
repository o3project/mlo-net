/**
 * SdtncSerdesImpl.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXB;

import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;
import org.o3project.mlo.server.rpc.service.SdtncSerdes;


/**
 * This class is the implementation class of {@link SdtncSerdes} interface.
 */
public class SdtncSerdesImpl implements SdtncSerdes {
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service#deserializeFromXml(java.io.InputStream)
	 */
	@Override
	public SdtncResponseDto deserializeFromXml(InputStream istream) {
		SdtncResponseDto response = JAXB.unmarshal(istream , SdtncResponseDto.class);
		return response;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service#serializeToXml(org.o3project.mlo.server.rpc.dto.SdtncRequestDto, java.io.OutputStream)
	 */
	@Override
	public void serializeToXml(
			SdtncRequestDto reqDto, 
			OutputStream ostream) {
		JAXB.marshal(reqDto, ostream);
	}
}
