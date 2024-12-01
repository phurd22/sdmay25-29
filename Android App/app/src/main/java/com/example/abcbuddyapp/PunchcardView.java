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

    private static final int NUM_COLUMNS = 75; // Total columns
    private static final int NUM_ROWS = 10;    // Total rows

    private List<List<Boolean>> punchedGrid; // Store punched status for each cell
    private Paint greyPaint, blackPaint;
    private int cellWidth, cellHeight; // Width and height of each punch area
    private int padding = 8; // Padding between cells

    public PunchcardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        punchedGrid = new ArrayList<>();
        for (int col = 0; col < NUM_COLUMNS; col++) {
            List<Boolean> column = new ArrayList<>();
            for (int row = 0; row < NUM_ROWS; row++) {
                column.add(false); // Initially, no punches
            }
            punchedGrid.add(column);
        }

        greyPaint = new Paint();
        greyPaint.setColor(Color.LTGRAY);
        greyPaint.setStyle(Paint.Style.FILL);

        blackPaint = new Paint();
        blackPaint.setColor(Color.BLACK);
        blackPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Dynamically calculate cell sizes based on view dimensions
        int totalPadding = padding * (NUM_COLUMNS - 1);
        cellWidth = (w - totalPadding) / NUM_COLUMNS;
        cellHeight = (h - totalPadding) / NUM_ROWS;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int startX = 0;

        for (int col = 0; col < NUM_COLUMNS; col++) {
            int startY = 0;

            // Draw greyed-out punch positions for this column
            for (int row = 0; row < NUM_ROWS; row++) {
                float rectLeft = startX;
                float rectTop = startY;
                float rectRight = startX + cellWidth;
                float rectBottom = startY + cellHeight;

                canvas.drawRoundRect(rectLeft, rectTop, rectRight, rectBottom, 10, 10, greyPaint);

                // Draw punched hole if marked
                if (punchedGrid.get(col).get(row)) {
                    float cx = startX + cellWidth / 2f;
                    float cy = startY + cellHeight / 2f;
                    float radius = Math.min(cellWidth, cellHeight) / 2.5f; // Larger radius
                    canvas.drawCircle(cx, cy, radius, blackPaint);
                }

                startY += cellHeight + padding; // Move to next row
            }

            startX += cellWidth + padding; // Move to the next column
        }
    }

    public void punchCell(int column, int row) {
        if (column >= 0 && column < NUM_COLUMNS && row >= 0 && row < NUM_ROWS) {
            punchedGrid.get(column).set(row, true);
            invalidate(); // Redraw the view
        }
    }

    public void clearPunches() {
        for (List<Boolean> column : punchedGrid) {
            for (int row = 0; row < NUM_ROWS; row++) {
                column.set(row, false);
            }
        }
        invalidate(); // Redraw the view
    }
}





