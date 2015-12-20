package com.msf.metasploit.events;

import com.msf.metasploit.model.MsfServer;

public class MsfServerEvent {

    public final MsfServer msfServer;

    public MsfServerEvent(MsfServer msfServer) {
        this.msfServer = msfServer;
    }
}
