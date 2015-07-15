/**
 * SshShellPipe.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

import java.io.Closeable;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is the pipe class for IO of SSH connection.
 */
public class SshShellPipe {
	private static final Log LOG = LogFactory.getLog(SshShellPipe.class);
	
	private static final Integer PIPE_SIZE = 16 * 1024;
	
	final private PipedInputStream sshStdInputStream;
	final private PipedOutputStream sshStdOutputStream;
	
	final private PipedOutputStream appStdinOutputStream;
	final private PipedInputStream appStdoutInputStream;
	
	private SshMessageHandler sshMessageHandler = null;
	private SshExceptionHandler sshExceptionHandler = null;
	
	/**
	 * A constructor.
	 */
	public SshShellPipe() {
		sshStdInputStream = new PipedInputStream(PIPE_SIZE);
		appStdinOutputStream = new PipedOutputStream();
		
		appStdoutInputStream = new PipedInputStream(PIPE_SIZE);
		sshStdOutputStream = new PipedOutputStream();
	}
	
	/**
	 * @param sshMessageHandler the sshMessageHandler to set
	 */
	public void setSshMessageHandler(SshMessageHandler sshMessageHandler) {
		this.sshMessageHandler = sshMessageHandler;
	}
	
	/**
	 * @param sshMessageHandler the sshMessageHandler to set
	 */
	public void setSshExceptionHandler(SshExceptionHandler sshExceptionHandler) {
		this.sshExceptionHandler = sshExceptionHandler;
	}
	
	/**
	 * Connects inputstream and outputstream.
	 * @throws IOException
	 */
	public void connect() throws IOException {
		sshStdInputStream.connect(appStdinOutputStream);
		appStdoutInputStream.connect(sshStdOutputStream);
	}
	
	/**
	 * @return the sshStdInputStream
	 */
	public PipedInputStream getSshStdInputStream() {
		return sshStdInputStream;
	}
	
	/**
	 * @return the sshStdOutputStream
	 */
	public PipedOutputStream getSshStdOutputStream() {
		return sshStdOutputStream;
	}
	
	/**
	 * @return the appStdinOutputStream
	 */
	public PipedOutputStream getAppStdinOutputStream() {
		return appStdinOutputStream;
	}
	
	/**
	 * @return the appStdoutInputStream
	 */
	public PipedInputStream getAppStdoutInputStream() {
		return appStdoutInputStream;
	}
	
	/**
	 * Closes all streams.
	 */
	public void close() {
		closeAClosable(sshStdInputStream);
		closeAClosable(appStdinOutputStream);

		closeAClosable(appStdoutInputStream);
		closeAClosable(sshStdOutputStream);
	}
	
	private static IOException closeAClosable(Closeable closable) {
		IOException eThrown = null;
		try {
			closable.close();
		} catch (IOException e) {
			String className = closable.getClass().getName();
			LOG.error("Failed to close stream. : " + className, e);
			if (eThrown == null) {
				eThrown = e;
			}
		}
		return eThrown;
	}
	
	/**
	 * Handles message.
	 * @param line handled message.
	 */
	public void handleMessage(String line) {
		if (sshMessageHandler != null) {
			sshMessageHandler.onMessage(line);
		}else{
			LOG.error("sshMessageHandler is null.");
		}
	}
	
	/**
	 * Handles exception.
	 * @param throable handled exception.
	 */
	public void handleException(Throwable throable) {
		if (sshExceptionHandler != null) {
			sshExceptionHandler.onException(throable);
		}else{
			LOG.error("sshExceptionHandler is null.");
		}
	}
}
