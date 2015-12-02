
package com.msf.metasploit.rpc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.msf.metasploit.activities.LoginActivity;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;

public class MsfController {
    public static final String CONNECT = "connect";
    public static final String PASSWORD = "password";
    public static final String RESULT = "result";
    public static final String CMD = "cmd";
    public static final String ARGS = "args";
    public static final String TAG = "tag";

    public static void registerListener(Context context, String action, BroadcastReceiver responseListener) {
        LocalBroadcastManager.getInstance(context).registerReceiver(responseListener, new IntentFilter(action));
    }

    public static void unregisterListener(Context context, BroadcastReceiver responseListener) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(responseListener);
    }

    public static Intent getIntent(String cmd, Object args, String tag) {
        Intent intent = new Intent();
        intent.putExtra(CMD, cmd);
        intent.putExtra(MsfController.ARGS, (Serializable) args);
        intent.putExtra(MsfController.TAG, tag);
        return intent;
    }

    public static void runCmdAsync(Context context, Intent cmdIntent) {
        Intent intent = new Intent(context, RpcService.class);
        intent.putExtras(cmdIntent);
        context.startService(intent);
    }

    public Intent runCmd(String cmd) {
        return runCmd(cmd, null);
    }

    public Intent runCmd(String cmd, Object args) {
        return runCmd(getIntent(cmd, args, null));
    }

    public Intent runCmd(Intent intent) {
        String tag = intent.getStringExtra(TAG);
        String cmd = intent.getStringExtra(CMD);
        Object[] args = (Object[]) intent.getSerializableExtra(ARGS);
        if (tag == null) {
            tag = cmd;
        }
        try {
            String argstring = null;
            if (args != null) {
                for (Object a : args) {
                    argstring += " " + a;
                }
            }
            Log.e(MsfController.class.getSimpleName(), cmd + " " + argstring);
            Object response = args != null ? msgRpcImpl.execute(cmd, args)
                    : msgRpcImpl.execute(cmd);
            Log.e(MsfController.class.getSimpleName(), MsfController.RESULT + tag + "" + response);
            Intent result = new Intent(tag);
            result.putExtra(CMD, cmd);
            result.putExtra(RESULT, (Serializable) response);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MsfController mInstance;

    private MsfController() {
    }

    public static MsfController getInstance() {
        if (mInstance == null) {
            mInstance = new MsfController();
        }
        return mInstance;
    }

    private MsgRpcImpl msgRpcImpl = null;

    public MsgRpcImpl getMsgRpcImpl() {
        return msgRpcImpl;
    }

    public void connect(String host, int port, String username,
            String password, boolean ssl) {
        try {
            msgRpcImpl = new MsgRpcImpl(username, password, host, port, ssl,
                    false);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void startMsf(String host, int port, String username,
            String password, boolean ssl) {
        Log.e("e", "startMsf");
        try {
            connect(host, port, username, password, ssl);
            
            Log.e("e", "connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleIntent(Intent intent, Context context) {
        if (intent.getBooleanExtra(CONNECT, false)) {
            Uri uri = intent.getData();
            String username = uri.getUserInfo();
            String password = intent.getStringExtra(PASSWORD);
            String host = uri.getHost();
            int port = uri.getPort();
            if (port == -1) {
                port = 55553;
            }
            boolean useSSL = true;// uri.getScheme();

            startMsf(host, port, username, password, useSSL);
            
            Intent rpcintent = new Intent(context, LoginActivity.class);
            rpcintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            rpcintent.setAction(CONNECT);
            rpcintent.putExtra(RESULT, (msgRpcImpl != null) ? null : "-1");
            context.startActivity(rpcintent);
            
        } else {

            Intent result = runCmd(intent);
            LocalBroadcastManager.getInstance(context).sendBroadcast(result);
        }
    }
}
