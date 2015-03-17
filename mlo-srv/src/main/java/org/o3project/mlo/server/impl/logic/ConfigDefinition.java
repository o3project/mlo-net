/**
 * ConfigDefinition.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.logic.ConfigConstants;
import org.o3project.mlo.server.logic.TopologyConfigConstants;

/**
 * This is the class to define default configuration values.
 */
final class ConfigDefinition implements ConfigConstants, TopologyConfigConstants {
	private static final Log LOG = LogFactory.getLog(ConfigDefinition.class);
	
	private ConfigDefinition(){
		super();
	}

	/**
	 * Main method to write default property values.
	 * @param args Not used at all. 
	 */
	public static void main(String[] args) {
		try {
			File propsFile = new File("src/main/resources", "default.mlo-srv.properties");
			Map<String, String> props = new LinkedHashMap<String, String>();
			defineProps(props);
			ConfigProviderImpl.storeProps(propsFile, props);
		} catch (IOException e) {
			LOG.error("Failed to store properties.", e);
		}
	}

	/**
	 * Defines default values of configuration.
	 * @param props Default values.
	 */
	static void defineProps(Map<String, String> props) {
		
		/*
		 * Common
		 */
		props.put(PROP_KEY_DEBUG_RESTIF_ENABLE, "false");
		props.put(PROP_KEY_DEBUG_CLIENTS, "debugClient");
		props.put(PROP_KEY_EXTRA_VLAN_ID_OFFSET, "430");
		props.put(PROP_KEY_DEBUG_FEATURE_VERSION, null);
		
		/*
		 * For OdenOS
		 */
		definePropsForOdenos(props);
		definePropsForOdenosTopology(props);

		/*
		 * For SDTNC
		 */
		definePropsForSdtnc(props);

		/*
		 * For Network topology.
		 */
		definePropsForNetworkTopology(props);
	}

	/**
	 */
	private static void definePropsForOdenos(Map<String, String> props) {
		props.put(PROP_KEY_ODENOS_REMOTE_SYSTEM_MANAGER_ID,   "systemmanager");
		props.put(PROP_KEY_ODENOS_REMOTE_SYSTEM_MANAGER_HOST, "127.0.0.1");
		props.put(PROP_KEY_ODENOS_REMOTE_SYSTEM_MANAGER_PORT, "6379");
		
		props.put(PROP_KEY_ODENOS_COMPONENT_MANAGER_ID,   "compmgr1");
		props.put(PROP_KEY_ODENOS_COMPONENT_MANAGER_HOST, "127.0.0.1");
		props.put(PROP_KEY_ODENOS_COMPONENT_MANAGER_PORT, "55555");
		props.put(PROP_KEY_ODENOS_COMPONENT_MANAGER_BASE_URL, null);
		
		props.put(PROP_KEY_ODENOS_LAUNCHER_HOST, "127.0.0.1");
		props.put(PROP_KEY_ODENOS_LAUNCHER_PORT, "20001");

		props.put(PROP_KEY_ODENOS_NETWORK_COMPONENT_ID_L2,   "networkcomponent2");
		props.put(PROP_KEY_ODENOS_NETWORK_COMPONENT_ID_L012,   "networkcomponent2");
		
		props.put(PROP_KEY_ODENOS_PT_DRIVER_ID, "ptdriver");
		
		props.put(PROP_KEY_ODENOS_CONNECTION_ID, "conn1");
		props.put(PROP_KEY_ODENOS_CONNECTION_TYPE, "original");
		props.put(PROP_KEY_ODENOS_CONNECTION_STATE, null);
		props.put(PROP_KEY_ODENOS_RESPONSE_TIMEOUTSEC, "10");
		props.put(PROP_KEY_ODENOS_IS_AVAILABLE_CREATE_L2, "true");
		props.put(PROP_KEY_ODENOS_IS_AVAILABLE_REQ_LINK_ESTABLISHED_COMPLETION, "false");
	}

