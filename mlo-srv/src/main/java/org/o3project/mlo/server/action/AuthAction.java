/**
 * AuthAction.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.action;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.o3project.mlo.server.form.AuthForm;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * AuthAction
 */
public class AuthAction {
	
	@Resource
	protected HttpSession session;
	
	@Resource
	protected HttpServletRequest request;
	
	@Resource
	protected HttpServletResponse response;

	@Resource
	@ActionForm
	protected AuthForm authForm;
	
	@Execute(validator = false)
	public String logout() throws IOException {
		String redirectTo = "/";
		if (session != null) {
			session.invalidate();
			
			if (authForm != null && authForm.at != null) {
				redirectTo = authForm.at;
			}
		}
		response.sendRedirect(redirectTo);
		return null;
	}

}
