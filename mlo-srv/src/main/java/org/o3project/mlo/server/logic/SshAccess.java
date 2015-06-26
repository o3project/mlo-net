/**
 * SshAccess.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is the control class for SSH connection.
 */
public class SshAccess {
	private static final Log LOG = LogFactory.getLog(SshAccess.class);
	
	private final Object oMutex = new Object();
	private SshShellPipe sshShellPipe;
	private ConfigProvider configProvider;
	private Future<Void> response;
	private ExecutorService executor;
	private SshShellTask sshShellTask;
	
	/**
	 * Setter.
	 * @param sshShellTask
	 */
	public void setSshShellTask(SshShellTask sshShellTask) {
		this.sshShellTask = sshShellTask;
	}

	/**
	 * Setter.
	 * @param configProvider
	 */
	public void setConfigProvider(ConfigProvider configProvider) {
		this.configProvider = configProvider;
	}

	/**
	 * Setter.
	 * @param executor
	 */
	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}
	
	/**
	 * Setter.
	 * @param sshShellPipe
	 */
	public void setSshShellPipe(SshShellPipe sshShellPipe) {
		this.sshShellPipe = sshShellPipe;
	}
	
	/**
	 * Opens SSH connection.
	 * @param nodeName name of target node.
	 * @param sshMessageHandler message handler to register.
	 * @param sshExceptionandler exception handler to register.
	 */
	public boolean open(String nodeName, SshMessageHandler sshMessageHandler, SshExceptionHandler sshExceptionandler) {
		boolean result = false;
		synchronized (oMutex) {
			
			if (response == null) {
				SshNodeConfig sshNodeConfig = createSshNodeConfig();
				sshShellTask.init(nodeName, sshNodeConfig, sshShellPipe);

				sshShellPipe.setSshExceptionHandler(sshExceptionandler);
				sshShellPipe.setSshMessageHandler(sshMessageHandler);
				
				try {
					sshShellPipe.connect();
					// Starts SSH connection.
					response = executor.submit(sshShellTask);
					result = true;
				} catch (IOException e) {
					sshShellPipe.handleException(e);
					LOG.error("Failed to open.", e);
				}
			} else {
				LOG.warn("SSH connection is already opened.");
			}
		}
		return result;
	}
	
	/**
	 * Closes SSH connection.
	 */
	public boolean close() {
		boolean result = false;
		synchronized (oMutex) {
			if( response != null && !response.isCancelled()){
				sshShellPipe.setSshMessageHandler(null);
				sshShellPipe.setSshExceptionHandler(null);
				sshShellPipe.close();
				response.cancel(true);
				result = true;
				LOG.debug("SSH connection closed.");
			} else {
				LOG.warn("Can't close SSH connection. SSH connection is not opened.");
			}
		}
		return result;
	}

	/**
	 * Sends a command.
	 * @param cmd string of command to send..
	 */
	public boolean send(String cmd) {
		boolean result = false;
		synchronized (oMutex) {
			if( response != null && !response.isDone()){
				try {
					sshShellPipe.getAppStdinOutputStream().write((cmd + "\n").getBytes("UTF-8"));
					result = true;
					LOG.debug("Send a command.");
				} catch (IOException e) {
					sshShellPipe.handleException(e);
					LOG.error("Failed to send command.", e);
				}
			} else {
				LOG.warn("Can't send command. SSH connection is not opened.");
			}
		}
		return result;
	}
	
	SshNodeConfig createSshNodeConfig() {
		return new SshNodeConfig() {
			
			@Override
			public String getHost() {
				return configProvider.getProperty(ConfigConstants.PROP_KEY_REMOTE_NODE_ACCESS_HOST);
			}
			
			@Override
			public int getSshPort() {
				return configProvider.getIntegerProperty(ConfigConstants.PROP_KEY_REMOTE_NODE_ACCESS_SSH_PORT);
			}
			@Override
			public String getUserid() {
				return configProvider.getProperty(ConfigConstants.PROP_KEY_REMOTE_NODE_ACCESS_USERID);
			}
			
			@Override
			public String getPassword() {
				return configProvider.getProperty(ConfigConstants.PROP_KEY_REMOTE_NODE_ACCESS_PASSWORD);
			}
			
			@Override
			public int getSshSessionTimeout() {
				return configProvider.getIntegerProperty(ConfigConstants.PROP_KEY_REMOTE_NODE_ACCESS_SSH_SESSION_TIMEOUT_MSEC);
			}
			
			@Override
			public int getSshChannelTimeout() {
				return configProvider.getIntegerProperty(ConfigConstants.PROP_KEY_REMOTE_NODE_ACCESS_SSH_CHANNEL_TIMEOUT_MSEC);
			}
			
			@Override
			public  String getLdWorkspaceDirpash() {
				return configProvider.getProperty(ConfigConstants.PROP_KEY_REMOTE_NODE_ACCESS_LD_WORKSPACE_DIRPATH);
			}
		};
	}
}

