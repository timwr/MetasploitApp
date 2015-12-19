
package com.msf.metasploit;

import android.content.Intent;
import android.test.AndroidTestCase;

import com.msf.metasploit.model.Defaults;
import com.msf.metasploit.model.MsfServer;
import com.msf.metasploit.model.MsfMain;
import com.msf.metasploit.rpc.MsfController;
import com.msf.metasploit.rpc.RpcConstants;

import java.util.HashMap;

public class MsfMainTest extends AndroidTestCase {

    public static final String SECOND_HOST = "10.0.2.2";

    Object runCmd(String cmd,Object[] args) {
        MsfController msfController = MsfController.getInstance();
        Intent intent = msfController.runCmd(cmd,args);
        return intent.getSerializableExtra(MsfController.RESULT);
    }
    Object runCmd(String cmd) {
        return runCmd(cmd, null);
    }

    public void testMsfMainAddToken() {
        MsfMain msfMain = new MsfMain(getContext());
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
        msfServer2.rpcAddress = "msf://msf@host2:3333";
        msfServer2.rpcToken = null;

        msfMain.addMsfSession(msfServer2);
        assertEquals(msfMain.getMsfRpcSessions().size(), 2);
    }

    public void testSwitchRpcServer() throws Exception {
        HashMap<String, String> version;
        MsfController.getInstance().connect(Defaults.DEFAULT_HOST, Integer.valueOf(Defaults.DEFAULT_PORT), Defaults.DEFAULT_USER, Defaults.DEFAULT_PASSWORD, true);
        version = (HashMap<String, String>) runCmd(RpcConstants.CORE_VERSION);
        System.err.println("vers " + version);
        assertEquals("1.0", version.get("api"));
        MsfController.getInstance().connect(SECOND_HOST, Integer.valueOf(Defaults.DEFAULT_PORT), Defaults.DEFAULT_USER, Defaults.DEFAULT_PASSWORD, true);
        version = (HashMap<String, String>) runCmd(RpcConstants.CORE_VERSION);
        System.err.println("vers " + version);
        assertEquals("1.0", version.get("api"));
    }
}
