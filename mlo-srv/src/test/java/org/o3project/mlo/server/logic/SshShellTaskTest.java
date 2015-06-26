package org.o3project.mlo.server.logic;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jcraft.jsch.UserInfo;

public class SshShellTaskTest {

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellTask#init(java.lang.String, org.o3project.mlo.server.logic.SshNodeConfig, org.o3project.mlo.server.logic.SshShellPipe)}.
	 */
	@Test
	public void testInit() {
		SshShellTask obj = new SshShellTask();
		obj.init("s1", new SshNodeConfig(){

			@Override
			public String getHost() {
				return null;
			}

			@Override
			public int getSshPort() {
				return 0;
			}

			@Override
			public String getUserid() {
				return null;
			}

			@Override
			public String getPassword() {
				return null;
			}

			@Override
			public int getSshSessionTimeout() {
				return 0;
			}

			@Override
			public int getSshChannelTimeout() {
				return 0;
			}

			@Override
			public String getLdWorkspaceDirpash() {
				return null;
			}}, new SshShellPipe());

		assertEquals("s1", obj.targetId);
		assertNotNull(obj.config);
		assertNotNull(obj.sshShellPipe);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellTask#getConfig()}.
	 */
	@Test
	public void testGetConfig() {
		SshShellTask obj = new SshShellTask();
		obj.init("s1", new SshNodeConfig(){

			@Override
			public String getHost() {
				return "111.111.111.111.";
			}
			
			@Override
			public int getSshPort() {
				return 1111;
			}
			@Override
			public String getUserid() {
				return "TestId";
			}
			
			@Override
			public String getPassword() {
				return "TestPass";
			}
			
			@Override
			public int getSshSessionTimeout() {
				return 2222;
			}
			
			@Override
			public int getSshChannelTimeout() {
				return 3333;
			}
			
			@Override
			public  String getLdWorkspaceDirpash() {
				return "/test";
			}
			
			}, new SshShellPipe());
		assertNotNull(obj.getConfig());
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellTask#getSshInfo()}.
	 */
	@Test
	public void testGetSshInfo() {
		SshShellTask obj = new SshShellTask();
		obj.init("s1", new SshNodeConfig(){

			@Override
			public String getHost() {
				return "111.111.111.111";
			}
			
			@Override
			public int getSshPort() {
				return 1111;
			}
			@Override
			public String getUserid() {
				return "TestId";
			}
			
			@Override
			public String getPassword() {
				return "TestPass";
			}
			
			@Override
			public int getSshSessionTimeout() {
				return 2222;
			}
			
			@Override
			public int getSshChannelTimeout() {
				return 3333;
			}
			
			@Override
			public  String getLdWorkspaceDirpash() {
				return "/test";
			}
			
			}, new SshShellPipe());
		
		SshNodeConfig sshNodeConfig = obj.getConfig();
		assertEquals("TestId@111.111.111.111:1111, (sessionTimeout, channelTimeout)=(2222, 3333)", SshShellTask.getSshInfo(sshNodeConfig));
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellTask#createuserInfo()}.
	 */
	@Test
	public void testCreateUserInfo() {
		SshShellTask obj = new SshShellTask();
		obj.init("s1", new SshNodeConfig(){

			@Override
			public String getHost() {
				return "111.111.111.111";
			}
			
			@Override
			public int getSshPort() {
				return 1111;
			}
			@Override
			public String getUserid() {
				return "TestId";
			}
			
			@Override
			public String getPassword() {
				return "TestPass";
			}
			
			@Override
			public int getSshSessionTimeout() {
				return 2222;
			}
			
			@Override
			public int getSshChannelTimeout() {
				return 3333;
			}
			
			@Override
			public  String getLdWorkspaceDirpash() {
				return "/test";
			}
			
			}, new SshShellPipe());
		
		UserInfo userInfo = obj.createUserInfo();
		assertTrue(userInfo.promptYesNo(""));
		assertNull(userInfo.getPassword());
		assertFalse(userInfo.promptPassword(""));
		assertNull(userInfo.getPassphrase());
		assertFalse(userInfo.promptPassphrase(""));
	}
}
