
package com.msf.metasploit.model;

public class DefaultRpcServer  {

    public static RpcServer createDefaultRpcServer() {
        RpcServer rpcServer = new RpcServer();
        rpcServer.rpcUser = Defaults.DEFAULT_USER;
        rpcServer.rpcHost = Defaults.DEFAULT_HOST;
        rpcServer.rpcPassword = Defaults.DEFAULT_PASSWORD;
        rpcServer.rpcPort = Integer.valueOf(Defaults.DEFAULT_PORT);
        return rpcServer;
    }

    public static RpcServer createDefaultRpcServer(String host) {
        RpcServer rpcServer = new RpcServer();
        rpcServer.rpcUser = Defaults.DEFAULT_USER;
        rpcServer.rpcHost = host;
        rpcServer.rpcPassword = Defaults.DEFAULT_PASSWORD;
        rpcServer.rpcPort = Integer.valueOf(Defaults.DEFAULT_PORT);
        return rpcServer;
    }
}
