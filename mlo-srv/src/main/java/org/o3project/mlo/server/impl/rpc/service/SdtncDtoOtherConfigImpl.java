/**
 * SdtncDtoOtherConfigImpl.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.logic.ApiCallException;
import org.o3project.mlo.server.logic.ConfigConstants;
import org.o3project.mlo.server.logic.ConfigProvider;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.rpc.service.SdtncDtoConfig;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.InitMethod;


/**
 * This class is the implementation class of {@link SdtncDtoConfig} interface.
 */
public class SdtncDtoOtherConfigImpl implements SdtncDtoConfig, ConfigConstants {

	@Binding
	private ConfigProvider configProvider;
	
	private Map<String, String> portIdMap;
	private static final String DELIMITER = "_";
	private static final Log LOG = LogFactory.getLog(SdtncDtoOtherConfigImpl.class);
	
	/**
	 * Initializes this component.
	 * This method is called only at once after the application is launched.
	 */
	@InitMethod
	public void init() {
		portIdMap = new HashMap<String, String>();
		
		FileReader fr = null;
		BufferedReader bf = null;
		String extraPortIdFile = null;
		
		try{
			try{
				extraPortIdFile = getVlanExtraMappingFilePath();
				fr = new FileReader(extraPortIdFile);
				bf = new BufferedReader(fr);
				String line;
				while ((line = bf.readLine()) != null) {
					String[] strAry = line.split(",");
					// strAry[0] = VlanId
					// strAry[1] = portId A
					// strAry[2] = portId Z
					portIdMap.put(strAry[0].trim() + DELIMITER + PROP_SUBKEY_PATH_END_POINT_A_PORT_ID, strAry[1].trim());
					portIdMap.put(strAry[0].trim() + DELIMITER + PROP_SUBKEY_PATH_END_POINT_Z_PORT_ID, strAry[2].trim());
				}
			}finally{
				if(bf != null){
					bf.close();
				}
				if(fr != null){
					fr.close();
				}
			}
		} catch (IOException e) {
			// In the case that failure occurs in loading external file,
			// continues to process.
			LOG.warn("Extra portId file read failed [file=" + extraPortIdFile + "]");
		}
	}
	
	/**
	 * Setter method (for DI setter injection).
	 * @param configProvider The instance. 
	 */
	public void setConfigProvider(ConfigProvider configProvider) {
		this.configProvider = configProvider;
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncDtoConfig#getLoginPassword()
	 */
	@Override
	public String getLoginPassword() {
		return configProvider.getProperty(PROP_KEY_SDTNC_DTO_OTHER_LOGIN_PASSWORD);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncDtoConfig#getSliceId()
	 */
	@Override
	public String getSliceId() {
		return configProvider.getProperty(PROP_KEY_SDTNC_DTO_OTHER_SLICE_ID);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncDtoConfig#getLoginIpAddress()
	 */
	@Override
	public String getLoginIpAddress() {
		return configProvider.getProperty(PROP_KEY_SDTNC_DTO_HITACHI_LOGIN_IP_ADDRESS);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncDtoConfig#getHopPathId()
	 */
	@Override
	public String getHopPathId() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncDtoConfig#getCutPathId()
	 */
	@Override
	public String getCutPathId() {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncDtoConfig#getCutLinkId()
	 */
	@Override
	public String getCutLinkId() {
		return configProvider.getProperty(PROP_KEY_SDTNC_DTO_OTHER_CUT_LINK_ID);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncDtoConfig#getHopLinkId()
	 */
	@Override
	public String getHopLinkId() {
		return configProvider.getProperty(PROP_KEY_SDTNC_DTO_OTHER_HOP_LINK_ID);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncDtoConfig#getNeIdA()
	 */
	@Override
	public String getNeIdA() {
		return configProvider.getProperty(PROP_KEY_SDTNC_DTO_OTHER_NE_ID_A);
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncDtoConfig#getNeIdZ()
	 */
	@Override
	public String getNeIdZ() {
		return configProvider.getProperty(PROP_KEY_SDTNC_DTO_OTHER_NE_ID_Z);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncDtoConfig#getHopPortIdA()
	 */
	@Override
	public String getHopPortIdA() {
		return configProvider.getProperty(PROP_KEY_SDTNC_DTO_OTHER_HOP_PORT_ID_A);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncDtoConfig#getHopPortIdZ()
	 */
	@Override
	public String getHopPortIdZ() {
		return configProvider.getProperty(PROP_KEY_SDTNC_DTO_OTHER_HOP_PORT_ID_Z);
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncDtoConfig#getCutPortIdA()
	 */
	@Override
	public String getCutPortIdA() {
		return configProvider.getProperty(PROP_KEY_SDTNC_DTO_OTHER_CUT_PORT_ID_A);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncDtoConfig#getCutPortIdZ()
	 */
	@Override
	public String getCutPortIdZ() {
		return configProvider.getProperty(PROP_KEY_SDTNC_DTO_OTHER_CUT_PORT_ID_Z);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncDtoConfig#getPirRate()
	 */
	@Override
	public String getPirRate() {
		return configProvider.getProperty(PROP_KEY_SDTNC_DTO_OTHER_PIR_RATE);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncDtoConfig#getSlaMode()
	 */
	@Override
	public String getSlaMode() {
		return configProvider.getProperty(PROP_KEY_SDTNC_DTO_OTHER_SLA_MODE);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncDtoConfig#isPutVPathWorkaroundMode()
	 */
	@Override
	public Boolean isPutVPathWorkaroundMode() {
		return configProvider.getBooleanProperty(PROP_KEY_SDTNC_DTO_OTHER_PUT_VPATH_WORKAROUND_MODE);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncDtoConfig#getVlanProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public String getVlanProperty(String vlanId, String subKey) throws MloException {
		String val = null;
		Map<String, String> subPropMap = configProvider.getSubProperties(PROP_KEY_SDTNC_DTO_OTHER_VLAN_PREFIX_ + vlanId + ".");
		if (subPropMap != null) {
			val = subPropMap.get(subKey);
		}
		if (val == null) {
			val = portIdMap.get(vlanId + DELIMITER + subKey);
		}
		
		if (val == null) {
			throw new ApiCallException(String.format(
					"Unexpected situation. Failed to get a vlan property of subKey %s for %s.",
					subKey, vlanId));
		}
		return val;
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncDtoConfig#getExtraFilePath()
	 */
	@Override
	public String getVlanExtraMappingFilePath() {
		return configProvider.getProperty(PROP_KEY_SDTNC_DTO_OTHER_VLAN_EXTRA_MAPPING_FILE_PATH);
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncDtoConfig#canCreateExtraFlows()
	 */
	@Override
	public Boolean canCreateExtraFlows() {
		throw new UnsupportedOperationException();
	}
}
