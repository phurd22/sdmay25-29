package com.example.abcbuddyapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private PunchcardView punchcardView;
    private TextView[] variableTextViews = new TextView[5]; // Holds TextViews for equation variables
    private StringBuilder currentSegment; // Stores the coefficient currently being entered
    private int segmentStartColumn; // Start column of the current 15-column segment
    private boolean awaitingVariable; // True if waiting for variable input after coefficient
    private boolean isNegative, isFirstNumber; // Flags for input handling
    private int currentVariableIndex; // Tracks the variable being assigned (x, y, z, etc.)
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private final String DEVICE_ADDRESS = "94:A9:90:08:9E:85";
    //private final String DEVICE_NAME = "Base10ESP32"; // ESP32 BLE name
    private final UUID SERVICE_UUID = UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b");
    private final UUID CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8");
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        punchcardView = findViewById(R.id.punchcard);
        variableTextViews[0] = findViewById(R.id.Var1);
        variableTextViews[1] = findViewById(R.id.Var2);
        variableTextViews[2] = findViewById(R.id.Var3);
        variableTextViews[3] = findViewById(R.id.Var4);
        variableTextViews[4] = findViewById(R.id.Var5);

//        punchcardView.post(new Runnable() {
//            @Override
//            public void run() {
//                int widthInPx = punchcardView.getWidth();
//                float density = punchcardView.getContext().getResources().getDisplayMetrics().density;
//                float widthInDp = widthInPx / density;
//
//                Log.d("ViewWidth", "Width: " + widthInDp + " dp");
//            }
//        });

        resetEquationState();

        // Setup numpad buttons
        GridLayout numpad = findViewById(R.id.numpad);
        for (int i = 0; i < numpad.getChildCount(); i++) {
            View child = numpad.getChildAt(i);
            if (child instanceof Button) {
                Button button = (Button) child;
                button.setOnClickListener(v -> handleNumpadInput(button.getText().toString()));
            }
        }

        Button clearButton = findViewById(R.id.buttonClear);
        clearButton.setOnClickListener(v -> clearEquation());
        Button uploadButton = findViewById(R.id.submitButton);
        uploadButton.setOnClickListener(v -> {
            if (!isDeviceConnected()) {
                Log.d("BLE", "Not connected to ESP32, starting scan...");
                startScan(); // Will connect and run uploadNumbers();
            }
            else {
                Log.d("BLE", "Connected to ESP32, uploading numbers...");
                uploadEquation();
            }
//            uploadEquation();
        });
