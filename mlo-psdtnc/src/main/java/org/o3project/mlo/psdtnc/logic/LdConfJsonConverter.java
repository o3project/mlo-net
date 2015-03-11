/**
 * LdConfJsonConverter.java
 * (C) 2015, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.psdtnc.logic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.o3project.mlo.server.dto.LdTopoDto;

/**
 * LdConfJsonConverter
 *
 */
public interface LdConfJsonConverter {
	void convertToJson(InputStream confInputStream, OutputStream jsonOutputStream) throws IOException;
	
	LdTopoDto convertFromConf(InputStream confInputStream) throws IOException;
}
