package com.example.abcbuddyapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private PunchcardView punchcardView;
    private TextView[] variableTextViews = new TextView[5]; // Holds TextViews for equation variables
    private StringBuilder currentSegment; // Stores the coefficient currently being entered
    private int segmentStartColumn; // Start column of the current 15-column segment
    private boolean awaitingVariable; // True if waiting for variable input after coefficient
    private boolean isNegative, isFirstNumber; // Flags for input handling
    private int currentVariableIndex; // Tracks the variable being assigned (x, y, z, etc.)

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
    }

    private void handleNumpadInput(String input) {
        if (input.equalsIgnoreCase("X")) {
            if (awaitingVariable) {
                char nextVariable = getNextVariable();
                currentSegment.append(nextVariable);

                // Update the corresponding TextView
                if (currentVariableIndex < variableTextViews.length) {
                    variableTextViews[currentVariableIndex].setText(currentSegment.toString());
                }

                // Punch all digits from right to left
                punchStoredNumber();

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
                    isNegative = true; // Mark number as negative
                } else {
                    if (isNegative) {
                        currentSegment.append("-"); // Add leading zero for negatives
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
        // Get the coefficient as a string (excluding the variable)
        String coefficient = currentSegment.toString().replaceAll("[^0-9]", ""); // Remove non-numeric chars
        boolean hasLeadingZero = currentSegment.toString().startsWith("0");

        // Determine where the first digit should be punched
        int punchColumn = segmentStartColumn + 14 - (coefficient.length() - 1);
        if (hasLeadingZero) {
            punchColumn--; // Shift to make space for the leading zero
        }

        // Punch leading zero for negative numbers
        if (hasLeadingZero) {
            punchcardView.punchCell(punchColumn, 0);
            punchColumn++; // Shift back to punch the actual number
        }

        // Punch each digit in reverse order
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
}
