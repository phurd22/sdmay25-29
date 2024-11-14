package com.example.abcbuddyapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, buttonClear, buttonVariable;
    TextView equation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        equation = findViewById(R.id.equation);
        button0.findViewById(R.id.button0);
        button1.findViewById(R.id.button1);
        button2.findViewById(R.id.button2);
        button3.findViewById(R.id.button3);
        button4.findViewById(R.id.button4);
        button5.findViewById(R.id.button5);
        button6.findViewById(R.id.button6);
        button7.findViewById(R.id.button7);
        button8.findViewById(R.id.button8);
        button9.findViewById(R.id.button9);
        buttonClear.findViewById(R.id.buttonClear);
        buttonVariable.findViewById(R.id.buttonVariable);
    }
}