package com.msf.metasploit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.msf.metasploit.R;
import com.msf.metasploit.model.RpcServer;

public class RpcServerView extends FrameLayout {

    public RpcServerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public RpcServerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RpcServerView(Context context) {
        super(context);
        initView();
    }

    private TextView textviewName;
    private TextView textviewStatus;
    private FrameLayout layoutStatus;

    private void initView() {
        View view = inflate(getContext(), R.layout.view_item_server, null);
        textviewName = (TextView) view.findViewById(R.id.textview_name);
        textviewStatus = (TextView) view.findViewById(R.id.textview_status);
        layoutStatus = (FrameLayout) view.findViewById(R.id.layout_status);
        addView(view);
    }

    public void setRpcServer(RpcServer rpcServer) {
        textviewName.setText(rpcServer.getRpcServerName());
        if (rpcServer.getStatus() == RpcServer.STATUS_NEW) {
            textviewStatus.setText("");
        } else if (rpcServer.getStatus() == RpcServer.STATUS_CONNECTING) {
            textviewStatus.setText("Connecting...");
        } else {
            textviewStatus.setText("Status: " + rpcServer.getStatus());
        }
    }
}
