/**
 * LdTopologyRepository.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

import java.util.List;

import org.o3project.mlo.server.dto.LdTopoDto;
import org.o3project.mlo.server.dto.RyLinkDto;
import org.o3project.mlo.server.dto.RySwitchDto;

/**
 * This interface designates the LD topology repository.
 */
public interface LdTopologyRepository {

	/**
	 * Obtains the topo.conf topology.
	 * @return the topology instance. 
	 * @throws MloException Failed to obtain the topology.
	 */
	LdTopoDto getLdTopo() throws MloException;

	/**
	 * Obtains the Ryu switches topology.
	 * @return the topology instance. 
	 * @throws MloException Failed to obtain the topology.
	 */
	List<RySwitchDto> getRySwitches() throws MloException;

	/**
	 * Obtains the Ryu links topology.
	 * @return the topology instance. 
	 * @throws MloException Failed to obtain the topology.
	 */
	List<RyLinkDto> getRyLinks() throws MloException;
}