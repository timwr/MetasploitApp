package com.msf.metasploit;

public class Msf {

    public static Msf get() {
        return MsfApplication.Msf();
    }

    public final MsfServerList msfServerList = new MsfServerList();

}
