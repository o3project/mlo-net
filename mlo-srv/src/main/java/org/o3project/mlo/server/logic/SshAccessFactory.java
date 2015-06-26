/**
 * SshAccessFactory.java
 * (C) 2015, Hitachi, Ltd.
 */

package org.o3project.mlo.server.logic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is the factory class for SshAccess class. 
 */
public class SshAccessFactory {
	private static final Log LOG = LogFactory.getLog(SshAccessFactory.class);
	
	private static final Integer THREAD_POOL_SIZE = 32;
	private static final Integer EXECUTOR_SERVICE_TERMINATION_TIMEOUT_SEC = 20;
	
	ExecutorService executorService;
	private ConfigProvider configProvider;
	
	/**
	 * Setter method (for DI setter injection).
	 * @param configProvider The instance. 
	 */
	public void setConfigProvider(ConfigProvider configProvider) {
		this.configProvider = configProvider;
	}

	/**
	 * Initializes the instance.
	 */
	public void init(){
		executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
	}
	
	/**
	 * Destroys the instance.
	 */
	public void destroy(){
		if (executorService != null) {
			Integer timeoutSec = EXECUTOR_SERVICE_TERMINATION_TIMEOUT_SEC;
			executorService.shutdownNow();
			try {
				executorService.awaitTermination(timeoutSec, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				LOG.warn("Interruption occurs.", e);
			} finally {
				executorService = null;
			}
		}
	}
	
	/**
	 * Creates SshAccess instance.
	 * @return SshAccess instance. 
	 */
	public SshAccess create(){
		SshAccess sshAccess = new SshAccess();
		sshAccess.setExecutor(executorService);
		sshAccess.setConfigProvider(configProvider);
		sshAccess.setSshShellTask(new SshShellTask());
		sshAccess.setSshShellPipe(new SshShellPipe());
		return sshAccess;
	}
}
