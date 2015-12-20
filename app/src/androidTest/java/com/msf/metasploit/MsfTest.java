
package com.msf.metasploit;

import android.test.AndroidTestCase;

import com.msf.metasploit.model.DefaultRpcServer;

public class MsfTest extends AndroidTestCase {

    public void testLoginAndGenerateModels() {
        Msf msf = new Msf();
        msf.addRpcServer(DefaultRpcServer.createDefaultRpcServer());
        msf.addRpcServer(DefaultRpcServer.createDefaultRpcServer("10.0.2.2"));
    }

}
