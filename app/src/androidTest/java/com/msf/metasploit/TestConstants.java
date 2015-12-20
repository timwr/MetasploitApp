package com.msf.metasploit;

import com.msf.metasploit.model.Defaults;
import com.msf.metasploit.model.MsfServer;
import com.msf.metasploit.rpc.MsfRpc;

public class TestConstants {

    public static MsfRpc connect() {
        MsfRpc msfRpc = new MsfRpc();
        msfRpc.createURL(Defaults.DEFAULT_HOST, Integer.valueOf(Defaults.DEFAULT_PORT), true);
        msfRpc.connect(Defaults.DEFAULT_USER, Defaults.DEFAULT_PASSWORD);
        return msfRpc;
    }

    public static MsfRpc connect(String host) {
        MsfRpc msfRpc = new MsfRpc();
        msfRpc.createURL(host, Integer.valueOf(Defaults.DEFAULT_PORT), true);
        msfRpc.connect(Defaults.DEFAULT_USER, Defaults.DEFAULT_PASSWORD);
        return msfRpc;
    }

    public static MsfServer getDefaultServer() {
        MsfServer msfServer = new MsfServer();
        msfServer.rpcAddress = "https://" + Defaults.DEFAULT_HOST + ":" + Defaults.DEFAULT_PORT;
        return msfServer;
    }

}

