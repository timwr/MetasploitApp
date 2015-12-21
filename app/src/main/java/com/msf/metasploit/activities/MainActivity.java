package com.msf.metasploit.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.msf.metasploit.Msf;
import com.msf.metasploit.MsfServerList;
import com.msf.metasploit.R;
import com.msf.metasploit.adapter.ModelAdapter;
import com.msf.metasploit.adapter.ModelPresenter;
import com.msf.metasploit.fragments.TerminalFragment;
import com.msf.metasploit.model.RpcServer;

public class MainActivity extends AppCompatActivity implements ModelPresenter.UpdateListener {

    private static final int ADD_NEW_SERVER = 1;
    private static final int MODIFY_SERVER = 2;

    private Drawer drawer;
    private AccountHeader accountHeader;

    private ModelPresenter modelPresenter;
    private MsfServerList msfServerList;
    private RpcServer rpcServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the AccountHeader
        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .withHeaderBackground(R.drawable.side_nav_bar)
//                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
//                    @Override
//                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
//                        if (profile.getIdentifier() == ADD_NEW_SERVER) {
//                            startActivity(new Intent(MainActivity.this, ServerActivity.class));
//                        } else if (profile.getIdentifier() == MODIFY_SERVER) {
//                            startActivity(new Intent(MainActivity.this, ServerActivity.class));
//                        }
//                        return false;
//                    }
//                })
                .withSavedInstance(savedInstanceState)
                .build();


        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
//                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                    @Override
//                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                        return false;
//                    }
//                })
//                .withOnDrawerListener(new Drawer.OnDrawerListener() {
//                    @Override
//                    public void onDrawerOpened(View drawerView) {
//                        KeyboardUtil.hideKeyboard(MainActivity.this);
//                    }
//
//                    @Override
//                    public void onDrawerClosed(View drawerView) {
//
//                    }
//
//                    @Override
//                    public void onDrawerSlide(View drawerView, float slideOffset) {
//
//                    }
//                })
                .withHasStableIds(false)
                .withFireOnInitialOnClick(true)
                .withSavedInstance(savedInstanceState)
                .build();

        drawer.keyboardSupportEnabled(this, true);

        msfServerList = Msf.get().msfServerList;
        rpcServer = msfServerList.fromIntent(getIntent());
        modelPresenter = new ModelPresenter();
        modelPresenter.setConnection(rpcServer.getRpc());

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateView();
        modelPresenter.update();
        modelPresenter.addListener(this);
    }

    private void updateView() {
        ModelAdapter.updateHeader(accountHeader, rpcServer, msfServerList);
        ModelAdapter.updateView(drawer, rpcServer);
    }

    @Override
    protected void onStop() {
        modelPresenter.removeListener(this);
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = drawer.saveInstanceState(outState);
        outState = accountHeader.saveInstanceState(outState);
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
        Fragment consoleFragment = TerminalFragment.newInstance(null, rpcServer);
        ft.replace(R.id.frame_container, consoleFragment).commit();
        setTitle("Console");
    }

    @Override
    public void onUpdated() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateView();
            }
        });
    }
}
