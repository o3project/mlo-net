package org.o3project.mlo.server.logic;

import static org.junit.Assert.*;

import org.junit.Test;
import org.o3project.mlo.server.impl.logic.ConfigProviderImpl;

public class SshAccessFactoryTest {

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccessFactory#setConfigProvider(org.o3project.mlo.server.logic.ConfigProvider)}.
	 */
	@Test
	public void testSetConfigProvider() {
		SshAccessFactory obj = new SshAccessFactory();
		try{
			obj.setConfigProvider(new ConfigProviderImpl("default.mlo-srv.properties", "/etc/mlo/mlo-srv.properties"));
		} catch (Throwable th) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccessFactory#init()}.
	 */
	@Test
	public void testInit() {
		SshAccessFactory obj = new SshAccessFactory();
		obj.init();
		assertNotNull(obj.executorService);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccessFactory#destroy()}.
	 */
	@Test
	public void testDestroy() {
		SshAccessFactory obj = new SshAccessFactory();
		obj.init();
		SshShellTask sshShellTask = new SshShellTask() {
			@Override
			public Void call() {
				while(true);
			}
		};
		obj.executorService.submit(sshShellTask);
		try{
			obj.destroy();
			assertNull(obj.executorService);
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccessFactory#destroy()}.
	 */
	@Test
	public void testDestroy_null() {
		SshAccessFactory obj = new SshAccessFactory();
		try{
			obj.destroy();
			assertNull(obj.executorService);
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccessFactory#destroy()}.
	 */
	@Test
	public void testDestroy_intereputed() {
		final SshAccessFactory obj = new SshAccessFactory();
		obj.init();
		SshShellTask sshShellTask = new SshShellTask() {
			@Override
			public Void call() {
				while(true);
			}
		};

		obj.executorService.submit(sshShellTask);
		
		Thread thread = new Thread() {
			@Override
			public void run() {
				obj.destroy();
			}
		};
		
		thread.start();
		thread.interrupt();
		assertFalse(obj.executorService.isTerminated());
		
		obj.destroy();
		assertNull(obj.executorService);
		
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccessFactory#create()}.
	 */
	@Test
	public void testCreate() {
		SshAccessFactory obj = new SshAccessFactory();
		
		SshAccess sshAccess = obj.create();
		assertNotNull(sshAccess);
	}

}
