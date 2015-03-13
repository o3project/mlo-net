/**
 * RestifComponentDto.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * This is the class of sender/receiver component.
 */
public class RestifComponentDto {
	
	/** Component name. */
	@XmlElement(name = "ComponentName")
	public String name;

}
