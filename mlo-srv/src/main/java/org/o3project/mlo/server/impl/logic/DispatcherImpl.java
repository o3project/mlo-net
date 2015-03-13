/**
 * DispatcherImpl.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.logic.Dispatcher;
import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.SliceOperationTask;


/**
 * This class is the implementation class of {@link Dispatcher} interface.
 *
 */
public class DispatcherImpl implements Dispatcher {
	
	private static final Log LOG = LogFactory.getLog(DispatcherImpl.class);
	
	/**
	 *  Mutex object.
	 */
	private final Object oMutex = new Object();

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.Dispatcher#init()
	 */
	@Override
	public void init() throws MloException {
		// nothing to do.
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.Dispatcher#dispose()
	 */
	@Override
	public void dispose() throws MloException {
		// nothing to do. 
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.Dispatcher#dispatch(org.o3project.mlo.server.logic.Command, org.o3project.mlo.server.dto.RestifRequestDto)
	 */
	@Override
	public RestifResponseDto dispatch(SliceOperationTask sliceOpTask, RestifRequestDto reqDto)
			throws MloException {
		RestifResponseDto resDto = null;
		
		sliceOpTask.setRequestDto(reqDto);
		
		try {
			synchronized (oMutex) {
				resDto = sliceOpTask.call();
			}
		} catch (Throwable t) {
			handleThrowable(t);
		}
		
		return resDto;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.Dispatcher#dispatch(org.o3project.mlo.server.logic.SliceOperationTask, java.util.Map)
	 */
	@Override
	public RestifResponseDto dispatch(SliceOperationTask sliceOpTask,
			Map<String, String> paramMap) throws MloException {
		RestifResponseDto resDto = null;
		
		sliceOpTask.setRequestParamMap(paramMap);
		
		try {
			synchronized (oMutex) {
				resDto = sliceOpTask.call();
			}
		} catch (Throwable t) {
			handleThrowable(t);
		}
		
		return resDto;
	}

	/**
	 * Handles exceptions.
	 */
	private void handleThrowable(Throwable t) throws MloException {
		if (t instanceof MloException) {
			// NBI error, e.g. invalid parameter.
			if (LOG.isWarnEnabled()) {
				LOG.warn(t.getMessage(), t);
			}
			throw (MloException) t;
		} else if (t instanceof Exception) {
			// Probably bug of MLO here.
			if (LOG.isWarnEnabled()) {
				LOG.warn(t.getMessage(), t);
			}
			throw new InternalException(t.getMessage(), t);
		} else {
			// In the case of Throwable.
			// Fatal error, e.g. no memory, no storage and so on.
			if (LOG.isFatalEnabled()) {
				LOG.fatal(t.getMessage(), t);
			}
			throw new InternalException(t.getMessage(), t);
		}
	}
}
