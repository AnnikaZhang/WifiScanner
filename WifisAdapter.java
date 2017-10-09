package com.example.annika.wifiscanner;

import java.util.List;


import android.net.wifi.ScanResult;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class WifisAdapter extends BaseAdapter {

    private List<ScanResult> array;

    public WifisAdapter(List<ScanResult> wifis) {
        this.array = wifis;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public ScanResult getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            v = View.inflate(parent.getContext(), R.layout.listview_item, null);
        }

        ViewHolder holder = (ViewHolder) v.getTag();

        if (holder == null) {
            holder = new ViewHolder(v);
            v.setTag(holder);
        }

        ScanResult info = getItem(position);
        holder.tv_name.setText(info.SSID);
        holder.tv_mac.setText(info.BSSID);

        return v;
    }

    class ViewHolder {
    	TextView tv_name;
        TextView tv_mac;

        public ViewHolder(View v) {
            tv_name = (TextView) v.findViewById(R.id.wifi_name);
            tv_mac = (TextView) v.findViewById(R.id.wifi_bssid);
        }
    }

}

