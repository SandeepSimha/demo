package com.sancheru.demo;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    private static String TAG = "PermissionDemo";

    private RecyclerView mRecyclerView;
    private BluetoothAdapter myBluetoothAdapter;
    private ArrayAdapter<BLEData> BTArrayAdapter;
    private ListView listView;
    private List<BLEData> list = new ArrayList<>();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            showPermissionDialog(this,
                    Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_PERMISSION_REQUEST_CODE);
        }

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
            return "Device Name:" + name + "\nAddress:" + address + "\n" + "RSSI:" + rssi + " dBm";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.i(TAG, "Permission has been granted by user");
                    find();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }

    }

    public boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void showPermissionDialog(@NonNull Activity activity,
                                     @NonNull String permission,
                                     final int requestCode) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            // Add a dialog explaning why we need this permission.
            ActivityCompat.requestPermissions(activity,
                    new String[]{permission},
                    requestCode);
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{permission},
                    requestCode);
        }
    }
}
