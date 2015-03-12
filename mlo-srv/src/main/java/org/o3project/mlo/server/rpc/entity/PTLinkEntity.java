/**
 * PTLinkEntity.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.entity;

/**
 * This class is the data class designates link information for packet transport nodes.
 */
public class PTLinkEntity {
	
	/**
	 * Link ID. 
	 */
	public String linkId;
	
	/**
	 * Source node name.
	 */
	public String srcNode;
	
	/**
	 * Source port.
	 */
	public String srcPort;
	
	/**
	 * Destination node name.
	 */
	public String dstNode;
	
	/**
	 * Destination port.
	 */
	public String dstPort;
	
	/**
	 * Operation status.
	 */
	public String operStatus;
	
	/**
	 * Delay time.
	 */
	public String reqLatency;
	
	/**
	 * Band width. 
	 */
	public String reqBandwidth;
	
	/**
	 * Establishment status.
	 */
	public String establishmentStatus;
	
	/**
	 * Creates an instance.
	 * @param linkId Link ID.
	 * @param srcNode Source node name. 
	 * @param srcPort Source port.
	 * @param dstNode Destination node name.
	 * @param dstPort Destination port.
	 * @param operStatus Operation status.
	 * @param reqLatency Delay time.
	 * @param reqBandwidth Band width.
	 * @param establishmentStatus Establishment status.
	 */
	public PTLinkEntity(
			String linkId, String srcNode, String srcPort, String dstNode, String dstPort, 
			String operStatus, String reqLatency, String reqBandwidth, String establishmentStatus) {
		this.linkId = linkId;
		this.srcNode = srcNode;
		this.srcPort = srcPort;
		this.dstNode = dstNode;
		this.dstPort = dstPort;
		this.operStatus = operStatus;
		this.reqLatency = reqLatency;
		this.reqBandwidth = reqBandwidth;
		this.establishmentStatus = establishmentStatus;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s", 
				linkId, srcNode, srcPort, dstNode, dstPort, 
				operStatus, reqLatency, reqBandwidth, establishmentStatus);
	}
}
