/**
 * ConfigConstants.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

/**
 * This interface is the constant definition interface used by {@link ConfigProvider}.
 *
 */
public interface ConfigConstants {
	
	/**
	 * Prefix of property key.
	 */
	String PROP_KEY_PREFIX_ = "mlo.server.config.";
	
	/*
	 * Common
	 */
	
	/**
	 * This property key designates a property value, which is whether MLO behaves in debugging mode or not.
	 * The type of the property value is boolean.
	 * If true, MLO behaves in debugging mode.
	 */
	String PROP_KEY_DEBUG_RESTIF_ENABLE = PROP_KEY_PREFIX_ + "debug.restif.enable";
	
	/**
	 * This property key designates a property value, which is component name in debugging mode.
	 * The type of the property value is string.
	 */
	String PROP_KEY_DEBUG_CLIENTS = PROP_KEY_PREFIX_ + "debug.clients";
	
	/**
	 * This property key designates a property value, which is the offset value of auto-generated VLAN ID.
	 * The type of the property value is positive integer.
	 */
	String PROP_KEY_EXTRA_VLAN_ID_OFFSET = PROP_KEY_PREFIX_ + "extraVlanIdOffset";
	
	/**
	 * This property key designates a property value, which is the feature version.
	 * The type of the property value is string.
	 */
	String PROP_KEY_DEBUG_FEATURE_VERSION = PROP_KEY_PREFIX_ + "debug.featureVersion";
	
	/*
	 * For Odenos
	 */

	/**
	 * This property key designates a property value, which is the ID of the Odenos remote system manager.
	 * The type of the property value is string.
	 */
	String PROP_KEY_ODENOS_REMOTE_SYSTEM_MANAGER_ID   = PROP_KEY_PREFIX_ + "odenos.remoteSystemManager.id";
	
	/**
	 * This property key designates a property value, which is the host name of the Odenos remote system manager.
	 * The type of the property value is string.
	 */
	String PROP_KEY_ODENOS_REMOTE_SYSTEM_MANAGER_HOST = PROP_KEY_PREFIX_ + "odenos.remoteSystemManager.host";
	
	/**
	 * This property key designates a property value, which is the port number of the Odenos remote system manager.
	 * The type of the property value is positive integer.
	 */
	String PROP_KEY_ODENOS_REMOTE_SYSTEM_MANAGER_PORT = PROP_KEY_PREFIX_ + "odenos.remoteSystemManager.port";
	
	/**
	 * This property key designates a property value, which is the ID of the Odenos component manager.
	 * The type of the property value is string.
	 */
	String PROP_KEY_ODENOS_COMPONENT_MANAGER_ID   = PROP_KEY_PREFIX_ + "odenos.componentManager.id";
	
	/**
	 * This property key designates a property value, which is the host name of the Odenos component manager.
	 * The type of the property value is string.
	 */
	String PROP_KEY_ODENOS_COMPONENT_MANAGER_HOST = PROP_KEY_PREFIX_ + "odenos.componentManager.host";
	
	/**
	 * This property key designates a property value, which is the port number of the Odenos component manager.
	 * The type of the property value is positive integer.
	 */
	String PROP_KEY_ODENOS_COMPONENT_MANAGER_PORT = PROP_KEY_PREFIX_ + "odenos.componentManager.port";
	
	/**
	 * This property key designates a property value, which is the base url of the Odenos component manager.
	 * The type of the property value is string.
	 */
	String PROP_KEY_ODENOS_COMPONENT_MANAGER_BASE_URL = PROP_KEY_PREFIX_ + "odenos.componentManager.baseUrl";
	
	/**
	 * This property key designates a property value, which is this host name of the Odenos launcher.
	 * The type of the property value is string.
	 */
	String PROP_KEY_ODENOS_LAUNCHER_HOST = PROP_KEY_PREFIX_ + "odenos.launcher.host";
	
	/**
	 * This property key designates a property value, which is this port number of the Odenos launcher.
	 * The type of the property value is positive integer.
	 */
	String PROP_KEY_ODENOS_LAUNCHER_PORT = PROP_KEY_PREFIX_ + "odenos.launcher.port";
	
