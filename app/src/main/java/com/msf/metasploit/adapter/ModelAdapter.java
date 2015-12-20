package com.msf.metasploit.adapter;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.msf.metasploit.model.RpcServer;

import java.util.List;

public class ModelAdapter {

    public static void buildDrawerFromModel(Drawer drawer) {

        drawer.removeAllItems();
        drawer.addItems(
                new PrimaryDrawerItem().withName("Name").withIcon(GoogleMaterial.Icon.gmd_sun).withIdentifier(1),
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

    public static IProfile[] buildProfiles(List<RpcServer> rpcServers) {
        IProfile[] profiles = new IProfile[rpcServers.size()];
        for (int i = 0; i < rpcServers.size(); i++) {
            RpcServer current = rpcServers.get(i);
            final IProfile profile = new ProfileDrawerItem().withName(current.getRpcServerName()).withIdentifier(current.uid);
            profiles[i] = profile;
        }
        return profiles;

    }
}
