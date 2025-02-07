package com.example.abcbuddyapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.List;

public class Base2PunchActivity extends AppCompatActivity {

    private Base2PunchView base2PunchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base2punch);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.base2punch), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        base2PunchView = findViewById(R.id.base2PunchView);

        List<Long> numbers = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L,
                -1L, -2L, -3L, -4L, -5L, -6L, -7L, -8L, -9L, -10L,
                100L, 200L, 300L, 400L, 500L, 600L, 700L, 800L, 900L, 1000L);
        base2PunchView.setNumbers(numbers);
    }
}
