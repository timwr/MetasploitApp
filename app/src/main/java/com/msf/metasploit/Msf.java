package com.msf.metasploit;

import com.msf.metasploit.model.RpcServer;

import java.util.List;

public class Msf {

    public static Msf get() {
        return MsfApplication.Msf();
    }

    public final MsfServerList msfServerList = new MsfServerList();

    public List<RpcServer> getServerList() {
        return msfServerList.getServerList();
    }

    public void addRpcServer(RpcServer rpcServer) {
        msfServerList.getServerList().add(rpcServer);
    }


}
