package com.msf.metasploit.rpc;

import java.io.IOException;

/**
 * This is a modification of msfgui/RpcConnection.java by scriptjunkie. Taken from 
 * the Metasploit Framework Java GUI. 
 */
public interface RpcConnection {
	public Object execute(String methodName) throws IOException;
	public Object execute(String methodName, Object[] params) throws IOException;
}
