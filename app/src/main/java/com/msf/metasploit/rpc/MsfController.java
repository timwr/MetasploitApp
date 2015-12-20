
package com.msf.metasploit.rpc;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.msf.metasploit.model.MsfServer;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class MsfController {

    public static final String CONNECT = "connect";
    public static final String PASSWORD = "password";
    public static final String RESULT = "result";
    public static final String CMD = "cmd";
    public static final String ARGS = "args";
    public static final String TAG = "tag";

    private MsfMain msfMain;
    private HashMap<String, MsfRpc> rpcConnections = new HashMap<>();

    public MsfController(MsfMain msfMain) {
        this.msfMain = msfMain;
    }

    public void handleIntent(Intent intent) {
        String msfServerId = intent.getStringExtra(MsfServer.MSF_SERVER);

        if (!rpcConnections.containsKey(msfServerId)) {
            MsfServer msfServer = msfMain.getMsfSession(msfServerId);
            MsfRpc msfRpc = new MsfRpc(msfServer);
            rpcConnections.put(msfServerId, msfRpc);
        }

        runCmd(intent);
    }

//    public static void registerListener(Context context, String action, BroadcastReceiver responseListener) {
//        LocalBroadcastManager.getInstance(context).registerReceiver(responseListener, new IntentFilter(action));
//    }
//
//    public static void unregisterListener(Context context, BroadcastReceiver responseListener) {
//        LocalBroadcastManager.getInstance(context).unregisterReceiver(responseListener);
//    }

    public static Intent getConnectIntent(String msfServerId, String username, String password) {
        Intent intent = getIntent(CONNECT, new Object[]{ username, password }, null);
        intent.putExtra(MsfServer.MSF_SERVER, msfServerId);
        return intent;
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
        String msfServerId = intent.getStringExtra(MsfServer.MSF_SERVER);
        MsfRpc msfRpc = rpcConnections.get(msfServerId);
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
            Object response = msfRpc.execute(cmd, args);
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
//
//    public void connect(String host, int port, String username, String password, boolean ssl) {
//        MsfRpc msfRpc = new MsfRpc();
//        msfRpc.createURL(host, port, ssl);
//        msfRpc.connect(username, password);
//        msgRpcImpl = msfRpc;
//    }
//
//    public void startMsf(String host, int port, String username, String password, boolean ssl) {
//        try {
//            connect(host, port, username, password, ssl);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void handleIntent(Intent intent, Context context) {
//        if (intent.getBooleanExtra(CONNECT, false)) {
//
//            Uri uri = intent.getData();
//            String username = uri.getUserInfo();
//            String password = intent.getStringExtra(PASSWORD);
//            String host = uri.getHost();
//            int port = uri.getPort();
//            if (port == -1) {
//                port = 55553;
//            }
//            boolean useSSL = true;// uri.getScheme();
//
//            startMsf(host, port, username, password, useSSL);
//
//            Intent rpcintent = new Intent(context, ServerDetailActivity.class);
//            rpcintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            rpcintent.setAction(CONNECT);
//            rpcintent.putExtra(RESULT, (msgRpcImpl != null) ? null : "-1");
//            context.startActivity(rpcintent);
//
//        } else {
//
//            Intent result = runCmd(intent);
//            LocalBroadcastManager.getInstance(context).sendBroadcast(result);
//        }
//    }
}
