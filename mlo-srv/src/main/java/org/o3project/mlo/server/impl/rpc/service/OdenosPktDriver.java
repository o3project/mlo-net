/**
 * PktTransDriver.java
 * (C) 2015, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.logic.OtherException;
import org.o3project.mlo.server.rpc.entity.PTFlowEntity;
import org.o3project.mlo.server.rpc.entity.PTLinkEntity;
import org.o3project.mlo.server.rpc.service.OdenOSConfig;
import org.o3project.mlo.server.rpc.service.OdenOSConstants;
import org.o3project.mlo.server.rpc.service.OdenOSDriver;
import org.o3project.mlo.server.rpc.service.OdenOSListener;
import org.o3project.odenos.core.component.Driver;
import org.o3project.odenos.core.component.NetworkInterface;
import org.o3project.odenos.core.component.network.flow.Flow;
import org.o3project.odenos.core.component.network.flow.FlowChanged;
import org.o3project.odenos.core.component.network.flow.FlowObject.FlowStatus;
import org.o3project.odenos.core.component.network.flow.basic.BasicFlow;
import org.o3project.odenos.core.component.network.flow.basic.BasicFlowMatch;
import org.o3project.odenos.core.component.network.flow.basic.FlowAction;
import org.o3project.odenos.core.component.network.flow.basic.FlowActionOutput;
import org.o3project.odenos.core.component.network.topology.Link;
import org.o3project.odenos.core.component.network.topology.LinkChanged;
import org.o3project.odenos.core.component.network.topology.Node;
import org.o3project.odenos.core.component.network.topology.NodeChanged;
import org.o3project.odenos.core.component.network.topology.Port;
import org.o3project.odenos.core.component.network.topology.PortChanged;
import org.o3project.odenos.core.manager.system.ComponentConnection;
import org.o3project.odenos.core.manager.system.ComponentConnectionLogicAndNetwork;
import org.o3project.odenos.core.manager.system.event.ComponentConnectionChanged;
import org.o3project.odenos.remoteobject.message.Event;
import org.o3project.odenos.remoteobject.message.Response;
import org.o3project.odenos.remoteobject.messagingclient.MessageDispatcher;
import org.seasar.framework.container.SingletonS2Container;

/**
 * The class is the implementation class of {@link OdenOSDriver} interface.
 */
public class OdenosPktDriver extends Driver implements OdenOSDriver {
	private static final Log LOG = LogFactory.getLog(OdenosPktDriver.class);
	
	private static final int HTTP_SC_OK = 200;
	private static final int HTTP_SC_CREATED = 201;
	private static final int HTTP_SC_CONFLICT = 409;

	private final Object oMutex = new Object();
	
	private OdenOSConfig odenOSConfig = null;
	
	private OdenOSListener listener = null;
	
	private String connectedNetworkId = null;

	private final Map<String, NetworkInterface> extraNetworkInterfaceMap = new LinkedHashMap<>();
	
	/**
	 * A constructor.
	 * @param objectId objectId.
	 * @param dispatcher Message dispatcher.
	 * @throws Exception Failure.
	 */
	public OdenosPktDriver(String objectId, MessageDispatcher dispatcher) throws Exception {
		super(objectId, dispatcher);
		resetEventSubscription();
		LOG.info("[Odenos] A constructor is called. : objectId = " + objectId);
	}

	/* (non-Javadoc)
	 * @see org.o3project.odenos.core.component.Component#getDescription()
	 */
	@Override
	protected String getDescription() {
		return "Packet transport driver";
	}
	
