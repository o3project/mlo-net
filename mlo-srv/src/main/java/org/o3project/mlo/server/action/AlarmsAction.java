/**
 * AlarmsAction.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.action;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.dto.AlarmDto;
import org.o3project.mlo.server.form.AlarmsForm;
import org.o3project.mlo.server.logic.AlarmProcessor;
import org.o3project.mlo.server.logic.MloException;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * AlarmsAction
 */
public class AlarmsAction {
	
	private static final Log LOG = LogFactory.getLog(AlarmsAction.class);
	
	@Resource 
	private HttpServletRequest request;
	
	@Resource 
	private HttpServletResponse response;
	
	@Resource
	@ActionForm
	protected AlarmsForm alarmsForm;
	
	AlarmProcessor alarmProcessor;
	
	/**
	 * @param alarmProcessor the alarmProcessor to set
	 */
	public void setAlarmProcessor(AlarmProcessor alarmProcessor) {
		this.alarmProcessor = alarmProcessor;
	}
	
	@Execute(validator = false)
	public String index() throws IOException, MloException {
    	logAccess(request);
		String jsp = null;
    	if (ActionUtil.isSupportingHttpMethod(request.getMethod(), "GET")) {
    		List<AlarmDto> processedAlarmDtos = alarmProcessor.getAlarms();
    		JSON.encode(processedAlarmDtos, response.getOutputStream());
    	} else if (ActionUtil.isSupportingHttpMethod(request.getMethod(), "POST")) {
    		AlarmDto[] alarmDtos = JSON.decode(request.getInputStream(), AlarmDto[].class);
    		List<AlarmDto> processedAlarmDtos = alarmProcessor.postAlarms(Arrays.asList(alarmDtos));
    		JSON.encode(processedAlarmDtos, response.getOutputStream());
    	} else {
    		throw new RuntimeException("Unsupported HTTP method.");
    	}
		return jsp;
	}
	@Execute(validator = false, urlPattern = "{alarmId}")
	public String id() throws IOException, MloException {
    	logAccess(request);
		String jsp = null;
    	if (ActionUtil.isSupportingHttpMethod(request.getMethod(), "DELETE")) {
    		AlarmDto processedAlarmDto = alarmProcessor.deleteAlarm(alarmsForm.alarmId);
    		JSON.encode(processedAlarmDto, response.getOutputStream());
    	} else {
    		throw new RuntimeException("Unsupported HTTP method.");
    	}
    	return jsp;
	}
    
    /**
     * Logs http access.
     * @param req HTTP requests.P
     */
    static void logAccess(HttpServletRequest req) {
    	if (LOG.isInfoEnabled()) {
    		LOG.info(String.format("%s %s %s", 
    				req.getMethod(), req.getRequestURI(), req.getQueryString()));
    	}
    }
}
