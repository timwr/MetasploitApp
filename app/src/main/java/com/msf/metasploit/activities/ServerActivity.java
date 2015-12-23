package com.msf.metasploit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.msf.metasploit.BuildConfig;
import com.msf.metasploit.Msf;
import com.msf.metasploit.MsfServerList;
import com.msf.metasploit.R;
import com.msf.metasploit.adapter.ServerListAdapter;
import com.msf.metasploit.model.DefaultRpcServer;
import com.msf.metasploit.model.RpcServer;

import java.util.List;

public class ServerActivity extends Activity implements MsfServerList.UpdateListener {

    private ListView listviewServers;
    private ServerListAdapter listAdapter;

    private MsfServerList msfServerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        listviewServers = (ListView) findViewById(R.id.listview_servers);

        msfServerList = Msf.get().msfServerList;

        final List<RpcServer> serverList = msfServerList.getServerList();
        listAdapter = new ServerListAdapter(this, serverList);
        listviewServers.setAdapter(listAdapter);
        listviewServers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RpcServer rpcServer = msfServerList.getRpcServer(i);
                if (rpcServer.status == RpcServer.STATUS_AUTHORISED) {
                    Intent intent = new Intent(ServerActivity.this, MainActivity.class);
                    intent.putExtra(MsfServerList.RPC_SERVER_ID, i);
                    startActivity(intent);
                    finish();
                } else if (rpcServer.status != RpcServer.STATUS_CONNECTING) {
                    msfServerList.connectAsync(rpcServer);
                }
            }
        });

        if (BuildConfig.DEBUG) {
//            RpcServer emulator = DefaultRpcServer.createDefaultRpcServer("10.0.2.2");
            RpcServer autoConnect = DefaultRpcServer.createDefaultRpcServer();
            msfServerList.getServerList().add(autoConnect);
            msfServerList.connectAsync(autoConnect);
            startServerDetailActivity(0);
        }
    }

    private void updateView() {
        listAdapter.updateView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        msfServerList.addListener(this);
        updateView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        msfServerList.removeListener(this);
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

    public void clickAddServer(View view) {
        startServerDetailActivity(-1);
    }

    public void startServerDetailActivity(int rpcServer) {
        Intent intent = new Intent(this, ServerDetailActivity.class);
        intent.putExtra(MsfServerList.RPC_SERVER_ID, rpcServer);
        startActivity(intent);
    }
}

