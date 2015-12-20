
package com.msf.metasploit.rpc;

// Credits to scriptjunkie and rsmudge

import com.msf.metasploit.model.MsfModel;
import com.msf.metasploit.model.RpcServer;

import java.io.IOException;

public class RpcConnection implements RpcConstants {

    private MsfRpc msfRpc = new MsfRpc();
    private MsfModel msfModel = new MsfModel();

    public void connect(RpcServer rpcServer) throws IOException {
        msfRpc = new MsfRpc();
        msfRpc.createURL(rpcServer.rpcHost, rpcServer.rpcPort, true);
        rpcServer.rpcToken = msfRpc.connect(rpcServer.rpcUser, rpcServer.rpcPassword);
    }

    public void updateModel(RpcServer rpcServer) throws IOException {
		Object consoles = msfRpc.execute(RpcConstants.CONSOLE_LIST);
        msfModel.updateModel(CONSOLE_LIST, consoles);

    }

    public MsfModel getModel() {
        return msfModel;
    }
}
