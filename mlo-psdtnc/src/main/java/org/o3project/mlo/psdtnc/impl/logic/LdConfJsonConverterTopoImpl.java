/**
 * LdConfJsonConverterTopoImpl.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.psdtnc.impl.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.arnx.jsonic.JSON;

import org.o3project.mlo.psdtnc.logic.LdConfJsonConverter;
import org.o3project.mlo.server.dto.LdBridgeDto;
import org.o3project.mlo.server.dto.LdFlowDto;
import org.o3project.mlo.server.dto.LdNodeDto;
import org.o3project.mlo.server.dto.LdTopoDto;

/**
 * LdConfJsonConverterTopoImpl
 *
 */
public class LdConfJsonConverterTopoImpl implements LdConfJsonConverter {
	
	static final Pattern PTN_SWITCHES = Pattern.compile("^SWITCHES=(.*)$");
	static final Pattern PTN_HOSTS = Pattern.compile("^HOSTS=(.*)$");
	static final Pattern PTN_CONTROLLERS = Pattern.compile("^CONTROLLERS=(.*)$");
	static final Pattern PTN_FLOWS = Pattern.compile("^FLOWS=(.*)$");
	
	static final Pattern PTN_NODE_BR = Pattern.compile("^(.*)_BR=(.*)$");
	static final Pattern PTN_NODE_ETH = Pattern.compile("^(.*)_ETH=(.*)$");
	static final Pattern PTN_NODE_IP = Pattern.compile("^(.*)_IP=(.*)$");
	static final Pattern PTN_NODE_MAC = Pattern.compile("^(.*)_MAC=(.*)$");
	static final Pattern PTN_NODE_DPID = Pattern.compile("^(.*)_DPID=(.*)$");
	static final Pattern PTN_NODE_TYPE = Pattern.compile("^(.*)_TYPE=(.*)$");
	
	static final Pattern PTN_FLOW_FLOW = Pattern.compile("^(.*)_FLOW=(.*)$");

	static final Pattern PTN_BRIDGE_DELAY = Pattern.compile("^(.*)_DELAY=(.*)$");
	static final Pattern PTN_BRIDGE_BW = Pattern.compile("^(.*)_BW=(.*)$");