	/**
	 * 
	 */
	private static void definePropsForOdenosTopology(Map<String, String> props) {
		String nwSdn = "NW=SDN";
		String nodePtA = nwSdn + ",NE=PT1";
		String nodePtB = nwSdn + ",NE=PT2";
		String nodePtC = nwSdn + ",NE=PT3";
		String portPtAEther1 = nodePtA + ",Layer=Ether,TTP=1";
		String portPtAEther2 = nodePtA + ",Layer=Ether,TTP=2";
		String portPtBEther1 = nodePtB + ",Layer=Ether,TTP=1";
		String portPtBEther2 = nodePtB + ",Layer=Ether,TTP=2";
		String portPtCEther1 = nodePtC + ",Layer=Ether,TTP=1";
		String portPtCEther2 = nodePtC + ",Layer=Ether,TTP=2";
		String portPtALsp3 = nodePtA + ",Layer=LSP,TTP=3";
		String portPtCLsp3 = nodePtC + ",Layer=LSP,TTP=3";
		String linkTl1 = nwSdn + ",Layer=Ether,TL=1";
		String linkTl2 = nwSdn + ",Layer=Ether,TL=2";
		String linkTl3 = nwSdn + ",Layer=Ether,TL=3";
		String linkTl4 = nwSdn + ",Layer=Ether,TL=4";
		String linkTl5 = nwSdn + ",Layer=Ether,TL=5";
		String linkTl6 = nwSdn + ",Layer=Ether,TL=6";
		
		String linkNameFmt = "link-%s-%s_%s";
		String linkNameTl1 = String.format(linkNameFmt, AMN6400_1_2_LINK_ID, AMN64001_NAME, AMN64002_NAME); // "50000012";
		String linkNameTl2 = String.format(linkNameFmt, AMN6400_2_3_LINK_ID, AMN64002_NAME, AMN64003_NAME); // "50000023";
		String linkNameTl3 = String.format(linkNameFmt, AMN6400_1_3_LINK_ID, AMN64001_NAME, AMN64003_NAME); // "50000013";
		String linkNameTl4 = String.format(linkNameFmt, AMN6400_2_1_LINK_ID, AMN64002_NAME, AMN64001_NAME); // "50000021";
		String linkNameTl5 = String.format(linkNameFmt, AMN6400_3_2_LINK_ID, AMN64003_NAME, AMN64002_NAME); // "50000032";
		String linkNameTl6 = String.format(linkNameFmt, AMN6400_3_1_LINK_ID, AMN64003_NAME, AMN64001_NAME); // "50000031";
		
		putOdenOSMplsConnectionLinkProps(linkNameTl1, linkTl1, 
				nodePtA, portPtAEther2, nodePtB, portPtBEther1, 
				props);
		putOdenOSMplsConnectionLinkProps(linkNameTl2, linkTl2, 
				nodePtB, portPtBEther2, nodePtC, portPtCEther1, 
				props);
		putOdenOSMplsConnectionLinkProps(linkNameTl3, linkTl3, 
				nodePtA, portPtAEther1, nodePtC, portPtCEther2, 
				props);
		putOdenOSMplsConnectionLinkProps(linkNameTl4, linkTl4, 
				nodePtB, portPtBEther1, nodePtA, portPtAEther2, 
				props);
		putOdenOSMplsConnectionLinkProps(linkNameTl5, linkTl5, 
				nodePtC, portPtCEther1, nodePtB, portPtBEther2, 
				props);
		putOdenOSMplsConnectionLinkProps(linkNameTl6, linkTl6, 
				nodePtC, portPtCEther2, nodePtA, portPtAEther1, 
				props);
		
		String flowName1 = "flow1";
		String flowName2 = "flow2";
		String flowName3 = "flow3";
		String flowName4 = "flow4";
		
		putOdenOSMplsConnectionFlowProps(flowName1, 
				nodePtA, portPtALsp3, nodePtC, portPtCLsp3, 
				Arrays.asList(linkNameTl1, linkNameTl2), props);
		putOdenOSMplsConnectionFlowProps(flowName2, 
				nodePtC, portPtCLsp3, nodePtA, portPtALsp3, 
				Arrays.asList(linkNameTl5, linkNameTl4), props);
		putOdenOSMplsConnectionFlowProps(flowName3, 
				nodePtA, portPtALsp3, nodePtC, portPtCLsp3, 
				Arrays.asList(linkNameTl3), props);
		putOdenOSMplsConnectionFlowProps(flowName4, 
				nodePtC, portPtCLsp3, nodePtA, portPtALsp3, 
				Arrays.asList(linkNameTl6), props);
	}
	
