package com.msf.metasploit.rpc;


public interface Async {
	public void execute_async(String methodName);
	public void execute_async(String methodName, Object[] args);
}
