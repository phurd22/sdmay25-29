package com.example.abcbuddyapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class Base2PunchView extends View {

    private final int dotRadius = 8; // Dot size for grid points
    private int xOffset;
    private int yOffset;
    private int xSpacer;
    private int ySpacer;

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

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        xOffset = screenWidth / 40;
        yOffset = screenHeight / 40;
        xSpacer = (screenWidth - 2 * xOffset) / 29;
        ySpacer = (screenHeight - 2 * yOffset) / 49;
    }

    public void setNumbers(List<Long> numbers) {
        bitArray.clear();
        for (long number : numbers) {
            bitArray.add(toTwosComplement50Bit(number));
        }
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int row = 0; row < bitArray.size(); row++) {
            String bits = bitArray.get(row);
            for (int col = 0; col < bits.length(); col++) {
                if (bits.charAt(col) == '1') {
                    float x = xOffset + row * xSpacer;
                    float y = yOffset + col * ySpacer;
                    canvas.drawCircle(x, y, dotRadius, dotPaint);
                }
            }
        }

        drawLabels(canvas);
    }

    private void drawLabels(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30);

        canvas.drawText("b0", xSpacer, yOffset + 50 * ySpacer + 40, textPaint);
        canvas.drawText("n0", xOffset, yOffset + 50 * ySpacer + 80, textPaint);
    }
}
