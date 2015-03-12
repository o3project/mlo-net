/**
 * IndexAction.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.action;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.logic.Orchestrator;
import org.o3project.mlo.server.logic.Serdes;
import org.o3project.mlo.server.logic.SliceOperationTask;
import org.o3project.mlo.server.logic.Validator;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.struts.annotation.Execute;

/**
 * This is the MLO API action class.
 * This class is a facade action class to receive MLO API requests.
 */
public class IndexAction {
	
	private static final Log LOG = LogFactory.getLog(IndexAction.class);
	
	@Resource 
	private HttpServletRequest request;
	
	@Resource 
	private HttpServletResponse response;

	@Binding
	private Serdes serdes;
	
	@Binding
	private Orchestrator orchestrator;
	
	@Binding
	private SliceOperationTask createSliceTask;

	@Binding
	private SliceOperationTask updateSliceTask;

	@Binding
	private SliceOperationTask deleteSliceTask;

	@Binding
	private SliceOperationTask readSliceTask;

	@Binding
	private Validator createSliceValidator;

	@Binding
	private Validator updateSliceValidator;

	@Binding
	private Validator deleteSliceValidator;

	@Binding
	private Validator readSliceValidator;
	
	/**
	 * Dummy method.
	 * This method is not used at all.
	 * @return JSP name.
	 */
	@Execute(validator = false)
	public String index() {
        return "index.jsp";
	}

    /**
     * Handles CREATE actions of MLO API.
     * This method is called when accessed to URI {@code http://<host>:<port>/<root-path>/CREATE}.
     * This method only supports POST method, 
     * processes user requests and then returns results to HttpServletResponse.
     * @return Always returns null.
     * @throws IOException Thrown if error occurs.
     */
    @Execute(validator = false, urlPattern = "CREATE")
    public String create() throws IOException {
    	logAccess(request);
    	String jsp = null;
    	if (ActionUtil.isSupportingHttpMethod(request.getMethod(), "POST")) {
    		jsp = ActionUtil.doAction(orchestrator, serdes,
    				createSliceValidator, createSliceTask, 
    				request.getInputStream(), response.getOutputStream());
    	} else {
    		throw new RuntimeException("Unsupported HTTP method.");
    	}
    	return jsp;
    }

    /**
     * Handles UPDATE actions of MLO API.
     * This method is called when accessed to URI {@code http://<host>:<port>/<root-path>/UPDATE}.
     * This method only supports POST method, 
     * processes user requests and then returns results to HttpServletResponse.
     * @return Always returns null.
     * @throws IOException Thrown if error occurs.
     * 
     */
    @Execute(validator = false, urlPattern = "UPDATE")
    public String update() throws IOException {
    	logAccess(request);
    	String jsp = null;
    	if (ActionUtil.isSupportingHttpMethod(request.getMethod(), "POST")) {
    		jsp = ActionUtil.doAction(orchestrator, serdes,
    				updateSliceValidator, updateSliceTask, 
    				request.getInputStream(), response.getOutputStream());
    	} else {
    		throw new RuntimeException("Unsupported HTTP method.");
    	}
    	return jsp;
    }

    /**
     * Handles DELETE actions of MLO API.
     * This method is called when accessed to URI {@code http://<host>:<port>/<root-path>/DELETE}.
     * This method only supports POST method, 
     * processes user requests and then returns results to HttpServletResponse.
     * @return Always returns null.
     * @throws IOException Thrown if error occurs.
     * 
     */
    @Execute(validator = false, urlPattern = "DELETE")
    public String delete() throws IOException {
    	logAccess(request);
    	String jsp = null;
    	if (ActionUtil.isSupportingHttpMethod(request.getMethod(), "POST")) {
    		jsp = ActionUtil.doAction(orchestrator, serdes,
    				deleteSliceValidator, deleteSliceTask, 
    				request.getInputStream(), response.getOutputStream());
    	} else {
    		throw new RuntimeException("Unsupported HTTP method.");
    	}
    	return jsp;
    }

    /**
     * Handles READ actions of MLO API.
     * This method is called when accessed to URI {@code http://<host>:<port>/<root-path>/READ}.
     * This method only supports POST method, 
     * processes user requests and then returns results to HttpServletResponse.
     * @return Always returns null.
     * @throws IOException Thrown if error occurs.
     * 
     */
    @Execute(validator = false, urlPattern = "READ")
    public String read() throws IOException {
    	logAccess(request);
    	String jsp = null;
    	if (ActionUtil.isSupportingHttpMethod(request.getMethod(), "POST")) {
    		jsp = ActionUtil.doAction(orchestrator, serdes, 
    				readSliceValidator, readSliceTask, 
    				request.getInputStream(), response.getOutputStream());
    	} else {
    		throw new RuntimeException("Unsupported HTTP method.");
    	}
    	return jsp;
    }
    
    /**
     * Logs user accesses.
     * @param req HTTP request instance. 
     */
    static void logAccess(HttpServletRequest req) {
    	if (LOG.isInfoEnabled()) {
    		LOG.info(String.format("%s %s %s", 
    				req.getMethod(), req.getRequestURI(), req.getQueryString()));
    	}
    }
}
