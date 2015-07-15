package org.o3project.mlo.server.logic;

import static org.junit.Assert.*;

import org.junit.Test;

public class SshConnectionExceptionTest {

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshConnectionException#SshConnectionException(java.lang.String)}.
	 */
	@Test
	public void testSshShellPipe() {
		SshConnectionException obj = new SshConnectionException("test.");
		assertEquals("test.", obj.getMessage());
	}

}
