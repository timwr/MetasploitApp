package com.msf.metasploit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.msf.metasploit.MsfApplication;
import com.msf.metasploit.R;
import com.msf.metasploit.adapter.ServerListAdapter;
import com.msf.metasploit.model.MsfServer;

import java.util.List;

public class ServerActivity extends Activity {

    private ListView listviewServers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        List<MsfServer> serverList = MsfApplication.getMsfMain().getMsfRpcSessions();
        listviewServers = (ListView) findViewById(R.id.listview_servers);
        ListAdapter listAdapter = new ServerListAdapter(this, 0, 0, serverList);
        listviewServers.setAdapter(listAdapter);
    }

    public void clickAddServer(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}

