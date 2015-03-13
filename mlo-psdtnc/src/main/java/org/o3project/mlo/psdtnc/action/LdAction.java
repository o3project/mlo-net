/**
 * LdAction.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.psdtnc.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.o3project.mlo.psdtnc.impl.logic.LdOperationServiceDummyLocalImpl;
import org.o3project.mlo.psdtnc.logic.ConfigConstants;
import org.o3project.mlo.psdtnc.logic.LdOperationException;
import org.o3project.mlo.psdtnc.logic.LdOperationService;
import org.o3project.mlo.server.logic.ConfigProvider;
import org.seasar.struts.annotation.Execute;

/**
 * LdAction
 *
 */
public class LdAction implements ConfigConstants {
	private static final int BUFFER_LEN = 16;
	
	@Resource 
	private HttpServletRequest request;
	
	@Resource 
	private HttpServletResponse response;
	
	private ConfigProvider configProvider;
	
	private LdOperationService ldOperationService;
	
	private LdOperationServiceDummyLocalImpl ldOperationServiceDummyLocalImpl;

	/**
	 * @param configProvider the configProvider to set
	 */
	public void setConfigProvider(ConfigProvider configProvider) {
		this.configProvider = configProvider;
	}
	
	/**
	 * @param ldOperationService the ldOperationService to set
	 */
	public void setLdOperationService(LdOperationService ldOperationService) {
		this.ldOperationService = ldOperationService;
	}
	
	/**
	 * @param ldOperationServiceDummyLocalImpl the ldOperationServiceDummyLocalImpl to set
	 */
	public void setLdOperationServiceDummyLocalImpl(LdOperationServiceDummyLocalImpl ldOperationServiceDummyLocalImpl) {
		this.ldOperationServiceDummyLocalImpl = ldOperationServiceDummyLocalImpl;
	}
	
	
	private LdOperationService getAvailableLdOperationService() {
		boolean isDebugLocal = configProvider.getBooleanProperty(PROP_KEY_LD_DEBUG_ENABLE_LOCAL_SAMPLE);
		if (!isDebugLocal) {
			return ldOperationService;
		} else {
			return ldOperationServiceDummyLocalImpl;
		}
	}
	
	@Execute(validator = false)
	public String status() throws IOException, LdOperationException {
		if (ActionUtil.isSupportingHttpMethod(request.getMethod(), "PUT")) {
			Integer reqStatus = readStatus(request.getInputStream());
			if (reqStatus == null) {
				throw new IllegalStateException("Unexpected situation. reqStatus == null.");
			} else if (reqStatus == 0) {
				getAvailableLdOperationService().doStart();
				Integer status = getAvailableLdOperationService().doStatus();
				writeStatus(status, response.getOutputStream());
			} else if (reqStatus == 1) {
				getAvailableLdOperationService().doStop();
				Integer status = getAvailableLdOperationService().doStatus();
				writeStatus(status, response.getOutputStream());
			} else {
				throw new UnsupportedOperationException("Unsupported status. status = " + reqStatus);
			}
		} else if (ActionUtil.isSupportingHttpMethod(request.getMethod(), "GET")) {
			Integer status = getAvailableLdOperationService().doStatus();
			writeStatus(status, response.getOutputStream());
		} else {
			ActionUtil.handleNotImplementedMethod(response);
		}
		return null;
	}
	
	@Execute(validator = false, urlPattern = "ssh/channelexec/action")
	public String executeCommand() throws IOException, LdOperationException {
		if (ActionUtil.isSupportingHttpMethod(request.getMethod(), "POST")) {
			String shellCommand = readText(request.getInputStream());
			if (shellCommand == null) {
				throw new IllegalStateException("Unexpected situation. cmdText == null.");
			} else {
				Integer exitStatus = getAvailableLdOperationService().doExecuteSingleCommand(shellCommand);
				writeStatus(exitStatus, response.getOutputStream());
			}
		} else {
			ActionUtil.handleNotImplementedMethod(response);
		}
		return null;
	}
	
	Integer readStatus(InputStream inputStream) throws IOException {
		return Integer.parseInt(readText(inputStream));
	}
	
	String readText(InputStream inputStream) throws IOException {
		StringBuilder sb = new StringBuilder();
		byte[] buf = new byte[BUFFER_LEN];
		int rlen = -1;
		while (!Thread.currentThread().isInterrupted()) {
			rlen = inputStream.read(buf, 0, buf.length);
			if (rlen < 0) {
				break;
			}
			sb.append(new String(buf, 0, rlen, "UTF-8"));
		}
		return sb.toString();
	}
	
	void writeStatus(Integer status, OutputStream outputStream) throws IOException {
		String sStatus = status.toString();
		byte[] buf = sStatus.getBytes("UTF-8");
		outputStream.write(buf);
	}
}
