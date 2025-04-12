package com.example.abcbuddyappbase2write;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Base2PunchView base2PunchView;
    private int currentPage;
    private int totalPages;
    private List<List<Long>> allNumbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.base2punch), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        currentPage = 1;
        totalPages = 3;

        base2PunchView = findViewById(R.id.base2PunchView);

        List<Long> numbers1 = Arrays.asList(-1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L,
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
    }

    private void updatePage() {
        base2PunchView.setNumbers(allNumbers.get(currentPage - 1));
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
