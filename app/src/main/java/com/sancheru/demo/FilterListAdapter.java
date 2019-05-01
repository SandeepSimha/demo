package com.sancheru.demo;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * List filter items
 */
public class FilterListAdapter extends RecyclerView.Adapter<FilterListAdapter.FilterViewHolder> {
    private final Context mContext;
    private final List<BluetoothDevice> mLeDevices;

    FilterListAdapter(Context context, List<BluetoothDevice> bluetoothDevices) {
        this.mContext = context;
        this.mLeDevices = bluetoothDevices;
    }

    @Override
    public FilterListAdapter.FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FilterListAdapter.FilterViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_filter_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final FilterListAdapter.FilterViewHolder holder, final int position) {
        holder.mDeviceName.setText(mLeDevices.get(position).getName());
        holder.mDeviceId.setText(mLeDevices.get(position).getType());
        holder.mDeviceRssi.setText(mLeDevices.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mLeDevices.size();
    }

    public void addDevice(BluetoothDevice device) {
        if (!mLeDevices.contains(device)) {
            mLeDevices.add(device);
        }
    }

    /**
     * Filter item view holder class
     */
    class FilterViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView mDeviceName;
        TextView mDeviceId;
        TextView mDeviceRssi;

        FilterViewHolder(View rowView) {
            super(rowView);
            mView = rowView;
            mDeviceName = rowView.findViewById(R.id.device_name);
            mDeviceId = rowView.findViewById(R.id.device_id);
            mDeviceRssi = rowView.findViewById(R.id.device_rssi);
        }
    }
}