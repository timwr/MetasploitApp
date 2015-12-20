
package com.msf.metasploit.model;

import com.msf.metasploit.rpc.MsfRpc;

public class RpcServer {

    public int uid;
    public int rpcPort;
    public String rpcHost;
    public String rpcToken;
    public String rpcUser;
    public String rpcPassword;
    public MsfRpc rpcConnection;

    public String getRpcServerName() {
        return "name";
    }

    public boolean isAuthenticated() {
        return true;
    }

    public void connect() {

    }
}
