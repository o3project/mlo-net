/**
 * DebugRestifChecker.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.logic.ApiCallException;
import org.o3project.mlo.server.logic.ConfigConstants;
import org.o3project.mlo.server.logic.ConfigProvider;
import org.o3project.mlo.server.logic.DbAccessException;
import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.OtherException;
import org.o3project.mlo.server.logic.TimeOutException;
import org.seasar.framework.container.annotation.tiger.Binding;

/**
 * This is the class to occur errors of MLO API.
 * This class is only for debug.
 * Real code (not debug code) should be written in this class.
 */
public class DebugRestifChecker implements ConfigConstants {
	
	private static final Log LOG = LogFactory.getLog(DebugRestifChecker.class);
	
	@Binding
	private ConfigProvider configProvider;
	
	private final Set<String> beforeOpeToggledSliceNames = new HashSet<String>();
	
	private final Set<String> afterOpeToggledSliceNames = new HashSet<String>();
	
	private final Map<String, Class<?>> errorMap = new HashMap<String, Class<?>>();
	
	/**
	 * A constructor. 
	 */
	public DebugRestifChecker() {
		errorMap.put("APICallError",  ApiCallException.class);
		errorMap.put("DBAccessError", DbAccessException.class);
		errorMap.put("InternalError", InternalException.class);
		errorMap.put("TimeOutError",  TimeOutException.class);
		errorMap.put("OtherError",    OtherException.class);
	}
	
	/**
	 * {@link ConfigProvider} setter method (for DI setter injection)。
	 * @param configProvider The instance. 
	 */
	public void setConfigProvider(ConfigProvider configProvider) {
		this.configProvider = configProvider;
	}
	
	/**
	 * Occurs errors before slice operation.
	 * @param sliceName The target slice name. 
	 * @param opeName Operation name.
	 * @throws MloException Dummy exception for debug.
	 */
	void checkBefore(String sliceName, String opeName) throws MloException {
		if (!isDebugRestifEnable()) {
			return; // do nothing.
		}
		
		checkSimpleDummyError(sliceName, opeName);
	
		// Toggle exception generator.
		// Exception occurs if slice name is as follows:
		// BeforeCreateToggledInternalError (32bytes!)
		checkToggledDummyError(sliceName, opeName, "Before", beforeOpeToggledSliceNames);
	}
	
	/**
	 * Occurs errors after slice operation.
	 * @param sliceName The target slice name. 
	 * @param opeName The operation name.
	 * @throws MloException Dummy exception for debug.
	 */
	void checkAfter(String sliceName, String opeName) throws MloException {
		if (!isDebugRestifEnable()) {
			return; // do nothing.
		}
		
		// Toggle exception generator.
		// Exception occurs if slice name is as follows:
		// AfterCreateToggledInternalError (31 bytes)
		checkToggledDummyError(sliceName, opeName, "After", afterOpeToggledSliceNames);
	}

	/**
	 */
	private void checkSimpleDummyError(String sliceName, String opeName) throws MloException {
		String msg = "BadRequest/DebugDummy/" + sliceName;
		for (String errName : errorMap.keySet()) {
			if ((opeName + errName).equalsIgnoreCase(sliceName)) {
				Class<?> type = errorMap.get(errName);
				throwException(type, msg);
			}
		}
	}

	/**
	 */
	private void checkToggledDummyError(String sliceName, String opeName, String prefix, Set<String> sliceNameSet) throws MloException {
		String msg = "BadRequest/ToggledDebugDummy/" + (prefix + opeName) + "/" + sliceName;
		for (String errName : errorMap.keySet()) {
			if ((prefix + opeName + "Toggled" + errName).equalsIgnoreCase(sliceName)) {
				if (sliceNameSet.contains(sliceName)) {
					sliceNameSet.remove(sliceName);
				} else {
					sliceNameSet.add(sliceName);
					throwException(errorMap.get(errName), msg);
				}
			}
		}
	}

	/**
	 */
	private void throwException(Class<?> type, String msg) throws MloException {
		try {
			Constructor<?> constructor = type.getConstructor(String.class);
			throw (MloException) constructor.newInstance(msg);
		} catch (MloException e) {
			throw e;
		} catch (Exception e) {
			LOG.error("create Exception failed", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 */
	private boolean isDebugRestifEnable() {
		boolean isEnable = false;
		if (configProvider == null) {
			isEnable = false;
		} else {
			isEnable = configProvider.getBooleanProperty(PROP_KEY_DEBUG_RESTIF_ENABLE);
		}
		return isEnable;
	}
}
