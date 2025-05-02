package com.example.abcbuddyappbase2read;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

// Models the ABC base-2 punch card.
// Has more modes to show binary and decimal numbers.
// Also has the ability to flip the bits left to right.
public class Base2PunchView extends View {

    private final int dotRadius = 4; // Dot size for grid points
    private float xOffsetRight;
    private float xOffsetLeft;
    private float totalXOffset;
    private float yOffset;
    private float xSpacer;
    private float ySpacer;
    private int mode; // 0 for dots, 1 for binary, 2 for decimal
    private int direction; // 0 for normal, 1 for readable

    private List<String> bitArray = new ArrayList<>(); // Holds 50-bit binary strings
    private List<Long> numbers = new ArrayList<>(); // Holds the actual numbers
    private Paint dotPaint;
    private Paint linePaint;
    private Paint labelPaint;
    private Paint numberPaint;
    private Paint binaryNumPaint;

    public Base2PunchView(Context context) {
        super(context);
        init(context);
    }

    public Base2PunchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Base2PunchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        dotPaint = new Paint();
        dotPaint.setColor(Color.BLACK);
        dotPaint.setStyle(Paint.Style.FILL);
        linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth((float)0.5);
        linePaint.setStyle(Paint.Style.STROKE);
        labelPaint = new Paint();
        labelPaint.setColor(Color.BLACK);
        labelPaint.setTextSize(20);
        numberPaint = new Paint();
        numberPaint.setColor(Color.BLACK);
        numberPaint.setTextSize(20);
        binaryNumPaint = new Paint();
        binaryNumPaint.setColor(Color.BLACK);
        binaryNumPaint.setTextSize(15);
        mode = 0;
        direction = 0;
        calculateDimensions(context);
    }

    private void calculateDimensions(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }

        float screenWidth = (float)displayMetrics.widthPixels;
        float screenHeight = (float)displayMetrics.heightPixels;
        Log.d("Base2PunchView", "Screen width: " + screenWidth + ", screen height: " + screenHeight);

        xOffsetRight = screenWidth / 30;
        xOffsetLeft = screenWidth / 8;
        totalXOffset = xOffsetRight + xOffsetLeft;
        yOffset = screenHeight / 15;
        xSpacer = (screenWidth - totalXOffset) / 49;
        ySpacer = (screenHeight - 2 * yOffset) / 29;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int numRows = bitArray.size();
        int numCols = 50;
        int charIndex;

        if (mode == 0 || mode == 1) {
            for (int row = 0; row < numRows; row++) {
                String bits = bitArray.get(row);
                charIndex = 49;
                for (int col = 0; col < numCols; col++) {
                    float x;
                    float y;
                    if (mode == 0) {
                        y = yOffset / 2 + (row * ySpacer); // Start from top
                    }
                    else {
                        y = yOffset / 2 + (row * ySpacer) + 10; // Start from top
                    }
                    if (direction == 1 || mode == 1) {
                        x = (float) getWidth() - xOffsetRight - (col * xSpacer);  // Start at right
                    }
                    else {
                        x = xOffsetRight + 8 + (col * xSpacer); // Start at left
                    }
                    if (bits.charAt(charIndex) == '1') {
                        if (mode == 0) {
                            canvas.drawCircle(x, y, dotRadius, dotPaint);
                        }
                        else {
                            canvas.drawText("1", x, y, binaryNumPaint);
                        }
                    }
                    else if (mode == 1) {
                        canvas.drawText("0", x, y, binaryNumPaint);
                    }
                    charIndex--;
                }
                if (mode == 1 && row != numRows - 1) { // Draw lines between binary numbers
                    float startX = getWidth() - xOffsetRight - ((numCols - 1) * xSpacer) - 3;
                    float stopX = getWidth() - xOffsetRight + 12;
                    float y = yOffset / 2 + (row * ySpacer) + 16;
                    canvas.drawLine(startX, y, stopX, y, linePaint);
                }
            }
            if (mode == 0 || mode == 1) {
                drawLabels(canvas);
            }
        }
        else {
            // Get width of widest number in array
            float maxTextWidth = 0;
            for (int i = 0; i < numbers.size(); i++) {
                float width = numberPaint.measureText(String.valueOf(numbers.get(i)));
                if (width > maxTextWidth) {
                    maxTextWidth = width;
                }
            }

            // Use widest width to determine where right edge of numbers should be
            float rightEdge = (float) getWidth() / 2 + maxTextWidth / 2;

            for (int row = 0; row < numRows; row++) {
                String text = String.valueOf(numbers.get(row));
                float textWidth = numberPaint.measureText(text); // Get text width
                //float x = (float) (getWidth() / 2) - (textWidth / 2); // Center horizontally
                float x = rightEdge - textWidth - 30;
                float y = yOffset / 2 + (row * ySpacer); // Start from top
                canvas.drawText(String.valueOf(numbers.get(row)), x, y, numberPaint);
            }
        }
    }

    public void setNumbers(List<Long> numbers) {
        bitArray.clear();
        for (long number : numbers) {
            bitArray.add(toTwosComplement50Bit(number));
        }
        this.numbers = numbers;
        Log.d("Base2PunchView", numbers.toString());
        invalidate(); // Redraw the view with new data
    }

    public int getMode() {
        return mode;
    }

    public void changeMode() {
        mode = (mode + 1) % 3;
        Log.d("Base2PunchView", "Mode changed to: " + mode);
        invalidate();
    }

    public void changeDirection() {
        direction = (direction + 1) % 2;
        Log.d("Base2PunchView", "Direction changed to: " + direction);
        invalidate();
    }

    private String toTwosComplement50Bit(long number) {
        if (number >= 0) {
            return String.format("%050d", new java.math.BigInteger(Long.toBinaryString(number)));
        } else {
            long twosComplement = (1L << 50) + number;
            return String.format("%050d", new java.math.BigInteger(Long.toBinaryString(twosComplement)));
        }
    }

    private void drawLabels(Canvas canvas) {
        if (direction == 0 && mode == 0) {
            // Top-right: b49
            canvas.drawText("b49", xOffsetRight + 49 * xSpacer - 8, yOffset - 36, labelPaint);

            // Top-left: b0
            canvas.drawText("b0", xOffsetRight - 5, yOffset - 36, labelPaint);

            // Top-most: n0
            canvas.drawText("n0", xOffsetRight - 32, yOffset - 20, labelPaint);
        }
        else if (mode == 0) {
            // Top-left: b49
            canvas.drawText("b49", xOffsetRight - 9, yOffset - 36, labelPaint);

            // Top-right: b0
            canvas.drawText("b0", xOffsetRight + 49 * xSpacer - 4, yOffset - 36, labelPaint);

            // Top-most: n0
            canvas.drawText("n0", xOffsetRight - 32, yOffset - 20, labelPaint);
        }
        else if (mode == 1) {
            // Top-left: b49
            canvas.drawText("b49", xOffsetRight - 7, yOffset - 36, labelPaint);

            // Top-right: b0
            canvas.drawText("b0", xOffsetRight + 49 * xSpacer - 1, yOffset - 36, labelPaint);

            // Top-most: n0
            canvas.drawText("n0", xOffsetRight - 30, yOffset - 14, labelPaint);
        }
    }
}