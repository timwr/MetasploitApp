package com.msf.metasploit.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.msf.metasploit.GCMController;
import com.msf.metasploit.R;
import com.msf.metasploit.model.Defaults;
import com.msf.metasploit.rpc.MsfController;
import com.msf.metasploit.rpc.RpcService;

public class LoginActivity extends Activity {

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
        edittextIp.setText(Defaults.DEFAULT_HOST);
        edittextUser.setText(Defaults.DEFAULT_USER);
        edittextPort.setText(Defaults.DEFAULT_PORT);
        edittextPass.setText(Defaults.DEFAULT_PASSWORD);

        GCMController.checkRegistration(this);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        if (MsfController.CONNECT.equals(intent.getAction())) {
            if (progressDialog != null) {
                progressDialog.cancel();
                progressDialog = null;
            }

            String result = intent.getStringExtra(MsfController.RESULT);
            if (result == null) {
//                startActivity(new Intent(this, ConsoleActivity.class));
                finish();
            } else {
                new AlertDialog.Builder(this).setMessage("Connection error: " + result).show();
            }
        }

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

    public void connect(View view) {
        Intent msgIntent = new Intent(this, RpcService.class);
        msgIntent.putExtra(MsfController.CONNECT, true);
        msgIntent.putExtra(MsfController.PASSWORD, edittextPass.getText()
                .toString());
        String uriString = "msf://" + edittextUser.getText() + "@"
                + edittextIp.getText() + ":" + edittextPort.getText();
        msgIntent.setData(Uri.parse(uriString));
        startService(msgIntent);

        progressDialog =  new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Connecting");
        progressDialog.show();
    }

}

