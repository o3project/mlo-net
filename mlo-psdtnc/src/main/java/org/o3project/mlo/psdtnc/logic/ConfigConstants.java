/**
 * ConfigConstants.java
 * (C) 2014, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.psdtnc.logic;

/**
 * ConfigConstants
 *
 */
public interface ConfigConstants {
	
	/**
	 * Common prefix for keys.
	 */
	String PROP_KEY_PREFIX_ = "mlo.psdtnc.config.";

	/*
	 * Keys for SSH
	 */
	
	String PROP_KEY_SSH_SESSION_CONNECTION_TIMEOUT_MSEC = PROP_KEY_PREFIX_ + "ssh.sessionConnectionTimeoutMsec";
	
	String PROP_KEY_SSH_CHANNEL_CONNECTION_TIMEOUT_MSEC = PROP_KEY_PREFIX_ + "ssh.channelConnectionTimeoutMsec";
	
	/*
	 * Keys for LD
	 */
	
	String PROP_KEY_LD_DEBUG_ENABLE_LOCAL_SAMPLE = PROP_KEY_PREFIX_ + "ld.debug.enable.localSample";
	
	String PROP_KEY_LD_HOST = PROP_KEY_PREFIX_ + "ld.host";
	
	String PROP_KEY_LD_SSH_PORT = PROP_KEY_PREFIX_ + "ld.sshPort";
	
	String PROP_KEY_LD_USERID = PROP_KEY_PREFIX_ + "ld.userid";
	
	String PROP_KEY_LD_PASSWORD = PROP_KEY_PREFIX_ + "ld.password";
	
	String PROP_KEY_LD_OPERATION_TIMEOUT_SEC = PROP_KEY_PREFIX_ + "ld.operationTimeoutSec";
	
	String PROP_KEY_LD_OPERATION_SHELL_COMMAND_START = PROP_KEY_PREFIX_ + "ld.operationShellCommand.start";
	
	String PROP_KEY_LD_OPERATION_SHELL_COMMAND_STOP = PROP_KEY_PREFIX_ + "ld.operationShellCommand.stop";
	
	String PROP_KEY_LD_OPERATION_SHELL_COMMAND_STATUS = PROP_KEY_PREFIX_ + "ld.operationShellCommand.status";
	
	String PROP_KEY_LD_OPERATION_SHELL_COMMAND_DELETE_ALL_FLOWS = PROP_KEY_PREFIX_ + "ld.operationShellCommand.deleteAllFlows";
	
	String PROP_KEY_LD_OPERATION_SHELL_COMMAND_FMT_PUT_FLOW = PROP_KEY_PREFIX_ + "ld.operationShellCommandFmt.putFlow";
	
	String PROP_KEY_LD_OPERATION_SHELL_COMMAND_FMT_DELETE_FLOW = PROP_KEY_PREFIX_ + "ld.operationShellCommandFmt.deleteFlow";
	
	String PROP_KEY_LD_OPERATION_PATH_TOPO_CONF = PROP_KEY_PREFIX_ + "ld.operationPath.topoConf";
	
	/*
	 * Keys for pseudo SDTN-C VRM
	 */
	
	String PROP_KEY_VRM_LOGIN_IDS = PROP_KEY_PREFIX_ + "vrm.loginIds";
	
	String PROP_KEY_VRM_LOGIN_ID_PREFIX = PROP_KEY_PREFIX_ + "vrm.loginId.";
	
	String PROP_SUBKEY_VRM_LOGIN_ID_PASSWORD = "password";
	
	String PROP_SUBKEY_VRM_SLICE_ID = "sliceId";
	
}
