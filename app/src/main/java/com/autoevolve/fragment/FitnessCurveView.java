package com.autoevolve.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import java.util.List;

public class FitnessCurveView extends View {
    private List<Double> bestHistory;
    private List<Double> avgHistory;
    private Paint bestPaint, avgPaint, axisPaint, textPaint;

    public FitnessCurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bestPaint = new Paint(); bestPaint.setColor(Color.parseColor("#4CAF50")); bestPaint.setStrokeWidth(4);
        bestPaint.setAntiAlias(true); bestPaint.setStyle(Paint.Style.STROKE);
        avgPaint = new Paint(); avgPaint.setColor(Color.parseColor("#2196F3")); avgPaint.setStrokeWidth(3);
        avgPaint.setAntiAlias(true); avgPaint.setStyle(Paint.Style.STROKE);
        axisPaint = new Paint(); axisPaint.setColor(Color.GRAY); axisPaint.setStrokeWidth(2);
        textPaint = new Paint(); textPaint.setColor(Color.BLACK); textPaint.setTextSize(24);
    }

    public void setData(List<Double> best, List<Double> avg) {
        this.bestHistory = best;
        this.avgHistory = avg;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bestHistory == null || bestHistory.isEmpty()) return;

        float w = getWidth(), h = getHeight();
        float padLeft = 60, padBottom = 40, padTop = 40, padRight = 20;
        float drawW = w - padLeft - padRight;
        float drawH = h - padTop - padBottom;

        // 坐标轴
        canvas.drawLine(padLeft, h - padBottom, w - padRight, h - padBottom, axisPaint);
        canvas.drawLine(padLeft, padTop, padLeft, h - padBottom, axisPaint);

        // 标签
        canvas.drawText("代数", w / 2, h - 8, textPaint);
        canvas.save();
        canvas.rotate(-90, 20, h / 2);
        canvas.drawText("适应度", 20, h / 2, textPaint);
        canvas.restore();
        canvas.drawText("最优(绿)", w - 140, padTop - 10, textPaint);
        canvas.drawText("平均(蓝)", w - 140, padTop + 20, textPaint);

        double maxFit = 0;
        for (double v : bestHistory) if (v > maxFit) maxFit = v;
        for (double v : avgHistory) if (v > maxFit) maxFit = v;
        if (maxFit < 0.01) maxFit = 1.0;

        int n = bestHistory.size();
        float stepX = drawW / Math.max(n - 1, 1);

        // 画最优曲线
        for (int i = 0; i < n - 1; i++) {
            float x1 = padLeft + i * stepX;
            float y1 = h - padBottom - (float)(bestHistory.get(i) / maxFit) * drawH;
            float x2 = padLeft + (i + 1) * stepX;
            float y2 = h - padBottom - (float)(bestHistory.get(i + 1) / maxFit) * drawH;
            canvas.drawLine(x1, y1, x2, y2, bestPaint);
        }

        // 画平均曲线
        for (int i = 0; i < n - 1; i++) {
            float x1 = padLeft + i * stepX;
            float y1 = h - padBottom - (float)(avgHistory.get(i) / maxFit) * drawH;
            float x2 = padLeft + (i + 1) * stepX;
            float y2 = h - padBottom - (float)(avgHistory.get(i + 1) / maxFit) * drawH;
            canvas.drawLine(x1, y1, x2, y2, avgPaint);
        }
    }
}
