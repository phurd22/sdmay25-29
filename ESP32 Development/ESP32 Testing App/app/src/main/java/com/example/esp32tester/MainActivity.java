package com.example.esp32tester;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button redButton;
    private Button yellowButton;
    private Button greenButton;
    private Button connectButton;
    private boolean redLEDOn;
    private boolean yellowLEDOn;
    private boolean greenLEDOn;

    private static final String ESP32_MAC_ADDRESS = "08:A6:F7:BC:5E:C6";
    private static final UUID ESP32_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        redButton = findViewById(R.id.redButton);
        yellowButton = findViewById(R.id.yellowButton);
        greenButton = findViewById(R.id.greenButton);
        connectButton = findViewById(R.id.connectButton);

        redButton.setBackgroundColor(getResources().getColor(R.color.gray));
        yellowButton.setBackgroundColor(getResources().getColor(R.color.gray));
        greenButton.setBackgroundColor(getResources().getColor(R.color.gray));

        redLEDOn = false;
        yellowLEDOn = false;
        greenLEDOn = false;

        requestBluetoothPermissions();

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectToEsp32();
            }
        });

        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (redLEDOn) {
                    sendIntBluetoothCommand(10);
                    redLEDOn = false;
                    redButton.setBackgroundColor(getResources().getColor(R.color.gray));
                }
                else {
                    sendIntBluetoothCommand(11);
                    redLEDOn = true;
                    redButton.setBackgroundColor(getResources().getColor(R.color.red));
                }
            }
        });

        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yellowLEDOn) {
                    sendIntBluetoothCommand(20);
                    yellowLEDOn = false;
                    yellowButton.setBackgroundColor(getResources().getColor(R.color.gray));
                }
                else {
                    sendIntBluetoothCommand(21);
                    yellowLEDOn = true;
                    yellowButton.setBackgroundColor(getResources().getColor(R.color.yellow));
                }
            }
        });

        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (greenLEDOn) {
                    sendIntBluetoothCommand(30);
                    greenLEDOn = false;
                    greenButton.setBackgroundColor(getResources().getColor(R.color.gray));
                }
                else {
                    sendIntBluetoothCommand(31);
                    greenLEDOn = true;
                    greenButton.setBackgroundColor(getResources().getColor(R.color.green));
                }
            }
        });
    }

    private void requestBluetoothPermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.add(android.Manifest.permission.BLUETOOTH);
        permissions.add(android.Manifest.permission.BLUETOOTH_SCAN);
        permissions.add(android.Manifest.permission.BLUETOOTH_CONNECT);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> neededPermissions = new ArrayList<>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(perm);
            }
        }

        if (!neededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, neededPermissions.toArray(new String[0]), 1);
        }
    }

    private void connectToEsp32() {
        // Check permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "You have to accept permissions", Toast.LENGTH_SHORT).show();
            return;
        }

        // Connect to ESP32
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(ESP32_MAC_ADDRESS);
        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(ESP32_UUID);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            Toast.makeText(this, "Connected to ESP32", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("Bluetooth", "Connection failed", e);
            Toast.makeText(this, "Failed to connect to ESP32", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void sendIntBluetoothCommand(int value) {
        if (outputStream != null) {
            try {
                // Send the integer as a 4-byte sequence
                outputStream.write(ByteBuffer.allocate(4).putInt(value).array());
            } catch (IOException e) {
                Log.e("Bluetooth", "Error sending integer", e);
            }
        }
    }

    private void sendBluetoothCommand(String command) {
        if (outputStream != null) {
            try {
                outputStream.write(command.getBytes());
                Toast.makeText(this, "Sent command: " + command, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e("Bluetooth", "Error sending command", e);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
        } catch (IOException e) {
            Log.e("Bluetooth", "Error closing socket", e);
        }
    }
}