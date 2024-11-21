package com.example.abcbuddyapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PunchcardView extends View {

    private List<String> equationParts = new ArrayList<>(); // Store equation parts
    private Paint paint;
    private int cellSize = 50; // Size of each binary cell
    private int padding = 10; // Padding between rows

    public PunchcardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int startY = padding;

        for (String part : equationParts) {
            drawEquationPart(canvas, part, startY);
            startY += cellSize + padding; // Move to the next row
        }
    }

    private void drawEquationPart(Canvas canvas, String part, int y) {
        int startX = padding;

        for (char bit : part.toCharArray()) {
            if (bit == '1') {
                canvas.drawCircle(startX + cellSize / 2, y + cellSize / 2, cellSize / 3, paint); // Draw a hole for 1
            } else {
                // Leave blank for 0
            }
            startX += cellSize; // Move to the next cell
        }
    }

    public void addEquationPart(String binaryPart) {
        equationParts.add(binaryPart);
        invalidate(); // Redraw the view
    }

    public void clearEquationParts() {
        equationParts.clear();
        invalidate(); // Redraw the view
    }

}


