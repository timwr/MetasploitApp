
package com.msf.metasploit.model;

public class Terminal extends RpcObject {

    public final static int TYPE_CONSOLE = 0;
    public final static int TYPE_SHELL = 1;
    public final static int TYPE_METERPRETER = 3;

    public int type;
    public String id;
    public String prompt;
    public StringBuffer text = new StringBuffer();

}
