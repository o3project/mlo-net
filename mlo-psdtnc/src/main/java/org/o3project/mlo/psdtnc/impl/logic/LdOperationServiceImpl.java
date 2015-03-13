/**
 * LdOperationServiceImpl.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.psdtnc.impl.logic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.arnx.jsonic.JSON;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.psdtnc.logic.ConfigConstants;
import org.o3project.mlo.psdtnc.logic.LdConfJsonConverter;
import org.o3project.mlo.psdtnc.logic.LdOperationException;
import org.o3project.mlo.psdtnc.logic.LdOperationService;
import org.o3project.mlo.psdtnc.logic.SshChannelResult;
import org.o3project.mlo.psdtnc.logic.SshInputStreamTextHandler;
import org.o3project.mlo.psdtnc.logic.SshNodeConfig;
import org.o3project.mlo.psdtnc.logic.SshSftpGetTask;
import org.o3project.mlo.psdtnc.logic.SshTask;
import org.o3project.mlo.psdtnc.logic.SshTaskResult;
import org.o3project.mlo.server.dto.LdTopoDto;
import org.o3project.mlo.server.logic.ConfigProvider;

/**
 * LdOperationServiceImpl
 *
 */
public class LdOperationServiceImpl implements LdOperationService, ConfigConstants {

	private static final Log LOG = LogFactory.getLog(LdOperationServiceImpl.class);
	
	private static final int EXEC_SERVICE_TERMINATION_TIMEOUT_SEC = 60;
	
	private final Object oMutex = new Object();
	
	private boolean isInitialized = false;
	
	ConfigProvider configProvider;
	
	ExecutorService sshTaskExecService;
	
	ExecutorService spontaneousOpExecService;
	
	LdConfJsonConverter ldConfJsonConverter;
	
	/**
	 * @param configProvider the configProvider to set
	 */
	public void setConfigProvider(ConfigProvider configProvider) {
		this.configProvider = configProvider;
	}
	
	/**
	 * @param ldConfJsonConverter the ldConfJsonConverter to set
	 */
	public void setLdConfJsonConverter(LdConfJsonConverter ldConfJsonConverter) {
		this.ldConfJsonConverter = ldConfJsonConverter;
	}
	
	public void init() {
		synchronized (oMutex) {
			if (!isInitialized) {
				if (sshTaskExecService == null) {
					sshTaskExecService = Executors.newSingleThreadExecutor();
				}
				if (spontaneousOpExecService == null) {
					spontaneousOpExecService = Executors.newSingleThreadExecutor();
				}
				isInitialized = true;
			}
		}
	}
	