	/**
	 * This property key designates a property value, which is the Odenos network component name of L2.
	 * The type of the property value is string.
	 */
	String PROP_KEY_ODENOS_NETWORK_COMPONENT_ID_L2   = PROP_KEY_PREFIX_ + "odenos.networkComponent.id.l2";
	
	/**
	 * This property key designates a property value, which is the Odenos network component name of L012.
	 * The type of the property value is string.
	 */
	String PROP_KEY_ODENOS_NETWORK_COMPONENT_ID_L012   = PROP_KEY_PREFIX_ + "odenos.networkComponent.id.l012";

	/**
	 * This property key designates a property value, which is the ID of the Odenos packet transport driver.
	 * The type of the property value is string.
	 */
	String PROP_KEY_ODENOS_PT_DRIVER_ID   = PROP_KEY_PREFIX_ + "odenos.ptdriver.id";
	
	/**
	 * This property key designates a property value, which is the Odenos connection ID.
	 * The type of the property value is string.
	 */
	String PROP_KEY_ODENOS_CONNECTION_ID    = PROP_KEY_PREFIX_ + "odenos.conn.id";
	
	/**
	 * This property key designates a property value, which is the Odenos connection type.
	 * The type of the property value is string.
	 */
	String PROP_KEY_ODENOS_CONNECTION_TYPE  = PROP_KEY_PREFIX_ + "odenos.conn.type";
	
	/**
	 * This property key designates a property value, which is the Odenos connection state.
	 * The type of the property value is string.
	 */
	String PROP_KEY_ODENOS_CONNECTION_STATE = PROP_KEY_PREFIX_ + "odenos.conn.state";
	
	/**
	 * This property key designates a property value, which is the Odenos timeout.
	 * The type of the property value is positive integer.
	 * Its unit is second.
	 */
	String PROP_KEY_ODENOS_RESPONSE_TIMEOUTSEC = PROP_KEY_PREFIX_ + "odenos.response.timeoutsec";
	
	/**
	 * This property key designates a property value, which is whether MLOS creates L2 network component or not.
	 * The type of the property value is boolean.
	 * If true, L2 network component is created by MLO.
	 */
	String PROP_KEY_ODENOS_IS_AVAILABLE_CREATE_L2   = PROP_KEY_PREFIX_ + "odenos.isAvailable.createL2";
	
	/**
	 * This property key designates a property value, which is whether MLO waits for "established" state in Odenos PUT_LINK callback or not.
	 * The type of the property value is boolean.
	 * If true, MLOL waits "established" state.
	 */
	String PROP_KEY_ODENOS_IS_AVAILABLE_REQ_LINK_ESTABLISHED_COMPLETION   = PROP_KEY_PREFIX_ + "odenos.isAvailable.reqLink.establishedCompletion";
	
	/*
	 * Keys for OdenOS-MPLS connection.
	 */

	/**
	 * Prefix string of odenos link object configuration.
	 */
	String PROP_KEY_ODENOS_TOPOLOGY_LINKS_PREFIX_ = PROP_KEY_PREFIX_ + "odenos.topology.links.";

	/**
	 * Prefix string of odenos flow object configuration.
	 */
	String PROP_KEY_ODENOS_TOPOLOGY_FLOWS_PREFIX_ = PROP_KEY_PREFIX_ + "odenos.topology.flows.";

	/**
	 * This sub property key designates a property value, which is the ID in Odenos.
	 * The type of the property value is string.
	 */
	String PROP_SUBKEY_ODENOS_ID = "odenosId";
	
	/**
	 * This sub property key designates a property value, which is the source node name in Odenos link.
	 * The type of the property value is string.
	 */
	String PROP_SUBKEY_SRC_NODE = "srcNode";
	
	/**
	 * This sub property key designates a property value, which is the source port name in Odenos link.
	 * The type of the property value is string.
	 */
	String PROP_SUBKEY_SRC_PORT = "srcPort";
	
	/**
	 * This sub property key designates a property value, which is the destination node name in Odenos link.
	 * The type of the property value is string.
	 */
	String PROP_SUBKEY_DST_NODE = "dstNode";
	
