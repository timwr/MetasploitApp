
package com.msf.metasploit;

import android.test.AndroidTestCase;

import com.msf.metasploit.model.DefaultRpcServer;
import com.msf.metasploit.model.RpcServer;

import java.io.IOException;

public class MsfTest extends AndroidTestCase {

    private RpcServer rpcServer;

    public void testLogin() throws IOException {
        Msf msf = new Msf();
        msf.addRpcServer(DefaultRpcServer.createDefaultRpcServer());
        RpcServer rpcServer = msf.getServerList().get(0);
        assertTrue(rpcServer.status == 0);
        msf.msfServerList.connectServer(rpcServer);
        assertTrue(rpcServer.status == RpcServer.STATUS_AUTHORISED);
        this.rpcServer = rpcServer;
    }

    public void testLoginAndGenerateModels() throws IOException {
        if (rpcServer == null) {
            testLogin();
        }

//        rpcServer.rpcConnection = null;
//        rpcServer.rpcPassword = null;
//        assertFalse(rpcServer.isAuthenticated());
//        rpcServer.connect();
//        assertTrue(rpcServer.isAuthenticated());

        rpcServer.updateModel();
        assertNotNull(rpcServer.getModel().getConsoles());
        assertNotNull(rpcServer.getModel().getJobs());
        assertNotNull(rpcServer.getModel().getSessions());

//        rpcServer.rpcConnection.execute(RpcConstants.CONSOLE_CREATE);
//        rpcServer.updateModel();
//        assertTrue(rpcServer.getModel().getConsoles().size() > 0);
    }
//
//    public void skipTestLoginTwoServers() {
//        Msf msf = new Msf();
//        msf.addRpcServer(DefaultRpcServer.createDefaultRpcServer());
//        MsfRpc.connect(msf.getServerList().get(0));
//        msf.addRpcServer(DefaultRpcServer.createDefaultRpcServer("10.0.2.2"));
//        MsfRpc.connect(msf.getServerList().get(1));
//    }

}
