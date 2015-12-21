package com.msf.metasploit.adapter;

import android.os.Handler;

import com.msf.metasploit.model.Terminal;
import com.msf.metasploit.rpc.Async;
import com.msf.metasploit.rpc.RpcConnection;
import com.msf.metasploit.rpc.RpcConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TerminalPresenter {

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

    private void refreshAfterInterval(long pollingInterval) {
        handler.removeCallbacksAndMessages(null);
        if (listeners.size() > 0) {
            handler.postDelayed(updateHandler, pollingInterval);
        }
    }

    public void removeListener(UpdateListener listener) {
        listeners.remove(listener);
        if (listeners.size() == 0) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    public void setConsole(RpcConnection rpc, String consoleId) {
        rpcConnection = rpc;
        terminal = new Terminal();
        terminal.id = consoleId;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public interface UpdateListener {
        void onUpdated();
    }

    private void updateListeners() {
        for (UpdateListener listener : listeners) {
            listener.onUpdated();
        }
    }

    private Terminal terminal;
    private RpcConnection rpcConnection;
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
        if (terminal.id == null) {
            HashMap<String, String> consoleInfo = (HashMap<String, String>) rpcConnection.execute(RpcConstants.CONSOLE_CREATE);
            terminal.id = consoleInfo.get("id");
        }

        if (commandList.length() > 0) {
            String command = commandList.toString();
            commandList.setLength(0);
            rpcConnection.execute(RpcConstants.CONSOLE_WRITE, new Object[]{terminal.id, command});
            terminal.text.append(terminal.prompt);
            terminal.text.append(command);
        }

        HashMap<String, Object> consoleObject = (HashMap<String, Object>) rpcConnection.execute(RpcConstants.CONSOLE_READ, new Object[]{terminal.id});
        String prompt = (String) consoleObject.get("prompt");
        if (prompt != null) {
            prompt = prompt.replaceAll("\\x01|\\x02", "");
            terminal.prompt = prompt;
        }

        String data = (String) consoleObject.get("data");
        if (data != null) {
            terminal.text.append(data);
        }
        updateListeners();

        Boolean busy = (Boolean) consoleObject.get("busy");
        if (busy) {
            refreshAfterInterval(100);
        } else {
            refreshAfterInterval(POLLING_INTERVAL);
        }
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
