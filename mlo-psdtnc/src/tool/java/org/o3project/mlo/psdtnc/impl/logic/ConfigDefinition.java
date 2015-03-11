/**
 * ConfigDefinition.java
 * (C) 2014, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.psdtnc.impl.logic;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.psdtnc.logic.ConfigConstants;
import org.o3project.mlo.server.impl.logic.ConfigProviderImpl;

/**
 * ConfigDefinition
 *
 */
class ConfigDefinition implements ConfigConstants {
	private static final Log LOG = LogFactory.getLog(ConfigDefinition.class);

	/**
	 * コンフィグレーションのデフォルトプロパティファイルを出力するメインメソッドです。 
	 * @param args 引数（未使用）
	 */
	public static void main(String[] args) {
		try {
			File propsFile = new File("src/main/resources", "default.mlo-psdtnc.properties");
			Map<String, String> props = new LinkedHashMap<String, String>();
			defineProps(props);
			ConfigProviderImpl.storeProps(propsFile, props);
		} catch (IOException e) {
			LOG.error("Failed to store properties.", e);
		}
	}

	/**
	 * コンフィグレーションのデフォルト値を定義します。
	 * @param props デフォルト値を登録するプロパティマップ
	 */
	static void defineProps(Map<String, String> props) {
		props.put(PROP_KEY_SSH_SESSION_CONNECTION_TIMEOUT_MSEC, "180000"); // 3 min.
		props.put(PROP_KEY_SSH_CHANNEL_CONNECTION_TIMEOUT_MSEC, "120000"); // 2 min.

		props.put(PROP_KEY_LD_DEBUG_ENABLE_LOCAL_SAMPLE, "false");
		
		props.put(PROP_KEY_LD_HOST, "127.0.0.1");
		props.put(PROP_KEY_LD_SSH_PORT, "22");
		props.put(PROP_KEY_LD_USERID, "developer");
		props.put(PROP_KEY_LD_PASSWORD, "developer");
		props.put(PROP_KEY_LD_OPERATION_TIMEOUT_SEC, "300"); // 5 min. 
		
		props.put(PROP_KEY_LD_OPERATION_SHELL_COMMAND_START,  "(cd mlo-net/lagopus-docker;./lagopus-docker.sh start)");
		props.put(PROP_KEY_LD_OPERATION_SHELL_COMMAND_STOP,   "(cd mlo-net/lagopus-docker;./lagopus-docker.sh stop)");
		props.put(PROP_KEY_LD_OPERATION_SHELL_COMMAND_STATUS, "(cd mlo-net/lagopus-docker;./lagopus-docker.sh status)");
		props.put(PROP_KEY_LD_OPERATION_SHELL_COMMAND_DELETE_ALL_FLOWS, "(cd mlo-net/lagopus-docker/workspace;./clear_flow.sh)");
		props.put(PROP_KEY_LD_OPERATION_SHELL_COMMAND_FMT_PUT_FLOW, "(cd mlo-net/lagopus-docker/workspace;./%s.conf.sh -s)");
		props.put(PROP_KEY_LD_OPERATION_SHELL_COMMAND_FMT_DELETE_FLOW, "(cd mlo-net/lagopus-docker/workspace;./%s.conf.sh -d -e)");
		props.put(PROP_KEY_LD_OPERATION_PATH_TOPO_CONF, "mlo-net/lagopus-docker/workspace/topo.conf");
		
		props.put(PROP_KEY_VRM_LOGIN_IDS, "admin");
		props.put(PROP_KEY_VRM_LOGIN_ID_PREFIX + "admin." + PROP_SUBKEY_VRM_LOGIN_ID_PASSWORD, "Admin");
		props.put(PROP_KEY_VRM_LOGIN_ID_PREFIX + "admin." + PROP_SUBKEY_VRM_SLICE_ID, "2");
	}
}
