/**
 * VrmAction.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.psdtnc.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXB;

import org.o3project.mlo.psdtnc.dto.SdtncVrmifLoginDto;
import org.o3project.mlo.psdtnc.dto.SdtncVrmifVlinkListDto;
import org.o3project.mlo.psdtnc.dto.SdtncVrmifVpathDto;
import org.o3project.mlo.psdtnc.dto.SdtncVrmifVpathListDto;
import org.o3project.mlo.psdtnc.form.SdtncVrmifForm;
import org.o3project.mlo.psdtnc.logic.PseudoSdtncVrmService;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * VrmAction
 *
 */
public class VrmAction {
	
	@Resource 
	private HttpServletRequest request;
	
	@Resource 
	private HttpServletResponse response;

	@Resource
	@ActionForm
	protected SdtncVrmifForm sdtncVrmifForm;
	
	PseudoSdtncVrmService pseudoSdtncVrmService;
	
	/**
	 * @param pseudoSdtncVrmService the pseudoSdtncVrmService to set
	 */
	public void setPseudoSdtncVrmService(PseudoSdtncVrmService pseudoSdtncVrmService) {
		this.pseudoSdtncVrmService = pseudoSdtncVrmService;
	}
	
	@Execute(validator = false)
	public String login() throws IOException {
		if (ActionUtil.isSupportingHttpMethod(request.getMethod(), "POST")) {
			postLoginLocal(request.getInputStream(), response.getOutputStream());
		} else {
			ActionUtil.handleNotImplementedMethod(response);
		}
		return null;
	}
	
	@Execute(validator = false)
	public String logout() throws IOException {
		if (ActionUtil.isSupportingHttpMethod(request.getMethod(), "POST")) {
			postLogoutLocal(request.getInputStream(), response.getOutputStream());
		} else {
			ActionUtil.handleNotImplementedMethod(response);
		}
		return null;
	}
	
	@Execute(validator = false)
	public String path() throws IOException {
		if (ActionUtil.isSupportingHttpMethod(request.getMethod(), "POST")) {
			postPathLocal(request.getInputStream(), response.getOutputStream());
		} else if (ActionUtil.isSupportingHttpMethod(request.getMethod(), "DELETE")) {
			deletePathLocal(sdtncVrmifForm, response.getOutputStream());
		} else if (ActionUtil.isSupportingHttpMethod(request.getMethod(), "GET")) {
			getPathLocal(sdtncVrmifForm, response.getOutputStream());
		} else {
			ActionUtil.handleNotImplementedMethod(response);
		}
		return null;
	}

	@Execute(validator = false)
	public String link() throws IOException {
		if (ActionUtil.isSupportingHttpMethod(request.getMethod(), "GET")) {
			getLinkLocal(sdtncVrmifForm, response.getOutputStream());
		} else {
			ActionUtil.handleNotImplementedMethod(response);
		}
		return null;
	}
	
	void postLoginLocal(InputStream inputStream, OutputStream outputStream) {
		SdtncVrmifLoginDto reqDto = deserializeFrom(inputStream, SdtncVrmifLoginDto.class);
		SdtncVrmifLoginDto resDto = pseudoSdtncVrmService.doPostLogin(reqDto);
		serializeTo(outputStream, resDto);
	}

	void postLogoutLocal(InputStream inputStream, OutputStream outputStream) {
		SdtncVrmifLoginDto reqDto = deserializeFrom(inputStream, SdtncVrmifLoginDto.class);
		SdtncVrmifLoginDto resDto = pseudoSdtncVrmService.doPostLogout(reqDto);
		serializeTo(outputStream, resDto);
	}

	void postPathLocal(InputStream inputStream, OutputStream outputStream) {
		SdtncVrmifVpathDto reqDto = deserializeFrom(inputStream, SdtncVrmifVpathDto.class);
		SdtncVrmifVpathDto resDto = pseudoSdtncVrmService.doPostPath(reqDto);
		serializeTo(outputStream, resDto);
	}

	void deletePathLocal(SdtncVrmifForm params, OutputStream outputStream) {
		SdtncVrmifVpathDto resDto = pseudoSdtncVrmService.doDeletePath(params);
		serializeTo(outputStream, resDto);
	}

	void getPathLocal(SdtncVrmifForm params, OutputStream outputStream) {
		SdtncVrmifVpathListDto resDto = pseudoSdtncVrmService.doGetPath(params);
		serializeTo(outputStream, resDto);
	}

	void getLinkLocal(SdtncVrmifForm params, OutputStream outputStream) {
		SdtncVrmifVlinkListDto resDto = pseudoSdtncVrmService.doGetLink(params);
		serializeTo(outputStream, resDto);
	}

	<DTO> DTO deserializeFrom(InputStream inputStream, Class<DTO> dtoClass) {
		return JAXB.unmarshal(inputStream, dtoClass);
	}
	
	<DTO> void serializeTo(OutputStream outputStream, DTO dto) {
		JAXB.marshal(dto, outputStream);
	}
}
