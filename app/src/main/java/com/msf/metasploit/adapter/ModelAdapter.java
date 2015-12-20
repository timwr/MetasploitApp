package com.msf.metasploit.adapter;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
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

import java.util.List;

public class ModelAdapter {

    public static void updateView(Drawer drawer, RpcServer rpcServer) {
        drawer.removeAllItems();

        List<Console> consoles = rpcServer.getModel().getConsoles();
        if (consoles == null) {
            drawer.addItems(
                    new PrimaryDrawerItem().withName("Loading...").withIcon(GoogleMaterial.Icon.gmd_sun).withIdentifier(1)
            );
            return;
        }

        String consoleString = "Console (" + consoles.size() + ")";
        drawer.addItems(
                new PrimaryDrawerItem().withName(consoleString).withIcon(GoogleMaterial.Icon.gmd_sun).withIdentifier(1),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_action_bar_drawer).withIcon(FontAwesome.Icon.faw_home).withBadge("22").withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)).withIdentifier(2).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_multi_drawer).withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(3),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_non_translucent_status_drawer).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(4),
//                        new PrimaryDrawerItem().withDescription("A more complex sample").withName(R.string.drawer_item_advanced_drawer).withIcon(GoogleMaterial.Icon.gmd_adb).withIdentifier(5),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_keyboard_util_drawer).withIcon(GoogleMaterial.Icon.gmd_labels).withIdentifier(6),
//                        new SectionDrawerItem().withName(R.string.drawer_item_section_header),
                new SecondaryDrawerItem().withName("String lol").withIcon(FontAwesome.Icon.faw_github),
                new SecondaryDrawerItem().withName("String hello").withIcon(GoogleMaterial.Icon.gmd_format_color_fill).withTag("Bullhorn")
        );
    }

    public static void updateHeader(AccountHeader accountHeader, RpcServer rpcServer, MsfServerList msfServerList) {
        List<RpcServer> rpcServers = msfServerList.getServerList();
        IProfile active = null;
        IProfile[] profiles = new IProfile[rpcServers.size()];
        for (int i = 0; i < rpcServers.size(); i++) {
            RpcServer current = rpcServers.get(i);
            final IProfile profile = new ProfileDrawerItem().withName(current.getRpcServerName()).withIdentifier(current.uid);
            if (current.uid == rpcServer.uid) {
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
