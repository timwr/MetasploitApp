package com.msf.metasploit.adapter;

import android.os.Handler;

import com.msf.metasploit.model.Console;
import com.msf.metasploit.model.RpcServer;
import com.msf.metasploit.rpc.Async;
import com.msf.metasploit.rpc.RpcConnection;
import com.msf.metasploit.rpc.RpcConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ConsolePresenter {

    private static final long POLLING_INTERVAL = 8000;

    private Handler handler = new Handler();
    private Runnable updateHandler = new Runnable() {
        @Override
        public void run() {
            update();
        }
    };

    private List<UpdateListener> listeners = new LinkedList<>();

    public void addListener(UpdateListener listener) {
        listeners.add(listener);
    }

    private void refreshAfterInterval() {
        handler.removeCallbacks(updateHandler);
        if (listeners.size() > 0) {
            handler.postDelayed(updateHandler, POLLING_INTERVAL);
        }
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

    public Console console;
    public RpcServer rpcServer;
    public RpcConnection rpcConnection;
    private StringBuilder commandList = new StringBuilder();

    public void sendCommand(String command) {
        commandList.append(command);
    }

    private void updateSync() {
        try {
            updateConsole();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateConsole() throws IOException {
        if (console.id == null) {
            HashMap<String, String> consoleInfo = (HashMap<String, String>) rpcConnection.execute(RpcConstants.CONSOLE_CREATE);
            console.id = consoleInfo.get("id");
        }

        if (commandList.length() > 0) {
            String command = commandList.toString();
            commandList.setLength(0);
            rpcConnection.execute(RpcConstants.CONSOLE_WRITE, new Object[]{console.id, command});
            console.text.append(console.prompt);
            console.text.append(command);
        }

        HashMap<String, Object> consoleObject = (HashMap<String, Object>) rpcConnection.execute(RpcConstants.CONSOLE_READ, new Object[]{console.id});
        Boolean busy = (Boolean) consoleObject.get("busy");
        while (busy) {
            consoleObject = (HashMap<String, Object>) rpcConnection.execute(RpcConstants.CONSOLE_READ, new Object[]{console.id});
            busy = (Boolean) consoleObject.get("busy");
        }

        String prompt = (String) consoleObject.get("prompt");
        if (prompt != null) {
            prompt = prompt.replaceAll("\\x01|\\x02", "");
            console.prompt = prompt;
        }

        String data = (String) consoleObject.get("data");
        if (data != null) {
            console.text.append(data);
        }
        updateListeners();
        refreshAfterInterval();
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
