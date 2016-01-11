
package com.msf.metasploit.model;

public class DefaultRpcServer  {

    public static RpcServer createDefaultRpcServer() {
//        if (true) {
//            return createEmulatorRpcServer();
//        }
        RpcServer rpcServer = new RpcServer();
        rpcServer.rpcUser = Defaults.DEFAULT_USER;
        rpcServer.rpcHost = Defaults.DEFAULT_HOST;
        rpcServer.rpcPassword = Defaults.DEFAULT_PASSWORD;
        rpcServer.rpcPort = Integer.valueOf(Defaults.DEFAULT_PORT);
        return rpcServer;
    }

    public static RpcServer createEmulatorRpcServer() {
        RpcServer rpcServer = new RpcServer();
        rpcServer.rpcUser = Defaults.DEFAULT_USER;
        rpcServer.rpcHost = "10.0.2.2";
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
