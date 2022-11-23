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

    private byte[] readBuffer; // 수신 된 문자열을 저장하기 위한 버퍼
    private int readBufferPosition; // 버퍼 내 문자 저장 위치

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

        // Enable bluetooth 블루투스 활성화 시키기
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

    //페어링 되어있는 기기 보여주기
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

    //기기 검색하기
    public void onClickButtonSearch(View view) {
        Toast.makeText(getApplicationContext(), "조금만 기다려 주세요.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(), "블루투스가 꺼져있습니다.\n블루투스를 켜주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //기기 검색하기 이어서
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

    //기기 검색시 사용한 자원 할당 해제
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);
    }

    //통신을 위한 소켓
    private BluetoothSocket btSocket = null; // 블루투스 소켓

    //선택한 기기와 연결
    public class myOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getApplicationContext(), btArrayAdapter.getItem(position), Toast.LENGTH_SHORT).show();

            textStatus.setText("상태: 연결중...");

            final String name = btArrayAdapter.getItem(position); // get name
            final String address = deviceAddressArray.get(position); // get address
            boolean flag = true;

            BluetoothDevice device = btAdapter.getRemoteDevice(address);

            UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            for (BluetoothDevice tempDevice : pairedDevices) {
                // 사용자가 선택한 이름과 같은 디바이스로 설정하고 반복문 종료
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
                Toast.makeText(getApplicationContext(), "블루투스 연결 성공!", Toast.LENGTH_LONG).show();
                btArrayAdapter.clear();
                btArrayAdapter.notifyDataSetChanged();
                //홈 화면으로 가기
//                Intent HomeIntent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivityForResult(HomeIntent, 123);
                // 데이터 송,수신 스트림을 얻어옵니다.
//                outputStream = bluetoothSocket.getOutputStream();
//                inputStream = btSocket.getInputStream();
                // 데이터 수신 함수 호출
//                receiveData();
            } catch (IOException e) {
                e.printStackTrace();
                textStatus.setText("상태: 연결 실패, 다시 시도해 보세요.");
                flag = false;
                Toast.makeText(getApplicationContext(), "블루투스 연결 실패!", Toast.LENGTH_LONG).show();
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
//            String text = connectedThread.read() + "입니다.";
            String text = connectedThread.read() + "입니다.";
            Log.d("받아온 값: ", text); }
    }


    //블루투스 권한 허용/거부 결과에 대한 활동
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                //'사용'을 누른 경우
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "사용을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                }
                //'취소'를 누른 경우
                else {
                    Toast.makeText(getApplicationContext(), "취소를 눌렀습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}