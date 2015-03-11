/**
 * LdTopologyRepositoryImpl.java
 * (C) 2015, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.psdtnc.impl.logic;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.psdtnc.logic.ConfigConstants;
import org.o3project.mlo.psdtnc.logic.LdOperationException;
import org.o3project.mlo.psdtnc.logic.LdOperationService;
import org.o3project.mlo.server.dto.LdTopoDto;
import org.o3project.mlo.server.dto.RyLinkDto;
import org.o3project.mlo.server.dto.RySwitchDto;
import org.o3project.mlo.server.logic.ConfigProvider;
import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.LdTopologyRepository;
import org.o3project.mlo.server.logic.MloException;

/**
 * LdTopologyRepositoryImpl
 *
 */
public class LdTopologyRepositoryImpl implements LdTopologyRepository, ConfigConstants {


	private static final Log LOG = LogFactory.getLog(LdTopologyRepositoryImpl.class);
	
	private static final int EXEC_SERVICE_TERMINATION_TIMEOUT_SEC = 60;

	private static final int SET_UP_FUTURE_TIMEOUT_SEC = 60;
	
	private final Object oMutex = new Object();
	
	private boolean isInitialized = false;
	
	ConfigProvider configProvider;
	
	LdOperationService ldOperationService;
	
	LdOperationService ldOperationServiceDummyLocalImpl;
	
	ExecutorService execService;
	
	Future<?> setUpFuture;
	
	LdTopoDto ldTopoDto;
	
	List<RySwitchDto> rySwitches;
	
	List<RyLinkDto> ryLinks;
	
	/**
	 * @param configProvider the configProvider to set
	 */
	public void setConfigProvider(ConfigProvider configProvider) {
		this.configProvider = configProvider;
	}

	/**
	 * @param ldOperationService the ldOperationService to set
	 */
	public void setLdOperationService(LdOperationService ldOperationService) {
		this.ldOperationService = ldOperationService;
	}
	
	/**
	 * @param ldOperationServiceDummyLocalImpl the ldOperationServiceDummyLocalImpl to set
	 */
	public void setLdOperationServiceDummyLocalImpl(LdOperationService ldOperationServiceDummyLocalImpl) {
		this.ldOperationServiceDummyLocalImpl = ldOperationServiceDummyLocalImpl;
	}
	
	private LdOperationService getAvailableLdOperationService() {
		boolean isDebugLocal = configProvider.getBooleanProperty(PROP_KEY_LD_DEBUG_ENABLE_LOCAL_SAMPLE);
		if (!isDebugLocal) {
			return ldOperationService;
		} else {
			return ldOperationServiceDummyLocalImpl;
		}
	}
	
	public void init() {
		synchronized (oMutex) {
			if (!isInitialized) {
				if (execService == null) {
					execService = Executors.newSingleThreadExecutor();
				}
				if (setUpFuture != null) {
					setUpFuture.cancel(true);
				}
				setUpFuture = execService.submit(createSetUpTask());
				isInitialized = true;
			}
		}
	}
	
	public void destroy() {
		synchronized (oMutex) {
			if (isInitialized) {
				if (execService != null) {
					execService.shutdown();
					try {
						execService.awaitTermination(EXEC_SERVICE_TERMINATION_TIMEOUT_SEC, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						LOG.warn("Interrupted in waiting to shutdown.", e);
					} finally {
						execService = null;
					}
				}
				isInitialized = false;
			}
		}
	}
	
	void setUp() throws LdOperationException {
		LdOperationService service = getAvailableLdOperationService();
		service.doStart();
		Integer status = service.doStatus();
		if (status == null || status == 4) {
			throw new LdOperationException("Failed to start ld.");
		}
		ldTopoDto = service.doLoadTopoConf();
	}
	
	void setUpEmpty() {
		ldTopoDto = LdTopoDto.constructEmptyObject();
	}
	
	Runnable createSetUpTask() {
		return new Runnable() {
			/* (non-Javadoc)
			 * @see java.lang.Runnable#run()
			 */
			@Override
			public void run() {
				try {
					setUp();
				} catch (LdOperationException e) {
					LOG.error("Failed to setup topology repository.", e);
					setUpEmpty();
				}
			}
		};
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.LdTopologyRepository#getLdTopo()
	 */
	@Override
	public LdTopoDto getLdTopo() throws MloException {
		syncSettingUp();
		return ldTopoDto;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.LdTopologyRepository#getRySwitches()
	 */
	@Override
	public List<RySwitchDto> getRySwitches() throws MloException {
		syncSettingUp();
		return rySwitches;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.LdTopologyRepository#getRyLinks()
	 */
	@Override
	public List<RyLinkDto> getRyLinks() throws MloException {
		syncSettingUp();
		return ryLinks;
	}

	/**
	 * 
	 */
	void syncSettingUp() throws MloException {
		Future<?> future = null;
		synchronized (oMutex) {
			future = setUpFuture;
		}
		
		try {
			future.get(SET_UP_FUTURE_TIMEOUT_SEC, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			String msg = "Interruption occurs in setting up.";
			LOG.error(msg, e);
			throw new InternalException(msg, e);
		} catch (ExecutionException e) {
			String msg = "Failed to set up.";
			LOG.error(msg, e.getCause());
			throw new InternalException(msg, e.getCause());
		} catch (TimeoutException e) {
			String msg = "Timeout occurs in waiting completion of setting up.";
			LOG.warn(msg, e);
			throw new InternalException(msg, e);
		}
	}
}
