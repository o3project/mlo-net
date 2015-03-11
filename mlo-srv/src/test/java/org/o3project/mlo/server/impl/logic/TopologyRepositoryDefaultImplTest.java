/**
 * TopologyRepositoryDefaultImplTest.java
 * (C) 2015, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.component.ComponentFactory;
import org.o3project.mlo.server.dto.FlowDto;
import org.o3project.mlo.server.dto.LinkInfoDto;
import org.o3project.mlo.server.logic.ConfigProvider;
import org.o3project.mlo.server.logic.MloException;

/**
 * TopologyRepositoryDefaultImplTest
 *
 */
public class TopologyRepositoryDefaultImplTest {

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

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyRepositoryDefaultImpl#init()}.
	 */
	@Test
	public void testInit() {
		TopologyRepositoryDefaultImpl obj = createObj();
		
		Throwable tha = null;
		try {
			obj.init();
			tha = null;
		} catch (Throwable th) {
			tha = th;
		}
		assertNull(tha);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyRepositoryDefaultImpl#queryFlowTemplate(org.o3project.mlo.server.dto.FlowDto)}.
	 * @throws MloException 
	 */
	@Test
	public void testQueryFlowTemplate() throws MloException {
		TopologyRepositoryDefaultImpl obj = createObj();

		FlowDto reqFlowDto = null;
		FlowDto tmplFlowDto = null;
		String expectedFlowName = null;

		/*
		 * osaka-tokyo
		 */
		reqFlowDto = createReqFlowDto("flow1-1", "osaka", "eth1", "tokyo", "eth1", 10, 25);
		expectedFlowName = "osaka11slow-fw";
		tmplFlowDto = obj.queryFlowTemplate(reqFlowDto);
		assertNotNull(tmplFlowDto);
		assertEquals(expectedFlowName, tmplFlowDto.name);
		
		reqFlowDto = createReqFlowDto("flow1-1", "tokyo", "eth1", "osaka", "eth1", 10, 25);
		expectedFlowName = "osaka11slow-bw";
		tmplFlowDto = obj.queryFlowTemplate(reqFlowDto);
		assertNotNull(tmplFlowDto);
		assertEquals(expectedFlowName, tmplFlowDto.name);
		
		reqFlowDto = createReqFlowDto("flow1-1", "osaka", "eth1", "tokyo", "eth1", 10, 15);
		expectedFlowName = "osaka12fast-fw";
		tmplFlowDto = obj.queryFlowTemplate(reqFlowDto);
		assertNotNull(tmplFlowDto);
		assertEquals(expectedFlowName, tmplFlowDto.name);
		
		reqFlowDto = createReqFlowDto("flow1-1", "tokyo", "eth1", "osaka", "eth1", 10, 15);
		expectedFlowName = "osaka12fast-bw";
		tmplFlowDto = obj.queryFlowTemplate(reqFlowDto);
		assertNotNull(tmplFlowDto);
		assertEquals(expectedFlowName, tmplFlowDto.name);
		
		reqFlowDto = createReqFlowDto("flow1-1", "osaka", "eth1", "tokyo", "eth1", 10, 5);
		expectedFlowName = "osaka13cutthrough-fw";
		tmplFlowDto = obj.queryFlowTemplate(reqFlowDto);
		assertNotNull(tmplFlowDto);
		assertEquals(expectedFlowName, tmplFlowDto.name);
		
		reqFlowDto = createReqFlowDto("flow1-1", "tokyo", "eth1", "osaka", "eth1", 10, 5);
		expectedFlowName = "osaka13cutthrough-bw";
		tmplFlowDto = obj.queryFlowTemplate(reqFlowDto);
		assertNotNull(tmplFlowDto);
		assertEquals(expectedFlowName, tmplFlowDto.name);
		
		/*
		 * akashi-tokyo
		 */
		reqFlowDto = createReqFlowDto("flow1-1", "akashi", "eth1", "tokyo", "eth1", 10, 25);
		expectedFlowName = "akashi21slow-fw";
		tmplFlowDto = obj.queryFlowTemplate(reqFlowDto);
		assertNotNull(tmplFlowDto);
		assertEquals(expectedFlowName, tmplFlowDto.name);
		
		reqFlowDto = createReqFlowDto("flow1-1", "tokyo", "eth1", "akashi", "eth1", 10, 25);
		expectedFlowName = "akashi21slow-bw";
		tmplFlowDto = obj.queryFlowTemplate(reqFlowDto);
		assertNotNull(tmplFlowDto);
		assertEquals(expectedFlowName, tmplFlowDto.name);
		
		reqFlowDto = createReqFlowDto("flow1-1", "akashi", "eth1", "tokyo", "eth1", 10, 15);
		expectedFlowName = "akashi22fast-fw";
		tmplFlowDto = obj.queryFlowTemplate(reqFlowDto);
		assertNotNull(tmplFlowDto);
		assertEquals(expectedFlowName, tmplFlowDto.name);
		
		reqFlowDto = createReqFlowDto("flow1-1", "tokyo", "eth1", "akashi", "eth1", 10, 15);
		expectedFlowName = "akashi22fast-bw";
		tmplFlowDto = obj.queryFlowTemplate(reqFlowDto);
		assertNotNull(tmplFlowDto);
		assertEquals(expectedFlowName, tmplFlowDto.name);
		
		reqFlowDto = createReqFlowDto("flow1-1", "akashi", "eth1", "tokyo", "eth1", 10, 5);
		expectedFlowName = "akashi23cutthrough-fw";
		tmplFlowDto = obj.queryFlowTemplate(reqFlowDto);
		assertNotNull(tmplFlowDto);
		assertEquals(expectedFlowName, tmplFlowDto.name);
		
		reqFlowDto = createReqFlowDto("flow1-1", "tokyo", "eth1", "akashi", "eth1", 10, 5);
		expectedFlowName = "akashi23cutthrough-bw";
		tmplFlowDto = obj.queryFlowTemplate(reqFlowDto);
		assertNotNull(tmplFlowDto);
		assertEquals(expectedFlowName, tmplFlowDto.name);
	}
	
	FlowDto createReqFlowDto(String name, String srcCeName, String srcCePortNo, String dstCeName, String dstCePortNo, Integer reqWidth, Integer reqDelay) {
		FlowDto reqFlowDto = null;
		reqFlowDto = new FlowDto();
		reqFlowDto.name = name;
		reqFlowDto.srcCENodeName = srcCeName;
		reqFlowDto.srcCEPortNo = srcCePortNo;
		reqFlowDto.dstCENodeName = dstCeName;
		reqFlowDto.dstCEPortNo = dstCePortNo;
		reqFlowDto.reqBandWidth = reqWidth;
		reqFlowDto.reqDelay = reqDelay;
		reqFlowDto.protectionLevel = "0";
		return reqFlowDto;
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyRepositoryDefaultImpl#queryVlanId(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testQueryVlanIdStringString() {
		TopologyRepositoryDefaultImpl obj = createObj();
		
		String vlanId = null;

		vlanId = obj.queryVlanId("tokyo", "eth1");
		assertNull(vlanId);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyRepositoryDefaultImpl#queryVlanId(org.o3project.mlo.server.dto.FlowDto)}.
	 * @throws MloException 
	 */
	@Test
	public void testQueryVlanIdFlowDto() throws MloException {
		TopologyRepositoryDefaultImpl obj = createObj();

		FlowDto reqFlowDto = null;
		FlowDto tmplFlowDto = null;
		String actual = null;
		Set<String> vlanIdSet = new LinkedHashSet<>();

		reqFlowDto = createReqFlowDto("flow1-1", "akashi", "eth1", "tokyo", "eth1", 10, 8);
		tmplFlowDto = obj.queryFlowTemplate(reqFlowDto);
		actual = obj.queryVlanId(tmplFlowDto);
		assertNotNull(actual);
		vlanIdSet.add(actual);

		reqFlowDto = createReqFlowDto("flow1-1", "tokyo", "eth1", "akashi", "eth1", 10, 8);
		tmplFlowDto = obj.queryFlowTemplate(reqFlowDto);
		actual = obj.queryVlanId(tmplFlowDto);
		assertNotNull(actual);
		vlanIdSet.add(actual);
		
		assertEquals(2, vlanIdSet.size());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyRepositoryDefaultImpl#getExtraVlanIdOffset()}.
	 */
	@Test
	public void testGetExtraVlanIdOffset() {
		TopologyRepositoryDefaultImpl obj = createObj();
		assertEquals(430, obj.getExtraVlanIdOffset().intValue());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyRepositoryDefaultImpl#getLdTopo()}.
	 * @throws MloException 
	 */
	@Test
	public void testGetLdTopo() throws MloException {
		TopologyRepositoryDefaultImpl obj = createObj();
		assertNotNull(obj.getLdTopo());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyRepositoryDefaultImpl#getRySwitches()}.
	 * @throws MloException 
	 */
	@Test
	public void testGetRySwitches() throws MloException {
		TopologyRepositoryDefaultImpl obj = createObj();
		assertNotNull(obj.getRySwitches());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyRepositoryDefaultImpl#getRyLinks()}.
	 * @throws MloException 
	 */
	@Test
	public void testGetRyLinks() throws MloException {
		TopologyRepositoryDefaultImpl obj = createObj();
		assertNotNull(obj.getRyLinks());
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyRepositoryDefaultImpl#loadData()}.
	 */
	//@Test
	public void testLoadData() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyRepositoryDefaultImpl#createFlowDtoTemplates(java.util.Map)}.
	 */
	//@Test
	public void testCreateFlowDtoTemplates() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyRepositoryDefaultImpl#createFlowObjFlowDtoMap(java.util.Map, org.o3project.mlo.server.component.TopologyObj)}.
	 */
	//@Test
	public void testCreateFlowObjFlowDtoMap() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyRepositoryDefaultImpl#extractFlowDtos(org.o3project.mlo.server.component.FlowObj, org.o3project.mlo.server.component.TopologyObj, java.util.Map)}.
	 */
	//@Test
	public void testExtractFlowDtos() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyRepositoryDefaultImpl#convertToFlowDto(org.o3project.mlo.server.component.FlowObj, org.o3project.mlo.server.component.TopologyObj)}.
	 */
	//@Test
	public void testConvertToFlowDto() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyRepositoryDefaultImpl#isMplsNode(org.o3project.mlo.server.component.NodeObj)}.
	 */
	//@Test
	public void testIsMplsNode() {
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.TopologyProviderRemoteLdImpl#sortFlowDtos(java.util.List)}.
	 */
	@Test
	public void testSortFlowDtos() {
		FlowDto[] flowDtoArray = null;
		List<FlowDto> flowDtoList = null;
		
		List<LinkInfoDto> links = Arrays.asList(
				FlowDtoUtil.createLinkInfo(1, "src-1-1", 1101, "dst-1-1", 1201),
				FlowDtoUtil.createLinkInfo(2, "src-2-1", 2101, "dst-2-1", 2201)
		);
		
		// Setups
		flowDtoArray = new FlowDto[]{
				FlowDtoUtil.createConcreteFlow(links, 11, 20),
				FlowDtoUtil.createConcreteFlow(links, 10, 20),
		};
		flowDtoList = Arrays.asList(flowDtoArray);
		
		// Executes
		TopologyRepositoryDefaultImpl.sortFlowDtos(flowDtoList);
		
		// Asserts
		assertEquals(10, flowDtoList.get(0).usedBandWidth.intValue());
		assertEquals(11, flowDtoList.get(1).usedBandWidth.intValue());
		
		// Setups
		flowDtoArray = new FlowDto[]{
				FlowDtoUtil.createConcreteFlow(links, 10, 20),
				FlowDtoUtil.createConcreteFlow(links, 11, 20),
		};
		flowDtoList = Arrays.asList(flowDtoArray);
		
		// Executes
		TopologyRepositoryDefaultImpl.sortFlowDtos(flowDtoList);
		
		// Asserts
		assertEquals(10, flowDtoList.get(0).usedBandWidth.intValue());
		assertEquals(11, flowDtoList.get(1).usedBandWidth.intValue());
		
		// Setups
		flowDtoArray = new FlowDto[]{
				FlowDtoUtil.createConcreteFlow(links, 10, 20),
				FlowDtoUtil.createConcreteFlow(links, 10, 21),
		};
		flowDtoList = Arrays.asList(flowDtoArray);
		
		// Executes
		TopologyRepositoryDefaultImpl.sortFlowDtos(flowDtoList);
		
		// Asserts
		assertEquals(21, flowDtoList.get(0).delayTime.intValue());
		assertEquals(20, flowDtoList.get(1).delayTime.intValue());
		
		// Setups
		flowDtoArray = new FlowDto[]{
				FlowDtoUtil.createConcreteFlow(links, 10, 21),
				FlowDtoUtil.createConcreteFlow(links, 10, 20),
		};
		flowDtoList = Arrays.asList(flowDtoArray);
		
		// Executes
		TopologyRepositoryDefaultImpl.sortFlowDtos(flowDtoList);
		
		// Asserts
		assertEquals(21, flowDtoList.get(0).delayTime.intValue());
		assertEquals(20, flowDtoList.get(1).delayTime.intValue());
		
		// Setups
		flowDtoArray = new FlowDto[]{
				FlowDtoUtil.createConcreteFlow(links, 10, 20),
				FlowDtoUtil.createConcreteFlow(links, 10, 20),
		};
		flowDtoList = Arrays.asList(flowDtoArray);
		
		// Executes
		TopologyRepositoryDefaultImpl.sortFlowDtos(flowDtoList);
		
		// Asserts
		assertEquals(flowDtoArray[0], flowDtoList.get(0));
		assertEquals(flowDtoArray[1], flowDtoList.get(1));
		
	}
	
	public static TopologyRepositoryDefaultImpl createObj() {
		ConfigProviderImpl configProvider = ConfigProviderImplTest.createConfigProviderImpl(null);
		
		HttpRequestInvokerDummyImpl dummyInvoker = new HttpRequestInvokerDummyImpl();
		TopologyProviderRemoteLdImpl topologyProviderRemoteLdImpl = TopologyProviderRemoteLdImplTest.createObj(dummyInvoker);

		return createObj(configProvider, topologyProviderRemoteLdImpl);
	}
	
	public static TopologyRepositoryDefaultImpl createObjLoaded(ConfigProvider configProvider) {
		TopologyProviderRemoteLdImpl topologyProviderRemoteLdImpl = null;
		
		TopologyRepositoryDefaultImpl obj = createObj(configProvider, topologyProviderRemoteLdImpl);
		obj.loadData();

		return obj;
	}

	/**
	 * @param configProvider
	 * @param topologyProviderRemoteLdImpl
	 * @return
	 */
	private static TopologyRepositoryDefaultImpl createObj(ConfigProvider configProvider, TopologyProviderRemoteLdImpl topologyProviderRemoteLdImpl) {
		ComponentFactory componentFactory = new ComponentFactory();
		
		TopologyProviderLocalImpl topologyProviderLocalImpl = new TopologyProviderLocalImpl();
		topologyProviderLocalImpl.setComponentFactory(componentFactory);
		
		TopologyRepositoryDefaultImpl obj = new TopologyRepositoryDefaultImpl();
		obj.setConfigProvider(configProvider);
		obj.setTopologyProviderLocalImpl(topologyProviderLocalImpl);
		obj.setTopologyProviderRemoteLdImpl(topologyProviderRemoteLdImpl);
		return obj;
	}

}
