package com.msf.metasploit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.msf.metasploit.Msf;
import com.msf.metasploit.MsfServerList;
import com.msf.metasploit.R;
import com.msf.metasploit.model.RpcServer;
import com.msf.metasploit.view.RpcServerView;

public class ServerDetailActivity extends Activity implements MsfServerList.UpdateListener {

    private EditText edittextIp;
    private EditText edittextPort;
    private EditText edittextUser;
    private EditText edittextPass;

    private MsfServerList msfServerList;
    private RpcServer rpcServer;
    private RpcServerView rpcServerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        edittextIp = (EditText) findViewById(R.id.edittext_ip);
        edittextPort = (EditText) findViewById(R.id.edittext_port);
        edittextUser = (EditText) findViewById(R.id.edittext_user);
        edittextPass = (EditText) findViewById(R.id.edittext_pass);
        rpcServerView = (RpcServerView) findViewById(R.id.rpcserverview_server);

        msfServerList = Msf.get().msfServerList;
        rpcServer = msfServerList.fromIntent(getIntent());

        updateView(rpcServer);
    }

    @Override
    protected void onStart() {
        super.onStart();
        msfServerList.addListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        msfServerList.removeListener(this);
    }

    @Override
    public void onUpdated() {
        if (rpcServer.status == RpcServer.STATUS_AUTHORISED) {
            Intent intent = new Intent(this, MainActivity.class);
            MsfServerList.toIntent(intent, rpcServer);
            startActivity(intent);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateView(rpcServer);
            }
        });
    }

    private void updateView(RpcServer rpcServer) {
        rpcServerView.updateView(rpcServer);
        edittextIp.setText(rpcServer.rpcHost);
        edittextUser.setText(rpcServer.rpcUser);
        edittextPort.setText(String.valueOf(rpcServer.rpcPort));
        edittextPass.setText(rpcServer.rpcPassword);
    }

    /*
    private void showProgress() {
        new AlertDialog.Builder(this).setMessage("Connection error").show();

        Uri uri = intent.getData();
        if (uri != null) {
            String user = uri.getUserInfo();
            String host = uri.getHost();

            int port = uri.getPort();
            if (port == -1) {
                port = 55553;
            }
            edittextIp.setText(host);
            edittextUser.setText(user);
            edittextPort.setText(String.valueOf(port));
            edittextPass.setText(intent.getStringExtra(MsfController.PASSWORD));

            if (intent.getBooleanExtra(MsfController.CONNECT, false)) {
                connect(null);
            }
        }
    }
        */

    public void connect(View view) {
        Msf.get().msfServerList.connectAsync(rpcServer);
    }

}

