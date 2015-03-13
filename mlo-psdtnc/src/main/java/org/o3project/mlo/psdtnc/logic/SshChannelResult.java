/**
 * SshTaskResult.java
 * (C) 2014,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.psdtnc.logic;

/**
 * SSH チャネルの結果クラスです。
 */
public class SshChannelResult {
	
	SshChannelResult(String shellCommand, Integer exitStatus) {
		this.shellCommand = shellCommand;
		this.exitStatus = exitStatus;
	}
	
	/**
	 * シェルコマンド
	 */
	public final String shellCommand;
	
	/**
	 * exit ステータス
	 */
	public final Integer exitStatus;
}
