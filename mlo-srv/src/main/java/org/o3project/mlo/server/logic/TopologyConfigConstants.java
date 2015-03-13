/**
 * TopologyConfigConstants.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

/**
 * This interface defines topology-related constants.
 */
public interface TopologyConfigConstants {

	/**
	 * This attribute key designates an attribute value of {@link org.o3project.mlo.server.dto.LinkInfoDto}, which is the name of the link.
	 */
	String LINK_ATTR_KEY_LINK_NAME = "linkName";
	
	/**
	 * This attribute key designates an attribute value of {@link org.o3project.mlo.server.dto.LinkInfoDto}, which is the band width of the link.
	 */
	String LINK_ATTR_KEY_BAND_WIDTH = "bandWidth";
	
	/**
	 * This attribute key designates an attribute value of {@link org.o3project.mlo.server.dto.LinkInfoDto}, which is the delay of the link.
	 */
	String LINK_ATTR_KEY_DELAY = "delay";
	
	/**
	 * This attribute key designates an attribute value of {@link org.o3project.mlo.server.dto.FlowDto}, which is the name of the flow.
	 */
	String FLOW_ATTR_KEY_FLOW_NAME = "flowName";

	/**
	 * This attribute key designates an attribute value of {@link org.o3project.mlo.server.dto.FlowDto}, which is the reversed-direction flow of the flow.
	 */
	String FLOW_ATTR_KEY_REVERSE = "reverse";

	/**
	 * This attribute key designates an attribute value of {@link org.o3project.mlo.server.dto.FlowDto}, which is the VLAN ID of the flow.
	 */
	String FLOW_ATTR_KEY_VLAN_ID = "vlanId";

	/**
	 * This attribute key designates an attribute value of {@link org.o3project.mlo.server.dto.FlowDto}, which is the SDTNC link ID of the flow.
	 */
	String FLOW_ATTR_KEY_SDTNC_LINK_ID = "sdtncLinkId";
	
	/**
	 * The Odenos connection ID of PT node, PT1.
	 */
	Integer AMN64001_ID = 1;
	
	/**
	 * The Odenos connection name of PT node, PT1.
	 */
	String AMN64001_NAME = "AMN64001";
	
	/**
	 * The Odenos connection ID of PT node, PT2.
	 */
	Integer AMN64002_ID = 2;
	
	/**
	 * The Odenos connection name of PT node, PT2.
	 */
	String AMN64002_NAME = "AMN64002";
	
	/**
	 * The Odenos connection ID of PT node, PT3.
	 */
	Integer AMN64003_ID = 3;
	
	/**
	 * The Odenos connection name of PT node, PT3.
	 */
	String AMN64003_NAME = "AMN64003";
	
	/**
	 * The Odenos connection link ID between PT1 and PT2.
	 */
	Integer AMN6400_1_2_LINK_ID = 50000012;
	
	/**
	 * The Odenos connection link ID between PT2 and PT3.
	 */
	Integer AMN6400_2_3_LINK_ID = 50000023;
	
	/**
	 * The Odenos connection link ID between PT3 and PT1.
	 */
	Integer AMN6400_3_1_LINK_ID = 50000031;
	
	/**
	 * The Odenos connection link ID between PT2 and PT1.
	 */
	Integer AMN6400_2_1_LINK_ID = 50000021;
	
	/**
	 * The Odenos connection link ID between PT3 and PT2.
	 */
	Integer AMN6400_3_2_LINK_ID = 50000032;
	
	/**
	 * The Odenos connection link ID between PT1 and PT3.
	 */
	Integer AMN6400_1_3_LINK_ID = 50000013;
	
	/**
	 * The Odenos connection ID of PT node, NodeA.
	 */
	Integer PT_NODEA_ID = 10000000;
	
	/**
	 * The Odenos connection node name of PT node, NodeA.
	 */
	String PT_NODEA_NAME = "nodeA";
	
	/**
	 * The Odenos connection ID of PT node, NodeC.
	 */
	Integer PT_NODEC_ID = 10000002;
	
	/**
	 * The Odenos connection node name of PT node, NodeC.
	 */
	String PT_NODEC_NAME = "nodeC";
	
	/**
	 * The Odenos connection ID of PT node, NodeD.
	 */
	Integer PT_NODED_ID = 10000003;
	
	/**
	 * The Odenos connection node name of PT node, NodeD.
	 */
	String PT_NODED_NAME = "nodeD";
	
	/**
	 * The Odenos connection link ID between NodeA and NodeC.
	 */
	Integer PT_NODE_A_D_LINK_ID = 20000000;
	
	/**
	 * The Odenos connection link ID between NodeC and NodeD.
	 */
	Integer PT_NODE_D_C_LINK_ID = 20000001;
	
	/**
	 * The Odenos connection link ID between NodeA and NodeD.
	 */
	Integer PT_NODE_A_C_LINK_ID = 20000002;
	
	/**
	 * The Odenos connection link ID between NodeC and NodeA.
	 */
	Integer PT_NODE_D_A_LINK_ID = 30000000;
	
	/**
	 * The Odenos connection link ID between NodeD and NodeC.
	 */
	Integer PT_NODE_C_D_LINK_ID = 30000001;
	
	/**
	 * The Odenos connection link ID between NodeD and NodeA.
	 */
	Integer PT_NODE_C_A_LINK_ID = 30000002;

}
