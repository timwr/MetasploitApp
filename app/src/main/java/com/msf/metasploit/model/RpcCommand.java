package com.msf.metasploit.model;


public class RpcCommand {

    public final String command;
    public final Object[] arguments;

    public RpcCommand(String command, Object[] arguments) {
        this.command = command;
        this.arguments = arguments;
    }
}
