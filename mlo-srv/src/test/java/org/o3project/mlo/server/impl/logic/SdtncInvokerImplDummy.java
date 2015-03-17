/**
 * SdtncInvokerImplDummy.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.io.IOException;
import java.util.Map;


import org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl;
import org.o3project.mlo.server.impl.rpc.service.SdtncTestUtil;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.OtherException;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;
import org.o3project.mlo.server.rpc.service.SdtncInvoker;
import org.o3project.mlo.server.rpc.service.SdtncMethod;

/**
 * This class is the dummy implementation of {@link SdtncInvoker} interface.
 */
public class SdtncInvokerImplDummy implements SdtncInvoker {

    private static final String DATA_PATH = "src/test/resources/org/o3project/mlo/server/logic/data/base";

    private final SdtncSerdesImpl serdes = new SdtncSerdesImpl();

    private String xmlName;

    /*
     * (non-Javadoc)
     * @see org.o3project.mlo.server.rpc.service.SdtncInvoker#invoke(org.o3project.mlo.server.rpc.service.SdtncMethod, org.o3project.mlo.server.rpc.dto.SdtncRequestDto, java.lang.String, java.util.Map)
     */
    @Override
    public SdtncResponseDto invoke(SdtncMethod method,
            SdtncRequestDto reqDto, String path, Map<String, String> params)
                    throws MloException {
        SdtncResponseDto resDto = null;
        try {
            resDto = invokeInternal(method, reqDto, path, params);
        } catch (IOException e) {
            throw new OtherException("", e);
        } finally {
            ;
        }
        return resDto;
    }

    /**
     * Executes
     * @param method the method instance.
     * @param reqDto the request instance.
     * @param path URL path
     * @param paramMap the query parameter map.
     * @return the response.
     * @throws IOException IO failure.
     * @throws MloException API error.
     */
    private SdtncResponseDto invokeInternal(SdtncMethod method,
            SdtncRequestDto reqDto, String path, Map<String, String> paramMap)
                    throws IOException, MloException {

        SdtncResponseDto resDto = null;
        try {
            resDto = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, xmlName);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return resDto;
    }

    // Specifies expected response XML.
    public void setXmlName(String xmlName) {
        this.xmlName = xmlName;
    }

}
