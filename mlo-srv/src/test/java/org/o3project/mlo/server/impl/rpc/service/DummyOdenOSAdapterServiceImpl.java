/**
 * DummyOdenOSAdapterServiceImpl.java
 * (C) 2014,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.rpc.entity.PTFlowEntity;
import org.o3project.mlo.server.rpc.entity.PTLinkEntity;
import org.o3project.mlo.server.rpc.service.OdenOSDriver;
import org.o3project.mlo.server.rpc.service.OdenOSListener;
import org.o3project.mlo.server.rpc.service.OdenOSAdapterService;

/**
 * DummyOdenOSAdapterServiceImpl
 */
public class DummyOdenOSAdapterServiceImpl implements OdenOSAdapterService,
		OdenOSListener {

	@Override
	public void setDriver(OdenOSDriver driver) {
		return;
	}

	@Override
	public void notifyLinkChanged(String linkId) {
		return;
	}

	@Override
	public void notifyFlowDeleted(String odenosFlowId) {
		return;
	}

	@Override
	public void notifyFlowChanged(String odenosFlowId) {
		return;
	}

	@Override
	public void init() {
		return;
	}

	@Override
	public void dispose() {
		return;
	}

	@Override
	public PTLinkEntity getLink(String odenOSLinkId) throws MloException {
		return null;
	}

	@Override
	public void requestLink(PTLinkEntity link) throws MloException {
		return;
	}

	@Override
	public void deleteFlow(String odenosFlowId) throws MloException {
		return;
	}

	@Override
	public String putFlow(PTFlowEntity entity) throws MloException {
		return entity.flowId;
	}

}
