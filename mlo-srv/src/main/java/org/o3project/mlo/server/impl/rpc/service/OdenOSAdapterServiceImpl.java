/**
 * OdenOSAdapterServiceImpl.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.h2.util.StringUtils;
import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.TimeOutException;
import org.o3project.mlo.server.rpc.entity.PTFlowEntity;
import org.o3project.mlo.server.rpc.entity.PTLinkEntity;
import org.o3project.mlo.server.rpc.service.OdenOSDriver;
import org.o3project.mlo.server.rpc.service.OdenOSListener;
import org.o3project.mlo.server.rpc.service.OdenOSAdapterService;
import org.o3project.mlo.server.rpc.service.OdenOSConfig;
import org.seasar.framework.container.annotation.tiger.Aspect;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;


/**
 * This class is the implementation class of {@link OdenOSAdapterService} interface.
 */
@Aspect("traceInterceptor")
public class OdenOSAdapterServiceImpl implements OdenOSAdapterService, OdenOSListener {

	private static final Log LOG = LogFactory.getLog(OdenOSAdapterServiceImpl.class);
	
	@Binding
	private OdenOSConfig odenOSConfig;
	
	private OdenosTask odenosTask;

	private OdenOSDriver driver = null;
	
	private ExecutorService odenosTaskExecutor = null;
	
	private final Map<String, BlockingQueue<String>> linkQueueMap 
		= new HashMap<String, BlockingQueue<String>>();

	private final Map<String, BlockingQueue<String>> deleteFlowQueueMap
		= new HashMap<String, BlockingQueue<String>>();

	private final Map<String, BlockingQueue<String>> flowChangedQueueMap 
		= new HashMap<String, BlockingQueue<String>>();
	
	/**
	 * Setter method (for DI setter injection).
	 * @param odenOSConfig The instance. 
	 */
	public void setOdenOSConfig(OdenOSConfig odenOSConfig) {
		this.odenOSConfig = odenOSConfig;
	}
	
	/**
	 * Setter method (for DI setter injection).
	 * @param odenosTask the odenosTask to set
	 */
	public void setOdenosTask(OdenosTask odenosTask) {
		this.odenosTask = odenosTask;
	}

