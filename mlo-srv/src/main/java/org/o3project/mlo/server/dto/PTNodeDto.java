/**
 * PTNodeDto.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.dto;

/**
 * This class is the DTO class of packet transport node.
 */
public class PTNodeDto {

	/**
	 * Node ID
	 */
	public Integer id;

	/**
	 * Node name. 
	 */
	public String name;
	
	/**
	 * Creates an instance.
	 * @param id Node ID.
	 * @param name Node name.
	 * @return An instance.
	 */
	public static PTNodeDto createInstance(Integer id, String name) {
		PTNodeDto obj = new PTNodeDto();
		obj.id = id;
		obj.name = name;
		return obj;
	}
}
