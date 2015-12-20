package com.msf.metasploit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.msf.metasploit.MsfApplication;
import com.msf.metasploit.R;
import com.msf.metasploit.adapter.ServerListAdapter;
import com.msf.metasploit.model.DefaultRpcServer;
import com.msf.metasploit.model.RpcServer;

import java.util.List;

public class ServerActivity extends Activity {

    private ListView listviewServers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        List<RpcServer> serverList = MsfApplication.Msf().getServerList();
        serverList.add(DefaultRpcServer.createDefaultRpcServer());

        listviewServers = (ListView) findViewById(R.id.listview_servers);
        ListAdapter listAdapter = new ServerListAdapter(this, 0, 0, serverList);
        listviewServers.setAdapter(listAdapter);
        listviewServers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (view.getId() == R.id.imageview_delete) {
                    System.err.println("lol");
                }
                startServerDetailActivity();
            }
        });
    }

    public void clickAddServer(View view) {
        startServerDetailActivity();
    }

    public void startServerDetailActivity() {
        Intent intent = new Intent(this, ServerDetailActivity.class);
        startActivity(intent);
    }
}

