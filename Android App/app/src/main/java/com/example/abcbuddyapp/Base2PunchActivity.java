package com.example.abcbuddyapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Base2PunchActivity extends AppCompatActivity {

    private Base2PunchView base2PunchView;
    private int currentPage;
    private int totalPages;
    private List<List<Long>> allNumbers = new ArrayList<>();
    private static final String ESP32_MAC_ADDRESS = "08:A6:F7:BC:5E:C6";
    private static final UUID ESP32_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base2punch);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.base2punch), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        currentPage = 1;
        totalPages = 3;

        base2PunchView = findViewById(R.id.base2PunchView);
        handler = new Handler(Looper.getMainLooper());

        List<Long> numbers1 = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L,
                -1L, -2L, -3L, -4L, -5L, -6L, -7L, -8L, -9L, -10L,
                100L, 200L, 300L, 400L, 500L, 600L, 700L, 800L, 900L, 1000L);

        List<Long> numbers2 = Arrays.asList(-1L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L,
                100L, 200L, 300L, 400L, 500L, 600L, 700L, 800L, 900L, 1000L,
                1001L, 1100L, 1200L, 1300L, 1400L, 1500L, 1600L, 1700L, 1800L, 1900L);

        List<Long> numbers3 = Arrays.asList(1L, 2L, 4L, 8L, 16L, 32L, 64L, 128L, 256L, 512L,
                1024L, 2048L, 4096L, 8192L, 16384L, 32768L, 65536L, 131072L, 262144L, 524288L,
                1048576L, 2097152L, 4194304L, 8388608L, 16777216L, 33554432L, 67108864L,
                134217728L, 268435456L, 536870912L);

        allNumbers.add(numbers1);
        allNumbers.add(numbers2);
        allNumbers.add(numbers3);

        updatePage();
        requestBluetoothPermissions();
        connectToESP32();

        base2PunchView.setOnPageChangeListener(new Base2PunchView.OnPageChangeListener() {
            @Override
            public void onPreviousPage() {
                if (currentPage > 1) {
                    currentPage--;
                    updatePage();
                }
            }

            @Override
            public void onNextPage() {
                if (currentPage < totalPages) {
                    currentPage++;
                    updatePage();
                }
            }
        });
    }

    private void listenForData() {
        new Thread(() -> {
            byte[] buffer = new byte[240];
            int bytesRead = 0;
            while (true) {
                try {
                    while (bytesRead < 240) { // Keep reading until we get 240 bytes
                        int result = inputStream.read(buffer, bytesRead, 240 - bytesRead);
                        if (result == -1) break; // End of stream
                        bytesRead += result;
                    }
                    allNumbers.add(new ArrayList<>());
                    ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
                    for (int i = 0; i < 30; ++i) {
                        allNumbers.get(allNumbers.size() - 1).add(byteBuffer.getLong());
                    }

                    for (Long value : allNumbers.get(allNumbers.size() - 1)) {
                        Log.d("Bluetooth", "Received: " + value);
                    }

                    totalPages += 1;
                    Log.d("AllNumbers", "allNumbers size: " + allNumbers.size());
                    Log.d("AllNumbers", allNumbers.get(allNumbers.size() - 1).toString());
                    handler.post(this::updatePage);
                    bytesRead = 0;
                } catch (IOException e) {
                    Log.e("Bluetooth", "Error reading data", e);
                    break;
                }
            }
        }).start();
    }

    private void updatePage() {
        base2PunchView.setDrawLeftArrow(true);
        base2PunchView.setDrawRightArrow(true);
        if (currentPage == 1) {
            base2PunchView.setDrawLeftArrow(false);
        }
        if (currentPage == totalPages) {
            base2PunchView.setDrawRightArrow(false);
        }
        Log.d("Base2PunchView", allNumbers.get(currentPage - 1).toString());
        base2PunchView.setNumbers(allNumbers.get(currentPage - 1), currentPage, totalPages);
    }

    private void connectToESP32() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            return;
        }

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(ESP32_MAC_ADDRESS);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You have to accept bluetooth permissions, clear app storage and cache and try again.", Toast.LENGTH_LONG).show();
                return;
            }
            bluetoothSocket = device.createRfcommSocketToServiceRecord(ESP32_UUID);
            bluetoothSocket.connect();
            inputStream = bluetoothSocket.getInputStream();
            Toast.makeText(this, "Connected to ESP32", Toast.LENGTH_SHORT).show();
            listenForData();
        } catch (IOException e) {
            Log.e("Bluetooth", "Connection failed", e);
            Toast.makeText(this, "Failed to connect", Toast.LENGTH_LONG).show();
        }
    }

    private void requestBluetoothPermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.add(android.Manifest.permission.BLUETOOTH);
        permissions.add(android.Manifest.permission.BLUETOOTH_SCAN);
        permissions.add(android.Manifest.permission.BLUETOOTH_CONNECT);
        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
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
}
