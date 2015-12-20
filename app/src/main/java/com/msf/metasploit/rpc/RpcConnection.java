
package com.msf.metasploit.rpc;

// Credits to scriptjunkie and rsmudge

import com.msf.metasploit.model.MsfModel;
import com.msf.metasploit.model.RpcServer;

public class RpcConnection implements RpcConstants {

    private MsfRpc msfRpc = new MsfRpc();
    private MsfModel msfModel = new MsfModel();

    public void connect(RpcServer rpcServer) {
        msfRpc = new MsfRpc();
        msfRpc.createURL(rpcServer.rpcHost, rpcServer.rpcPort, true);
        rpcServer.rpcToken = msfRpc.connect(rpcServer.rpcUser, rpcServer.rpcPassword);
    }

    public void updateModel(RpcServer rpcServer) {

        MapmsfRpc.execute(CONSOLE_LIST);




    }

}
