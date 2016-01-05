
package com.msf.metasploit;

import android.test.AndroidTestCase;

import com.msf.metasploit.model.DefaultRpcServer;
import com.msf.metasploit.model.RpcServer;

import java.io.IOException;

public class MsfTest extends AndroidTestCase {

    protected RpcServer rpcServer;

    protected void testLogin() throws IOException {
        Msf msf = new Msf();
        RpcServer rpcServer = DefaultRpcServer.createDefaultRpcServer();
        msf.msfServerList.serverList.add(rpcServer);
        assertTrue(rpcServer.status == RpcServer.STATUS_NEW);
        msf.msfServerList.connectServer(rpcServer);
        assertTrue(rpcServer.status == RpcServer.STATUS_AUTHORISED);
        this.rpcServer = rpcServer;
    }

    public void testLoginAndGenerateModels() throws IOException {
        if (rpcServer == null) {
            testLogin();
        }

        rpcServer.getRpc().updateModel();
        assertNotNull(rpcServer.getModel().getConsoles());
        assertNotNull(rpcServer.getModel().getJobs());
        assertNotNull(rpcServer.getModel().getSessions());
        assertTrue(rpcServer.getModel().getSessions().size() > 0);
    }


//    public void skipTestLoginTwoServers() {
//        Msf msf = new Msf();
//        msf.addRpcServer(DefaultRpcServer.createDefaultRpcServer());
//        MsfRpc.connect(msf.getServerList().get(0));
//        msf.addRpcServer(DefaultRpcServer.createDefaultRpcServer("10.0.2.2"));
//        MsfRpc.connect(msf.getServerList().get(1));
//    }

}