	/**
	 * Creates link queue (for UT).
	 * @param linkId link_id
	 */
	public void setLinkQueue(String linkId) {
		BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1);
		synchronized (linkQueueMap) {
			linkQueueMap.put(linkId, queue);
		}
	}
	
	/**
	 * Creates deletion flow queue (for UT).
	 * @param flowId flow_id
	 */
	public void setDeleteFlowQueue(String flowId) {
		BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1);
		synchronized (deleteFlowQueueMap) {
			deleteFlowQueueMap.put(flowId, queue);
		}
	}
	
	/* (非 Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.LinkLayerizerService#init()
	 */
	@Override
	@InitMethod
	public void init() {

		if (LOG.isDebugEnabled()) {
			LOG.debug("OdenOSAdapterServiceImpl#init start");
		}
		
		if (odenosTaskExecutor == null) {
			odenosTaskExecutor = Executors.newSingleThreadExecutor();
			odenosTaskExecutor.submit(odenosTask);
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("OdenOSAdapterServiceImpl#init term");
		}
	}
	
	/* (非 Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.LinkLayerizerService#dispose()
	 */
	@Override
	@DestroyMethod
	public void dispose() {
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("OdenOSAdapterServiceImpl#dispose start");
		}

		if (odenosTaskExecutor != null) {
			odenosTaskExecutor.shutdownNow();
			try {
				odenosTaskExecutor.awaitTermination(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				LOG.warn("Odenos task executor shutdown interrupted.", e);
			}
		}
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("OdenOSAdapterServiceImpl#dispose term");
		}
		
		
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSAdapterService#getLink(java.lang.String)
	 */
	@Override
	public PTLinkEntity getLink(String odenOSLinkId) throws MloException {

		// Checks whether odenos is running or not.
		if (driver == null) {
			throw new InternalException("OdenOS has not started.");
		}

		if (StringUtils.isNullOrEmpty(odenOSLinkId)) {
			throw new InternalException("OdenOS getLink Invalid parameter");
		}
		
		PTLinkEntity response = null;
		try {
			response = driver.getLink(odenOSLinkId);
		} catch (MloException e) {
			LOG.debug(e);
			throw e;
		} catch (Exception e) {
			LOG.debug(e);
			throw new InternalException("OdenOS Failed to getLink : " + odenOSLinkId, e);
		}
		return response;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSAdapterService#requestLink(org.o3project.mlo.server.rpc.entity.PTLinkEntity)
	 */
	@Override
	public void requestLink(PTLinkEntity link) throws MloException {
		
		// Checks whether odenos is running or not.
		if (driver == null) {
			throw new InternalException("OdenOS has not started.");
		}
		
		if (link == null) {
			throw new InternalException("OdenOS requestLink Invalid parameter");
		}
		
		String response = null;
		BlockingQueue<String> queue = null;
		try {
			try {
				synchronized (linkQueueMap) {
					queue = linkQueueMap.get(link.linkId);
					if (queue == null) {
						queue = new ArrayBlockingQueue<String>(1);
						linkQueueMap.put(link.linkId, queue);
					}
				}
				
				driver.requestLink(link);
				
				// Waits response from driver.
				response = queue.poll(odenOSConfig.getResponseTimeout(), TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				throw new InternalException("OdenOS requestLink BlockingQueue Interrupted", e);
			} catch (Exception e) {
				throw new InternalException("OdenOS requestLink request failed", e);
			}
			if (response == null) {
				throw new TimeOutException("OdenOS response timeout at requestLink");
			}
			if ("".equals(response) || !link.linkId.equals(response)) {
				throw new InternalException("OdenOS requestLink request failed : " + response);
			}
		} finally {
			if (queue != null) {
				synchronized (linkQueueMap) {
					linkQueueMap.remove(link.linkId);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.IOdenOSListener#setDriver(org.o3project.mlo.server.rpc.service.IOdenOSDriver)
	 */
	@Override
	public void setDriver(OdenOSDriver driver) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("OdenOSAdapterServiceImpl#setDriver");
		}
		this.driver = driver;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.IOdenOSListener#notifyLinkChanged(java.lang.String)
	 */
	@Override
	public void notifyLinkChanged(String linkId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("OdenOSAdapterServiceImpl#notifyLinkChanged : " + linkId);
		}
		// Must not put null to BlockingQueue to detect timeout.
		String element = linkId;
		if (element == null) {
			element = "";
		}
		try {
			synchronized (linkQueueMap) {
				BlockingQueue<String> queue = linkQueueMap.get(element);
				if (queue != null) {
					queue.put(element);
				} else {
					LOG.error("notifyLinkChanged Incorrect response : " + element);
				}
			}
		} catch (InterruptedException e) {
			LOG.error("Interrupted in the notifyLinkChanged", e);
		}
	}

	@Override
	public void notifyFlowDeleted(String odenosFlowId) {
		// Must not put null to BlockingQueue to detect timeout.
		String element = odenosFlowId;
		if (element == null) {
			element = "";
		}
		try {
			synchronized (deleteFlowQueueMap) {
				BlockingQueue<String> queue = deleteFlowQueueMap.get(element);
				if (queue != null) {
					queue.put(element);
				} else {
					LOG.error("notifyFlowDeleted Incorrect response : " + element);
				}
			}
		} catch (InterruptedException e) {
			LOG.error("Interrupted in the notifyFlowDeleted", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.IOdenOSListener#notifyFlowChanged(java.lang.String)
	 */
	@Override
	public void notifyFlowChanged(String odenosFlowId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("OdenOSAdapterServiceImpl#notifyFlowChanged : " + odenosFlowId);
		}
		// Must not put null to BlockingQueue to detect timeout.
		String element = odenosFlowId;
		if (element == null) {
			element = "";
		}
		try {
			synchronized (flowChangedQueueMap) {
				BlockingQueue<String> queue = flowChangedQueueMap.get(element);
				if (queue != null) {
					queue.put(element);
				} else {
					LOG.error("notifyFlowChanged Incorrect response : " + element);
				}
			}
		} catch (InterruptedException e) {
			LOG.error("Interrupted in the notifyFlowChanged", e);
		}
	}
	/*
	 * (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSAdapterService#deleteFlow(java.lang.String)
	 */
	@Override
	public void deleteFlow(String odenosFlowId) throws MloException {
		// Checks whether odenos is running or not.
		if (driver == null) {
			throw new InternalException("OdenOS has not started.");
		}

		if (StringUtils.isNullOrEmpty(odenosFlowId)) {
			throw new InternalException("OdenOS deleteFlow Invalid parameter");
		}
		
		String response = null;
		BlockingQueue<String> queue = null;
		try {
			try {
				// Waits response from driver.
				synchronized (deleteFlowQueueMap) {
					queue = deleteFlowQueueMap.get(odenosFlowId);
					if (queue == null) {
						queue = new ArrayBlockingQueue<String>(1);
						deleteFlowQueueMap.put(odenosFlowId, queue);
					}
				}
				
				driver.deleteFlow(odenosFlowId);
				
				response = queue.poll(odenOSConfig.getResponseTimeout(), TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				throw new InternalException("OdenOS deleteFlow BlockingQueue Interrupted", e);
			} catch (Exception e) {
				throw new InternalException("OdenOS deleteFlow request failed", e);
			}
			if (response == null) {
				throw new TimeOutException("OdenOS response timeout at deleteFlow");
			}
			if ("".equals(response) || !odenosFlowId.equals(response)) {
				throw new InternalException("OdenOS deleteFlow request failed : " + response);
			}
		} finally {
			if (queue != null) {
				synchronized (deleteFlowQueueMap) {
					deleteFlowQueueMap.remove(odenosFlowId);
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSAdapterService#putFlow(org.o3project.mlo.server.rpc.entity.PTFlowEntity)
	 */
	@Override
	public String putFlow(PTFlowEntity entity) throws MloException {
		// Checks whether odenos is running or not.
		if (driver == null) {
			throw new InternalException("OdenOS has not started.");
		}

		if (entity == null) {
			throw new InternalException("OdenOS putFlow Invalid parameter");
		}
		
		String flowId = entity.flowId;
		String response = null;
		BlockingQueue<String> queue = null;
		try {
			try {
				// Waits response from driver.
				synchronized (flowChangedQueueMap) {
					queue = flowChangedQueueMap.get(flowId);
					if (queue == null) {
						queue = new ArrayBlockingQueue<String>(1);
						flowChangedQueueMap.put(flowId, queue);
					}
				}
				
				driver.putFlow(entity);
				
				response = queue.poll(odenOSConfig.getResponseTimeout(), TimeUnit.SECONDS);
			} catch (MloException e) {
				throw e;
			} catch (InterruptedException e) {
				throw new InternalException("OdenOS putFlow BlockingQueue Interrupted", e);
			} catch (Exception e) {
				throw new InternalException("OdenOS putFlow request failed", e);
			}
			if (response == null) {
				throw new TimeOutException("OdenOS response timeout at putFlow");
			}
			if ("".equals(response) || !flowId.equals(response)) {
				throw new InternalException("OdenOS putFlow request failed : " + response);
			}
			return response;
		} finally {
			if (queue != null) {
				synchronized (flowChangedQueueMap) {
					flowChangedQueueMap.remove(flowId);
				}
			}
		}
	}
}