	/* (non-Javadoc)
	 * @see org.o3project.mlo.psdtnc.logic.LdConfJsonConverter#convertToJson(java.io.InputStream, java.io.OutputStream)
	 */
	@Override
	public void convertToJson(InputStream confInputStream, OutputStream jsonOutputStream) throws IOException {
		LdTopoDto topoConf = readTopoConf(confInputStream);
		writeJson(topoConf, jsonOutputStream);
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.psdtnc.logic.LdConfJsonConverter#convertFromJson(java.io.InputStream)
	 */
	@Override
	public LdTopoDto convertFromConf(InputStream confInputStream) throws IOException {
		return readTopoConf(confInputStream);
	}
	
	static LdTopoDto readTopoConf(InputStream confInputStream) throws IOException {
		LdTopoDto topoConf = new LdTopoDto();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(confInputStream, "utf-8"));
			String line = br.readLine();
			while (line != null) {
				parseLine(line, topoConf);
				line = br.readLine();
			}
		} finally {
			if (br != null) {
				br.close();
				br = null;
			}
		}
		return topoConf;
	}
	
	static void writeJson(LdTopoDto topoConf, OutputStream jsonOutputStream) throws IOException {
		JSON.encode(topoConf, jsonOutputStream, true);
	}
	
	static void parseLine(String line, LdTopoDto topoConf) {
		if (PTN_SWITCHES.matcher(line).find()) {
			parseSwitchesLine(line, topoConf);
		} else if (PTN_HOSTS.matcher(line).find()) {
			parseHostsLine(line, topoConf);
		} else if (PTN_CONTROLLERS.matcher(line).find()) {
			parseControllersLine(line, topoConf);
		} else if (PTN_FLOWS.matcher(line).find()) {
			parseFlowsLine(line, topoConf);
		} else if (PTN_NODE_BR.matcher(line).find()) {
			parseNodePropLine(line, topoConf, PTN_NODE_BR, "br");
		} else if (PTN_NODE_ETH.matcher(line).find()) {
			parseNodePropLine(line, topoConf, PTN_NODE_ETH, "eth");
		} else if (PTN_NODE_IP.matcher(line).find()) {
			parseNodePropLine(line, topoConf, PTN_NODE_IP, "ip");
		} else if (PTN_NODE_MAC.matcher(line).find()) {
			parseNodePropLine(line, topoConf, PTN_NODE_MAC, "mac");
		} else if (PTN_NODE_DPID.matcher(line).find()) {
			parseNodePropLine(line, topoConf, PTN_NODE_DPID, "dpid");
		} else if (PTN_NODE_TYPE.matcher(line).find()) {
			parseNodePropLine(line, topoConf, PTN_NODE_TYPE, "type");
		} else if (PTN_FLOW_FLOW.matcher(line).find()) {
			parseFlowPropLine(line, topoConf, PTN_FLOW_FLOW, "flow");
		} else if (PTN_BRIDGE_DELAY.matcher(line).find()) {
			parseBridgePropLine(line, topoConf, PTN_BRIDGE_DELAY, "delay");
		} else if (PTN_BRIDGE_BW.matcher(line).find()) {
			parseBridgePropLine(line, topoConf, PTN_BRIDGE_BW, "bw");
		}
	}

	static void parseSwitchesLine(String line, LdTopoDto topoConf) {
		Matcher m = PTN_SWITCHES.matcher(line);
		m.find();
		String value = m.group(1);
		List<String> names = splitToList(value);
		topoConf.switches = new ArrayList<LdNodeDto>();
		for (String name : names) {
			LdNodeDto nodeConf = getNodeConfByNameFromNodeMap(name, topoConf);
			nodeConf.name = name;
			topoConf.switches.add(nodeConf);
		}
	}
	
	static void parseHostsLine(String line, LdTopoDto topoConf) {
		Matcher m = PTN_HOSTS.matcher(line);
		m.find();
		String value = m.group(1);
		List<String> names = splitToList(value);
		topoConf.hosts = new ArrayList<LdNodeDto>();
		for (String name : names) {
			LdNodeDto nodeConf = getNodeConfByNameFromNodeMap(name, topoConf);
			nodeConf.name = name;
			topoConf.hosts.add(nodeConf);
		}
	}
	
	static void parseControllersLine(String line, LdTopoDto topoConf) {
		Matcher m = PTN_CONTROLLERS.matcher(line);
		m.find();
		String value = m.group(1);
		List<String> names = splitToList(value);
		topoConf.controllers = names;
	}
	
	static void parseNodePropLine(String line, LdTopoDto topoConf, Pattern ptn, String propName) {
		Matcher m = ptn.matcher(line);
		m.find();
		String name = m.group(1);
		String value = m.group(2);
		
		LdNodeDto nodeConf = getNodeConfByNameFromNodeMap(name, topoConf);
		
		if ("eth".equals(propName)) {
			nodeConf.portNames = splitToList(value);
		} else if ("br".equals(propName)) {
			nodeConf.brNames = splitToList(value);
			for (String brName : nodeConf.brNames) {
				LdBridgeDto bridgeConf = getBridgeConfByNameFromBridgeMap(brName, topoConf);
				addBridgeConfIfNotExists(bridgeConf, topoConf);
			}
		} else if ("ip".equals(propName)) {
			nodeConf.ip = trimQuatation(value.trim());
		} else if ("mac".equals(propName)) {
			nodeConf.mac = trimQuatation(value.trim());
		} else if ("dpid".equals(propName)) {
			nodeConf.dpid = trimQuatation(value.trim());
		} else if ("type".equals(propName)) {
			nodeConf.type = trimQuatation(value.trim());
		}
	}
	
	static void parseFlowsLine(String line, LdTopoDto topoConf) {
		Matcher m = PTN_FLOWS.matcher(line);
		m.find();
		String value = m.group(1);
		List<String> names = splitToList(value);
		topoConf.flows = new ArrayList<LdFlowDto>();
		for (String name : names) {
			LdFlowDto flowConf = getFlowConfByNameFromFlowMap(name, topoConf);
			topoConf.flows.add(flowConf);
		}
	}
	
	static void parseFlowPropLine(String line, LdTopoDto topoConf, Pattern ptn, String propName) {
		Matcher m = ptn.matcher(line);
		m.find();
		String name = m.group(1);
		String value = m.group(2);
		LdFlowDto flowConf = getFlowConfByNameFromFlowMap(name, topoConf);
		
		if ("flow".equals(propName)) {
			flowConf.brNames = splitToList(value);
			for (String brName : flowConf.brNames) {
				LdBridgeDto bridgeConf = getBridgeConfByNameFromBridgeMap(brName, topoConf);
				addBridgeConfIfNotExists(bridgeConf, topoConf);
			}
		}
	}
	
	static void parseBridgePropLine(String line, LdTopoDto topoConf, Pattern ptn, String propName) {
		Matcher m = ptn.matcher(line);
		m.find();
		String name = m.group(1);
		String value = m.group(2);
		
		LdBridgeDto bridgeConf = getBridgeConfByNameFromBridgeMap(name, topoConf);
		addBridgeConfIfNotExists(bridgeConf, topoConf);
		
		if ("delay".equals(propName)) {
			bridgeConf.delay = trimQuatation(value.trim());
		} else if ("bw".equals(propName)) {
			bridgeConf.bw = trimQuatation(value.trim());
		}
	}

	/**
	 * @param name
	 * @param topoConf
	 * @return
	 */
	static LdNodeDto getNodeConfByNameFromNodeMap(String name, LdTopoDto topoConf) {
		LdNodeDto nodeConf = topoConf.nameNodeMap.get(name);
		if (nodeConf == null) {
			nodeConf = new LdNodeDto();
			nodeConf.name = name;
			topoConf.nameNodeMap.put(name, nodeConf);
		}
		return nodeConf;
	}

	/**
	 * @param name
	 * @param topoConf
	 * @return
	 */
	static LdFlowDto getFlowConfByNameFromFlowMap(String name, LdTopoDto topoConf) {
		LdFlowDto flowConf = topoConf.nameFlowMap.get(name);
		if (flowConf == null) {
			flowConf = new LdFlowDto();
			flowConf.name = name;
			topoConf.nameFlowMap.put(name, flowConf);
		}
		return flowConf;
	}

	/**
	 * @param name
	 * @param topoConf
	 * @return
	 */
	static LdBridgeDto getBridgeConfByNameFromBridgeMap(String name, LdTopoDto topoConf) {
		LdBridgeDto bridgeConf = topoConf.nameBridgeMap.get(name);
		if (bridgeConf == null) {
			bridgeConf = new LdBridgeDto();
			bridgeConf.bw = LdBridgeDto.DEFAULT_BW;
			bridgeConf.delay = LdBridgeDto.DEFAULT_DELAY;
			bridgeConf.name = name;
			topoConf.nameBridgeMap.put(name, bridgeConf);
		}
		return bridgeConf;
	}

	/**
	 * @param bridgeConf
	 * @param topoConf
	 */
	static void addBridgeConfIfNotExists(LdBridgeDto bridgeConf, LdTopoDto topoConf) {
		if (topoConf.bridges == null) {
			topoConf.bridges = new ArrayList<LdBridgeDto>();
		}
		if (!topoConf.bridges.contains(bridgeConf)) {
			topoConf.bridges.add(bridgeConf);
		}
	}
	
	static List<String> splitToList(String value) {
		List<String> splited = null;
		String trimed = trimQuatation(value.trim()).trim();
		if (trimed.length() > 0) {
			splited = Arrays.asList(trimed.split("\\s+"));
		} else {
			splited = Arrays.asList(new String[]{});
		}
		return splited;
	}
	
	static String trimQuatation(String value) {
		String trimed = null;
		if (value.length() > 0 && value.startsWith("\"")) {
			trimed = trimQuatation(value.substring(1));
		} else if (value.length() > 0 && value.endsWith("\"")) {
			trimed = trimQuatation(value.substring(0, value.length() - 1));
		} else {
			trimed = value;
		}
		return trimed;
	}
}
