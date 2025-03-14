package com.example.abcbuddyapp;

import android.Manifest;
import android.os.Bundle;
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
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;

import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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
    private BluetoothSocket bluetoothSocket;
    private static final String ESP32_MAC_ADDRESS = "08:A6:F7:BC:5E:C6";
    private static final UUID ESP32_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private InputStream inputStream;
    private OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        uploadButton.setOnClickListener(v -> uploadEquation());
        Button connectButton = findViewById(R.id.esp32Button);
        connectButton.setOnClickListener(v -> connectToESP32());

        requestBluetoothPermissions();
    }

    private void handleNumpadInput(String input) {
        if (input.equalsIgnoreCase("X")) {
            if (awaitingVariable) {
                char nextVariable = getNextVariable();

                // Prepare display text (remove leading zero before showing)
                String displayText = currentSegment.toString() + nextVariable;
                boolean hasNeg = displayText.startsWith("-");
                if (hasNeg) {
                    displayText = displayText.substring(1);
                }
                while (displayText.charAt(0) == '0') {
                    displayText = displayText.substring(1);
                }
                if (hasNeg) {
                    displayText = "-" + displayText;
                }

                if (currentVariableIndex < variableTextViews.length) {
                    variableTextViews[currentVariableIndex].setText(displayText);
                }

                punchStoredNumber(); // Now properly aligned & includes leading zero (only in punch)

                // Move to the next segment
                currentVariableIndex++;
                segmentStartColumn += 15;
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
                    if (isNegative) {
                        currentSegment.insert(0, "-"); // Ensure negative sign is stored
                    }
                    currentSegment.append(number);
                    isFirstNumber = false;
                }
            } else {
                currentSegment.append(number);
            }

            awaitingVariable = true;
        }
    }

    private char getNextVariable() {
        String variables = "xyzwj";
        return variables.charAt(currentVariableIndex);
    }

    private void punchStoredNumber() {
        String rawCoefficient = currentSegment.toString();
        boolean isNegative = rawCoefficient.startsWith("0"); // Detect negative indicator
        String coefficient = rawCoefficient.replaceAll("[^0-9]", ""); // Extract all digits

        int punchColumn = segmentStartColumn + 14 - (coefficient.length() - 1);

        // Punch the leading zero if it's negative
        if (isNegative) {
            punchcardView.punchCell(punchColumn - 1 , 0);
        }
        
        // Punch each digit, ensuring all zeroes are included
        for (int i = 0; i < coefficient.length(); i++) {
            int digit = Character.getNumericValue(coefficient.charAt(i));
            punchcardView.punchCell(punchColumn + i, digit);
        }
    }

    private void clearEquation() {
        resetEquationState();
        punchcardView.clearPunches();
        for (TextView textView : variableTextViews) {
            textView.setText("");
        }
    }

    private void resetEquationState() {
        currentSegment = new StringBuilder();
        segmentStartColumn = 0;
        awaitingVariable = false;
        isNegative = false;
        isFirstNumber = true;
        currentVariableIndex = 0;
    }

    private void uploadEquation() {
        String equation = "";
        equation = variableTextViews[0].getText().toString();
        equation += variableTextViews[1].getText().toString();
        equation += variableTextViews[2].getText().toString();
        equation += variableTextViews[3].getText().toString();
        equation += variableTextViews[4].getText().toString();
        equation = equation.replaceAll("[^0-9x-]", "x");
        equation = equation.substring(0, equation.length() - 1) + "d";
        Log.d("Test", "Equation sent: " + equation);

        if (outputStream != null) {
            try {
                outputStream.write(equation.getBytes(StandardCharsets.UTF_8));
                Log.d("Bluetooth", "Equation sent: " + equation);
            } catch (IOException e) {
                Log.e("Bluetooth", "Error sending integer", e);
            }
        }
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
            outputStream = bluetoothSocket.getOutputStream();
            Toast.makeText(this, "Connected to ESP32", Toast.LENGTH_SHORT).show();
            //listenForData();
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
