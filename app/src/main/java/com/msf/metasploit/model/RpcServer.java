
package com.msf.metasploit.model;

import com.msf.metasploit.rpc.RpcConnection;

import java.io.IOException;

public class RpcServer {

    public int uid;
    public int rpcPort;
    public int status;
    public String rpcHost;
    public String rpcToken;
    public String rpcUser;
    public String rpcPassword;
    public RpcConnection rpcConnection;

    public String getRpcServerName() {
        return "name";
    }

    public boolean isAuthenticated() {
        return rpcToken != null;
    }

    public void connect() throws IOException {
        rpcConnection = new RpcConnection();
        rpcConnection.connect(this);
    }

    public void updateModel() throws IOException {
        rpcConnection.updateModel(this);
    }

    public MsfModel getModel() {
        return rpcConnection.getModel();
    }
}
