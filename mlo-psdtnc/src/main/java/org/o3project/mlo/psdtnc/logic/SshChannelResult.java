/**
 * SshTaskResult.java
 * (C) 2014,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.psdtnc.logic;

/**
 * This class is the result class of SSH channel.
 */
public class SshChannelResult {
	
	SshChannelResult(String shellCommand, Integer exitStatus) {
		this.shellCommand = shellCommand;
		this.exitStatus = exitStatus;
	}
	
	/**
	 * Shell commands.
	 */
	public final String shellCommand;
	
	/**
	 * exit status.
	 */
	public final Integer exitStatus;
}
