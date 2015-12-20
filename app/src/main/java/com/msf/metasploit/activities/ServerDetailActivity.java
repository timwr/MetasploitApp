package com.msf.metasploit.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.msf.metasploit.Msf;
import com.msf.metasploit.R;
import com.msf.metasploit.model.RpcServer;
import com.msf.metasploit.rpc.Async;

public class ServerDetailActivity extends Activity {

    public static final String RPC_SERVER_ID = "rpc_server_id";

    private EditText edittextIp;
    private EditText edittextPort;
    private EditText edittextUser;
    private EditText edittextPass;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        edittextIp = (EditText) findViewById(R.id.edittext_ip);
        edittextPort = (EditText) findViewById(R.id.edittext_port);
        edittextUser = (EditText) findViewById(R.id.edittext_user);
        edittextPass = (EditText) findViewById(R.id.edittext_pass);

        int rpcServerId = getIntent().getIntExtra(RPC_SERVER_ID, 0);
        if (rpcServerId != 0) {
            for (RpcServer rpcServer : Msf.get().getServerList()) {
                if (rpcServer.uid == rpcServerId) {
                    updateView(rpcServer);
                }
            }
        }
    }

    private void updateView(RpcServer rpcServer) {
        edittextIp.setText(rpcServer.rpcHost);
        edittextUser.setText(rpcServer.rpcUser);
        edittextPort.setText(String.valueOf(rpcServer.rpcPort));
        edittextPass.setText("");
    }

    private void showProgress() {
        if (progressDialog != null) {
            progressDialog.cancel();
            progressDialog = null;
        }

        new AlertDialog.Builder(this).setMessage("Connection error").show();

    /*
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
        */
    }

    public void connect(View view) {
        progressDialog =  new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Connecting");
        progressDialog.show();

        new Async() {
            @Override
            protected Void doInBackground(Void... arg0) {
                return super.doInBackground(arg0);
            }
        }.execute();
    }

}

