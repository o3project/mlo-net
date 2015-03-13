/**
 * LdConfJsonConverterTopoImplTest.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.psdtnc.impl.logic;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import net.arnx.jsonic.JSON;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.dto.LdBridgeDto;
import org.o3project.mlo.server.dto.LdFlowDto;
import org.o3project.mlo.server.dto.LdNodeDto;
import org.o3project.mlo.server.dto.LdTopoDto;
import org.seasar.framework.util.ResourceUtil;

/**
 * LdConfJsonConverterTopoImplTest
 *
 */
public class LdConfJsonConverterTopoImplTest {
	static final String TOPO_CONF_PATH = "org/o3project/mlo/psdtnc/impl/logic/dummy.topo.conf.default.dat";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testConvertToJson() throws Exception {
		LdConfJsonConverterTopoImpl obj = new LdConfJsonConverterTopoImpl();
		String json = null;
		InputStream is = null;
		OutputStream os = null;
		try {
			os = new ByteArrayOutputStream();
			try {
				is = ResourceUtil.getResourceAsStream(TOPO_CONF_PATH);
				obj.convertToJson(is, os);
			} finally {
				if (is != null) {
					is.close();
					is = null;
				}
			}
			
			json = os.toString();
		} finally {
			if (os != null) {
				os.close();
			}
		}
		
		System.out.println(json);
		assertNotNull(json);
		
		LdTopoDto topoConf = JSON.decode(json, LdTopoDto.class);
		_assertTopoConf(topoConf);
	}

	/**
	 * Test method for {@link org.o3project.mlo.psdtnc.impl.logic.LdConfJsonConverterTopoImpl#toJsonValue(java.io.InputStream)}.
	 */
	@Test
	public void testReadTopoConf() throws Exception {
		LdTopoDto topoConf = null;
		InputStream is = null;
		try {
			is = ResourceUtil.getResourceAsStream(TOPO_CONF_PATH);
			topoConf = LdConfJsonConverterTopoImpl.readTopoConf(is);
		} finally {
			if (is != null) {
				is.close();
				is = null;
			}
		}
		
		_assertTopoConf(topoConf);
	}

