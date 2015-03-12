/**
 * RyPortDto.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.dto;

import net.arnx.jsonic.JSONHint;

/**
 * This is a DTO class which indicates a network port installed in a network switch.
 */
public class RyPortDto {
	/**
	 * The name of the port, e.g. "eth-1" and so on.
	 */
	public String name;
	
	/**
	 * The port number of the port.
	 */
	@JSONHint(name = "port_no")
	public String portNo;

	/**
	 * The MAC address of the port. 
	 */
	@JSONHint(name = "hw_addr")
	public String hwAddr;

	/**
	 * The data path ID of the network switch, in which this port is installed.
	 */
	public String dpid;
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ("{"
				+ "\"name\":\"" + name + "\""
				+ ",\"port_no\":\"" +  portNo + "\""
				+ ",\"hw_addr\":\"" +  hwAddr + "\""
				+ ",\"dpid\":\"" +  dpid + "\""
				+ "}");
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
			return this.toString().equals(obj.toString());
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final Integer prime = 10723;
		return prime * this.toString().hashCode();
	}
}
