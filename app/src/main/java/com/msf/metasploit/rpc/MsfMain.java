
package com.msf.metasploit.rpc;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msf.metasploit.MsfApplication;
import com.msf.metasploit.model.MsfServer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MsfMain {

    private static final String MSF_MAIN = "msfMain";
    private static final String MSF_RPC_SESSIONS = "msfRpcSessions";

    private final Gson gson;
    private final SharedPreferences preferences;

    private List<MsfServer> msfRpcSessions;

    public MsfMain() {
        this.gson = new Gson();
        this.preferences = MsfApplication.getApplication().getSharedPreferences(MSF_MAIN, Context.MODE_PRIVATE);
        load();
    }

    public List<MsfServer> getMsfRpcSessions() {
        return msfRpcSessions;
    }

    public MsfServer getMsfSession(String id) {
        for (int i = 0; i < msfRpcSessions.size(); i++) {
            MsfServer current = msfRpcSessions.get(i);
            if (current.id.equals(id)) {
                return current;
            }
        }
        return null;
    }

    public String addMsfSession(MsfServer msfServer) {
        if (msfServer.id == null) {
            msfServer.id = String.valueOf(System.currentTimeMillis());
            msfRpcSessions.add(msfServer);
        } else {
            MsfServer existing = getMsfSession(msfServer.id);
            existing.rpcAddress = msfServer.rpcAddress;
            existing.rpcToken = msfServer.rpcToken;
        }
        save();
        return msfServer.id;
    }

    public void load() {
        String jsonSessions = preferences.getString(MSF_RPC_SESSIONS, null);
        if (jsonSessions == null) {
            msfRpcSessions = new ArrayList<>();
        } else {
            Type listType = new TypeToken<ArrayList<MsfServer>>() { }.getType();
            msfRpcSessions = gson.fromJson(jsonSessions, listType);
        }
    }

    public void save() {
        String jsonSession = gson.toJson(msfRpcSessions);
        preferences.edit().putString(MSF_RPC_SESSIONS, jsonSession).apply();
    }
}