	void initL2Topology() throws Exception {
		Node nodeA = new Node(OdenOSConstants.NODE_PT_A);
		Node nodeB = new Node(OdenOSConstants.NODE_PT_B);
		Node nodeC = new Node(OdenOSConstants.NODE_PT_C);
		
		Port portAEth1 = new Port(OdenOSConstants.PORT_PT_A_ETHER_1, nodeA.getId());
		Port portAEth2 = new Port(OdenOSConstants.PORT_PT_A_ETHER_2, nodeA.getId());
		Port portALsp3 = new Port(OdenOSConstants.PORT_PT_A_LSP_3, nodeA.getId());
		Port portBEth1 = new Port(OdenOSConstants.PORT_PT_B_ETHER_1, nodeB.getId());
		Port portBEth2 = new Port(OdenOSConstants.PORT_PT_B_ETHER_2, nodeB.getId());
		Port portCEth1 = new Port(OdenOSConstants.PORT_PT_C_ETHER_1, nodeC.getId());
		Port portCEth2 = new Port(OdenOSConstants.PORT_PT_C_ETHER_2, nodeC.getId());
		Port portCLsp3 = new Port(OdenOSConstants.PORT_PT_C_LSP_3, nodeC.getId());
		
		List<Node> nodes = Arrays.asList(nodeA, nodeB, nodeC);
		List<Port> ports = Arrays.asList(portAEth1, portAEth2, portALsp3,
				portBEth1, portBEth2, portCEth1, portCEth2, portCLsp3);
		List<Link> links = Arrays.asList();
		putTopology(nodes, ports, links);
	}
	
	void putTopology(Collection<Node> nodes, Collection<Port> ports, Collection<Link> links) throws Exception {
		String nwcId = connectedNetworkId;
		NetworkInterface networkInterface = getNetworkInterface(nwcId);
		Response res = null;
		
		for (Node node : nodes) {
			res = networkInterface.putNode(node);
			LOG.info("putNode response = " + res);
			checkResponse(res, node.getId());
		}
		for (Port port : ports) {
			res = networkInterface.putPort(port);
			LOG.info("putPort response = " + res);
			checkResponse(res, port.getId());
		}
		for (Link link : links) {
			res = networkInterface.putLink(link);
			LOG.info("putLink response = " + res);
			checkResponse(res, link.getId());
		}
	}
	
