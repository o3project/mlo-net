package org.o3project.mlo.server.rpc.entity;


import static org.junit.Assert.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.rpc.entity.PTLinkEntity;

public class PTLinkEntityTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConstructor() {
		PTLinkEntity link = createTl1LinkEntity();
		assertEquals(link.linkId, "NW=SDN,Layer=Ether,TL=1");
		assertEquals(link.srcNode, "NW=SDN,NE=PT1");
		assertEquals(link.srcPort, "NW=SDN,NE=PT1,Layer=Ether,TTP=2");
		assertEquals(link.dstNode, "NW=SDN,NE=PT2");
		assertEquals(link.dstPort, "NW=SDN,NE=PT2,Layer=Ether,TTP=1");
		assertEquals(link.operStatus, "DOWN");
		assertEquals(link.reqLatency, "9999 msec");
		assertEquals(link.reqBandwidth, "10000 Mbps");
		assertEquals(link.establishmentStatus, "establishing");
	}

	@Test
	public void testToString() {
		PTLinkEntity link = createTl1LinkEntity();
		assertEquals(link.toString(), "NW=SDN,Layer=Ether,TL=1, NW=SDN,NE=PT1, NW=SDN,NE=PT1,Layer=Ether,TTP=2, NW=SDN,NE=PT2, NW=SDN,NE=PT2,Layer=Ether,TTP=1, DOWN, 9999 msec, 10000 Mbps, establishing");
	}

	/**
	 * @return
	 */
	PTLinkEntity createTl1LinkEntity() {
		return new PTLinkEntity("NW=SDN,Layer=Ether,TL=1", "NW=SDN,NE=PT1", "NW=SDN,NE=PT1,Layer=Ether,TTP=2", "NW=SDN,NE=PT2", "NW=SDN,NE=PT2,Layer=Ether,TTP=1", "DOWN", "9999 msec", "10000 Mbps", "establishing");
	}
}
