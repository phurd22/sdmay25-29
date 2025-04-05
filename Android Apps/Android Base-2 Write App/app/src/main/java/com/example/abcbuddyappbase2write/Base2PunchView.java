package com.example.abcbuddyappbase2write;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

//TODO: Have dots update as you receive info from machine and send to read tablet
// when the page is full
public class Base2PunchView extends View {

    private final int dotRadius = 4; // Dot size for grid points
    private float xOffset;
    private float yOffset;
    private float xSpacer;
    private float ySpacer;
//    private int currentPage = 1;
//    private int totalPages = 1;

    private List<String> bitArray = new ArrayList<>(); // Holds 50-bit binary strings
    private Paint dotPaint;

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

        xOffset = screenWidth / 30;
        yOffset = screenHeight / 15;
        xSpacer = (screenWidth - 2 * xOffset) / 49;
        ySpacer = (screenHeight - 2 * yOffset) / 29;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int numRows = bitArray.size();
        int numCols = 50;
        int charIndex;

        for (int row = 0; row < numRows; row++) {
            String bits = bitArray.get(row);
            charIndex = 49;
            for (int col = 0; col < numCols; col++) {
                if (bits.charAt(charIndex) == '1') {
                    float x = xOffset / 2 + (col * xSpacer) + 21; // Start at left
                    float y = yOffset / 2 + (row * ySpacer) + 10; // Start from top
                    canvas.drawCircle(x, y, dotRadius, dotPaint);
                }
                charIndex--;
            }
        }

        drawLabels(canvas);
    }

    public void setNumbers(List<Long> numbers) {
        bitArray.clear();
        for (long number : numbers) {
            bitArray.add(toTwosComplement50Bit(number));
        }
        Log.d("Base2PunchView", numbers.toString());
        invalidate(); // Redraw the view with new data
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
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(20);
        int numRows = bitArray.size();

        // Top-right: b49
        canvas.drawText("b49", xOffset + 49 * xSpacer - 16, yOffset - 30, textPaint);

        // Top-left: b0
        canvas.drawText("b0", xOffset - 13, yOffset - 30, textPaint);

        // Top-most: n0
        canvas.drawText("n0", xOffset - 35, yOffset - 10, textPaint);
    }
}