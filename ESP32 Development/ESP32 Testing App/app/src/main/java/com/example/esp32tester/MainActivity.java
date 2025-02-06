package com.example.esp32tester;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button redButton;
    private Button yellowButton;
    private Button greenButton;
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

        redButton = findViewById(R.id.redButton);
        yellowButton = findViewById(R.id.yellowButton);
        greenButton = findViewById(R.id.greenButton);

        redButton.setBackgroundColor(getResources().getColor(R.color.gray));
        yellowButton.setBackgroundColor(getResources().getColor(R.color.gray));
        greenButton.setBackgroundColor(getResources().getColor(R.color.gray));

        redLEDOn = false;
        yellowLEDOn = false;
        greenLEDOn = false;

        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (redLEDOn) {
                    redLEDOn = false;
                    redButton.setBackgroundColor(getResources().getColor(R.color.gray));
                }
                else {
                    redLEDOn = true;
                    redButton.setBackgroundColor(getResources().getColor(R.color.red));
                }
            }
        });

        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yellowLEDOn) {
                    yellowLEDOn = false;
                    yellowButton.setBackgroundColor(getResources().getColor(R.color.gray));
                }
                else {
                    yellowLEDOn = true;
                    yellowButton.setBackgroundColor(getResources().getColor(R.color.yellow));
                }
            }
        });

        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (greenLEDOn) {
                    greenLEDOn = false;
                    greenButton.setBackgroundColor(getResources().getColor(R.color.gray));
                }
                else {
                    greenLEDOn = true;
                    greenButton.setBackgroundColor(getResources().getColor(R.color.green));
                }
            }
        });
    }
}