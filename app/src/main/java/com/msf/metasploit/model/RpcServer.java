
package com.msf.metasploit.model;

import com.msf.metasploit.rpc.RpcConnection;

import java.io.IOException;

public class RpcServer extends SavedRpcServer {

    public int status;
    public RpcConnection rpcConnection;

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
