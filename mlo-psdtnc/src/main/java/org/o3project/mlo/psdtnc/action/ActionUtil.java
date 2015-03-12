/**
 * ActionUtil.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.psdtnc.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * 境界層で使用する処理のユーティリティクラスです。
 */
public final class ActionUtil {
	
	private ActionUtil() {
		super();
	}

    /**
     * サポートしている HTTP メソッドを判定します。
     * @param httpMethod　要求された HTTP メソッド
     * @param supportingMethod　サポートする HTTP メソッド
     * @return 判定結果
     */
    static boolean isSupportingHttpMethod(String httpMethod, String supportingMethod) {
    	return isSupportingHttpMethod(httpMethod, new String[]{supportingMethod});
    }
    
    /**
     * サポートしている HTTP メソッドを判定します。
     * @param httpMethod　要求された HTTP メソッド
     * @param supportingMethods　サポートする HTTP メソッドの配列
     * @return 判定結果
     */
    static boolean isSupportingHttpMethod(String httpMethod, String[] supportingMethods) {
    	boolean bIsSupporting = false;
    	if (httpMethod != null) {
    		for (String supportingMethod : supportingMethods) {
    			if (httpMethod.equalsIgnoreCase(supportingMethod)) {
    				bIsSupporting = true;
    				break;
    			}
    		}
    	}
    	return bIsSupporting;
    }
	static void handleNotImplementedMethod(HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not implemented.");
	}
}
