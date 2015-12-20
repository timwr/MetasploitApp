package com.msf.metasploit;

import com.msf.metasploit.model.RpcServer;
import com.msf.metasploit.rpc.Async;
import com.msf.metasploit.rpc.RpcConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MsfServerList {

    public List<RpcServer> serverList = new ArrayList<RpcServer>();
    public List<RpcServer> getServerList() {
        return serverList;
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

    public void updateList() {
        for (UpdateListener listener : listeners) {
            listener.onUpdated();
        }
    }

    public void loadSavedServerList() {

    }

    public void connectServer(RpcServer rpcServer) {
        rpcServer.status = RpcServer.STATUS_CONNECTING;
        updateList();
        rpcServer.rpcConnection = new RpcConnection();
        try {
            rpcServer.rpcConnection.connect(rpcServer);
            if (rpcServer.rpcToken == null) {
                rpcServer.status = RpcServer.STATUS_AUTHORISED;
                updateList();
            } else {
                rpcServer.status = RpcServer.STATUS_AUTHORISATION_FAILED;
                updateList();
            }
        } catch (IOException e) {
            rpcServer.status = RpcServer.STATUS_CONNECTION_FAILED;
            updateList();
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
