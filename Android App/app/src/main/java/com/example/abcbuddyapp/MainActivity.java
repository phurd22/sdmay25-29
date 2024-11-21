package com.example.abcbuddyapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private PunchcardView punchcardView;
    private StringBuilder coefficientInput = new StringBuilder();
    private String[] variables = {"x", "y", "z", "w"}; // Expand as needed
    private int currentVariableIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        punchcardView = findViewById(R.id.punchcard);

        // Set up numpad
        GridLayout numpad = findViewById(R.id.numpad);
        for (int i = 0; i < numpad.getChildCount(); i++) {
            View child = numpad.getChildAt(i);
            if (child instanceof Button) {
                Button button = (Button) child;
                button.setOnClickListener(v -> handleNumpadInput(button.getText().toString()));
            }
        }

        // Set up variable button
        Button varButton = findViewById(R.id.buttonVariable);
        varButton.setOnClickListener(v -> handleVariableInput());

        // Set up clear button
        Button clearButton = findViewById(R.id.buttonClear);
        clearButton.setOnClickListener(v -> clearPunchcard());
    }

    private void handleNumpadInput(String input) {
        if (input.equals("Clear")) {
            coefficientInput.setLength(0); // Clear current input
            return;
        }

        coefficientInput.append(input);

        // Update the punchcard in real-time with the coefficient
        updatePunchcardPreview();
    }

    private void handleVariableInput() {
        if (coefficientInput.length() == 0) return;

        // Parse the coefficient
        int coefficient = Integer.parseInt(coefficientInput.toString());
        if (coefficientInput.charAt(0) == '0') {
            coefficient = -coefficient; // Convert to negative if starting with '0'
        }

        // Get the current variable
        String variable = variables[currentVariableIndex];

        // Format the term in binary
        String termBinary = coefficientToBinary(coefficient) + " (" + variable + ")";

        // Add the term to the punchcard
        punchcardView.addEquationPart(termBinary);

        // Cycle to the next variable
        currentVariableIndex = (currentVariableIndex + 1) % variables.length;

        // Clear the coefficient input for the next entry
        coefficientInput.setLength(0);
    }

    private void updatePunchcardPreview() {
        if (coefficientInput.length() > 0) {
            int coefficient = Integer.parseInt(coefficientInput.toString());
            if (coefficientInput.charAt(0) == '0') {
                coefficient = -coefficient; // Convert to negative if starting with '0'
            }

            // Preview the binary form of the current input
            String binary = coefficientToBinary(coefficient);
            punchcardView.addEquationPart(binary);
        }
    }

    private void clearPunchcard() {
        punchcardView.clearEquationParts();
        coefficientInput.setLength(0);
        currentVariableIndex = 0;
    }

    private String coefficientToBinary(int coefficient) {
        String binary = Integer.toBinaryString(Math.abs(coefficient));
        return (coefficient < 0 ? "1" : "0") + binary; // Add sign bit
    }
}