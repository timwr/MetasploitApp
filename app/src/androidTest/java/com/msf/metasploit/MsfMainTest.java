
package com.msf.metasploit;

import android.test.AndroidTestCase;

import com.msf.metasploit.model.Defaults;
import com.msf.metasploit.model.MsfServer;
import com.msf.metasploit.rpc.MsfMain;
import com.msf.metasploit.rpc.MsfRpc;
import com.msf.metasploit.rpc.RpcConstants;

import java.util.HashMap;

public class MsfMainTest extends AndroidTestCase {

    public static final String SECOND_HOST = "10.0.2.2";

    public void testMsfMainAddToken() {
        MsfMain msfMain = new MsfMain();
        msfMain.getMsfRpcSessions().clear();
        MsfServer msfServer = new MsfServer();
        msfServer.rpcAddress = "msf://msf@host:3333";
        msfServer.rpcToken = null;
        String id = msfMain.addMsfSession(msfServer);
        assertEquals(msfMain.getMsfRpcSessions().size(), 1);

        assertNotNull(msfServer.id);
        msfServer.rpcToken = "new token";
        String newid = msfMain.addMsfSession(msfServer);
        assertEquals(id, newid);
        assertEquals(msfMain.getMsfRpcSessions().size(), 1);

        MsfServer msfServer2 = new MsfServer();
        msfServer2.rpcAddress = msfServer.rpcAddress + "3";
        msfServer2.rpcToken = null;

        msfMain.addMsfSession(msfServer2);
        assertEquals(msfMain.getMsfRpcSessions().size(), 2);
    }

    public void testSwitchRpcServer() throws Exception {
        HashMap<String, String> version;
        MsfRpc msfRpc = TestConstants.connect();
        assertNotNull(msfRpc.getRpcToken());
        version = (HashMap<String, String>)msfRpc.execute(RpcConstants.CORE_VERSION);
        System.err.println("vers " + version);
        assertEquals("1.0", version.get("api"));

        MsfRpc msfRpc2 = TestConstants.connect(SECOND_HOST);
        assertNotNull(msfRpc2.getRpcToken());
        assertFalse(msfRpc.getRpcToken().equals(msfRpc2.getRpcToken()));
        version = (HashMap<String, String>)msfRpc2.execute(RpcConstants.CORE_VERSION);
        System.err.println("vers " + version);
        assertEquals("1.0", version.get("api"));
    }

    public void testConnectRpcServer() {
        MsfMain msfMain = new MsfMain();
        msfMain.getMsfRpcSessions().clear();

        MsfServer msfServer = new MsfServer();
        msfServer.rpcAddress = "https://" + Defaults.DEFAULT_HOST + ":" + Defaults.DEFAULT_PORT;
        msfServer.rpcToken = null;
        MsfRpc msfRpc = new MsfRpc(msfServer);
        msfRpc.connect(Defaults.DEFAULT_USER, Defaults.DEFAULT_PASSWORD);
        String id = msfMain.addMsfSession(msfServer);
        assertEquals(msfMain.getMsfRpcSessions().size(), 1);

        assertNotNull(msfServer.id);
        msfServer.rpcToken = "new token";
        String newid = msfMain.addMsfSession(msfServer);
        assertEquals(id, newid);
        assertEquals(msfMain.getMsfRpcSessions().size(), 1);

        MsfServer msfServer2 = new MsfServer();
        msfServer2.rpcAddress = msfServer.rpcAddress + "3";
        msfServer2.rpcToken = null;

        msfMain.addMsfSession(msfServer2);
        assertEquals(msfMain.getMsfRpcSessions().size(), 2);
    }
}
