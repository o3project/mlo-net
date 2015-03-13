/**
 * SdtncServiceImpl.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.xml.bind.JAXB;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.logic.ConfigConstants;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;
import org.o3project.mlo.server.rpc.dto.SdtncVlinkDto;
import org.o3project.mlo.server.rpc.dto.SdtncVpathDto;
import org.o3project.mlo.server.rpc.service.SdtncConfig;
import org.o3project.mlo.server.rpc.service.SdtncInvoker;
import org.o3project.mlo.server.rpc.service.SdtncMethod;
import org.o3project.mlo.server.rpc.service.SdtncService;
import org.seasar.framework.container.annotation.tiger.Aspect;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.InitMethod;

/**
 * This class is the implementation class to communicate with SDTNC.
 */
@Aspect("traceInterceptor")
public class SdtncServiceImpl implements ConfigConstants, SdtncService {

    private static final Log LOG = LogFactory.getLog(SdtncServiceImpl.class);

    /**
     * Login XML path.
     */
    private static final String PATH_LOGIN = "login";

    /**
     * Logout XML path.
     */
    private static final String PATH_LOGOUT = "logout";

    /**
     * VLink XML path.
     */
    private static final String PATH_LINK = "link";

    /**
     * Vpath XML path (GET, POST, DELETE)
     */
    private static final String PATH_PATH = "path";

    @Binding
    private SdtncInvoker trueInvoker;
    
    @Binding
    private SdtncInvoker dummyInvoker;
    
    @Binding(bindingType=BindingType.NONE)
    private SdtncInvoker sdtncInvoker;

    @Binding
    private SdtncMethod sdtncGetMethod;

    @Binding
    private SdtncMethod sdtncPostMethod;

    @Binding
    private SdtncMethod sdtncDeleteMethod;

    @Binding
    private SdtncConfig sdtncConfig;

    /**
     * Initializes this instance.
     */
    @InitMethod
    public void init(){
        if(sdtncConfig.getDummyInvokerSetFlag()){
            this.sdtncInvoker = dummyInvoker;
            LOG.info("\n dummyInvoker set");
        }else{
            this.sdtncInvoker = trueInvoker;
            LOG.info("\n trueInvoker set");
        }
    }

    /**
     * Setter method (for DI setter injection).
     * @param sdtncConfig the sdtncConfig to set
     */
    public void setSdtncConfig(SdtncConfig sdtncConfig) {
        this.sdtncConfig = sdtncConfig;
    }

    /**
     * Setter method (for DI setter injection).
     * @param sdtncInvoker The instance. 
     */
    public void setSdtncInvoker(SdtncInvoker sdtncInvoker) {
        this.sdtncInvoker = sdtncInvoker;
    }

    /**
     * Setter method (for DI setter injection).
     * @param sdtncGetMethod The instance. 
     */
    public void setSdtncGetMethod(SdtncMethod sdtncGetMethod) {
        this.sdtncGetMethod = sdtncGetMethod;
    }

    /**
     * Setter method (for DI setter injection).
     * @param sdtncPostMethod The instance.
     */
    public void setSdtncPostMethod(SdtncMethod sdtncPostMethod) {
        this.sdtncPostMethod = sdtncPostMethod;
    }

    /**
     * Setter method (for DI setter injection).
     * @param sdtncDeleteMethod The instance.
     */
    public void setSdtncDeleteMethod(SdtncMethod sdtncDeleteMethod) {
        this.sdtncDeleteMethod = sdtncDeleteMethod;
    }

    /*
     * (non-Javadoc)
     * @see org.o3project.mlo.server.rpc.service.SDTNCService#login(org.o3project.mlo.server.rpc.dto.SdtncRequestDto)
     */
    @Override
    public SdtncResponseDto login(SdtncRequestDto reqDto) throws MloException {
        logSdtncReq(reqDto);
        Map<String, String> params = null;
        SdtncResponseDto resDto = sdtncInvoker.invoke(sdtncPostMethod, reqDto, PATH_LOGIN, params);
        logSdtncRes(resDto);
        SdtncValidator.validateLogin(resDto);
        LOG.info("\n" + "[RESULT_OF_SDTNC] : METHOD = login : LoginID = " + resDto.login.loginId + " : errorDetail = " + resDto.head.errorDetail);
        return resDto;
    }

    /*
     * (non-Javadoc)
     * @see org.o3project.mlo.server.rpc.service.SDTNCService#logout(org.o3project.mlo.server.rpc.dto.SdtncRequestDto)
     */
    @Override
    public SdtncResponseDto logout(SdtncRequestDto reqDto) throws MloException {
        // Invalidates access token.
        logSdtncReq(reqDto);
        Map<String, String> params = null;
        SdtncResponseDto resDto = sdtncInvoker.invoke(sdtncPostMethod, reqDto, PATH_LOGOUT, params);
        logSdtncRes(resDto);
        SdtncValidator.validateLogout(resDto);
        LOG.info("\n" + "[RESULT_OF_SDTNC] : METHOD = logout : LoginID = " + resDto.login.loginId + " : errorDetail = " + resDto.head.errorDetail);
        return resDto;
    }

