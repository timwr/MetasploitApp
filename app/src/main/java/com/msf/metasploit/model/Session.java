package com.msf.metasploit.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Session extends RpcObject {
    String id;
    Map fields;
    public Session(String id, Map fields) {
        this.id = id;
        this.fields = fields;
    }
    @Override
    public String toString() {
        return id + fields;
    }
    
    public static List<Session> getList(Object o) {
        List<Session> sessions = new ArrayList<Session>();
        Map<String, Map> consoleMap = (Map<String, Map>) o;
        for (String key : consoleMap.keySet()) {
            Map<String, String> consoleResult = (Map<String, String>) consoleMap.get(key);
            sessions.add(new Session(key, consoleMap.get(key)));
        }
        return sessions;
    }
}