	/**
	 * This sub property key designates a property value, which is the destination port name in Odenos link.
	 * The type of the property value is string.
	 */
	String PROP_SUBKEY_DST_PORT = "dstPort";
	
	/**
	 * This sub property key designates a property value, which is the basic flow match node in odenos flow.
	 * The type of the property value is string.
	 */
	String PROP_SUBKEY_BASIC_FLOW_MATCH_NODE = "basicFlowMatch.node";
	
	/**
	 * This sub property key designates a property value, which is the basic flow match port in odenos flow.
	 * The type of the property value is string.
	 */
	String PROP_SUBKEY_BASIC_FLOW_MATCH_PORT = "basicFlowMatch.port";
	
	/**
	 * This sub property key designates a property value, which is the basic flow action node in odenos flow.
	 * The type of the property value is string.
	 */
	String PROP_SUBKEY_BASIC_FLOW_ACTION_NODE = "basicFlowAction.node";
	
	/**
	 * This sub property key designates a property value, which is the basic flow action port in odenos flow.
	 * The type of the property value is string.
	 */
	String PROP_SUBKEY_BASIC_FLOW_ACTION_PORT = "basicFlowAction.port";
	
	/**
	 * This sub property key designates a property value, which is the link name list.
	 * The odenos flow consists of the link list.
	 * The type of the property value is string.
	 */
	String PROP_SUBKEY_LINK_NAMES = "linkNames";

	
	/*
	 * Keys for SDTNC.
	 */
	
	/**
	 * This property key designates a property value, which is the base URI of SDTNC VRM NBI.
	 * The type of the property value is string.
	 */
	String PROP_KEY_SDTNC_NBI_BASE_URI = PROP_KEY_PREFIX_ + "sdtnc.nbi.baseUri";
	
	/**
	 * This property key designates a property value, which is the connection timeout seconds in connecting to SDTNC VRM NBI.
	 * The type of the property value is zero or positive integer.
	 * Its unit is "seconds".
	 */
	String PROP_KEY_SDTNC_CONNECTION_TIMEOUT_SEC = PROP_KEY_PREFIX_ + "sdtnc.connectionTimeoutSec";
	
	/**
	 * This property key designates a property value, which is the read timeout seconds in connecting to SDTNC VRM NBI.
	 * The type of the property value is zero or positive integer.
	 * Its unit is "seconds".
	 */
	String PROP_KEY_SDTNC_READ_TIMEOUT_SEC       = PROP_KEY_PREFIX_ + "sdtnc.readTimeoutSec";
	
	/**
	 * This property key designates a property value, which is the login IP address of SDTNC VRM NBI.
	 * The type of the property value is string.
	 */
	String PROP_KEY_SDTNC_DTO_HITACHI_LOGIN_IP_ADDRESS = PROP_KEY_PREFIX_ + "sdtnc.dto.login.ipAddress";
	
	/**
	 * This property key designates a property value, which is the login password of SDTNC VRM NBI.
	 * The type of the property value is string.
	 */
	String PROP_KEY_SDTNC_DTO_OTHER_LOGIN_PASSWORD = PROP_KEY_PREFIX_ + "sdtnc.dto.other.loginPassword";
	
	/**
	 * This property key designates a property value, which is the slice ID in SDTNC.
	 * The type of the property value is positive integer.
	 */
	String PROP_KEY_SDTNC_DTO_OTHER_SLICE_ID = PROP_KEY_PREFIX_ + "sdtnc.dto.other.sliceId";
	
	/**
	 * This property key designates a property value, which is the link ID of hop-by-hop path in SDTNC.
	 * The type of the property value is positive integer.
	 */
	String PROP_KEY_SDTNC_DTO_OTHER_HOP_LINK_ID = PROP_KEY_PREFIX_ + "sdtnc.dto.other.hopLinkId";
	
	/**
	 * This property key designates a property value, which is the link ID of cut-through path in SDTNC.
	 * The type of the property value is positive integer.
	 */
	String PROP_KEY_SDTNC_DTO_OTHER_CUT_LINK_ID = PROP_KEY_PREFIX_ + "sdtnc.dto.other.cutLinkId";
	
