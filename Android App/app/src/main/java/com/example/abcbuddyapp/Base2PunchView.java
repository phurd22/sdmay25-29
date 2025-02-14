package com.example.abcbuddyapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class Base2PunchView extends View {

    //TODO: Need to add ability to switch pages. Add touch event listener for
    // the left and right arrow buttons.
    private OnPageChangeListener pageChangeListener;

    private final int dotRadius = 8; // Dot size for grid points
    private int xOffset;
    private int botYOffset;
    private int topYOffset;
    private int xSpacer;
    private int ySpacer;
    private boolean drawLeftArrow;
    private boolean drawRightArrow;

    private int currentPage = 1;
    private int totalPages = 1;

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

    public void setOnPageChangeListener(OnPageChangeListener pageChangeListener) {
        this.pageChangeListener = pageChangeListener;
    }

    public interface OnPageChangeListener {
        void onPreviousPage();
        void onNextPage();
    }

    private void calculateDimensions(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        xOffset = screenWidth / 10;
        botYOffset = screenHeight / 20;
        topYOffset = screenHeight / 33;
        xSpacer = (screenWidth - 2 * xOffset) / 29;
        ySpacer = (screenHeight - (botYOffset + topYOffset)) / 49;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int row = 0; row < bitArray.size(); row++) {
            String bits = bitArray.get(row);
            for (int col = 0; col < bits.length(); col++) {
                if (bits.charAt(col) == '1') {
                    float x = xOffset + row * xSpacer;
                    float y = topYOffset + col * ySpacer;
                    canvas.drawCircle(x, y, dotRadius, dotPaint);
                }
            }
        }

        drawLabels(canvas);
        drawNavigationIndicators(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float touchX = event.getX();
            float touchY = event.getY();
            float arrowSize = xOffset - xOffset / 4; // Define the area for touch detection

            if (touchX < arrowSize && touchY > getHeight() / 2 - botYOffset && touchY < getHeight() / 2 + botYOffset) {
                // Left arrow tapped
                if (pageChangeListener != null) {
                    pageChangeListener.onPreviousPage();
                }
                return true;
            } else if (touchX > getWidth() - arrowSize && touchY > getHeight() / 2 - botYOffset && touchY < getHeight() / 2 + botYOffset) {
                // Right arrow tapped
                if (pageChangeListener != null) {
                    pageChangeListener.onNextPage();
                }
                return true;
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void setNumbers(List<Long> numbers, int currentPage, int totalPages) {
        bitArray.clear();
        for (long number : numbers) {
            bitArray.add(toTwosComplement50Bit(number));
        }
        this.currentPage = currentPage;
        this.totalPages = totalPages;
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
        textPaint.setTextSize(45);

        canvas.drawText("b0", xOffset / 2, (topYOffset + ySpacer * 50) - 25, textPaint);
        canvas.drawText("b49", xOffset / 2 - 20, topYOffset + 10, textPaint);
        canvas.drawText("n0", xOffset - 25, topYOffset + 50 * ySpacer + 17, textPaint);
    }

    private void drawNavigationIndicators(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(60);

        // Draw left arrow
        if (drawLeftArrow) {
            canvas.drawText("<", xOffset / 4, getHeight() / 2, textPaint);
        }

        // Draw right arrow
        if (drawRightArrow) {
            canvas.drawText(">", getWidth() - xOffset / 2, getHeight() / 2, textPaint);
        }

        // Draw page count in bottom right
        String pageText = currentPage + " / " + totalPages;
        canvas.drawText(pageText, getWidth() - xOffset, getHeight() - botYOffset / 2 + 15, textPaint);
    }

    public void setDrawLeftArrow(boolean drawLeftArrow) {
        this.drawLeftArrow = drawLeftArrow;
    }

    public void setDrawRightArrow(boolean drawRightArrow) {
        this.drawRightArrow = drawRightArrow;
    }
}