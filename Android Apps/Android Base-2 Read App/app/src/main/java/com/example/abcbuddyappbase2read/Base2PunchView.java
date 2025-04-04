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
//TODO: Need to flip it top to bottom and keep it that way
//TODO: Need to add button that flips it left to right, should start with least significant on left
// and most significant on right (backwards from normal)
public class Base2PunchView extends View {

    private final int dotRadius = 4; // Dot size for grid points
    private float xOffsetRight;
    private float xOffsetLeft;
    private float totalXOffset;
    private float yOffset;
    private float xSpacer;
    private float ySpacer;
    private int mode;

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
                    float x = (float) getWidth() - xOffsetRight - (col * xSpacer);  // Start at right
                    float y = (float) getHeight() - yOffset - (row * ySpacer); // Start from bottom
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
                    float y = getHeight() - yOffset - (row * ySpacer) - (ySpacer / 2) - 5;
                    canvas.drawLine(startX, y, stopX, y, linePaint);
                }
            }
            if (mode == 0) {
                drawLabels(canvas);
            }
        }
        else {
            for (int row = 0; row < numRows; row++) {
                String text = String.valueOf(numbers.get(row));
                float textWidth = numberPaint.measureText(text); // Get text width
                float x = (float) (getWidth() / 2) - (textWidth / 2); // Center horizontally
                float y = (float) getHeight() - yOffset - (row * ySpacer); // Start from bottom
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

    public void changeMode() {
        mode = (mode + 1) % 3;
        Log.d("Base2PunchView", "Mode changed to: " + mode);
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
        // Bottom-left: b49
        canvas.drawText("b49", xOffsetRight - 12, (float) getHeight() - yOffset + 30, labelPaint);

        // Bottom-right: b0
        canvas.drawText("b0", xOffsetRight + 49 * xSpacer - 2, (float) getHeight() - yOffset + 30, labelPaint);

        // Bottom-most: n0
        canvas.drawText("n0", xOffsetRight - 32, (float) getHeight() - yOffset + 7, labelPaint);
    }
}