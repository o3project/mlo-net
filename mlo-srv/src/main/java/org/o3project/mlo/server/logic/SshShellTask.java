/**
 * SshShellTask.java
 * (C) 2014,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

/**
 * This class is the SSH connection task class.
 */
public class SshShellTask implements Callable<Void> {
	
	private static final Log LOG = LogFactory.getLog(SshShellTask.class);
	
	 String targetId;
	 SshNodeConfig config;
	 SshShellPipe sshShellPipe;

	/**
	 * initializes.
	 * @param targetId name of target node.
	 * @param config the configuration.
	 * @param shellCommands the array of shell commands.
	 * @return 
	 */
	public void init(String targetId, SshNodeConfig config, SshShellPipe sshShellPipe) {
		this.targetId = targetId;
		this.config = config;
		this.sshShellPipe = sshShellPipe;
	}
	
	/**
	 * @return the config
	 */
	SshNodeConfig getConfig() {
		return config;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Void call() {
		LOG.info(String.format("BEGIN SSH task to %s", getSshInfo(config)));
		
		JSch jsch = new JSch();
		Session session = null;
		try {
			String userid = config.getUserid();
			String host = config.getHost();
			int sshPort = config.getSshPort();
			int sshSessionTimeout = config.getSshSessionTimeout();
			String password = config.getPassword();
			
			session = jsch.getSession(userid, host, sshPort);
			session.setPassword(password);
			session.setUserInfo(createUserInfo());
			session.connect(sshSessionTimeout);

			doChannelShell(session);
		} catch (Exception e) {
			LOG.error("Failed to open ssh session.", e);
			sshShellPipe.handleException(e);
		} finally {
			if (session != null) {
				session.disconnect();
				session = null;
			}
			LOG.info(String.format("END SSH task to %s", getSshInfo(config)));
		}
		return null;
	}
	
	/**
	 * Stops this task.
	 */
	public void requestToStop() {
		Thread.currentThread().interrupt();
	}
	
	void doChannelShell(Session session) throws Exception {
		//LOG.info("BEGIN doChannelShell.");
		ChannelShell channel = null;
		InputStream istream = null;
		BufferedReader reader = null;
		try {
			channel = (ChannelShell) session.openChannel("shell");
			channel.setPty(true);
			channel.setPtyType("vt220-m");
			channel.setInputStream(sshShellPipe.getSshStdInputStream(), true);
			istream = channel.getInputStream();
			
			LOG.info("Starts channel connection.");
			channel.connect(config.getSshChannelTimeout());
			reader = new BufferedReader(new InputStreamReader(istream, "UTF-8"));
			
			sendInitCommand();
			
			String line = reader.readLine();
			while (line != null && !Thread.currentThread().isInterrupted()) {
				if (channel.isClosed()) {
					System.out.println("exit-status: "+channel.getExitStatus());
					break;
				}
				LOG.info(line);
				sshShellPipe.handleMessage(line);
				line = reader.readLine();
			}
			LOG.info("Ends channel connection.");
			sshShellPipe.handleException(new SshConnectionException("SSH daemon is stopped"));
		} catch (InterruptedIOException e) {
			LOG.debug("SSH task is cancelled.");
		} finally {
			if (reader != null) {
				reader.close();
				reader = null;
			}
			if (channel != null) {
				channel.disconnect();
				LOG.info("Ssh channel disconnected.");
				channel = null;
			}
			//LOG.info("END doChannelShell.");
		}
	}
	
	void sendInitCommand() throws Exception{
		OutputStream ostream = sshShellPipe.getAppStdinOutputStream();
		String ldWsPath = config.getLdWorkspaceDirpash();
		String[] cmds = { String.format("cd \"%s\"\n", ldWsPath),
				String.format("./run attach %s\n", targetId),
				"unalias ls\n", "PS1=\"$ \"\n", };
		for (String cmd : cmds) {
			ostream.write(cmd.getBytes("UTF-8"));
		}
	}

	/**
	 * Obtains user information.
	 * @param config the configuration.
	 * @return User information.
	 */
	static String getSshInfo(SshNodeConfig config) {
		String sshInfo = null;
		if (config != null) {
			sshInfo = String.format("%s@%s:%d, (sessionTimeout, channelTimeout)=(%d, %d)", 
					config.getUserid(), 
					config.getHost(),
					config.getSshPort(),
					config.getSshSessionTimeout(),
					config.getSshChannelTimeout());
		}
		return sshInfo;
	}
	
	/**
	 * Creates user information.
	 * @return the user information.
	 */
	UserInfo createUserInfo() {
		UserInfo ui = new UserInfo() {
			
			@Override
			public void showMessage(String message) {
				LOG.info(message);
			}
			
			@Override
			public boolean promptYesNo(String message) {
				return true;
			}
			
			@Override
			public String getPassword() {
				return null;
			}
			
			@Override
			public boolean promptPassword(String message) {
				return false;
			}
			
			@Override
			public String getPassphrase() {
				return null;
			}
			
			@Override
			public boolean promptPassphrase(String message) {
				return false;
			}
		};
		
		return ui;
	}
}

