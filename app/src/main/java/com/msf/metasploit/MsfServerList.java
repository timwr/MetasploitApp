package com.msf.metasploit;

import com.msf.metasploit.model.RpcServer;
import com.msf.metasploit.rpc.Async;
import com.msf.metasploit.rpc.RpcConnection;
import com.msf.metasploit.rpc.RpcException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MsfServerList {

    public static final String RPC_SERVER_ID = "rpc_server_id";
    public static final int RPC_SERVER_ID_NEW = -1;

    public List<RpcServer> serverList = new ArrayList<RpcServer>();

    public List<RpcServer> getServerList() {
        return serverList;
    }

    public RpcServer getRpcServer(int rpcServerId) {
        return serverList.get(rpcServerId);
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
        List<UpdateListener> updateList = new ArrayList<UpdateListener>(listeners);
        for (UpdateListener listener : updateList) {
            listener.onUpdated();
        }
    }

    public void loadSavedServerList() {

    }

    public void saveServerList() {

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
        } catch (RpcException e) {
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