	/**
	 * @param topoConf
	 */
	void _assertTopoConf(LdTopoDto topoConf) {
		assertNotNull(topoConf);
		
		assertEquals(5, topoConf.switches.size());
		_assertNodeConf("s1", 
				null, Arrays.asList("br1", "br3", "br11", "br12"), 
				null, null, "0.00:00:00:00:00:01", "edge", 
				topoConf.switches.get(0));
		_assertNodeConf("s2", 
				null, Arrays.asList("br2", "br4", "br21"), 
				null, null, "0.00:00:00:00:00:02", "edge", 
				topoConf.switches.get(1));
		_assertNodeConf("s3", 
				null, Arrays.asList("br3", "br5", "br7", "br8"), 
				null, null, "0.00:00:00:00:00:03", "mpls", 
				topoConf.switches.get(2));
		_assertNodeConf("s4", 
				null, Arrays.asList("br4", "br6", "br7", "br8"), 
				null, null, "0.00:00:00:00:00:04", "mpls", 
				topoConf.switches.get(3));
		_assertNodeConf("s5", 
				null, Arrays.asList("br5", "br6"), 
				null, null, "0.00:00:00:00:00:05", "mpls", 
				topoConf.switches.get(4));
		
		assertEquals(5, topoConf.hosts.size());
		_assertNodeConf("h1", 
				null, Arrays.asList("br1"), 
				"169.254.0.1/24", "52:54:00:00:00:01", "0.52:54:00:00:00:1", "host", 
				topoConf.hosts.get(0));
		_assertNodeConf("h2", 
				null, Arrays.asList("br2"), 
				"169.254.0.2/24", "52:54:00:00:00:02", "0.52:54:00:00:00:2", "host", 
				topoConf.hosts.get(1));
		_assertNodeConf("tokyo", 
				null, Arrays.asList("br21"), 
				"169.254.0.21/24", "52:54:00:00:00:21", "0.52:54:00:00:00:21", "host", 
				topoConf.hosts.get(2));
		_assertNodeConf("osaka", 
				null, Arrays.asList("br11"), 
				"169.254.0.11/24", "52:54:00:00:00:11", "0.52:54:00:00:00:11", "host", 
				topoConf.hosts.get(3));
		_assertNodeConf("akashi", 
				null, Arrays.asList("br12"), 
				"169.254.0.12/24", "52:54:00:00:00:12", "0.52:54:00:00:00:12", "host", 
				topoConf.hosts.get(4));
		
		assertEquals(1, topoConf.controllers.size());
		assertEquals("c0", topoConf.controllers.get(0));
		
		assertEquals(9, topoConf.flows.size());
		_assertFlowConf("of1slow", Arrays.asList("br1", "br3", "br5", "br6", "br4", "br2"), 
				topoConf.flows.get(0));
		_assertFlowConf("of2fast", Arrays.asList("br1", "br3", "br7", "br4", "br2"), 
				topoConf.flows.get(1));
		_assertFlowConf("of3cutthrough", Arrays.asList("br1", "br3", "br8", "br4", "br2"), 
				topoConf.flows.get(2));
		_assertFlowConf("osaka11slow", Arrays.asList("br11", "br3", "br5", "br6", "br4", "br21"), 
				topoConf.flows.get(3));
		_assertFlowConf("osaka12fast", Arrays.asList("br11", "br3", "br7", "br4", "br21"), 
				topoConf.flows.get(4));
		_assertFlowConf("osaka13cutthrough", Arrays.asList("br11", "br3", "br8", "br4", "br21"), 
				topoConf.flows.get(5));
		_assertFlowConf("akashi21slow", Arrays.asList("br12", "br3", "br5", "br6", "br4", "br21"), 
				topoConf.flows.get(6));
		_assertFlowConf("akashi22fast", Arrays.asList("br12", "br3", "br7", "br4", "br21"), 
				topoConf.flows.get(7));
		_assertFlowConf("akashi23cutthrough", Arrays.asList("br12", "br3", "br8", "br4", "br21"), 
				topoConf.flows.get(8));
		
		assertEquals(11, topoConf.bridges.size());
		_assertBridgeConf("br1", LdBridgeDto.DEFAULT_DELAY, LdBridgeDto.DEFAULT_BW, topoConf.bridges.get(0));
		_assertBridgeConf("br3", LdBridgeDto.DEFAULT_DELAY, LdBridgeDto.DEFAULT_BW, topoConf.bridges.get(1));
		_assertBridgeConf("br11", LdBridgeDto.DEFAULT_DELAY, LdBridgeDto.DEFAULT_BW, topoConf.bridges.get(2));
		_assertBridgeConf("br12", LdBridgeDto.DEFAULT_DELAY, LdBridgeDto.DEFAULT_BW, topoConf.bridges.get(3));
		_assertBridgeConf("br2", LdBridgeDto.DEFAULT_DELAY, LdBridgeDto.DEFAULT_BW, topoConf.bridges.get(4));
		_assertBridgeConf("br4", LdBridgeDto.DEFAULT_DELAY, LdBridgeDto.DEFAULT_BW, topoConf.bridges.get(5));
		_assertBridgeConf("br21", LdBridgeDto.DEFAULT_DELAY, LdBridgeDto.DEFAULT_BW, topoConf.bridges.get(6));
		_assertBridgeConf("br5", "10ms", "10000000kbps", topoConf.bridges.get(7));
		_assertBridgeConf("br7", "10ms", "10000000kbps", topoConf.bridges.get(8));
		_assertBridgeConf("br8", "1ms", "100000000kbps", topoConf.bridges.get(9));
		_assertBridgeConf("br6", "10ms", "10000000kbps", topoConf.bridges.get(10));
	}
	
	public void _assertNodeConf(String name, List<String> portNames, List<String> brNames, String ip, String mac, String dpid, String type, LdNodeDto nodeConf) {
		assertEquals(name, nodeConf.name);
		if (nodeConf.portNames == null || portNames == null) {
			assertEquals(portNames, nodeConf.portNames);
		} else {
			for (int idx = 0; idx < portNames.size(); idx += 1) {
				assertEquals(portNames.get(idx), nodeConf.portNames.get(idx));
			}
		}
		for (int idx = 0; idx < brNames.size(); idx += 1) {
			assertEquals(brNames.get(idx), nodeConf.brNames.get(idx));
		}
		assertEquals(ip, nodeConf.ip);
		assertEquals(mac, nodeConf.mac);
		assertEquals(dpid, nodeConf.dpid);
		assertEquals(type, nodeConf.type);
	}
	
	public void _assertFlowConf(String name, List<String> brNames, LdFlowDto flowConf) {
		assertEquals(name, flowConf.name);
		for (int idx = 0; idx < brNames.size(); idx += 1) {
			assertEquals(brNames.get(idx), flowConf.brNames.get(idx));
		}
	}
	
	public void _assertBridgeConf(String name, String delay, String bw, LdBridgeDto bridgeConf) {
		assertEquals(name, bridgeConf.name);
		assertEquals(delay, bridgeConf.delay);
		assertEquals(bw, bridgeConf.bw);
	}
	
