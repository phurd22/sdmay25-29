package com.example.abcbuddyappbase2read;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

public class MainActivity extends AppCompatActivity {

    private Base2PunchView base2PunchView;
    private int currentPage;
    private int totalPages;
    private List<List<Long>> allNumbers = new ArrayList<>();
    private LinearLayout pageButtonContainer;
    private Button uploadButton;
    private Button modeButton;
    private Button resetButton;
    private Button flipButton;
    private List <Button> pageButtons = new ArrayList<>();
    ViewGroup.LayoutParams flipButtonParams;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private final String DEVICE_ADDRESS = "94:A9:90:0A:4F:E9"; // ESP32 BLE address
    private final UUID SERVICE_UUID = UUID.fromString("a5f08588-fdb1-4785-b1aa-c21acec22158");
    private final UUID CHARACTERISTIC_UUID = UUID.fromString("9317847f-cc24-4005-bb74-78b06b9757b0");
    Context context;
    private View topBar;
    private View pageDivider;
    private BluetoothServerSocket serverSocket;
    private BluetoothSocket classicSocket;
    private static final UUID CLASSIC_BT_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    private volatile boolean listening = false;
    private Thread classicListenerThread;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.base2punch), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        context = this;
        currentPage = 1;
        totalPages = 1;

        pageButtonContainer = findViewById(R.id.pageButtonContainer);
        base2PunchView = findViewById(R.id.base2PunchView);
        uploadButton = findViewById(R.id.buttonUpload);
        modeButton = findViewById(R.id.buttonMode);
        flipButton = findViewById(R.id.buttonFlip);
        flipButtonParams = flipButton.getLayoutParams();
        resetButton = findViewById(R.id.buttonReset);
        topBar = findViewById(R.id.topBar);
        pageDivider = findViewById(R.id.pageDivider);

        List<Long> numbers1 = Arrays.asList(-1L, 750599937895082L, 900719925474099L, 860982281703183L,
                0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L,
                0L, 0L, 0L, 0L, 0L);

//        List<Long> numbers2 = Arrays.asList(-1L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L,
//                100L, 200L, 300L, 400L, 500L, 600L, 700L, 800L, 900L, 1000L,
//                1001L, 1100L, 1200L, 1300L, 1400L, 1500L, 1600L, 1700L, 1800L, 1900L);
//
//        List<Long> numbers3 = Arrays.asList(1L, 2L, 4L, 8L, 16L, 32L, 64L, 128L, 256L, 512L,
//                1024L, 2048L, 4096L, 8192L, 16384L, 32768L, 65536L, 131072L, 262144L, 524288L,
//                1048576L, 2097152L, 4194304L, 8388608L, 16777216L, 33554432L, 67108864L,
//                134217728L, 268435456L, 536870912L);

        allNumbers.add(numbers1);
