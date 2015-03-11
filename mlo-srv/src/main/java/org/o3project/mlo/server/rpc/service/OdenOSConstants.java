/**
 * OdenOSConstants.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.rpc.service;


/**
 * This class defines constants used by Odenos.
 */
public class OdenOSConstants {

	/**
	 * The maximum number of flow per a slice.
	 */
	public static final int FLOW_MAX = 200;
	
	/**
	 * Attribute key of link request: operation status.
	 */
	public static final String LINK_ATTRIBUTE_OPER_STATUS = "oper_status";
	
	/**
	 * Attribute key of link request: delay time.
	 */
	public static final String LINK_ATTRIBUTE_REQ_LATENCY = "req_latency";
	
	/**
	 * Attribute key of link request: band width.
	 */
	public static final String LINK_ATTRIBUTE_REQ_BANDWIDTH = "req_bandwidth";
	
	/**
	 * Attribute key of link request: establishment status.
	 */
	public static final String LINK_ATTRIBUTE_ESTABLISHMENT_STATUS = "establishment_status";

	/**
	 * Common prefix of ID.
	 */
	public static final String NW_SDN = "NW=SDN";

	/**
	 * PT1 node name.
	 */
	public static final String NODE_PT_A = NW_SDN + ",NE=PT1";

	/**
	 * PT2 node name.2
	 */
	public static final String NODE_PT_B = NW_SDN + ",NE=PT2";

	/**
	 * PT3 node name.
	 */
	public static final String NODE_PT_C = NW_SDN + ",NE=PT3";

	/**
	 * Ether port ID of PT1 (PT1-PT3 link).
	 */
	public static final String PORT_PT_A_ETHER_1 = NODE_PT_A + ",Layer=Ether,TTP=1";

	/**
	 * Ether port ID of PT1 (PT1-PT2 link).
	 */
	public static final String PORT_PT_A_ETHER_2 = NODE_PT_A + ",Layer=Ether,TTP=2";

	/**
	 * Ether port ID of PT2 (PT1-PT2 link).
	 */
	public static final String PORT_PT_B_ETHER_1 = NODE_PT_B + ",Layer=Ether,TTP=1";

	/**
	 * Ether port ID of PT2 (PT2-PT3 link).
	 */
	public static final String PORT_PT_B_ETHER_2 = NODE_PT_B + ",Layer=Ether,TTP=2";

	/**
	 * Ether port ID of PT3 (PT3-PT2 link).
	 */
	public static final String PORT_PT_C_ETHER_1 = NODE_PT_C + ",Layer=Ether,TTP=1";

	/**
	 * Ether port ID of PT3 (PT3-PT1 link).
	 */
	public static final String PORT_PT_C_ETHER_2 = NODE_PT_C + ",Layer=Ether,TTP=2";

	/**
	 * LSP port ID of PT1.
	 */
	public static final String PORT_PT_A_LSP_3 = NODE_PT_A + ",Layer=LSP,TTP=3";
	
	/**
	 * LSP port ID of PT3.
	 */
	public static final String PORT_PT_C_LSP_3 = NODE_PT_C + ",Layer=LSP,TTP=3";

	/**
	 * Link ID 1 (PT1-PT2)
	 */
	public static final String LINK_TL_1 = NW_SDN + ",Layer=Ether,TL=1";
	
	/**
	 * Link ID 2 (PT2-PT3)
	 */
	public static final String LINK_TL_2 = NW_SDN + ",Layer=Ether,TL=2";
	
	/**
	 * Link ID 3 (PT1-PT3)
	 */
	public static final String LINK_TL_3 = NW_SDN + ",Layer=Ether,TL=3";
	
	/**
	 * Link ID 4 (PT2-PT1)
	 */
	public static final String LINK_TL_4 = NW_SDN + ",Layer=Ether,TL=4";
	
	/**
	 * Link ID 5 (PT3-PT2)
	 */
	public static final String LINK_TL_5 = NW_SDN + ",Layer=Ether,TL=5";
	
	/**
	 * Link ID 6 (PT3-PT1)
	 */
	public static final String LINK_TL_6 = NW_SDN + ",Layer=Ether,TL=6";

	/**
	 * Flow type name.
	 */
	public static final String FLOW_TYPE = "BASIC_FLOW";
	
	/**
	 * Attribute value of link request: operation status.
	 */
	public static final String OPER_STATUS = "DOWN";
	
	/**
	 * Attribute value of link request: delay time (9999 msec).
	 */
	public static final String LATENCY_9999 = "9999 msec";
	
	/**
	 * Attribute value of link request: delay time (8 msec).
	 */
	public static final String LATENCY_8 = "8 msec";
	
	/**
	 * Attribute value of link request: band width (10 Gbps).
	 */
	public static final String BANDWIDTH_10000 = "10000 Mbps";
	
	/**
	 * Attribute value of link request: establishment status (establishing).
	 */
	public static final String ESTABLISHMENT_STATUS = "establishing";

	private OdenOSConstants(){
		super();
	}
}
