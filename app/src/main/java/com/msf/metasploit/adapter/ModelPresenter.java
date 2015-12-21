package com.msf.metasploit.adapter;

import android.os.Handler;

import com.msf.metasploit.rpc.Async;
import com.msf.metasploit.rpc.RpcConnection;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ModelPresenter {

    private static final long POLLING_INTERVAL = 30000;

    private Handler handler = new Handler();
    private Runnable updateHandler = new Runnable() {
        @Override
        public void run() {
            update();
        }
    };

    private List<UpdateListener> listeners = new LinkedList<>();
    private RpcConnection rpcConnection;

    public void addListener(UpdateListener listener) {
        listeners.add(listener);
    }

//    private void refreshAfterInterval(long pollingInterval) {
//        handler.removeCallbacksAndMessages(null);
//        if (listeners.size() > 0) {
//            handler.postDelayed(updateHandler, pollingInterval);
//        }
//    }

    public void removeListener(UpdateListener listener) {
        listeners.remove(listener);
        if (listeners.size() == 0) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    public void setConnection(RpcConnection rpc) {
        rpcConnection = rpc;
    }

    public interface UpdateListener {
        void onUpdated();
    }

    private void updateListeners() {
        for (UpdateListener listener : listeners) {
            listener.onUpdated();
        }
    }
    private void updateSync() {
        try {
            rpcConnection.updateModel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateListeners();
    }

    public void update() {
        new Async() {
            @Override
            protected Void doInBackground(Void... arg0) {
                updateSync();
                return super.doInBackground(arg0);
            }
        }.execute();
    }

}
