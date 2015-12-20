
package com.msf.metasploit.model;

import com.msf.metasploit.rpc.RpcConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MsfModel {

    private List<Console> consoles;
    private List<Job> jobs;
    private Map<String, Object> jobmap;
    private List<Session> session;

    private Module module = new Module();
    private List<Plugin> pluginlist;
    private List<String> plugins;
//    pluginlist = new ArrayList<Plugin>();
//    pluginlist.add(new Plugin("gcm_notify", "New session notification", new Intent()));
//    pluginlist.add(new Plugin("auto_add_route", "Auto route new sessions", null));

    public List<Job> getJobs() {
        List<Job> list = new ArrayList<Job>();
        Map<String, Object> jobMap = jobmap;
        for (String key : jobMap.keySet()) {
            Object job = jobMap.get(key);
            String consoleResult = String.valueOf(job);
            Job l = new Job();
            l.id = key;
            l.name = consoleResult;
            list.add(l);
        }
        return list;
    }

    public Object updateModel(String cmd, Object object) {
        if (cmd.equals(RpcConstants.PLUGIN_LOADED)) {
            plugins = (List<String>) ((Map)object).get("plugins");
            for (Plugin plugin : pluginlist) {
                plugin.enabled = plugins.contains(plugin.id);
            }
        } else if (cmd.equals(RpcConstants.MODULE_POST) || cmd.equals(RpcConstants.MODULE_AUXILIARY) || cmd.equals(RpcConstants.MODULE_EXPLOITS) || cmd.equals(RpcConstants.MODULE_PAYLOADS)) {
            object = ((Map)object).get("modules");
        } else if (cmd.equals(RpcConstants.MODULE_INFO)) {
            module.info = (Map<String, Map<String, Object>>) object;
        } else if (cmd.equals(RpcConstants.MODULE_COMPATIBLE_PAYLOADS)) {
            module.payloads = (List<String>) ((Map)object).get("payloads");
        } else if (cmd.equals(RpcConstants.MODULE_OPTIONS)) {
            ModuleOption.addModuleOptions(module, object);
        } else if (cmd.equals(RpcConstants.JOB_LIST)) {
            jobmap = (Map) object;
        } else if (cmd.equals(RpcConstants.JOB_INFO)) {
            Map map = (Map) object;
            Object id = map.get("jid");
            jobmap.put(String.valueOf(id), map);
        }

        return object;
    }
}
