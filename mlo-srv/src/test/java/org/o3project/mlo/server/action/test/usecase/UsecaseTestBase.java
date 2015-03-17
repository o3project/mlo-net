/**
 * UsecaseTestBase.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.action.test.usecase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.o3project.mlo.server.action.ActionUtil;
import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.impl.logic.DtoTestUtils;
import org.o3project.mlo.server.logic.Orchestrator;
import org.o3project.mlo.server.logic.Serdes;
import org.o3project.mlo.server.logic.SliceOperationTask;
import org.o3project.mlo.server.logic.Validator;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.util.ResourceUtil;

/**
 * UsecaseTestBase
 *
 */
public abstract class UsecaseTestBase extends S2TestCase {

	private static final Log LOG = LogFactory.getLog(Usecase001Test.class);
	private static final String DATA_PATH = "org/o3project/mlo/server/action/test/usecase/";

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
	
	@Binding
	private SliceOperationTask slicesGetTask;

	/**
	 * 
	 */
	public UsecaseTestBase() {
		super();
	}

	/**
	 * @param name
	 */
	public UsecaseTestBase(String name) {
		super(name);
	}

	@Before
	protected void setUp() throws Exception {
		super.setUp();
	}

	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected void _assertMloAction(String ope, String reqXmlFileName, String resXmlFileName)
			throws Exception {
		Validator validator = null;
		SliceOperationTask task = null;
		if ("CREATE".equals(ope)) {
			validator = createSliceValidator;
			task = createSliceTask;
		} else if ("UPDATE".equals(ope)) {
			validator = updateSliceValidator;
			task = updateSliceTask;
		} else if ("DELETE".equals(ope)) {
			validator = deleteSliceValidator;
			task = deleteSliceTask;
		} else if ("READ".equals(ope)) {
			validator = readSliceValidator;
			task = readSliceTask;
		} else {
			fail("Unsupported operation.");
		}
		
		File reqXmlFile = ResourceUtil.getResourceAsFile(DATA_PATH + reqXmlFileName);
		assertTrue("reqXml not found: " + reqXmlFileName, reqXmlFile.exists());
		File resXmlFile = ResourceUtil.getResourceAsFile(DATA_PATH + resXmlFileName);
		assertTrue("resXml not found: " + resXmlFileName, resXmlFile.exists());
		
		InputStream reqIStream = null;
		InputStream resIStream = null;
		ByteArrayOutputStream ostream = null;
		InputStream istream = null;
		String resXml = null;
		boolean isSameXml = false;
		try {
			reqIStream = new FileInputStream(reqXmlFile);
			resIStream = new FileInputStream(resXmlFile);
			ostream = new ByteArrayOutputStream();
			ActionUtil.doAction(orchestrator, serdes, 
					validator, task, reqIStream, ostream);
			resXml = new String(ostream.toByteArray(), "UTF-8");
			istream = new ByteArrayInputStream(resXml.getBytes("UTF-8"));
			isSameXml = DtoTestUtils.isSameXml(resIStream, istream);
			if (!isSameXml) {
				LOG.info(resXml);
			}
		} finally {
			if (reqIStream != null) {
				reqIStream.close();
				reqIStream = null;
			}
			if (resIStream != null) {
				resIStream.close();
				resIStream = null;
			}
			if (ostream != null) {
				ostream.close();
				ostream = null;
			}
			if (istream != null) {
				istream.close();
				istream = null;
			}
		}
		assertTrue("Not Same XML", isSameXml);
	}

	protected void _assertMloSlicesGetAction(Map<String, String> paramMap, String resXmlFileName)
			throws Exception {
		SliceOperationTask task = slicesGetTask;

		File resXmlFile = ResourceUtil.getResourceAsFile(DATA_PATH + resXmlFileName);
		assertTrue("resXml not found: " + resXmlFileName, resXmlFile.exists());
		
		InputStream resIStream = null;
		ByteArrayOutputStream ostream = null;
		InputStream istream = null;
		String resXml = null;
		boolean isSameXml = false;
		try {
			resIStream = new FileInputStream(resXmlFile);
			ostream = new ByteArrayOutputStream();
			_doSlicesGetAction(orchestrator, serdes, task, paramMap, ostream);
			resXml = new String(ostream.toByteArray(), "UTF-8");
			LOG.info(resXml);
			istream = new ByteArrayInputStream(resXml.getBytes("UTF-8"));
			isSameXml = DtoTestUtils.isSameXml(resIStream, istream);
		} finally {
			if (resIStream != null) {
				resIStream.close();
				resIStream = null;
			}
			if (ostream != null) {
				ostream.close();
				ostream = null;
			}
			if (istream != null) {
				istream.close();
				istream = null;
			}
		}
		assertTrue("Not Same XML", isSameXml);
	}
	
	/**
	 * 
	 * @param orchestrator
	 * @param serdes
	 * @param sliceOpTask
	 * @param paramMap
	 * @param ostream
	 * @return
	 */
	private static String _doSlicesGetAction(Orchestrator orchestrator,
			Serdes serdes,
			SliceOperationTask sliceOpTask, 
			Map<String, String> paramMap, OutputStream ostream) {
    	RestifResponseDto resDto = null;
    	try {
    		resDto = orchestrator.handle(sliceOpTask, paramMap);
    	} finally {
    		if (resDto != null) {
    			serdes.marshal(resDto, ostream);
    		}
    	}
    	return null;
    }

}
