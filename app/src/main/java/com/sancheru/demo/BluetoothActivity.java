package com.sancheru.demo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Ramesh on 02/05/19.
 */
public class BluetoothActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private BluetoothAdapter myBluetoothAdapter;
    private ArrayAdapter<BLEData> BTArrayAdapter;
    private ListView listView;
    private List<BLEData> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.devices_list_view);
        listView = findViewById(R.id.listView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), manager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        //
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BTArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(BTArrayAdapter);
        find();
    }

    public void find() {
        if (myBluetoothAdapter.isDiscovering()) {
            // the button is pressed when it discovers, so cancel the discovery
            myBluetoothAdapter.cancelDiscovery();
        } else {
            BTArrayAdapter.clear();
            myBluetoothAdapter.startDiscovery();

            registerReceiver(bReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        }
    }


    final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                // add the name and the MAC address of the object to the arrayAdapter
//                BTArrayAdapter.add(device.getName() + "\n" + device.getAddress() + "\n" + "RSSI:" + rssi);
                list.add(new BLEData(device.getName(), device.getAddress(), rssi));
                Collections.sort(list, new Comparator<BLEData>() {
                    @Override
                    public int compare(BLEData o1, BLEData o2) {
                        return o2.rssi - o1.rssi;
                    }
                });
                BTArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    public class BLEData {
        String name;
        String address;
        int rssi;

        BLEData(String name, String address, int rssi) {
            this.name = name;
            this.address = address;
            this.rssi = rssi;
        }


        @Override
        public String toString() {
            return "Device Name:" + name + "\nAddress:" + address + "\n" + "RSSI:" + rssi +" dBm";
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(bReceiver);
    }
}
