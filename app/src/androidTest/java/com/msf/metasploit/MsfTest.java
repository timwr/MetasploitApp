
package com.msf.metasploit;

import android.test.AndroidTestCase;

import com.msf.metasploit.model.DefaultRpcServer;
import com.msf.metasploit.model.RpcServer;

public class MsfTest extends AndroidTestCase {

    public void testLoginAndGenerateModels() {
        Msf msf = new Msf();
        msf.addRpcServer(DefaultRpcServer.createDefaultRpcServer());
        RpcServer rpcServer = msf.getServerList().get(0);

        assertFalse(rpcServer.isAuthenticated());
        rpcServer.connect();
        assertTrue(rpcServer.isAuthenticated());

        rpcServer.rpcConnection = null;
        rpcServer.rpcPassword = null;

//        assertFalse(rpcServer.isAuthenticated());
//        rpcServer.connect();
//        assertTrue(rpcServer.isAuthenticated());

        rpcServer.updateModel();
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