	/**
	 * 
	 */
	private static void putOdenOSMplsConnectionLinkProps(String linkName, 
			String odenosId, 
			String srcNode, String srcPort, String dstNode, String dstPort, 
			Map<String, String> props) {
		putOdenOSMplsConnectionLinkProp(linkName, PROP_SUBKEY_ODENOS_ID, odenosId, props);
		putOdenOSMplsConnectionLinkProp(linkName, PROP_SUBKEY_SRC_NODE, srcNode, props);
		putOdenOSMplsConnectionLinkProp(linkName, PROP_SUBKEY_SRC_PORT, srcPort, props);
		putOdenOSMplsConnectionLinkProp(linkName, PROP_SUBKEY_DST_NODE, dstNode, props);
		putOdenOSMplsConnectionLinkProp(linkName, PROP_SUBKEY_DST_PORT, dstPort, props);
	}
	
	/**
	 * 
	 */
	private static void putOdenOSMplsConnectionLinkProp(String linkName, String subKey, String value, Map<String, String> props) {
		String key = PROP_KEY_ODENOS_TOPOLOGY_LINKS_PREFIX_ + linkName + "." + subKey;
		props.put(key, value);
	}
	
	/**
	 * 
	 */
	private static void putOdenOSMplsConnectionFlowProps(String flowName, 
			String basicFlowMatchNode, String basicFlowMatchPort, 
			String basicFlowActionNode, String basicFlowActionPort, 
			List<String> linkNames,
			Map<String, String> props) {
		putOdenOSMplsConnectionFlowProp(flowName, PROP_SUBKEY_BASIC_FLOW_MATCH_NODE, basicFlowMatchNode, props);
		putOdenOSMplsConnectionFlowProp(flowName, PROP_SUBKEY_BASIC_FLOW_MATCH_PORT, basicFlowMatchPort, props);
		putOdenOSMplsConnectionFlowProp(flowName, PROP_SUBKEY_BASIC_FLOW_ACTION_NODE, basicFlowActionNode, props);
		putOdenOSMplsConnectionFlowProp(flowName, PROP_SUBKEY_BASIC_FLOW_ACTION_PORT, basicFlowActionPort, props);

		StringBuilder sb = new StringBuilder();
		for (String linkName : linkNames) {
			if (sb.toString().length() != 0) {
				sb.append(",");
			}
			sb.append(linkName);
		}
		String linkNamesValue = sb.toString();
		putOdenOSMplsConnectionFlowProp(flowName, PROP_SUBKEY_LINK_NAMES, linkNamesValue, props);
	}
	
	private static void putOdenOSMplsConnectionFlowProp(String flowName, String subKey, String value, Map<String, String> props) {
		String key = PROP_KEY_ODENOS_TOPOLOGY_FLOWS_PREFIX_ + flowName + "." + subKey;
		props.put(key, value);
	}

