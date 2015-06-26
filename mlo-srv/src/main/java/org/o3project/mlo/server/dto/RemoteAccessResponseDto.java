/**
 * RemoteAccessResponseDto.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.dto;

import net.arnx.jsonic.JSON;

/**
 * This is a DTO class which indicates a response of remote access.
 */
public class RemoteAccessResponseDto {
	
	/**
	 * status of a SSH connection.
	 */
	public String status;
	
	/**
	 * Execution result of a command sent. 
	 */
	public String result;
	
	/**
	 * Exception information.
	 */
	public String exception;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JSON.encode(this);
	}
}
