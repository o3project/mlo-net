/**
 * CeNodeInfo.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

/**
 * This class represents the information of CE node.
 */
public class CeNodeInfo {

	/**
	 * Node name.
	 */
	public final String name;
	
	/**
	 * Connected port.
	 */
	public final String port;

	/**
	 * A constructor.
	 * @param name Node name.
	 * @param port Port.
	 */
	public CeNodeInfo(String name, String port) {
		this.name = name;
		this.port = port;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ("{"
				+ "\"name\":\"" + name + "\""
				+ ",\"port\":\"" + port + "\""
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
		final Integer prime = 10711;
		return prime * this.toString().hashCode();
	}
}
