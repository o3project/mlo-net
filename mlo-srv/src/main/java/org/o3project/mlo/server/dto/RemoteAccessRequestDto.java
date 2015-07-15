/**
 * RemoteAccessRequestDto.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.dto;

import net.arnx.jsonic.JSON;

/**
 * This is a DTO class which indicates a request of remote access.
 */
public class RemoteAccessRequestDto {
	
	/**
	 * String of a command to execute.
	 */
	public String commandString;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JSON.encode(this);
	}
}
