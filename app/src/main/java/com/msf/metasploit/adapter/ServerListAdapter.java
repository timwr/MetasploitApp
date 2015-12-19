package com.msf.metasploit.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.msf.metasploit.R;
import com.msf.metasploit.model.MsfServer;

import java.util.List;

public class ServerListAdapter extends ArrayAdapter<MsfServer> {

    private Activity activity;
    private List<MsfServer> innerList;

    public ServerListAdapter(Activity context, int resource, int textViewResourceId, List<MsfServer> objects) {
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
        MsfServer item = innerList.get(position);
        holder.textviewName.setText(item.rpcAddress);
        if (item.rpcToken != null) {
            holder.textviewStatus.setText("Status: ");
        } else {
            holder.textviewStatus.setText("");
        }

        return rowView;
    }
}