	/**
	 * This property key designates a property value, which is the start-edge PT node ID in SDTNC.
	 * The type of the property value is string.
	 */
	String PROP_KEY_SDTNC_DTO_OTHER_NE_ID_A = PROP_KEY_PREFIX_ + "sdtnc.dto.other.neIdA";
	
	/**
	 * This property key designates a property value, which is the end-edge PT node ID in SDTNC.
	 * The type of the property value is string.
	 */
	String PROP_KEY_SDTNC_DTO_OTHER_NE_ID_Z = PROP_KEY_PREFIX_ + "sdtnc.dto.other.neIdZ";
	
	/**
	 * This property key designates a property value, which is the start-edge PT port ID of hop-by-hop path.
	 * The type of the property value is string.
	 */
	String PROP_KEY_SDTNC_DTO_OTHER_HOP_PORT_ID_A = PROP_KEY_PREFIX_ + "sdtnc.dto.other.hopPortIdA";
	
	/**
	 * This property key designates a property value, which is the end-edge PT port ID of hop-by-hop path.
	 * The type of the property value is string.
	 */
	String PROP_KEY_SDTNC_DTO_OTHER_HOP_PORT_ID_Z = PROP_KEY_PREFIX_ + "sdtnc.dto.other.hopPortIdZ";
	
	/**
	 * This property key designates a property value, which is the start-edge PT port ID of cut-through path.
	 * The type of the property value is string.
	 */
	String PROP_KEY_SDTNC_DTO_OTHER_CUT_PORT_ID_A = PROP_KEY_PREFIX_ + "sdtnc.dto.other.cutPortIdA";
	
	/**
	 * This property key designates a property value, which is the end-edge PT port ID of cut-through path.
	 * The type of the property value is string.
	 */
	String PROP_KEY_SDTNC_DTO_OTHER_CUT_PORT_ID_Z = PROP_KEY_PREFIX_ + "sdtnc.dto.other.cutPortIdZ";
	
	/**
	 * This property key designates a property value, which is the PIR/CIR ratio.
	 * The value is used in putting PW to SDTNC.
	 * The type of the property value is double.
	 * If SLA mode is set to 1, this property is ignored.
	 */
	String PROP_KEY_SDTNC_DTO_OTHER_PIR_RATE = PROP_KEY_PREFIX_ + "sdtnc.dto.other.pirRate";
	
	/**
	 * This property key designates a property value, which is the SLA mode.
	 * The value is used in putting PW to SDTNC.
	 * The type of the property value is positive integer.
	 */
	String PROP_KEY_SDTNC_DTO_OTHER_SLA_MODE = PROP_KEY_PREFIX_ + "sdtnc.dto.other.slaMode";
	
	/**
	 * This property key designates a property value, which is whether workaround mode for SDTNC is available or not.
	 * The type of the property value is boolean.
	 * If true, MLO behaves in workaround mode.
	 */
	String PROP_KEY_SDTNC_DTO_OTHER_PUT_VPATH_WORKAROUND_MODE = PROP_KEY_PREFIX_ + "sdtnc.dto.other.putVPath.workaroundMode";
	
	/**
	 * Prefix string for VLAN property key.
	 */
	String PROP_KEY_SDTNC_DTO_OTHER_VLAN_PREFIX_ = PROP_KEY_PREFIX_ + "sdtnc.dto.other.vlan.";
	
	/**
	 * This property key designates a property value, which is VLAN extra mapping file path.
	 * The type of the property value is string.
	 */
	String PROP_KEY_SDTNC_DTO_OTHER_VLAN_EXTRA_MAPPING_FILE_PATH = PROP_KEY_PREFIX_ + "sdtnc.dto.other.vlan.extraMappingFilePath";
	
	/**
	 * This sub property key designates a property value, which is the start-edge port ID used in SDTNC.
	 * The type of the property value is string.
	 */
	String PROP_SUBKEY_PATH_END_POINT_A_PORT_ID = "pathEndPoint.a.portId";
	
	/**
	 * This sub property key designates a property value, which is the end-edge port ID used in SDTNC.
	 * The type of the property value is string.
	 */
	String PROP_SUBKEY_PATH_END_POINT_Z_PORT_ID = "pathEndPoint.z.portId";
	
