package com.msf.metasploit.model;

public class MsfServer {

    public static final String MSF_SERVER = "msf_server";
    public static final int STATUS_UNCONNECTED = 0;
    public static final int STATUS_CONNECTING = 1;
    public static final int STATUS_AUTHORIZED = 2;
    public static final int STATUS_UNAUTHORIZED = 3;

    public String id;
    public String rpcAddress;
    public String rpcToken;

    public transient int status;
    public transient String password;
}
