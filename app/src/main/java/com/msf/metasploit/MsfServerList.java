package com.msf.metasploit;

import android.content.Intent;

import com.msf.metasploit.model.RpcServer;
import com.msf.metasploit.rpc.Async;
import com.msf.metasploit.rpc.RpcConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MsfServerList {

    public static final String RPC_SERVER_ID = "rpc_server_id";

    public List<RpcServer> serverList = new ArrayList<RpcServer>();

    public List<RpcServer> getServerList() {
        return serverList;
    }

    public RpcServer fromIntent(Intent intent) {
        return getRpcServer(intent.getIntExtra(RPC_SERVER_ID, 0));
    }

    public static void toIntent(Intent intent, RpcServer rpcServer) {
        intent.putExtra(RPC_SERVER_ID, rpcServer.uid);
    }

    public RpcServer getRpcServer(int rpcServerId) {
        for (RpcServer rpcServer : serverList) {
            if (rpcServer.uid == rpcServerId) {
                return rpcServer;
            }
        }
        throw new RuntimeException();
    }

    private List<UpdateListener> listeners = new LinkedList<>();

    public void addListener(UpdateListener listener) {
        listeners.add(listener);
    }

    public void removeListener(UpdateListener listener) {
        listeners.remove(listener);
    }

    public interface UpdateListener {
        void onUpdated();
    }

    private void updateListeners() {
        for (UpdateListener listener : listeners) {
            listener.onUpdated();
        }
    }

    public void loadSavedServerList() {

    }

    public void connectServer(RpcServer rpcServer) {
        rpcServer.status = RpcServer.STATUS_CONNECTING;
        updateListeners();
        rpcServer.rpcConnection = new RpcConnection();
        try {
            rpcServer.rpcConnection.connect(rpcServer);
            if (rpcServer.rpcToken == null) {
                rpcServer.status = RpcServer.STATUS_AUTHORISATION_FAILED;
                updateListeners();
            } else {
                rpcServer.status = RpcServer.STATUS_AUTHORISED;
                updateListeners();
            }
        } catch (IOException e) {
            rpcServer.status = RpcServer.STATUS_CONNECTION_FAILED;
            updateListeners();
        }
    }

    public void connectAsync(final RpcServer rpcServer) {
        new Async() {
            @Override
            protected Void doInBackground(Void... arg0) {
                connectServer(rpcServer);
                return super.doInBackground(arg0);
            }
        }.execute();
    }
}
