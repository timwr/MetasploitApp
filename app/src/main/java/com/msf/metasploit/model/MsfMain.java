
package com.msf.metasploit.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MsfMain {

    private static final String MSF_RPC_SESSIONS = "msfRpcSessions";
    private static final String MSF_MAIN = "msfMain";

    private final Gson gson;
    private SharedPreferences preferences;

    public List<MsfServer> msfRpcSessions;

    public MsfMain(Context context) {
        this.gson = new Gson();
        this.preferences = context.getSharedPreferences(MSF_MAIN, Context.MODE_PRIVATE);
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

    private void load() {
        String jsonSessions = preferences.getString(MSF_RPC_SESSIONS, null);
        if (jsonSessions == null) {
            msfRpcSessions = new ArrayList<>();
        } else {
            Type listType = new TypeToken<ArrayList<MsfServer>>() { }.getType();
            msfRpcSessions = gson.fromJson(jsonSessions, listType);
        }
    }

    private void save() {
        String jsonSession = gson.toJson(msfRpcSessions);
        preferences.edit().putString(MSF_RPC_SESSIONS, jsonSession).apply();
    }

}
