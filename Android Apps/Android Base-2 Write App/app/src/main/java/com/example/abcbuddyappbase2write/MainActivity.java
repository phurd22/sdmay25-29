package com.example.abcbuddyappbase2write;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private Base2PunchView base2PunchView;
    private List<Long> numbers;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private final String DEVICE_ADDRESS = "94:A9:90:0A:4F:E9"; // ESP32 BLE address
    private final UUID SERVICE_UUID = UUID.fromString("246ccada-310c-46a4-9881-3d2feb165646");
    private final UUID CHARACTERISTIC_UUID = UUID.fromString("9d5a6f5c-e32c-429f-9faa-8209e6f2dda4");
    private ArrayList<String> binaryStringArray;
    private int binaryIndex;
    private final long BTDebounceDelay = 10;
    private long lastUpdateTime;
    Context context;

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
        base2PunchView = findViewById(R.id.base2PunchView);
        numbers = Arrays.asList(0L, 0L, 0L, 0L);
        binaryIndex = 49;
        binaryStringArray = new ArrayList<>();
        lastUpdateTime = 0;

        resetBinaryStrings();
        updatePage();
        requestBluetoothPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isDeviceConnected()) {
            startScan();
        }
        else {
            Toast.makeText(this, "Already connected to ESP32", Toast.LENGTH_SHORT).show();
        }
    }

    public void resetBinaryStrings() {
        binaryStringArray.clear();
        String binaryString = "00000000000000000000000000000000000000000000000000";
        for (int i = 0; i < 4; i++) {
            binaryStringArray.add(binaryString);
        }
    }

    private void updatePage() {
        for (int i = 0; i < 4; i++) {
            numbers.set(i, binaryToLongTwosComplement(binaryStringArray.get(i)));
        }
        base2PunchView.setNumbers(numbers);
    }

    public long binaryToLongTwosComplement(String binary) {
        if (binary.length() != 50) {
            throw new IllegalArgumentException("Binary string must be exactly 50 bits");
        }

        // Padding
        if (binary.charAt(0) == '1') {
            binary = "11111111111111" + binary;
        } else {
            binary = "00000000000000" + binary;
        }

        BigInteger big = new BigInteger(binary, 2);
        if (binary.length() == 64 && binary.charAt(0) == '1') {
            big = big.subtract(BigInteger.ONE.shiftLeft(64)); // Sign-extend
        }

        return big.longValue();
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
                    runOnUiThread(() -> {
                        Toast.makeText(context, "Disconnected from ESP32", Toast.LENGTH_SHORT).show();
                    });

                    // Restart scanning
                    bluetoothGatt.close(); // release resources
                    bluetoothGatt = null;
                    runOnUiThread(() -> startScan());
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.d("BLE", "Services discovered!");
                    BluetoothGattService service = gatt.getService(SERVICE_UUID);
                    if (service != null) {
                        BluetoothGattCharacteristic characteristic =
                                service.getCharacteristic(CHARACTERISTIC_UUID);
                        if (characteristic != null) {
                            gatt.setCharacteristicNotification(characteristic, true);

                            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                                    UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")); // Standard Client Characteristic Config

                            if (descriptor != null) {
                                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                gatt.writeDescriptor(descriptor); // triggers onDescriptorWrite if needed
                            }
                        }
                    }
                }
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                String value = new String(characteristic.getValue(), StandardCharsets.UTF_8);

                // Ignore duplicate notifications if they occur too soon
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastUpdateTime < BTDebounceDelay) {
                    Log.d("BLE", "Ignoring duplicate notification (debouncing).");
                    return;
                }
                lastUpdateTime = currentTime;

                Log.d("BLE", "Received from ESP32: " + value);
                if (binaryIndex < 0) {
                    Log.d("Binary Strings", "Resetting Page");
                    resetBinaryStrings();
                    binaryIndex = 49;
                }
                for (int i = 0; i < 4; i++) {
                    String newString = binaryStringArray.get(i);
                    StringBuilder sb = new StringBuilder(newString);
                    sb.setCharAt(binaryIndex, value.charAt(i));
                    binaryStringArray.set(i, sb.toString());
                }
                binaryIndex--;
                Log.d("Binary Strings", "Binary Index: " + binaryIndex);
                runOnUiThread(MainActivity.this::updatePage);
            }
        });
    }

    @SuppressLint("MissingPermission")
    private boolean isDeviceConnected() {
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