//        Button connectButton = findViewById(R.id.esp32Button);
//        connectButton.setOnClickListener(v -> startScan());

        requestBluetoothPermissions();
    }

    // Handles numpad input, stores the number, clears, or moves to next column
    private void handleNumpadInput(String input) {
        if (input.equalsIgnoreCase("X")) {
            if (awaitingVariable) {
                char nextVariable = getNextVariable();

                // Prepare display text (remove leading zero before showing)
                String displayText = currentSegment.toString();
                boolean hasNeg;
                if (displayText.startsWith("0") && displayText.length() > 1) {
                    hasNeg = true;
                    displayText = displayText.substring(1);
                }
                else {
                    hasNeg = false;
                }
//                if (hasNeg) {
//                    displayText = displayText.substring(1);
//                }
//                while (displayText.charAt(0) == '0') {
//                    displayText = displayText.substring(1);
//                }
                if (hasNeg) {
                    displayText = "-" + displayText;
                }
                displayText += nextVariable;

                boolean punched = punchStoredNumber(); // Now properly aligned & includes leading zero (only in punch)

                if (punched) {
                    // Move to the next segment
                    if (currentVariableIndex < variableTextViews.length) {
                        variableTextViews[currentVariableIndex].setText(displayText);
                    }
                    currentVariableIndex++;
                    segmentStartColumn += 16;
                }
                awaitingVariable = false;
                isNegative = false;
                isFirstNumber = true;
                currentSegment.setLength(0);

            }
        } else {
            int number = Integer.parseInt(input);
            if (isFirstNumber) {
                if (number == 0) {
                    isNegative = true; // Mark as negative
                    currentSegment.append("0"); // Store for punching only (not displayed)
                } else {
//                    if (isNegative) {
//                        currentSegment.insert(0, "-"); // Ensure negative sign is stored
//                    }
                    currentSegment.append(number);
                    isFirstNumber = false;
                }
            } else {
                currentSegment.append(number);
            }

            awaitingVariable = true;
        }
    }

    // Gets next letter variable
    private char getNextVariable() {
        String variables = "xyzwj";
        return variables.charAt(currentVariableIndex);
    }

    // Punches the stored number onto the punchcard
    private boolean punchStoredNumber() {
        String coefficient = currentSegment.toString();
        boolean isNegative;
        if (coefficient.startsWith("0") && coefficient.length() > 1) {
            isNegative = true;
            coefficient = coefficient.substring(1);
        }
        else {
            isNegative = false;
        }
        if (isNegative) {
            if (currentSegment.length() > 16) {
                Toast.makeText(context, "Number Too Long", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else {
            if (currentSegment.length() > 15) {
                Toast.makeText(context, "Number Too Long", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

//        String coefficient = rawCoefficient.replaceAll("[^0-9]", ""); // Extract all digits
//        Log.d("Test", "Raw Coefficient: " + rawCoefficient);
        Log.d("Test", "Coefficient: " + coefficient);
        Log.d("Test", "Is Negative: " + isNegative);


        int punchColumn = segmentStartColumn + 15 - (coefficient.length() - 1);

        // Punch the leading zero if it's negative
        if (isNegative) {
            punchcardView.punchCell(segmentStartColumn, 0);
        }
        
        // Punch each digit, ensuring all zeroes are included
        for (int i = 0; i < coefficient.length(); i++) {
            int digit = Character.getNumericValue(coefficient.charAt(i));
            punchcardView.punchCell(punchColumn + i, digit);
        }
        return true;
    }

    // Clears the currently punched equation
    private void clearEquation() {
        resetEquationState();
        punchcardView.clearPunches();
        for (TextView textView : variableTextViews) {
            textView.setText("");
        }
    }

    // Resets variables used to build equation
    private void resetEquationState() {
        currentSegment = new StringBuilder();
        segmentStartColumn = 0;
        awaitingVariable = false;
        isNegative = false;
        isFirstNumber = true;
        currentVariableIndex = 0;
    }

    // Uploads equation to ESP32
    @SuppressLint("MissingPermission")
    private void uploadEquation() {
        String equation = "";
        String number = "";
        for (int i = 0; i < variableTextViews.length; i++) {
            number = variableTextViews[i].getText().toString();
            if (!number.isEmpty()) {
                number = number.substring(0, number.length() - 1);
            }
            if (isNumeric(number)) {
                equation += number;
            } else {
                equation += "0";
            }
            if (i == variableTextViews.length - 1) {
                equation += "d";
            }
            else {
                equation += "x";
            }
        }
//        equation = variableTextViews[0].getText().toString();
//        equation += variableTextViews[1].getText().toString();
//        equation += variableTextViews[2].getText().toString();
//        equation += variableTextViews[3].getText().toString();
//        equation += variableTextViews[4].getText().toString();
//        equation = equation.replaceAll("[^0-9x-]", "x");
//        equation = equation.substring(0, equation.length() - 1) + "d";
        Log.d("Test", "Equation: " + equation);

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
        characteristic.setValue(equation);
        bluetoothGatt.writeCharacteristic(characteristic);
        runOnUiThread(() -> {
            Toast.makeText(context, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
        });
    }

    // Processes BLE scan results
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

    // Starts BLE scan
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

    // Connects to BLE device
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
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.d("BLE", "Services discovered!");
                    uploadEquation();
                }
            }
        });
    }

    // Checks if connected to ESP32
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

    // Checks if string is numeric
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