	@Test
	public void testPTN_SWITCHES() {
		String line = null;
		Matcher matcher = null;
		
		line = "SWITCHES=\"s1 s2 s3 s4 s5\"";
		matcher = LdConfJsonConverterTopoImpl.PTN_SWITCHES.matcher(line);
		assertEquals(true, matcher.find());
		
		line = "sWITCHES=\"s1 s2 s3 s4 s5\"";
		matcher = LdConfJsonConverterTopoImpl.PTN_SWITCHES.matcher(line);
		assertEquals(false, matcher.find());
	}
	
	@Test
	public void testPTN_HOSTS() {
		String line = null;
		Matcher matcher = null;
		
		line = "HOSTS=\"h1 h2\"";
		matcher = LdConfJsonConverterTopoImpl.PTN_HOSTS.matcher(line);
		assertEquals(true, matcher.find());
		
		line = "aHOSTS=\"h1 h2\"";
		matcher = LdConfJsonConverterTopoImpl.PTN_HOSTS.matcher(line);
		assertEquals(false, matcher.find());
	}
	
	@Test
	public void testPTN_NODE_ETH() {
		String line = null;
		Matcher matcher = null;
		
		line = "s1_ETH=\"eth1 eth2\"";
		matcher = LdConfJsonConverterTopoImpl.PTN_NODE_ETH.matcher(line);
		assertEquals(true, matcher.find());
		assertEquals("s1", matcher.group(1));
	}
	
	@Test
	public void testPTN_ANY_BR() {
		String line = null;
		Matcher matcher = null;
		
		line = "s1_BR=\"br1 br3\"";
		matcher = LdConfJsonConverterTopoImpl.PTN_NODE_BR.matcher(line);
		assertEquals(true, matcher.find());
		assertEquals("s1", matcher.group(1));
		
		line = "of1_FLOW=\"br1 br3 br5 br6 br4 br2\"";
		matcher = LdConfJsonConverterTopoImpl.PTN_FLOW_FLOW.matcher(line);
		assertEquals(true, matcher.find());
		assertEquals("of1", matcher.group(1));
	}
	
	@Test
	public void testSplitToList() {
		String value = null;
		List<String> splited = null;
		
		value = "\"s1 s2 s3 s4 s5\"";
		splited = LdConfJsonConverterTopoImpl.splitToList(value);
		assertEquals(5, splited.size());
		assertEquals("s1", splited.get(0));
		assertEquals("s2", splited.get(1));
		assertEquals("s3", splited.get(2));
		assertEquals("s4", splited.get(3));
		assertEquals("s5", splited.get(4));
		
		value = "s1 s2 s3 s4 s5";
		splited = LdConfJsonConverterTopoImpl.splitToList(value);
		assertEquals(5, splited.size());
		assertEquals("s1", splited.get(0));
		assertEquals("s2", splited.get(1));
		assertEquals("s3", splited.get(2));
		assertEquals("s4", splited.get(3));
		assertEquals("s5", splited.get(4));
		
		value = "  \"  s1   s2     s3    s4   s5  \"  ";
		splited = LdConfJsonConverterTopoImpl.splitToList(value);
		assertEquals(5, splited.size());
		assertEquals("s1", splited.get(0));
		assertEquals("s2", splited.get(1));
		assertEquals("s3", splited.get(2));
		assertEquals("s4", splited.get(3));
		assertEquals("s5", splited.get(4));
		
		value = "\"s1\"";
		splited = LdConfJsonConverterTopoImpl.splitToList(value);
		assertEquals(1, splited.size());
		assertEquals("s1", splited.get(0));
		
		value = "\"\"";
		splited = LdConfJsonConverterTopoImpl.splitToList(value);
		assertEquals(0, splited.size());
		
		value = "";
		splited = LdConfJsonConverterTopoImpl.splitToList(value);
		assertEquals(0, splited.size());
	}
	
	@Test
	public void testTrimQuatation() {
		String value = null;
		String actual = null;
		
		value = "\"a b c d e\"";
		actual = LdConfJsonConverterTopoImpl.trimQuatation(value);
		assertEquals("a b c d e", actual);
		
		value = "a b c d e\"";
		actual = LdConfJsonConverterTopoImpl.trimQuatation(value);
		assertEquals("a b c d e", actual);
		
		value = "\"a b c d e";
		actual = LdConfJsonConverterTopoImpl.trimQuatation(value);
		assertEquals("a b c d e", actual);
		
		value = "\"\"a b c d e\"\"\"";
		actual = LdConfJsonConverterTopoImpl.trimQuatation(value);
		assertEquals("a b c d e", actual);
		
		value = "\"\"a b \"c\" d e\"\"\"";
		actual = LdConfJsonConverterTopoImpl.trimQuatation(value);
		assertEquals("a b \"c\" d e", actual);
		
		value = "\"\"\"\"\"";
		actual = LdConfJsonConverterTopoImpl.trimQuatation(value);
		assertEquals("", actual);
	}
}
