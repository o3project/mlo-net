/**
 * SdtncBaseMethod.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;
import org.o3project.mlo.server.rpc.service.SdtncConfig;
import org.o3project.mlo.server.rpc.service.SdtncMethod;
import org.o3project.mlo.server.rpc.service.SdtncSerdes;
import org.seasar.framework.container.annotation.tiger.Binding;

/**
 * SdtncBaseMethod
 *
 */
public abstract class SdtncBaseMethod implements SdtncMethod {

    @Binding
    private SdtncSerdes sdtncSerdes;

    @Binding
    private SdtncConfig sdtncConfig;

    /**
     * Setter method (for DI setter injection).
     * @param sdtncConfig the sdtncConfig to set
     */
    public void setSdtncConfig(SdtncConfig sdtncConfig) {
        this.sdtncConfig = sdtncConfig;
    }

    /**
     * Setter method (for DI setter injection).
     * @param sdtncSerdes the sdtncSerdes to set
     */
    public void setSdtncSerdes(SdtncSerdes sdtncSerdes) {
        this.sdtncSerdes = sdtncSerdes;
    }

    /* (non-Javadoc)
     * @see org.o3project.mlo.server.rpc.service.SdtncMethod#handleReqOutput(org.o3project.mlo.server.rpc.dto.SdtncRequestDto, java.io.OutputStream)
     */
    @Override
    public void handleReqOutput(SdtncRequestDto reqDto, OutputStream ostream) {
        sdtncSerdes.serializeToXml(reqDto, ostream);
    }

    /* (non-Javadoc)
     * @see org.o3project.mlo.server.rpc.service.SdtncMethod#handleResInput(java.io.InputStream)
     */
    @Override
    public SdtncResponseDto handleResInput(InputStream istream) {
        return sdtncSerdes.deserializeFromXml(istream);
    }

    /* (non-Javadoc)
     * @see org.o3project.mlo.server.rpc.service.SdtncMethod#getConnectionTimeoutSec()
     */
    @Override
    public Integer getConnectionTimeoutSec() {
        return sdtncConfig.getConnectionTimeoutSec();
    }

    /* (non-Javadoc)
     * @see org.o3project.mlo.server.rpc.service.SdtncMethod#getReadTimeoutSec()
     */
    @Override
    public Integer getReadTimeoutSec() {
        return sdtncConfig.getReadTimeoutSec();
    }

    /* (non-Javadoc)
     * @see org.o3project.mlo.server.rpc.service.SdtncMethod#constructUrl(java.lang.String, java.util.Map)
     */
    @Override
    public String constructUrl(String path, Map<String, String> paramMap) {
        String sUrl = String.format("%s/%s", sdtncConfig.getNbiBaseUri(), path);
        if (paramMap != null && paramMap.size() > 0) {
            List<String> paramList = new ArrayList<String>();
            for (Entry<String, String> entry : paramMap.entrySet()) {
                paramList.add(String.format("%s=%s", entry.getKey(), entry.getValue()));
            }
            sUrl = sUrl + "?" + join(paramList);
        }
        return sUrl;
    }

    private static String join(List<String> paramList) {
        StringBuffer sb = new StringBuffer();
        for (int idx = 0; idx < paramList.size(); idx += 1) {
            if (idx != 0) {
                sb.append("&");
            }
            sb.append(paramList.get(idx));
        }
        return sb.toString();
    }
}

