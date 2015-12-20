package com.msf.metasploit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

        msfServerList = Msf.get().msfServerList;
        final List<RpcServer> serverList = msfServerList.getServerList();
        if (serverList.size() == 0) {
            serverList.add(DefaultRpcServer.createDefaultRpcServer());
        }

        listviewServers = (ListView) findViewById(R.id.listview_servers);
        listAdapter = new ServerListAdapter(this, 0, 0, serverList);
        listviewServers.setAdapter(listAdapter);
        listviewServers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RpcServer rpcServer = serverList.get(i);
                startServerDetailActivity(rpcServer);
            }
        });
    }

    private void updateView() {
        listAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateView();
        msfServerList.addListener(this);
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
        startServerDetailActivity(null);
    }

    public void startServerDetailActivity(RpcServer rpcServer) {
        Intent intent = new Intent(this, ServerDetailActivity.class);
        if (rpcServer != null) {
            intent.putExtra(ServerDetailActivity.RPC_SERVER_ID, rpcServer.uid);
        }
        startActivity(intent);
    }
}

