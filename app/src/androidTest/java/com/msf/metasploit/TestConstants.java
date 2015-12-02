package com.msf.metasploit;

import com.msf.metasploit.model.Defaults;
import com.msf.metasploit.rpc.MsfController;

public class TestConstants {

    public static void connect() {
        MsfController.getInstance().connect(Defaults.DEFAULT_HOST, Integer.valueOf(Defaults.DEFAULT_PORT), Defaults.DEFAULT_USER, Defaults.DEFAULT_PASSWORD, true);
    }
    
}

