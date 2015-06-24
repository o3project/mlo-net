/**
 * AlarmDto.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONHint;

import org.seasar.framework.beans.util.Beans;

/**
 * AlarmDto
 */
public class AlarmDto {
	
	public String id;

	@JSONHint(format="yyyy-MM-dd HH:mm:ss.SSSZ")
	public Date timestamp;
	
	public String state;
	
	public String type;
	
	public String targetType;
	
	public String targetId;
	
	public String description;
	
	public String generateIdentifier() {
		Map<String, Object> props = new HashMap<>();
		Beans.copy(this, props).execute();
		//props.remove("id");
		//props.remove("timestamp");
		//props.remove("description");
		return JSON.encode(props, true);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (!this.getClass().equals(obj.getClass())) {
			return false;
		} else {
			return (this.generateIdentifier().equals(((AlarmDto) obj).generateIdentifier()));
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final Integer prime = 17321;
		return prime * this.generateIdentifier().hashCode();
	}
	
	public static void main(String[] args) {
		AlarmDto obj = new AlarmDto();
		obj.id = "id";
		obj.timestamp = new Date();
		obj.state = "ok";
		obj.type = "type";
		obj.targetType = "node";
		obj.targetId = "s5";
		obj.description = "description";
		System.out.println(JSON.encode(obj, true));
		System.out.println(obj.generateIdentifier());
	}
}
