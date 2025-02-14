package com.example.abcbuddyapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PunchcardView extends View {

    private static final int NUM_COLUMNS = 75; // Total columns
    private static final int NUM_ROWS = 10;    // Total rows
    private static final int SEGMENT_COLUMNS = 15; // Columns per segment

    private final List<List<Boolean>> punchedGrid = new ArrayList<>(); // Store punched status
    private final Paint greyPaint = new Paint();
    private final Paint blackPaint = new Paint();
    private final Paint linePaint = new Paint();

    private int cellWidth, cellHeight; // Width and height of each punch area
    private int padding = 2; // Padding between cells

    public PunchcardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Initialize punchedGrid
        for (int col = 0; col < NUM_COLUMNS; col++) {
            List<Boolean> column = new ArrayList<>();
            for (int row = 0; row < NUM_ROWS; row++) {
                column.add(false); // Initially, no punches
            }
            punchedGrid.add(column);
        }

        // Paint for empty punch locations
        greyPaint.setColor(Color.LTGRAY);
        greyPaint.setStyle(Paint.Style.FILL);

        // Paint for punched locations
        blackPaint.setColor(Color.BLACK);
        blackPaint.setStyle(Paint.Style.FILL);

        // Paint for faint lines between equation parts
        linePaint.setColor(Color.DKGRAY);
        linePaint.setStrokeWidth(4f);
        linePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Dynamically calculate cell sizes based on view dimensions
        int totalPadding = padding * (NUM_COLUMNS - 1);
        cellWidth = (w - totalPadding) / NUM_COLUMNS;
        cellHeight = (h - padding * (NUM_ROWS - 1)) / NUM_ROWS;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int startX = 0;

        for (int col = 0; col < NUM_COLUMNS; col++) {
            int startY = 0;

            // Draw faint vertical separators for every 15 columns
            if (col % SEGMENT_COLUMNS == 0 && col > 0) {
                canvas.drawLine(startX, 0, startX, getHeight(), linePaint);
            }

            for (int row = 0; row < NUM_ROWS; row++) {
                float rectLeft = startX;
                float rectTop = startY;
                float rectRight = startX + cellWidth;
                float rectBottom = startY + cellHeight;

                RectF rect = new RectF(rectLeft, rectTop, rectRight, rectBottom);

                // Draw greyed-out punch positions
                canvas.drawRoundRect(rect, 10, 10, greyPaint);

                // Draw punched hole if marked
                if (punchedGrid.get(col).get(row)) {
                    canvas.drawRoundRect(rect, 10, 10, blackPaint);
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