	public void destroy() {
		synchronized (oMutex) {
			if (isInitialized) {
				if (spontaneousOpExecService != null) {
					spontaneousOpExecService.shutdown();
					try {
						spontaneousOpExecService.awaitTermination(EXEC_SERVICE_TERMINATION_TIMEOUT_SEC, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						LOG.warn("Interrupted in waiting to shutdown.", e);
					} finally {
						spontaneousOpExecService = null;
					}
				}
				if (sshTaskExecService != null) {
					sshTaskExecService.shutdown();
					try {
						sshTaskExecService.awaitTermination(EXEC_SERVICE_TERMINATION_TIMEOUT_SEC, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						LOG.warn("Interrupted in waiting to shutdown.", e);
					} finally {
						sshTaskExecService = null;
					}
				}
				isInitialized = false;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.psdtnc.logic.LdOperationService#doStart()
	 */
	@Override
	public void doStart() throws LdOperationException {
		String shellCommand = getShellCommandStart();
		doExecuteSingleCommand(shellCommand);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.psdtnc.logic.LdOperationService#doStop()
	 */
	@Override
	public void doStop() throws LdOperationException {
		String shellCommand = getShellCommandStop();
		doExecuteSingleCommand(shellCommand);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.psdtnc.logic.LdOperationService#doStatus()
	 */
	@Override
	public Integer doStatus() throws LdOperationException {
		String shellCommand = getShellCommandStatus();
		Integer exitStatus = doExecuteSingleCommand(shellCommand);
		return exitStatus;
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.psdtnc.logic.LdOperationService#doExecuteSingleCommand(java.lang.String)
	 */
	@Override
	public Integer doExecuteSingleCommand(String shellCommand) throws LdOperationException {
		SshTaskResult sshTaskResult = doExecuteSingleCommandInternal(shellCommand);
		return sshTaskResult.channelResults[0].exitStatus;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.psdtnc.logic.LdOperationService#doLoadTopoConf()
	 */
	@Override
	public LdTopoDto doLoadTopoConf() throws LdOperationException {
		LdTopoDto ldTopoDto = null;
		ldTopoDto = doLoadTopoConfFromRemote();
		return ldTopoDto;
	}
	
	private LdTopoDto doLoadTopoConfFromRemote() throws LdOperationException {
		SshNodeConfig config = createSshNodeConfig(configProvider);
		SshTaskResult sshTaskResult = null;
		final StringBuilder sb = new StringBuilder();
		
		String topoConfPath = getPathTopoConf();
		SshSftpGetTask sshSftpTask = createSshSftpTask(config, topoConfPath);
		sshSftpTask.setInputStreamTextHandler(new SshInputStreamTextHandler() {
			@Override
			public void handle(String text) {
				sb.append(text);
			}
		});
		sshTaskResult = doExecuteSingleSshTask(sshSftpTask);
		for (SshChannelResult chResult : sshTaskResult.channelResults) {
			LOG.debug(chResult.shellCommand + " -> " + chResult.exitStatus);
		}
		
		LdTopoDto ldTopoDto = null;
		ByteArrayInputStream inputStream = null;
		try {
			inputStream = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
			ldTopoDto = ldConfJsonConverter.convertFromConf(inputStream);
			LOG.debug(JSON.encode(ldTopoDto, true));
		} catch (IOException e) {
			LOG.warn("Failed to decode.", e);
			throw new LdOperationException("Failed to decode.", e);
		}
		
		return ldTopoDto;
	}

	/**
	 * @param shellCommand
	 * @return
	 * @throws LdOperationException
	 */
	private SshTaskResult doExecuteSingleCommandInternal(String shellCommand) throws LdOperationException {
		SshTaskResult sshTaskResult;
		SshNodeConfig config = createSshNodeConfig(configProvider);
		SshTask sshTask = createSshTask(shellCommand, config);
		sshTaskResult = doExecuteSingleSshTask(sshTask);
		for (SshChannelResult chResult : sshTaskResult.channelResults) {
			LOG.debug(chResult.shellCommand + " -> " + chResult.exitStatus);
		}
		return sshTaskResult;
	}

	/**
	 * @param shellCommand
	 * @return
	 * @throws LdOperationException
	 */
	private SshTaskResult doExecuteSingleSshTask(Callable<SshTaskResult> task) throws LdOperationException {
		Future<SshTaskResult> future = sshTaskExecService.submit(task);
		int operationTimeoutSec = configProvider.getIntegerProperty(PROP_KEY_LD_OPERATION_TIMEOUT_SEC);
		SshTaskResult sshTaskResult = null;
		try {
			sshTaskResult = future.get(operationTimeoutSec, TimeUnit.SECONDS);
		} catch (ExecutionException e) {
			LOG.debug("Execution exception occurs.", e.getCause());
			throw new LdOperationException("Failed to execute an ssh task.", e.getCause());
		} catch (InterruptedException e) {
			LOG.debug("Interrupted exception occurs.", e);
			throw new LdOperationException("An ssh task interrupted.", e);
		} catch (TimeoutException e) {
			future.cancel(true);
			LOG.debug("Timeout exception occurs.", e);
			throw new LdOperationException("Timeout occurs in executing an ssh task.", e);
		}
		return sshTaskResult;
	}

	SshTask createSshTask(String shellCommand, SshNodeConfig config) {
		SshTask task = null;
		task = new SshTask(config , shellCommand);
		return task;
	}

	SshSftpGetTask createSshSftpTask(SshNodeConfig config, String topoConfPath) {
		SshSftpGetTask task = null;
		task = new SshSftpGetTask(config, topoConfPath);
		return task;
	}

	private String getShellCommandStart() {
		return configProvider.getProperty(PROP_KEY_LD_OPERATION_SHELL_COMMAND_START);
	}
	
	private String getShellCommandStop() {
		return configProvider.getProperty(PROP_KEY_LD_OPERATION_SHELL_COMMAND_STOP);
	}
	
	private String getShellCommandStatus() {
		return configProvider.getProperty(PROP_KEY_LD_OPERATION_SHELL_COMMAND_STATUS);
	}
	
	private String getPathTopoConf() {
		return configProvider.getProperty(PROP_KEY_LD_OPERATION_PATH_TOPO_CONF);
	}

	/**
	 * @param configProvider TODO
	 * @return
	 */
	private SshNodeConfig createSshNodeConfig(final ConfigProvider configProvider) {
		SshNodeConfig config = new SshNodeConfig() {
			
			@Override
			public String getHost() {
				return configProvider.getProperty(PROP_KEY_LD_HOST);
			}
			
			@Override
			public int getSshPort() {
				return configProvider.getIntegerProperty(PROP_KEY_LD_SSH_PORT);
			}
			
			@Override
			public String getUserid() {
				return configProvider.getProperty(PROP_KEY_LD_USERID);
			}
			
			@Override
			public String getPassword() {
				return configProvider.getProperty(PROP_KEY_LD_PASSWORD);
			}
			
			@Override
			public int getSshSessionTimeout() {
				return configProvider.getIntegerProperty(PROP_KEY_SSH_SESSION_CONNECTION_TIMEOUT_MSEC);
			}
			
			@Override
			public int getSshChannelTimeout() {
				return configProvider.getIntegerProperty(PROP_KEY_SSH_CHANNEL_CONNECTION_TIMEOUT_MSEC);
			}
		};
		return config;
	}
}
