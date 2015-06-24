/**
 * EventDto.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.dto;

import java.util.Date;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONHint;

/**
 * EventDto
 */
public class EventDto {
	
	public String id;

	@JSONHint(format="yyyy-MM-dd HH:mm:ss.SSSZ")
	public Date timestamp;
	
	public String userid;
	
	public String type;
	
	public String targetType;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JSON.encode(this);
	}
}
