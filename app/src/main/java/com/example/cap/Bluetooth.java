package com.example.cap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.example.cap.utils.ConnectedThread;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class Bluetooth extends AppCompatActivity {

    private void permissionOn() {
        String[] permission_list = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH,
//                Manifest.permission.BLUETOOTH_SCAN,
//                Manifest.permission.BLUETOOTH_CONNECT,
//                Manifest.permission.BLUETOOTH_ADVERTISE
        };
        ActivityCompat.requestPermissions(Bluetooth.this, permission_list, REQUEST_ENABLE_BT);
    }

    public void bluetoothOn() {
        String[] permission_list = {
                Manifest.permission.BLUETOOTH,
//                Manifest.permission.BLUETOOTH_SCAN,
//                Manifest.permission.BLUETOOTH_CONNECT
        };
        ActivityCompat.requestPermissions(Bluetooth.this, permission_list, REQUEST_ENABLE_BT);
    }
    public void bluetoothOn_real(){
        bluetoothOn_real();
    }

    TextView textStatus;
    Button btnParied, btnSearch, btnSend;
    ListView listView;

    ConnectedThread connectedThread;

    private byte[] readBuffer; // ?????? ??? ???????????? ???????????? ?????? ??????
    private int readBufferPosition; // ?????? ??? ?????? ?????? ??????

    Set<BluetoothDevice> pairedDevices;
    ArrayAdapter<String> btArrayAdapter;
    ArrayList<String> deviceAddressArray;

    BluetoothAdapter btAdapter;

    private final static int REQUEST_ENABLE_BT = 1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_bluetooth);

        permissionOn();

        // variables
        textStatus = (TextView) findViewById(R.id.text_status);
        btnParied = (Button) findViewById(R.id.btn_paired);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSend = (Button) findViewById(R.id.btn_send);
        listView = (ListView) findViewById(R.id.listview);

        // Enable bluetooth ???????????? ????????? ?????????
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                bluetoothOn();
            }
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // show paired devices
        btArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        deviceAddressArray = new ArrayList<>();
        listView.setAdapter(btArrayAdapter);
        listView.setOnItemClickListener(new myOnItemClickListener());
    }

    //????????? ???????????? ?????? ????????????
    public void onClickButtonPaired(View view) {
        btArrayAdapter.clear();
        if (deviceAddressArray != null && !deviceAddressArray.isEmpty()) {
            deviceAddressArray.clear();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list2 = {
                    Manifest.permission.BLUETOOTH
            };
            ActivityCompat.requestPermissions(Bluetooth.this, permission_list2, 1);
        }
        pairedDevices = btAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                btArrayAdapter.add(deviceName);
                deviceAddressArray.add(deviceHardwareAddress);
                btArrayAdapter.notifyDataSetChanged();
            }
        }
    }

    //?????? ????????????
    public void onClickButtonSearch(View view) {
        Toast.makeText(getApplicationContext(), "????????? ????????? ?????????.", Toast.LENGTH_LONG).show();
        // Check if the device is already discovering
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            bluetoothOn();
        }
        if (btAdapter.isDiscovering()) {
            btAdapter.cancelDiscovery();
        } else {
            if (btAdapter.isEnabled()) {
                btAdapter.startDiscovery();
                btArrayAdapter.clear();
                if (deviceAddressArray != null && !deviceAddressArray.isEmpty()) {
                    deviceAddressArray.clear();
                }
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(receiver, filter);
            } else {
                Toast.makeText(getApplicationContext(), "??????????????? ??????????????????.\n??????????????? ????????????.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //?????? ???????????? ?????????
    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                    bluetoothOn();
                }
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                if (deviceName != null) {
                    btArrayAdapter.add(deviceName);
                    deviceAddressArray.add(deviceHardwareAddress);
                    btArrayAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    //?????? ????????? ????????? ?????? ?????? ??????
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);
    }

    //????????? ?????? ??????
    private BluetoothSocket btSocket = null; // ???????????? ??????

    //????????? ????????? ??????
    public class myOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getApplicationContext(), btArrayAdapter.getItem(position), Toast.LENGTH_SHORT).show();

            textStatus.setText("??????: ?????????...");

            final String name = btArrayAdapter.getItem(position); // get name
            final String address = deviceAddressArray.get(position); // get address
            boolean flag = true;

            BluetoothDevice device = btAdapter.getRemoteDevice(address);

            UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            for (BluetoothDevice tempDevice : pairedDevices) {
                // ???????????? ????????? ????????? ?????? ??????????????? ???????????? ????????? ??????
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                    bluetoothOn();
                }
                if (btArrayAdapter.getItem(position).equals(tempDevice.getName())) {
                    device = tempDevice;
                    break;
                }
            }

            // create & connect socket
            try {
                btSocket = device.createRfcommSocketToServiceRecord(uuid);
                btSocket.connect();
                Toast.makeText(getApplicationContext(), "???????????? ?????? ??????!", Toast.LENGTH_LONG).show();
                btArrayAdapter.clear();
                btArrayAdapter.notifyDataSetChanged();
                //??? ???????????? ??????
//                Intent HomeIntent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivityForResult(HomeIntent, 123);
                // ????????? ???,?????? ???????????? ???????????????.
//                outputStream = bluetoothSocket.getOutputStream();
//                inputStream = btSocket.getInputStream();
                // ????????? ?????? ?????? ??????
//                receiveData();
            } catch (IOException e) {
                e.printStackTrace();
                textStatus.setText("??????: ?????? ??????, ?????? ????????? ?????????.");
                flag = false;
                Toast.makeText(getApplicationContext(), "???????????? ?????? ??????!", Toast.LENGTH_LONG).show();
            }

            if(flag){
                textStatus.setText("connected to "+name);
                connectedThread = new ConnectedThread(btSocket);
                connectedThread.start();
            }
        }
    }

    public void onClickButtonSend(View view){
        if(connectedThread!=null){ connectedThread.write("a"); }
    }
    public void onClickButtonRead(View view){
        if(connectedThread!=null){
//            String text = connectedThread.read() + "?????????.";
            String text = connectedThread.read() + "?????????.";
            Log.d("????????? ???: ", text); }
    }


    //???????????? ?????? ??????/?????? ????????? ?????? ??????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                //'??????'??? ?????? ??????
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "????????? ???????????????.", Toast.LENGTH_SHORT).show();
                }
                //'??????'??? ?????? ??????
                else {
                    Toast.makeText(getApplicationContext(), "????????? ???????????????.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}