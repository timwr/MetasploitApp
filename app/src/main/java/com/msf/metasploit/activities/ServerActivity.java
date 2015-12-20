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
import com.msf.metasploit.model.MsfServer;
import com.msf.metasploit.rpc.MsfMain;

public class ServerActivity extends Activity {

    private MsfMain msfMain;
    private ListView listviewServers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        listviewServers = (ListView) findViewById(R.id.listview_servers);
        msfMain = MsfApplication.getMsfMain();
        ListAdapter listAdapter = new ServerListAdapter(this, 0, 0, msfMain.getMsfRpcSessions());
        listviewServers.setAdapter(listAdapter);
        listviewServers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (view.getId() == R.id.imageview_delete) {
                    System.err.println("lol");
                }
                MsfServer msfServer = msfMain.getMsfRpcSessions().get(i);
                startServerDetailActivity(msfServer);
            }
        });
    }

    public void clickAddServer(View view) {
        startServerDetailActivity(new MsfServer());
    }

    public void startServerDetailActivity(MsfServer msfServer) {
        Intent intent = new Intent(this, ServerDetailActivity.class);
        intent.putExtra(MsfServer.MSF_SERVER, msfServer.id);
        startActivity(intent);
    }
}

