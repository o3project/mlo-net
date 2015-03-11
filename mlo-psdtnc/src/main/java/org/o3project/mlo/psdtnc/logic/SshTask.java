/**
 * SshTask.java
 * (C) 2014, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.psdtnc.logic;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

/**
 * SSH 接続の実行タスククラスです。
 */
public class SshTask implements Callable<SshTaskResult> {
	
	private static final Log LOG = LogFactory.getLog(SshTask.class);
	
	private static final int BUFFER_LEN = 1024;
	
	private final SshNodeConfig config;
	
	private final String[] shellCommands;
	
	private SshInputStreamTextHandler inputStreamTextHandler;

	/**
	 * SSH 接続の実行タスクインスタンスを作成します。
	 * @param config 接続先情報
	 * @param shellCommands 実行コマンド配列
	 */
	public SshTask(SshNodeConfig config, String[] shellCommands) {
		this.config = config;
		this.shellCommands = shellCommands;
	}
	
	/**
	 * SSH 接続の実行タスクインスタンスを作成します。
	 * @param config 接続先情報
	 * @param shellCommand 実行コマンド
	 */
	public SshTask(SshNodeConfig config, String shellCommand) {
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
	public SshTaskResult call() throws JSchException, IOException {
		List<SshChannelResult> chResults = new ArrayList<>();
		SshChannelResult chResult = null;
		
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
			
			for (String command : shellCommands) {
				chResult = doChannelExec(session, command);
				chResults.add(chResult);
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

	/**
	 * ひとつのシェルコマンドを実行します。
	 * @param session SSH セッション
	 * @param shellCommand シェルコマンド
	 * @return 実行結果
	 * @throws JSchException SSH 実行時異常
	 * @throws IOException IO 異常
	 */
	SshChannelResult doChannelExec(Session session, String shellCommand)
			throws JSchException, IOException {
		ChannelExec channel = null;
		SshChannelResult chResult = null;
		ExecutorService execService = Executors.newFixedThreadPool(2);
		
		LOG.info(String.format("START executing: %s", shellCommand));
		
		try {
			channel = (ChannelExec) session.openChannel("exec");
			channel.setCommand(shellCommand);
			channel.setPty(true);
			channel.setInputStream(null);
			
			final InputStream stdInputstream = channel.getInputStream();
			final InputStream errInputStream = channel.getErrStream();

			channel.connect(config.getSshChannelTimeout());
			
			while (!Thread.currentThread().isInterrupted()) {
				handleInputStreams(execService, stdInputstream, errInputStream);
				
				if (channel.isClosed()) {
					int exitStatus = channel.getExitStatus();
					LOG.info("exit-status: " + exitStatus);
					chResult = new SshChannelResult(shellCommand, exitStatus);
					break;
				}
			}
		} finally {
			if (channel != null) {
				channel.disconnect();
				LOG.info("Ssh channel disconnected.");
				channel = null;
			}
			if (execService != null) {
				execService.shutdownNow();
				try {
					execService.awaitTermination(120, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					LOG.warn("Interrupted exception occurs in awaiting termination. : " + e.getMessage());
				}
			}
			LOG.info(String.format("END executing: %s", shellCommand));
		}
		return chResult;
	}

	/**
	 * @param execService
	 * @param stdInputstream
	 * @param errInputStream
	 * @throws IOException
	 */
	private void handleInputStreams(ExecutorService execService,
			final InputStream stdInputstream, final InputStream errInputStream)
			throws IOException {
		Future<Void> stdInputFuture = execService.submit(new Callable<Void>() {
			@Override
			public Void call() throws IOException {
				handleInputStream(stdInputstream, inputStreamTextHandler, "[STDOUT]");
				return null;
			}
		});
		Future<Void> errInputFuture = execService.submit(new Callable<Void>() {
			@Override
			public Void call() throws IOException {
				handleInputStream(errInputStream, null, "[ERROUT]");
				return null;
			}
		});
		awaitToBeDone(stdInputFuture);
		awaitToBeDone(errInputFuture);
	}

	/**
	 * @param future
	 * @throws IOException
	 */
	private void awaitToBeDone(Future<Void> future) throws IOException {
		try {
			future.get();
		} catch (ExecutionException e) {
			if (e.getCause() instanceof IOException) {
				throw (IOException)e.getCause();
			} else {
				throw new IllegalStateException("Unknown exception occurs.", e.getCause());
			}
		} catch (InterruptedException e) {
			throw new IllegalStateException("Interrupted.", e);
		}
	}
	
	private void handleInputStream(InputStream inputstream, SshInputStreamTextHandler textHandler, String logPrefix) throws IOException {
		byte[] buf = new byte[BUFFER_LEN];
		while (inputstream.available() > 0 && !Thread.currentThread().isInterrupted()) {
			int rlen = inputstream.read(buf, 0, buf.length);
			if (rlen < 0) {
				break;
			}
			String sbuf = new String(buf, 0, rlen, "UTF-8");
			
			String logMsg = "";
			if (logPrefix != null) {
				logMsg = logMsg + logPrefix + " ";
			}
			logMsg = logMsg + sbuf;
			LOG.info(logMsg);
			
			if (textHandler != null) {
				textHandler.handle(sbuf);
			}
		}
	}
	
	/**
	 * ユーザ情報文字列を取得します。
	 * @param config 装置コンフィグ
	 * @return ユーザ情報文字列
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
}

