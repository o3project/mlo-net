/**
 * OdenOSTask.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.rpc.service.OdenOSConfig;
import org.o3project.odenos.core.component.SystemManagerInterface;
import org.o3project.odenos.core.component.network.Network;
import org.o3project.odenos.core.manager.ComponentManager2;
import org.o3project.odenos.core.manager.system.ComponentConnection;
import org.o3project.odenos.core.manager.system.ComponentConnectionLogicAndNetwork;
import org.o3project.odenos.core.manager.system.SystemManagerIF;
import org.o3project.odenos.remoteobject.ObjectProperty;
import org.o3project.odenos.remoteobject.RemoteObject;
import org.o3project.odenos.remoteobject.RemoteObjectManager;
import org.o3project.odenos.remoteobject.message.Response;
import org.o3project.odenos.remoteobject.messagingclient.MessageDispatcher;

/**
 * This class is the odenos running task.
 */
public class OdenosTask implements Runnable {
	
	private static final Log LOG = LogFactory.getLog(OdenosTask.class);
	
	private OdenOSConfig odenOSConfig;
	
	/**
	 * Setter method (for DI setter injection).
	 * @param odenOSConfig the odenOSConfig to set
	 */
	public void setOdenOSConfig(OdenOSConfig odenOSConfig) {
		this.odenOSConfig = odenOSConfig;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOG.info("START running odenos.");
		{
			ClassLoader clsLoader = ClassLoader.getSystemClassLoader();
			LOG.debug(clsLoader);
		}
		try {
			doRunWithDispatcher();
		} catch (Throwable th) {
			LOG.warn("Failed in running odenos.", th);
		} finally {
			LOG.info("END running odenos.");
		}
	}
	
	private void doRunWithDispatcher() throws Exception {
		MessageDispatcher dispatcher = null;
		String systemManagerId = odenOSConfig.getRemoteSystemManagerId();
		String msgSrvHost = odenOSConfig.getRemoteSystemManagerHost();
		Integer msgSrvPort = odenOSConfig.getRemoteSystemManagerPort();
		
		try {
			dispatcher = new MessageDispatcher(systemManagerId, msgSrvHost, msgSrvPort);
			dispatcher.start();
			
			startRemoteObjectManager(dispatcher);
			setUpComponents(dispatcher);
			
			dispatcher.join();
		} finally {
			if (dispatcher != null) {
				dispatcher.close();
				dispatcher = null;
			}
		}
	}
	
	private RemoteObjectManager startRemoteObjectManager(MessageDispatcher dispatcher) throws Exception {
		SystemManagerIF systemManagerIf = new SystemManagerIF(dispatcher);
		
		String remoteObjectManagerId = getRemoteObjectManagerId();
		ComponentManager2 remoteObjectManager = new ComponentManager2(remoteObjectManagerId, dispatcher);
		
		Set<Class<? extends RemoteObject>> clsSet = new HashSet<>();
		clsSet.add(OdenosPktDriver.class);
		if (odenOSConfig.isAvailableCreateL2()) {
			clsSet.add(Network.class);
		}
		remoteObjectManager.registerComponents(clsSet);
		
		systemManagerIf.addComponentManager(remoteObjectManager.getProperty());
		
		remoteObjectManager.setState(ObjectProperty.State.RUNNING);
		
		return remoteObjectManager;
	}
	
	private void setUpComponents(MessageDispatcher dispatcher) throws Exception {
		SystemManagerInterface systemManagerInterface = new SystemManagerInterface(dispatcher);
		Response res = null;
		
		String l2NetworkId = odenOSConfig.getNetworkComponentIdL2();
		if (odenOSConfig.isAvailableCreateL2()) {
			String l2NetworkType = Network.class.getSimpleName();
			ObjectProperty networkProp = new ObjectProperty(l2NetworkType, l2NetworkId);
			networkProp.setProperty(ObjectProperty.PropertyNames.CM_ID, getRemoteObjectManagerId());
			res = systemManagerInterface.putComponent(networkProp);
			LOG.debug(res);
		}
		
		String driverId = odenOSConfig.getPTDriverId();
		String driverType = OdenosPktDriver.class.getSimpleName();
		ObjectProperty driverProp = new ObjectProperty(driverType, driverId);
		driverProp.setProperty(ObjectProperty.PropertyNames.CM_ID, getRemoteObjectManagerId());
		res = systemManagerInterface.putComponent(driverProp);
		LOG.debug(res);
		
		String connId = odenOSConfig.getConnectionId();
		String connType = odenOSConfig.getConnectionType();
		String connState = ComponentConnection.State.INITIALIZING;
		String logicId = driverId;
		String networkId = l2NetworkId;
		ComponentConnection conn = new ComponentConnectionLogicAndNetwork(connId, connType, connState, logicId, networkId);
		res = systemManagerInterface.putConnection(conn);
		LOG.debug(res);
	}

	/**
	 * @return
	 */
	private String getRemoteObjectManagerId() {
		return odenOSConfig.getComponentManagerId();
	}
}

