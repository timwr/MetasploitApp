
package com.msf.metasploit.rpc;

// Credits to scriptjunkie and rsmudge

import com.msf.metasploit.model.MsfModel;
import com.msf.metasploit.model.RpcServer;

import java.io.IOException;

public class RpcConnection implements RpcConstants {

    private MsfRpc msfRpc = new MsfRpc();
    private MsfModel msfModel = new MsfModel();

    public void connect(RpcServer rpcServer) throws IOException {
        msfRpc = new MsfRpc();
        msfRpc.createURL(rpcServer.rpcHost, rpcServer.rpcPort, true);
        rpcServer.rpcToken = msfRpc.connect(rpcServer.rpcUser, rpcServer.rpcPassword);
    }

    public void updateModel() throws IOException {
        for (String command : new String[] { CONSOLE_LIST, JOB_LIST, SESSION_LIST }) {
            Object object = msfRpc.execute(command);
            msfModel.updateModel(command, object);
        }
    }

    public void update() {
        try {
            updateModel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MsfModel getModel() {
        return msfModel;
    }

    public Object execute(String command) throws IOException{
        return msfRpc.execute(command);
    }

    public Object execute(String command, Object[] args) throws IOException{
        return msfRpc.execute(command, args);
    }

    public void updateAsync() {
        new Async() {
            @Override
            protected Void doInBackground(Void... arg0) {
                update();
                return super.doInBackground(arg0);
            }
        };
    }

}