//        allNumbers.add(numbers2);
//        allNumbers.add(numbers3);

        for (int i = 1; i <= totalPages; i++) {
            Button pageButton = new Button(this);
            pageButton.setText("Page " + i);
            pageButton.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            int pageIndex = i;
            pageButtons.add(pageButton);
            pageButton.setOnClickListener(v -> {
                currentPage = pageIndex;
                updatePage();
                for (Button b : pageButtons) {
                    b.setBackgroundResource(R.drawable.button_border);
                }
                pageButton.setBackgroundResource(R.drawable.selected_button_border);
            });
            pageButton.setBackgroundResource(R.drawable.button_border);
            pageButton.setWidth(109);
            pageButtonContainer.addView(pageButton);
        }
        if (totalPages > 0) {
            pageButtons.get(0).setBackgroundResource(R.drawable.selected_button_border);
        }

        // 9 for if flip button is visible
        if (totalPages < 10) {
            pageDivider.setVisibility(View.GONE);
        }
        if (totalPages == 0) {
            topBar.setVisibility(View.GONE);
        }

        modeButton.setOnClickListener(v -> {
            base2PunchView.changeMode();
            if (base2PunchView.getMode() == 0) {
                flipButtonParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                flipButton.setLayoutParams(flipButtonParams);
            }
            else {
                flipButtonParams.height = 0;
                flipButton.setLayoutParams(flipButtonParams);
            }
        });

        flipButton.setOnClickListener(v -> {
            base2PunchView.changeDirection();
        });

        resetButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Reset")
                    .setMessage("Are you sure you want to reset?")
                    .setPositiveButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .setNegativeButton("Yes", (dialog, which) -> reset())
                    .setCancelable(false)
                    .show();
        });

        uploadButton.setOnClickListener(v -> {
            if (totalPages > 0) {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                        Log.d("ClassicBT", "Closed classic BT socket");
                    } catch (IOException e) {
                        Log.e("ClassicBT", "Error closing classic BT socket", e);
                    }
                }
                if (classicSocket != null && classicSocket.isConnected()) {
                    try {
                        classicSocket.close();
                        Log.d("ClassicBT", "Closed classic BT socket");
                    } catch (IOException e) {
                        Log.e("ClassicBT", "Error closing classic BT socket", e);
                    }
                    listening = false;
                }
                if (!isESP32Connected()) {
                    Log.d("BLE", "Not connected to ESP32, starting scan...");
                    startScan(); // Will connect and run uploadNumbers(), and disconnect
                }
                else {
                    Log.d("BLE", "Connected to ESP32, uploading numbers...");
                    uploadNumbers();
                }
            }
        });

        updatePage();
        requestBluetoothPermissions();
        startClassicBluetoothListener();
    }

    private void reset() {
        Log.d("Reset", "Reset confirmed");
        pageDivider.setVisibility(View.GONE);
        topBar.setVisibility(View.GONE);
        currentPage = 0;
        totalPages = 0;
        allNumbers.clear();
        pageButtons.clear();
        pageButtonContainer.removeAllViews();
        updatePage();
    }

    private void addPage(String message, int pageNumber) {
        totalPages++;
        final int finalPageNumber = pageNumber;
        String[] numbers = message.substring(0, message.length() - 1).split("x");
        List<Long> numberList = new ArrayList<>();
        for (String number : numbers) {
            numberList.add(Long.parseLong(number));
        }
        for (int i = 0; i < 26; i++) {
            numberList.add(0L);
        }
        allNumbers.add(numberList);

        Button pageButton = new Button(this);
        pageButton.setText("Page " + pageNumber);
        pageButton.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        pageButtons.add(pageButton);
        pageButton.setBackgroundResource(R.drawable.button_border);
        pageButton.setWidth(109);
        pageButton.setOnClickListener(v -> {
            currentPage = finalPageNumber;
            updatePage();
            for (Button b : pageButtons) {
                b.setBackgroundResource(R.drawable.button_border);
            }
            pageButton.setBackgroundResource(R.drawable.selected_button_border);
        });
        pageButtonContainer.addView(pageButton);
        if (totalPages == 1) {
            currentPage = 1;
            pageButton.setBackgroundResource(R.drawable.selected_button_border);
            updatePage();
        }

        if (totalPages > 10) {
            pageDivider.setVisibility(View.VISIBLE);
        }
        if (totalPages > 0) {
            topBar.setVisibility(View.VISIBLE);
        }
    }

    private void updatePage() {
        if (allNumbers.size() > 0) {
            base2PunchView.setNumbers(allNumbers.get(currentPage - 1));
        }
        else {
            List<Long> allZero = Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L,
                    0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
            base2PunchView.setNumbers(allZero);
        }
    }

    @SuppressLint("MissingPermission")
    private void startClassicBluetoothListener() {
        classicListenerThread = new Thread(() -> {
            try {
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                serverSocket = adapter.listenUsingRfcommWithServiceRecord("ABC_Base2_Read", CLASSIC_BT_UUID);
                listening = true;
                Log.d("ClassicBT", "Listening for classic Bluetooth...");
                classicSocket = serverSocket.accept(); // Blocking
                Log.d("ClassicBT", "Connected to classic BT device");
                handleClassicBluetoothData(classicSocket.getInputStream());
            } catch (IOException e) {
                Log.e("ClassicBT", "Error listening for BT connection", e);
            } finally {
                listening = false;
            }
        });
        classicListenerThread.start();
    }

    private void handleClassicBluetoothData(InputStream inputStream) {
        try {
            byte[] buffer = new byte[1024];
            int bytes;
            StringBuilder totalMessage = new StringBuilder();

            while ((bytes = inputStream.read(buffer)) != -1) {
                String received = new String(buffer, 0, bytes);
                Log.d("ClassicBT", "Received: " + received);
                totalMessage.append(received);

                // Check if 'd' is in the accumulated message
                if (totalMessage.toString().contains("d")) {
                    Log.d("ClassicBT", "Complete message received: " + totalMessage.toString());
                    runOnUiThread(() -> {
                        addPage(totalMessage.toString(), totalPages + 1);
                    });
                    break; // Exit reading loop
                }
            }
        } catch (IOException e) {
            Log.e("ClassicBT", "Error reading input stream", e);
        } finally {
            try {
                if (classicSocket != null && classicSocket.isConnected()) {
                    classicSocket.close();
                }
                if (serverSocket != null) {
                    serverSocket.close(); // Ensure it’s closed before reopening
                }
            } catch (IOException e) {
                Log.e("ClassicBT", "Error closing socket", e);
            }

            // Restart listener in background
            new Handler(Looper.getMainLooper()).postDelayed(this::startClassicBluetoothListener, 200); // slight delay helps
        }
    }

    @SuppressLint("MissingPermission")
    private void uploadNumbers() {
        if (totalPages == 0) {
            return;
        }
        String numbers = "";
        for (int i = 0; i < 4; i++) {
            numbers += String.valueOf(allNumbers.get(currentPage - 1).get(i));
            if (i < 3) {
                numbers += "x";
            }
        }
        numbers += "d";

        if (bluetoothGatt == null) {
            Log.e("BLE", "Not connected to a device!");
            return;
        }

        BluetoothGattService service = bluetoothGatt.getService(SERVICE_UUID);
        if (service == null) {
            Log.e("BLE", "Service not found!");
            return;
        }

        BluetoothGattCharacteristic characteristic = service.getCharacteristic(CHARACTERISTIC_UUID);
        if (characteristic == null) {
            Log.e("BLE", "Characteristic not found!");
            return;
        }
        Log.d("BLE", "Found service and characteristic successfully.");
        characteristic.setValue(numbers);
        bluetoothGatt.writeCharacteristic(characteristic);
        runOnUiThread(() -> {
            Toast.makeText(context, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
        });
    }

    @SuppressLint("MissingPermission")
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            String deviceName = device.getName(); // This may be null
            String deviceAddress = device.getAddress();

            Log.d("BLE", "Found device: " + deviceName + " [" + deviceAddress + "]");
            if (DEVICE_ADDRESS.equals(deviceAddress)) {
                Log.d("BLE", "Found ESP32, connecting...");
                bluetoothAdapter.getBluetoothLeScanner().stopScan(this);
                connectToDevice(device);
            }
        }
    };

    @SuppressLint("MissingPermission")
    private void startScan() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Log.e("BLE", "Bluetooth is disabled!");
            return;
        }

        bluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);
        Log.d("BLE", "Scanning for ESP32...");
    }

    @SuppressLint("MissingPermission")
    private void connectToDevice(BluetoothDevice device) {
        bluetoothGatt = device.connectGatt(this, false, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d("BLE", "Connected to ESP32");
                    runOnUiThread(() -> {
                        Toast.makeText(context, "Connected to ESP32", Toast.LENGTH_SHORT).show();
                    });
                    gatt.discoverServices();
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.d("BLE", "Disconnected from ESP32");
                    gatt.close();
                    runOnUiThread(() -> {
                        Toast.makeText(context, "Disconnected from ESP32", Toast.LENGTH_SHORT).show();
                        startClassicBluetoothListener();
                    });
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.d("BLE", "Services discovered!");
                    uploadNumbers();
                }
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.d("BLE", "Data sent successfully");
                    runOnUiThread(() -> {
                        Toast.makeText(context, "Upload complete, disconnecting...", Toast.LENGTH_SHORT).show();
                    });

                    gatt.disconnect(); // Initiate disconnect after writing
                } else {
                    Log.e("BLE", "Failed to send data");
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private boolean isESP32Connected() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        List<BluetoothDevice> connectedDevices = bluetoothManager.getConnectedDevices(BluetoothProfile.GATT);

        for (BluetoothDevice device : connectedDevices) {
            if (DEVICE_ADDRESS.equals(device.getAddress())) {
                return true;
            }
        }
        return false;
    }

    private void requestBluetoothPermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.add(android.Manifest.permission.BLUETOOTH);
        permissions.add(android.Manifest.permission.BLUETOOTH_SCAN);
        permissions.add(android.Manifest.permission.BLUETOOTH_CONNECT);
        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.INTERNET);
        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        permissions.add(Manifest.permission.CHANGE_WIFI_STATE);

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