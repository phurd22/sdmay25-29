package com.example.abcbuddyapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Base2PunchActivity extends AppCompatActivity {

    private Base2PunchView base2PunchView;
    private int currentPage;
    private int totalPages;
    private List<List<Long>> allNumbers = new ArrayList<>();

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

        List<Long> numbers1 = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L,
                -1L, -2L, -3L, -4L, -5L, -6L, -7L, -8L, -9L, -10L,
                100L, 200L, 300L, 400L, 500L, 600L, 700L, 800L, 900L, 1000L);
        List<Long> numbers2 = Arrays.asList();
        List<Long> numbers3 = Arrays.asList();

        allNumbers.add(numbers1);
        allNumbers.add(numbers2);
        allNumbers.add(numbers3);

        base2PunchView.setDrawLeftArrow(true);
        base2PunchView.setDrawRightArrow(true);
        if (currentPage == 1) {
            base2PunchView.setDrawLeftArrow(false);
        }
        if (currentPage == totalPages) {
            base2PunchView.setDrawRightArrow(false);
        }
        base2PunchView.setNumbers(allNumbers.get(currentPage - 1), currentPage, totalPages);;
    }
}
