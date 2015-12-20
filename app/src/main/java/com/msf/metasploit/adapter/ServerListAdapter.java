package com.msf.metasploit.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.msf.metasploit.Msf;
import com.msf.metasploit.R;
import com.msf.metasploit.model.RpcServer;
import com.msf.metasploit.view.RpcServerView;

import java.util.List;

public class ServerListAdapter extends ArrayAdapter<RpcServer> {

    private Activity activity;
    private List<RpcServer> innerList;

    public ServerListAdapter(Activity context) {
        super(context, 0);
        activity = context;
    }

    private View.OnClickListener onItemClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();
            if (view.getId() == R.id.imageview_delete) {
                innerList.remove(position);
                notifyDataSetChanged();
            }
        }
    };

    static class ViewHolder {
        RpcServerView rpcServerView;
        ImageView imageviewEdit;
        ImageView imageviewDelete;
    }

    public void updateView() {
        innerList = Msf.get().msfServerList.getServerList();
        super.notifyDataSetInvalidated();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.item_server, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.rpcServerView = (RpcServerView) rowView.findViewById(R.id.rpcserverview_server);
            viewHolder.imageviewEdit = (ImageView) rowView.findViewById(R.id.imageview_edit);
            viewHolder.imageviewDelete = (ImageView) rowView.findViewById(R.id.imageview_delete);
            viewHolder.imageviewEdit.setOnClickListener(onItemClicked);
            viewHolder.imageviewDelete.setOnClickListener(onItemClicked);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        RpcServer item = innerList.get(position);
        holder.rpcServerView.updateView(item);
        holder.imageviewDelete.setTag(position);
        holder.imageviewEdit.setTag(position);
        return rowView;
    }
}
