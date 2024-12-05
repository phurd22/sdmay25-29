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
    private TextView equationTextView;
    private StringBuilder currentEquation;
    private int currentColumn; // Tracks the current column for punching
    private boolean awaitingVariable; // Tracks whether a variable should follow the current coefficient
    private int segmentStartColumn; // Tracks the start of each 15-column segment
    private boolean isNegative, isPositive; // Indicates if the current part of the equation is negative

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
        equationTextView = findViewById(R.id.textView);
        equationTextView.setText("Please enter an equation");
        currentEquation = new StringBuilder();
        currentColumn = 0;
        awaitingVariable = false;
        segmentStartColumn = 0;
        isNegative = false;
        isPositive = false;

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
                currentEquation.append(nextVariable);
                awaitingVariable = false;

                // Move to next segment
                segmentStartColumn += 15;
                currentColumn = segmentStartColumn;
                isNegative = false; // Reset negative indicator
                isPositive = false;
            }
        } else {
            int number = Integer.parseInt(input);
            if (number == 0) {
                // User entered 0 as a leading digit (negative indicator)
                isPositive = false;
                isNegative = true;
            } else {
                currentEquation.append(number);
                if (isNegative) {
                    currentEquation.insert(currentEquation.length() - 1, "-");
                    isNegative = false;
                }
            }
            if (number != 0) {
                isPositive = true;
            } else {
                currentEquation.append(number);
                if (isPositive) {
                    currentEquation.insert(currentEquation.length() - 1, "+");
                    isPositive = false;
                }
            }
            punchNumber(number);
            awaitingVariable = true;
        }

        equationTextView.setText(currentEquation.toString());
    }

    private char getNextVariable() {
        String variables = "xyzw";
        int index = (currentEquation.length() - segmentStartColumn) / 15;
        return variables.charAt(index % variables.length());
    }

    private void punchNumber(int number) {
        int row = number; // Row maps directly to the number (0-9)
        punchcardView.punchCell(currentColumn++, row);
    }

    private void clearEquation() {
        currentEquation.setLength(0);
        equationTextView.setText("");
        punchcardView.clearPunches();
        currentColumn = 0;
        segmentStartColumn = 0;
        equationTextView.setText("Please enter an equation");
        awaitingVariable = false;
        isNegative = false;
    }
}
