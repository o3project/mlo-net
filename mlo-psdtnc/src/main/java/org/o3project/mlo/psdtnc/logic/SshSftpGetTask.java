/**
 * SshTask.java
 * (C) 2014,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.psdtnc.logic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;
import com.jcraft.jsch.UserInfo;

/**
 * SSH 接続の実行タスククラスです。
 */
public class SshSftpGetTask implements Callable<SshTaskResult> {
	
	private static final Log LOG = LogFactory.getLog(SshSftpGetTask.class);
	
	private static final int BUFFER_LEN = 1024;
	
	private final SshNodeConfig config;
	
	private final String[] shellCommands;
	
	private SshInputStreamTextHandler inputStreamTextHandler;

	/**
	 * SSH 接続の実行タスクインスタンスを作成します。
	 * @param config 接続先情報
	 * @param shellCommands 実行コマンド配列
	 */
	public SshSftpGetTask(SshNodeConfig config, String[] shellCommands) {
		this.config = config;
		this.shellCommands = shellCommands;
	}
	
	/**
	 * SSH 接続の実行タスクインスタンスを作成します。
	 * @param config 接続先情報
	 * @param shellCommand 実行コマンド
	 */
	public SshSftpGetTask(SshNodeConfig config, String shellCommand) {
		this(config, new String[]{shellCommand});
	}
	
	public void setInputStreamTextHandler(SshInputStreamTextHandler inputStreamTextHandler) {
		this.inputStreamTextHandler = inputStreamTextHandler;
	}
	
	/**
	 * @return the shellCommands
	 */
	String[] getShellCommands() {
		return shellCommands;
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
	public SshTaskResult call() throws JSchException, SftpException, IOException {
		List<SshChannelResult> chResults = new ArrayList<>();
		SshChannelResult chResult = null;
		
		LOG.info(String.format("BEGIN SSH task to %s", getSshInfo(config)));
		
		JSch jsch = new JSch();
		Session session = null;
		try {
			session = jsch.getSession(config.getUserid(), config.getHost(), config.getSshPort());
			session.setPassword(config.getPassword());
			session.setUserInfo(createUserInfo());
			session.connect(config.getSshSessionTimeout());
			
			for (String command : shellCommands) {
				chResult = doChannelSftpGet(session, command);
				if (chResult != null) {
					chResults.add(chResult);
				}
			}
		} finally {
			if (session != null) {
				session.disconnect();
				session = null;
			}
			LOG.info(String.format("END SSH task to %s", getSshInfo(config)));
		}
		
		SshTaskResult taskResult = new SshTaskResult();
		taskResult.channelResults = chResults.toArray(new SshChannelResult[0]);
		return taskResult;
	}
	
	SshChannelResult doChannelSftpGet(Session session, String remoteFilePath) throws JSchException, SftpException, IOException {
		SshChannelResult sshChannelResult = null;
		LOG.info(String.format("START executing sftp get: %s", remoteFilePath));
		
		ChannelSftp channel = null;
		try {
			channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect(config.getSshChannelTimeout());
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("pwd  : " + channel.pwd());
				LOG.debug("lpwd : " + channel.lpwd());
			}
			
			// Gets the remote file.
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			SftpProgressMonitor monitor = createSftpProgressMonitor(outputStream, inputStreamTextHandler);
			InputStream inputStream = channel.get(remoteFilePath, monitor);
			try {
				byte[] buf = new byte[BUFFER_LEN];
				while (!Thread.currentThread().isInterrupted()) {
					int rlen = inputStream.read(buf, 0, buf.length);
					if (rlen < 0) {
						break;
					}
					LOG.debug(new String(buf, 0, rlen, "UTF-8"));
					outputStream.write(buf, 0, rlen);
				}
			} finally {
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			}
			
			// Quits
			channel.exit();
			
			sshChannelResult = new SshChannelResult(remoteFilePath, channel.getExitStatus());
			
		} catch (JSchException|SftpException|IOException e) {
			LOG.warn("Error occues in sftp task.", e);
			throw e;
		} finally {
			if (channel != null) {
				channel.disconnect();
				channel = null;
			}
			LOG.info(String.format("END executing sftp get: %s", remoteFilePath));
		}
		return sshChannelResult;
	}
	
	/**
	 * ユーザ情報文字列を取得します。
	 * @param accNodeConfig 装置コンフィグ
	 * @return ユーザ情報文字列
	 */
	static String getSshInfo(SshNodeConfig accNodeConfig) {
		String sshInfo = null;
		if (accNodeConfig != null) {
			sshInfo = String.format("%s@%s:%d", 
					accNodeConfig.getUserid(), 
					accNodeConfig.getHost(),
					accNodeConfig.getSshPort());
		}
		return sshInfo;
	}
	
	/**
	 * ユーザ情報を作成します。
	 * @return ユーザ情報
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
	
	SftpProgressMonitor createSftpProgressMonitor(final ByteArrayOutputStream outputStream, final SshInputStreamTextHandler textHandler) {
		return new SftpProgressMonitor() {
			@Override
			public void init(int op, String src, String dst, long max) {
				LOG.info(String.format("SftpProgressMonitor init called. : (op, src, dst, max)=(%d, %s, %s, %d)", 
						op, src, dst, max));
			}
			
			@Override
			public boolean count(long count) {
				LOG.info(String.format("SftpProgressMonitor count called. : (count,)=(%d,)", count));
				return true;
			}
			
			@Override
			public void end() {
				LOG.info("SftpProgressMonitor end called.");
				if (textHandler != null) {
					textHandler.handle(outputStream.toString());
				}
			}
		};
	}
}

