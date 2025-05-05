package com.example.abcbuddyapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PunchcardView extends View {

    private static final int NUM_COLUMNS = 80; // Total columns
    private static final int NUM_ROWS = 10;    // Total rows
    private static final int SEGMENT_COLUMNS = 16; // Columns per segment

    private final List<List<Boolean>> punchedGrid = new ArrayList<>(); // Store punched status
    private final Paint greyPaint = new Paint();
    private final Paint blackPaint = new Paint();
    private final Paint linePaint = new Paint();
    private final Paint textPaint = new Paint(); // Paint for numbers

    private int cellWidth, cellHeight; // Width and height of each punch area
    private int padding = 2; // Padding between cells
    private int numberMargin = 40; // Space for numbers on left & right

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

        // Paint for numbers
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("Size", "Width: " + w + " Height: " + h);

        // Dynamically calculate cell sizes based on view dimensions
        int totalPadding = padding * (NUM_COLUMNS - 1);
        cellWidth = (w - totalPadding - 2 * numberMargin) / NUM_COLUMNS;
        cellHeight = (h - padding * (NUM_ROWS - 1)) / NUM_ROWS;

        textPaint.setTextSize(cellHeight * 1.0f); // Adjust text size based on cell height
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int startX = numberMargin - 4; // Adjust start position to accommodate numbers

        for (int col = 0; col < NUM_COLUMNS; col++) {
            int startY = 0;

            // Draw faint vertical separators for every 15 columns
            if (col % SEGMENT_COLUMNS == 0 && col > 0) {
                canvas.drawLine(startX, 0, startX, getHeight(), linePaint);
                startX += 2;
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

        // Draw numbers (0-9) on the left and right
        int textXLeft = numberMargin / 2;
        int textXRight = getWidth() - numberMargin / 2;
        for (int row = 0; row < NUM_ROWS; row++) {
            float textY = (row * (cellHeight + padding)) + (cellHeight / 1.0f); //originally 1.5f
            String num = String.valueOf(row);

            canvas.drawText(num, textXLeft, textY, textPaint);  // Left side
            canvas.drawText(num, textXRight, textY, textPaint); // Right side
        }
    }

    // Punches the specified location
    public void punchCell(int column, int row) {
        if (column >= 0 && column < NUM_COLUMNS && row >= 0 && row < NUM_ROWS) {
            punchedGrid.get(column).set(row, true);
            invalidate(); // Redraw the view
        }
    }

    // Clears all punches
    public void clearPunches() {
        for (List<Boolean> column : punchedGrid) {
            for (int row = 0; row < NUM_ROWS; row++) {
                column.set(row, false);
            }
        }
        invalidate(); // Redraw the view
    }
}
