package com.msf.metasploit.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.msf.metasploit.R;
import com.msf.metasploit.model.RpcServer;

import java.util.List;

public class ServerListAdapter extends ArrayAdapter<RpcServer> {

    private Activity activity;
    private List<RpcServer> innerList;

    public ServerListAdapter(Activity context, int resource, int textViewResourceId, List<RpcServer> objects) {
        super(context, resource, textViewResourceId, objects);
        activity = context;
        innerList = objects;
    }

    static class ViewHolder {
        public TextView textviewName;
        public TextView textviewStatus;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.item_server, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textviewName = (TextView) rowView.findViewById(R.id.textview_name);
            viewHolder.textviewStatus = (TextView) rowView.findViewById(R.id.textview_status);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        RpcServer item = innerList.get(position);
        holder.textviewName.setText(item.getRpcServerName());
        if (item.isAuthenticated()) {
            holder.textviewStatus.setText("Status: Authenticated");
        } else {
            holder.textviewStatus.setText("Status: Saved");
        }

        return rowView;
    }
}
