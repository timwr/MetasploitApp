package com.msf.metasploit;

import com.msf.metasploit.model.RpcServer;

import java.util.ArrayList;
import java.util.List;

public class MsfServerList {

    public List<RpcServer> serverList = new ArrayList<RpcServer>();
    public List<RpcServer> getServerList() {
        return serverList;
    }

    public void loadSavedServerList() {

    }
}