	/**
	 */
	private static void definePropsForSdtnc(Map<String, String> props) {
		props.put(PROP_KEY_SDTNC_NBI_BASE_URI, "http://127.0.0.1:8080/nbiService/vrm");
		props.put(PROP_KEY_SDTNC_CONNECTION_TIMEOUT_SEC, "1200");
		props.put(PROP_KEY_SDTNC_READ_TIMEOUT_SEC,       "1200");
		props.put(PROP_KEY_SDTNC_DTO_HITACHI_LOGIN_IP_ADDRESS, "172.21.104.15");
		
		/*
		 * For joined-specific
		 */
		props.put(PROP_KEY_SDTNC_DTO_OTHER_LOGIN_PASSWORD, "Admin");
		props.put(PROP_KEY_SDTNC_DTO_OTHER_SLICE_ID, "2");
		props.put(PROP_KEY_SDTNC_DTO_OTHER_HOP_LINK_ID, "521");
		props.put(PROP_KEY_SDTNC_DTO_OTHER_CUT_LINK_ID, "221");
		props.put(PROP_KEY_SDTNC_DTO_OTHER_NE_ID_A, "1.1.1.0.3.0.1");
		props.put(PROP_KEY_SDTNC_DTO_OTHER_NE_ID_Z, "1.1.2.0.3.0.1");
		props.put(PROP_KEY_SDTNC_DTO_OTHER_HOP_PORT_ID_A, "1.1.1.3.5.1.3.1.0.1.1.2.0.3.0.2.1");
		props.put(PROP_KEY_SDTNC_DTO_OTHER_HOP_PORT_ID_Z, "1.1.2.3.5.1.3.1.0.1.1.2.0.3.0.2.1");
		props.put(PROP_KEY_SDTNC_DTO_OTHER_CUT_PORT_ID_A, "1.1.1.3.5.1.3.1.0.1.1.2.0.3.0.2.2");
		props.put(PROP_KEY_SDTNC_DTO_OTHER_CUT_PORT_ID_Z, "1.1.2.3.5.1.3.1.0.1.1.2.0.3.0.2.2");
		props.put(PROP_KEY_SDTNC_DTO_OTHER_PIR_RATE, "1.2");
		props.put(PROP_KEY_SDTNC_DTO_OTHER_SLA_MODE, "1");

		props.put(PROP_KEY_SDTNC_DTO_OTHER_PUT_VPATH_WORKAROUND_MODE, "false");
		props.put(PROP_KEY_SDTNC_DTO_OTHER_VLAN_PREFIX_ + "421." + PROP_SUBKEY_PATH_END_POINT_A_PORT_ID, 
				"1.1.1.3.5.1.3.1.0.1.1.1.0.3.0.2.3");
		props.put(PROP_KEY_SDTNC_DTO_OTHER_VLAN_PREFIX_ + "421." + PROP_SUBKEY_PATH_END_POINT_Z_PORT_ID, 
				"1.1.3.3.5.1.3.1.0.1.1.1.0.3.0.2.3");
		props.put(PROP_KEY_SDTNC_DTO_OTHER_VLAN_PREFIX_ + "422." + PROP_SUBKEY_PATH_END_POINT_A_PORT_ID, 
				"1.1.1.3.5.1.3.1.0.1.1.1.0.3.0.2.4");
		props.put(PROP_KEY_SDTNC_DTO_OTHER_VLAN_PREFIX_ + "422." + PROP_SUBKEY_PATH_END_POINT_Z_PORT_ID, 
				"1.1.3.3.5.1.3.1.0.1.1.1.0.3.0.2.4");
		
		/*
		 * For debugging SDTNC
		 */
		props.put(PROP_KEY_SDTNC_DUMMY_INVOKER_SET_FLAG, "false");
		
		/*
		 * For 100 flow specific.
		 */
		props.put(PROP_KEY_SDTNC_DTO_OTHER_VLAN_EXTRA_MAPPING_FILE_PATH, "/etc/mlo/sdtnc.extraVlanMapping.dat");
	}

	/**
	 * @param props
	 */
	static void definePropsForNetworkTopology(Map<String, String> props) {
		props.put(PROP_KEY_LD_DEBUG_REMOTE_DISABLE, "false");
		props.put(PROP_KEY_LD_DEBUG_REMOTE_DUMMY_ENABLE, "false");
		props.put(PROP_KEY_LD_CONNECTION_TIMEOUT_MSEC, "30000");
		props.put(PROP_KEY_LD_READ_TIMEOUT_MSEC,       "1200000");
		props.put(PROP_KEY_LD_LD_TOPO_URI,     "http://127.0.0.1:8080/nbiService/etc/ld/topo");
		props.put(PROP_KEY_LD_RY_SWITCHES_URI, "http://127.0.0.1:8888/v1.0/topology/switches");
		props.put(PROP_KEY_LD_RY_LINKS_URI,    "http://127.0.0.1:8888/v1.0/topology/links");
	}
}
