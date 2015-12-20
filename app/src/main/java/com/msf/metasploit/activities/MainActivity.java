package com.msf.metasploit.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.KeyboardUtil;
import com.msf.metasploit.Msf;
import com.msf.metasploit.MsfServerList;
import com.msf.metasploit.R;
import com.msf.metasploit.adapter.ModelAdapter;
import com.msf.metasploit.fragments.ConsoleFragment;
import com.msf.metasploit.model.RpcServer;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_NEW_SERVER = 1;
    private static final int MODIFY_SERVER = 2;

    private Drawer drawer;
    private AccountHeader accountHeader;

    private MsfServerList msfServerList;
    private RpcServer rpcServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        msfServerList = Msf.get().msfServerList;
        rpcServer = msfServerList.fromIntent(getIntent());

        // Create the AccountHeader
        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .addProfiles(
                        new ProfileSettingDrawerItem().withName("Add RPC Server").withDescription("Add new RPC server").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_plus).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(ADD_NEW_SERVER),
                        new ProfileSettingDrawerItem().withName("Manage RPC Servers").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(MODIFY_SERVER)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        if (profile.getIdentifier() == ADD_NEW_SERVER) {
                            startActivity(new Intent(MainActivity.this, ServerActivity.class));
                        } else if (profile.getIdentifier() == MODIFY_SERVER) {
                            startActivity(new Intent(MainActivity.this, ServerActivity.class));
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();


        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withAccountHeader(accountHeader)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        return false;
                    }
                })
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        KeyboardUtil.hideKeyboard(MainActivity.this);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }
                })
                .withFireOnInitialOnClick(true)
                .withSavedInstance(savedInstanceState)
                .build();

        drawer.keyboardSupportEnabled(this, true);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        ModelAdapter.updateView(drawer, rpcServer);
        ModelAdapter.updateHeader(accountHeader, rpcServer, msfServerList);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = drawer.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void selectItem(int position) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment consoleFragment = ConsoleFragment.newInstance(null, rpcServer);
        ft.replace(R.id.frame_container, consoleFragment).commit();
        setTitle("Console");
    }

}