	void checkResponse(Response res, String id) throws OtherException {
		String msg = null;
		switch(res.statusCode) {
		case HTTP_SC_CREATED:
		case HTTP_SC_OK:
			break;
		case HTTP_SC_CONFLICT:
			msg = String.format("OdenOS Failed to put \"%s\" because of confliction.", id);
			LOG.error(msg);
			throw new OtherException(msg);
		default:
			msg = String.format("OdenOS Failed to put \"%s\". Unexpected status code (%d).", id, res.statusCode);
			LOG.error(msg);
			throw new OtherException(msg);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.odenos.core.component.Logic#onConnectionChangedAddedPre(org.o3project.odenos.core.manager.system.event.ComponentConnectionChanged)
	 */
	@Override
	protected boolean onConnectionChangedAddedPre(ComponentConnectionChanged message) {
		LOG.info("[Odenos] BEGIN onConnectionChangedAddedPre : " + message);

		initOdenOSConfig();
		
		boolean canChange = check(message);
		if (canChange) {
			String l2NetworkId = odenOSConfig.getNetworkComponentIdL2();
			String networkId = message.curr().getProperty(ComponentConnectionLogicAndNetwork.NETWORK_ID);
			canChange = l2NetworkId.equals(networkId);
		}
		if (canChange) {
			if (connectedNetworkId == null) {
				canChange = true;
			} else {
				ComponentConnection componentConn = message.curr();
				componentConn.setConnectionState(ComponentConnection.State.ERROR);
				systemMngInterface().putConnection(componentConn);
				canChange = false;
			}
		}
		LOG.info("[Odenos] END onConnectionChangedAddedPre : " + canChange);
		return canChange;
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.odenos.core.component.Logic#onConnectionChangedAdded(org.o3project.odenos.core.manager.system.event.ComponentConnectionChanged)
	 */
	@Override
	protected void onConnectionChangedAdded(ComponentConnectionChanged message) {
		LOG.info("[Odenos] onConnectionChangedAdded : " + message);
		final ComponentConnection componentConn = message.curr();
		
		connectedNetworkId = componentConn.getProperty(ComponentConnectionLogicAndNetwork.NETWORK_ID);
		LOG.info("connectedNetworkId is set : " + connectedNetworkId);
		
		try {
			subscribeNetworkComponent(connectedNetworkId);
			initListener();
		} catch (Exception e) {
			LOG.error("Failed to subscribe network component.", e);
		}
		
		try {
			initL2Topology();
		} catch (Exception e) {
			LOG.error("Failed to initialize L2 topologys.", e);
		}
		
		componentConn.setConnectionState(ComponentConnection.State.RUNNING);
		systemMngInterface().putConnection(componentConn);
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.odenos.core.component.Logic#onEvent(org.o3project.odenos.remoteobject.message.Event)
	 */
	@Override
	protected void onEvent(Event event) {
		LOG.info("[OdenOS] called onEvent. eventType = " + event.eventType);
		super.onEvent(event);
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.odenos.core.component.Logic#onNodeAdded(java.lang.String, org.o3project.odenos.core.component.network.topology.Node)
	 */
	@Override
	protected void onNodeAdded(String networkId, Node node) {
		LOG.info(String.format("[OdenOS] node added. (networkId, id) = (%s, %s)", networkId, node.getId()));
		super.onNodeAdded(networkId, node);
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.odenos.core.component.Logic#onNodeChanged(java.lang.String, org.o3project.odenos.core.component.network.topology.NodeChanged)
	 */
	@Override
	protected void onNodeChanged(String networkId, NodeChanged msg) throws Exception {
		LOG.info(String.format("[OdenOS] node changed. (networkId, id) = (%s, %s)", networkId, msg.curr.getId()));
		super.onNodeChanged(networkId, msg);
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.odenos.core.component.Logic#onPortAdded(java.lang.String, org.o3project.odenos.core.component.network.topology.Port)
	 */
	@Override
	protected void onPortAdded(String networkId, Port port) {
		LOG.info(String.format("[OdenOS] port added. (networkId, id) = (%s, %s)", networkId, port.getId()));
		super.onPortAdded(networkId, port);
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.odenos.core.component.Logic#onPortChanged(java.lang.String, org.o3project.odenos.core.component.network.topology.PortChanged)
	 */
	@Override
	protected void onPortChanged(String networkId, PortChanged msg) throws Exception {
		LOG.info(String.format("[OdenOS] port changed. (networkId, id) = (%s, %s)", networkId, msg.curr.getId()));
		super.onPortChanged(networkId, msg);
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.odenos.core.component.Logic#onLinkAdded(java.lang.String, org.o3project.odenos.core.component.network.topology.Link)
	 */
	@Override
	protected void onLinkAdded(String networkId, Link link) {
		LOG.info(String.format("[OdenOS] link added. (networkId, id) = (%s, %s)", networkId, link.getId()));
		super.onLinkAdded(networkId, link);
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.odenos.core.component.Logic#onLinkChanged(java.lang.String, org.o3project.odenos.core.component.network.topology.LinkChanged)
	 */
	@Override
	protected void onLinkChanged(String networkId, LinkChanged msg) throws Exception {
		LOG.info(String.format("[OdenOS] link changed. (networkId, id) = (%s, %s)", networkId, msg.curr.getId()));
		super.onLinkChanged(networkId, msg);
		
		Link link = msg.curr;
		String estStatus = link.getAttribute(OdenOSConstants.LINK_ATTRIBUTE_ESTABLISHMENT_STATUS);
		if (LOG.isInfoEnabled()) {
			LOG.info(String.format(
					"[OdenOS] Received LinkChanged:id(%s),establishment_status(%s),"
					+ "srcHost(%s),srcPort(%s),dstHost(%s),dstPort(%s)",
					link.getId(), estStatus,
					link.getSrcNode(), link.getSrcPort(), link.getDstNode(), link.getDstPort()));
		}

		if (odenOSConfig.isAvailableReqLinkEstablishedCompletion()) {
			if ("established".equals(estStatus)) {
				listener.notifyLinkChanged(link.getId());
			}			
		} else {
			listener.notifyLinkChanged(link.getId());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.odenos.core.component.Logic#onFlowAdded(java.lang.String, org.o3project.odenos.core.component.network.flow.Flow)
	 */
	@Override
	protected void onFlowAdded(String networkId, Flow flow) {
		LOG.info(String.format("[OdenOS] flow added. (networkId, flowId) = (%s, %s)", networkId, flow.getFlowId()));
		super.onFlowAdded(networkId, flow);
		listener.notifyFlowChanged(flow.getFlowId());
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.odenos.core.component.Logic#onFlowChanged(java.lang.String, org.o3project.odenos.core.component.network.flow.FlowChanged)
	 */
	@Override
	protected void onFlowChanged(String networkId, FlowChanged msg) throws Exception {
		LOG.info(String.format("[OdenOS] flow changed. (networkId, action) = (%s, %s)", networkId, msg.action));
		super.onFlowChanged(networkId, msg);
		
		switch (msg.action) {
		case "add":
		case "update":
			listener.notifyFlowChanged(msg.curr.getFlowId());
			break;
		case "delete":
			listener.notifyFlowDeleted(msg.prev.getFlowId());
			break;
		default:
			LOG.info("Unsupported action. action = " + msg.action);
			break;
		}
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSDriver#getLink(java.lang.String)
	 */
	@Override
	public PTLinkEntity getLink(String linkId) throws Exception {
		LOG.info(String.format("[OdenOS] getLink(%s) start.", linkId));
		PTLinkEntity linkEntity = null;
		String nwcId = connectedNetworkId;
		NetworkInterface networkInterface = getNetworkInterface(nwcId);
		Link link = networkInterface.getLink(linkId);
		if (link != null) {
			linkEntity = convert(link);
		}
		LOG.info(String.format("[OdenOS] getLink(%s) end.: %s", linkId, "" + linkEntity));
		return linkEntity;
	}

	/**
	 * @param link
	 * @return
	 */
	private PTLinkEntity convert(Link link) {
		String linkId = link.getId();
		String srcNode = link.getSrcNode();
		String srcPort = link.getSrcPort();
		String dstNode = link.getDstNode();
		String dstPort = link.getDstPort();
		String operStatus = link.getAttribute(OdenOSConstants.LINK_ATTRIBUTE_OPER_STATUS);
		String reqLatency = link.getAttribute(OdenOSConstants.LINK_ATTRIBUTE_REQ_LATENCY);
		String reqBandwidth = link.getAttribute(OdenOSConstants.LINK_ATTRIBUTE_REQ_BANDWIDTH);
		String establishmentStatus = link.getAttribute(OdenOSConstants.LINK_ATTRIBUTE_ESTABLISHMENT_STATUS);
		PTLinkEntity entity = new PTLinkEntity(linkId, srcNode, srcPort, dstNode, dstPort, operStatus, reqLatency, reqBandwidth, establishmentStatus);
		return entity;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSDriver#requestLink(org.o3project.mlo.server.rpc.entity.PTLinkEntity)
	 */
	@Override
	public void requestLink(PTLinkEntity entity) throws Exception {
		LOG.info(String.format("[OdenOS] requestLink(%s) start.", entity.linkId));
		String nwcId = getL012NetworkComponentId();
		NetworkInterface networkInterface = getNetworkInterface(nwcId);
		Link link = convert(entity);
		Response res = networkInterface.putLink(link);
		LOG.info("putLink response = " + res);
		checkResponse(res, link.getId());
		LOG.info(String.format("[OdenOS] requestLink(%s) end.: %s", entity.linkId, "" + entity));
	}

	/**
	 * @param entity
	 * @return
	 */
	private Link convert(PTLinkEntity entity) {
		Link link = new Link(entity.linkId, entity.srcNode, entity.srcPort, entity.dstNode, entity.dstPort);
		link.putAttribute(OdenOSConstants.LINK_ATTRIBUTE_OPER_STATUS, entity.operStatus);
		link.putAttribute(OdenOSConstants.LINK_ATTRIBUTE_REQ_LATENCY, entity.reqLatency);
		link.putAttribute(OdenOSConstants.LINK_ATTRIBUTE_REQ_BANDWIDTH, entity.reqBandwidth);
		link.putAttribute(OdenOSConstants.LINK_ATTRIBUTE_ESTABLISHMENT_STATUS, entity.establishmentStatus);
		return link;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSDriver#deleteFlow(java.lang.String)
	 */
	@Override
	public void deleteFlow(String odenosFlowId) throws Exception {
		LOG.info(String.format("[OdenOS] deleteFlow(%s) start.", odenosFlowId));
		String nwcId = getL012NetworkComponentId();
		NetworkInterface networkInterface = getNetworkInterface(nwcId);
		Response res = networkInterface.delFlow(odenosFlowId);
		LOG.info("deleteFlow response = " + res);
		checkResponse(res, odenosFlowId);
		LOG.info(String.format("[OdenOS] deleteFlow(%s) end.:", odenosFlowId));
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSDriver#putFlow(org.o3project.mlo.server.rpc.entity.PTFlowEntity)
	 */
	@Override
	public void putFlow(PTFlowEntity entity) throws Exception {
		LOG.info(String.format("[OdenOS] putFlow(%s) start.", entity.flowId));
		String nwcId = getL012NetworkComponentId();
		NetworkInterface networkInterface = getNetworkInterface(nwcId);
		Flow flow = convert(entity);
		Response res = networkInterface.putFlow(flow);
		LOG.info("deleteFlow response = " + res);
		checkResponse(res, entity.flowId);
		LOG.info(String.format("[OdenOS] putFlow(%s) end.:", entity.flowId));
	}
	
	/**
	 * @param entity
	 * @return
	 */
	private Flow convert(PTFlowEntity entity) {
		String version = Flow.INITIAL_VERSION;
		String flowId = entity.flowId;
		String owner = "PktDriver";
		boolean enabled = true;
		String priority = Flow.DEFAULT_PRIORITY;
		String status = FlowStatus.ESTABLISHED.toString();
		List<BasicFlowMatch> matches = Arrays.asList(
				new BasicFlowMatch(entity.basicFlowMatchInNode, entity.basicFlowMatchInPort));
		List<String> path = entity.flowPath;
		Map<String, List<FlowAction>> edgeAction = new LinkedHashMap<>();
		edgeAction.put(entity.basicFlowActionOutputNode, Arrays.asList(((FlowAction) new FlowActionOutput(entity.basicFlowActionOutputPort))));
		Map<String, String> attributes = new LinkedHashMap<>(entity.attributes);
		Flow flow = new BasicFlow(version, flowId, owner, enabled, priority, status, matches, path, edgeAction, attributes);
		return flow;
	}

	private boolean check(ComponentConnectionChanged message) {
		String objectType = message.curr().getObjectType();
		if (!ComponentConnectionLogicAndNetwork.TYPE.equals(objectType)) {
			return false;
		}
		
		String logicId = message.curr().getProperty(ComponentConnectionLogicAndNetwork.LOGIC_ID);
		if (logicId == null || !logicId.equals(getObjectId())) {
			return false;
		}
		
		return true;
 	}
	
	private void initOdenOSConfig() {
		if (odenOSConfig == null) {
			odenOSConfig = SingletonS2Container.getComponent(OdenOSConfig.class);
			if (odenOSConfig == null) {
				String message = "Failed to get OdenOSConfig component from DI container.";
				LOG.error(message);
				throw new IllegalStateException(message);
			}
		}
	}
	
	private void initListener() {
		if (listener == null) {
			listener = SingletonS2Container.getComponent(OdenOSListener.class);
			listener.setDriver(this);
		}
	}
	
	private void subscribeNetworkComponent(String targetNetwrowkId) throws Exception {
		resetEventSubscription();
		
		String nwcId = targetNetwrowkId;
		addEntryEventSubscription(NODE_CHANGED, nwcId);
		addEntryEventSubscription(PORT_CHANGED, nwcId);
		addEntryEventSubscription(LINK_CHANGED, nwcId);
		addEntryEventSubscription(FLOW_CHANGED, nwcId);
		updateEntryEventSubscription(FLOW_CHANGED, nwcId, null);
		
		applyEventSubscription();
	}

	/**
	 * @param nwcId
	 * @return
	 */
	private NetworkInterface getNetworkInterface(String nwcId) {
		HashMap<String, NetworkInterface> nwifMap = networkInterfaces();
		LOG.debug("networkInterfaceMap : " + nwifMap);
		NetworkInterface networkInterface = null;
		if (nwifMap.containsKey(nwcId)) {
			networkInterface = nwifMap.get(nwcId);
		} else {
			synchronized (oMutex) {
				if (extraNetworkInterfaceMap.containsKey(nwcId)) {
					networkInterface = extraNetworkInterfaceMap.get(nwcId);
				} else {
					MessageDispatcher dispatcher = getMessageDispatcher();
					networkInterface = new NetworkInterface(dispatcher, nwcId);
					extraNetworkInterfaceMap.put(nwcId, networkInterface);
				}
			}
		}
		return networkInterface;
	}

	/**
	 * @return
	 */
	private String getL012NetworkComponentId() {
		String l012NetworkComponentId = odenOSConfig.getNetworkComponentIdL012();
		return l012NetworkComponentId;
	}
	
	@SuppressWarnings("unused")
	private void unsubscribeNetworkComponent() throws Exception {
		resetEventSubscription();
		
		applyEventSubscription();
	}
}