    /*
     * (non-Javadoc)
     * @see org.o3project.mlo.server.rpc.service.SDTNCService#getLspResource(java.util.Map)
     */
    @Override
    public SdtncResponseDto getLspResource(
            Map<String, String> paramMap) throws MloException {
        reqParameterLog(paramMap);
        SdtncResponseDto resDto = sdtncInvoker.invoke(sdtncGetMethod, null, PATH_LINK, paramMap);
        logSdtncRes(resDto);
        SdtncValidator.validateGetLspResource(resDto);
        for(SdtncVlinkDto vlinkDto : resDto.vlink){
            LOG.info("\n" + "[RESULT_OF_SDTNC] : METHOD = getLspResource : vlinkIdInt = " + vlinkDto.resourceIndex);
        }
        return resDto;
    }

    /*
     * (non-Javadoc)
     * @see org.o3project.mlo.server.rpc.service.SDTNCService#createPw(org.o3project.mlo.server.rpc.dto.SdtncRequestDto)
     */
    @Override
    public SdtncResponseDto createPw(SdtncRequestDto reqDto) throws MloException {
        logSdtncReq(reqDto);
        Map<String, String> params = null;
        SdtncResponseDto resDto = sdtncInvoker.invoke(sdtncPostMethod, reqDto, PATH_PATH, params);
        logSdtncRes(resDto);
        SdtncValidator.validateCreatePw(resDto);
        for(SdtncVpathDto vpathDto : resDto.vpath){
            LOG.info("\n" + "[RESULT_OF_SDTNC] : METHOD = createPw : pathidStr = " + vpathDto.vObjectIndex);
        }
        return resDto;
    }

    /*
     * (non-Javadoc)
     * @see org.o3project.mlo.server.rpc.service.SDTNCService#deletePw(org.o3project.mlo.server.rpc.dto.SdtncRequestDto)
     */
    @Override
    public SdtncResponseDto deletePw(Map<String, String> paramMap) throws MloException {
        reqParameterLog(paramMap);
        SdtncResponseDto resDto = sdtncInvoker.invoke(sdtncDeleteMethod, null, PATH_PATH, paramMap);
        logSdtncRes(resDto);
        SdtncValidator.validateDeletePw(resDto);
        for(SdtncVpathDto vpathDto : resDto.vpath){
            LOG.info("\n" + "[RESULT_OF_SDTNC] : METHOD = deletePw : pathidStr = " + vpathDto.vObjectIndex);
        }
        return resDto;
    }

    /*
     * (non-Javadoc)
     * @see org.o3project.mlo.server.rpc.service.SdtncService#getPw(java.util.Map)
     */
    @Override
    public SdtncResponseDto getPw(Map<String, String> paramMap) throws MloException {
        reqParameterLog(paramMap);
        SdtncResponseDto resDto = sdtncInvoker.invoke(sdtncGetMethod, null, PATH_PATH, paramMap);
        logSdtncRes(resDto);
        SdtncValidator.validateGetPw(resDto);
        for(SdtncVpathDto vpathDto : resDto.vpath){
            LOG.info("\n" + "[RESULT_OF_SDTNC] : METHOD = getPw : pathidStr = " + vpathDto.vObjectIndex);
        }
        return resDto;
    }

    private void reqParameterLog(Map<String, String> paramMap) {
        String logStr = "";
        for (String key : paramMap.keySet()) {
            logStr = logStr + key + ":" + paramMap.get(key) + "/";
        }
        LOG.info("\n" + "[REQUEST_PARAMETER_TO_SDTNC] : " + "PROCESS = " + Thread.currentThread().getStackTrace()[2].getMethodName() + "\n" + logStr);
    }

    private void logSdtncReq(SdtncRequestDto reqDto) {
        try {
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            JAXB.marshal(reqDto, ostream);
            String reqXml = new String(ostream.toByteArray(), "UTF-8");
            LOG.info("\n" + "[REQUEST_TO_SDTNC] : " + "PROCESS = " + Thread.currentThread().getStackTrace()[2].getMethodName() + "\n" + "<< SdtncRequestDto >>" + "\n" + reqXml);
        } catch (UnsupportedEncodingException e) {
            LOG.info(e);
        }
    }

    private void logSdtncRes(SdtncResponseDto resDto) {
        try {
            ByteArrayOutputStream ostream = null;
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            if (LOG.isDebugEnabled()) {
                ostream = new ByteArrayOutputStream();
                JAXB.marshal(resDto, ostream);
                String xml = new String(ostream.toByteArray(), "UTF-8");
                LOG.debug("\n" + "[RESPONSE_FROM_SDTNC] : " + "PROCESS = " + methodName + "\n" + "<< SdtncResponseDto >>" + "\n" + xml);
            } else if (LOG.isInfoEnabled()) {
                ostream = new ByteArrayOutputStream();
                JAXB.marshal(resDto.head, ostream);
                String xml = new String(ostream.toByteArray(), "UTF-8");
                LOG.info("\n" + "[RESPONSE_HEAD_FROM_SDTNC] : " + "PROCESS = " + methodName + "\n" + "<< SdtncResponseDto.head >>" + "\n" + xml);
            }
        } catch (UnsupportedEncodingException e) {
            LOG.warn(e);
        }
    }
}

