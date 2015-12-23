package com.msf.metasploit.adapter;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.msf.metasploit.MsfApplication;
import com.msf.metasploit.MsfServerList;
import com.msf.metasploit.R;
import com.msf.metasploit.model.Console;
import com.msf.metasploit.model.RpcServer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelAdapter {

    public static int ID_LOADING = 0;
    public static int ID_NEW_CONSOLE = 1;

    public static HashMap<Integer, Object> updateView(Drawer drawer, RpcServer rpcServer) {
        drawer.removeAllItems();
        HashMap<Integer,Object> itemMap = new HashMap<>();
        List<Console> consoles = rpcServer.getModel().getConsoles();
        if (consoles == null) {
            drawer.addItems(
                    new PrimaryDrawerItem().withName("Loading...").withIcon(GoogleMaterial.Icon.gmd_refresh).withIdentifier(ID_LOADING)
            );
            return itemMap;
        }

        int identifier = ID_NEW_CONSOLE;
        String consoleString = "Console (" + consoles.size() + ")";
        drawer.addItem(new PrimaryDrawerItem().withName(consoleString).withIcon(GoogleMaterial.Icon.gmd_format_playlist_add).withIdentifier(identifier));
        for (Console console : consoles) {
            identifier++;
            String consoleName = "Console: " + console.id;
            itemMap.put(identifier, console);
            drawer.addItem(new SecondaryDrawerItem().withName(consoleName).withLevel(2).withIcon(GoogleMaterial.Icon.gmd_format_playlist_add).withIdentifier(identifier));
        }

        Map<String, Map> sessions = rpcServer.getModel().getSessions();
        if (sessions != null && sessions.keySet().size() > 0) {
            drawer.addItem(new PrimaryDrawerItem().withName("Sessions").withIcon(GoogleMaterial.Icon.gmd_format_playlist_add).withIdentifier(++identifier));
            for (String id : sessions.keySet()) {
                String consoleName = "Session: " + id;
                itemMap.put(identifier, id);
                drawer.addItem(new SecondaryDrawerItem().withName(consoleName).withLevel(2).withIcon(GoogleMaterial.Icon.gmd_format_playlist_add).withIdentifier(++identifier));
            }
        }
        return itemMap;
    }

    public static void updateHeader(AccountHeader accountHeader, int currentId, MsfServerList msfServerList) {
        List<RpcServer> rpcServers = msfServerList.getServerList();
        IProfile active = null;
        IProfile[] profiles = new IProfile[rpcServers.size()];
        for (int i = 0; i < rpcServers.size(); i++) {
            RpcServer current = rpcServers.get(i);
            final IProfile profile = new ProfileDrawerItem().withName(current.getRpcServerName()).withIdentifier(i);
            if (currentId == i) {
                active = profile;
            }
            profiles[i] = profile;
        }
        accountHeader.clear();
        accountHeader.addProfiles(profiles);
        if (active != null) {
            accountHeader.setActiveProfile(active);
        }
        accountHeader.addProfiles(
                new ProfileSettingDrawerItem().withName("Add RPC Server").withDescription("Add new RPC server").withIcon(new IconicsDrawable(MsfApplication.getApplication(), GoogleMaterial.Icon.gmd_plus).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(0),
                new ProfileSettingDrawerItem().withName("Manage RPC Servers").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(2)
        );
    }

}