	/**
	 * This property key designates a property value, which is whether dummy SDTNC is used or not.
	 * The type of the property value is boolean.
	 * If true, MLO uses dummy SDTNC.
	 */
	String PROP_KEY_SDTNC_DUMMY_INVOKER_SET_FLAG       = PROP_KEY_PREFIX_ + "sdtnc.dummy.invoker.flag";
	
	/*
	 * Keys for Ld
	 */
	
	/**
	 * This property key designates a property value, which is whether MLO uses LD topology data or not.
	 * The type of the property value is boolean.
	 * If true, MLO does not use LD topology data and does use local topology data.
	 */
	String PROP_KEY_LD_DEBUG_REMOTE_DISABLE = PROP_KEY_PREFIX_ + "ld.debug.remote.disable";

	/**
	 * This property key designates a property value, which is whether MLO uses dummy LD topology data or not.
	 * The type of the property value is boolean.
	 * If true, MLO does not load LD topology data but load local dummy LD topology data.
	 */
	String PROP_KEY_LD_DEBUG_REMOTE_DUMMY_ENABLE = PROP_KEY_PREFIX_ + "ld.debug.remoteDummy.enable";
	
	/**
	 * This property key designates a property value, which is the connection timeout in connecting to LD topology.
	 * The type of the property value is zero or positive integer.
	 * The unit is milliseconds
	 */
	String PROP_KEY_LD_CONNECTION_TIMEOUT_MSEC = PROP_KEY_PREFIX_ + "ld.connectionTimeoutMsec";
	
	/**
	 * This property key designates a property value, which is the read timeout in connecting to LD topology.
	 * The type of the property value is zero or positive integer.
	 * The unit is milliseconds
	 */
	String PROP_KEY_LD_READ_TIMEOUT_MSEC       = PROP_KEY_PREFIX_ + "ld.readTimeoutMsec";

	/**
	 * This property key designates a property value, which is the LD topology URI.
	 * The type of the property value is string.
	 */
	String PROP_KEY_LD_LD_TOPO_URI     = PROP_KEY_PREFIX_ + "ld.ldTopo.uri";
	
	/**
	 * This property key designates a property value, which is the Ryu switches topology URI.
	 * The type of the property value is string.
	 */
	String PROP_KEY_LD_RY_SWITCHES_URI = PROP_KEY_PREFIX_ + "ld.rySwitches.uri";
	
	/**
	 * This property key designates a property value, which is the Ryu links topology URI.
	 * The type of the property value is string.
	 */
	String PROP_KEY_LD_RY_LINKS_URI    = PROP_KEY_PREFIX_ + "ld.ryLinks.uri";
	
	/**
	 * Property key of topology view URI.
	 * The type of the property value is string.
	 */
	String PROP_KEY_SERVER_TOPOLOGY_VIEW_URI = PROP_KEY_PREFIX_ + "server.topologyViewUri";
	
	String PROP_KEY_REMOTE_NODE_ACCESS_HOST = PROP_KEY_PREFIX_ + "remoteNodeAccess.host";
	
	String PROP_KEY_REMOTE_NODE_ACCESS_SSH_PORT = PROP_KEY_PREFIX_ + "remoteNodeAccess.sshPort";
	
	String PROP_KEY_REMOTE_NODE_ACCESS_USERID = PROP_KEY_PREFIX_ + "remoteNodeAccess.userid";
	
	String PROP_KEY_REMOTE_NODE_ACCESS_PASSWORD = PROP_KEY_PREFIX_ + "remoteNodeAccess.password";
	
	String PROP_KEY_REMOTE_NODE_ACCESS_SSH_SESSION_TIMEOUT_MSEC = PROP_KEY_PREFIX_ + "remoteNodeAccess.sshSessionTimeoutMsec";
	
	String PROP_KEY_REMOTE_NODE_ACCESS_SSH_CHANNEL_TIMEOUT_MSEC = PROP_KEY_PREFIX_ + "remoteNodeAccess.sshChannelTimeoutMsec";
	
	String PROP_KEY_REMOTE_NODE_ACCESS_LD_WORKSPACE_DIRPATH = PROP_KEY_PREFIX_ + "remoteNodeAccess.ldWorkspaceDirpath";
}
