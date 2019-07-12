package com.qyt.bm.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.utils.DateFormatUtil;
import com.qyt.bm.utils.LogInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 设备曲线图
 */
public class DeviceDataLine extends View {

    private final int PADDING_X = 20;
    private final int PADDING_Y = 24;
    private final int TEXT_SIZE = 10;
    private final int PADDING_X_R = 56;
    private int padding_X_px, padding_Y_px, textSize;
    private ArrayList<String> rawDatas = new ArrayList<>();
    private ArrayList<String> timeDatas = new ArrayList<>();
    private int lineType = 0;//0-温度，1-PH，2-溶氧
    private ArrayList<Long> timePoints = new ArrayList<>();
    private ArrayList<Double> valuePoints = new ArrayList<>();
    private long startTime, endTime;//数据时间节点
    public static final String TIME_FORMAT = "kk:mm";
    public static final String DATE_FORMAT = "M月d号";
    private String[] xFlags = new String[5];
    private String[] xFlags2 = new String[5];
    private double minValue, maxValue;
    private long minY, maxY;//Y轴最大、最小值
    private int lineX, lineY;//XY坐标轴长度
    private int startX, endX, startY, endY;//坐标系起止点
    private long dataInterval;//数据取点间隔
    private final long DEFAULT_INTERVAL = 10 * 60 * 1000;//默认数据取点间隔

    public DeviceDataLine(Context context) {
        this(context, null);
    }

    public DeviceDataLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DeviceDataLine(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        textSize = AppUtils.sp2px(context, TEXT_SIZE);
        padding_X_px = AppUtils.dp2px(context, PADDING_X);
        padding_Y_px = AppUtils.dp2px(context, PADDING_Y);
    }

    public void refreshViewData(int type, List<String> times, List<String> datas) {
        this.rawDatas.clear();
        this.rawDatas.addAll(datas);
        this.timeDatas.clear();
        this.timeDatas.addAll(times);
        this.lineType = type;
        dealTimes();
        dealValues();
        if (timePoints.size() != valuePoints.size()) {
            LogInfo.error("数据错误");
            return;
        }
        LogInfo.error("数据:" + timePoints.size());
        postInvalidate();
    }

    private void dealValues() {
        valuePoints.clear();
        minValue = 10000;
        maxValue = 0;
        for (String data : rawDatas) {
            double va = Double.parseDouble(data);
            valuePoints.add(va);
            if (lineType == 1 && va < 0) {
                va = 7;
            }
            minValue = minValue > va ? va : minValue;
            maxValue = maxValue < va ? va : maxValue;
        }
        long dev = Math.round((minValue + maxValue) / 2);
        long inv = Math.round(dev - minValue) + 5;
        minY = dev - inv;
        maxY = dev + inv;
    }

    private void dealTimes() {
        timePoints.clear();
        for (String time : timeDatas) {
            timePoints.add(Long.valueOf(time));
        }
        int size = timePoints.size();
        startTime = timePoints.get(0);
        endTime = timePoints.get(size - 1);
        int num = xFlags.length;
        long inv = (endTime - startTime) / (num - 1);
        for (int i = 0; i < num; i++) {
            xFlags[i] = DateFormatUtil.getDate2Str(new Date(startTime + inv * i), TIME_FORMAT);
            xFlags2[i] = DateFormatUtil.getDate2Str(new Date(startTime + inv * i), DATE_FORMAT);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculate();
        drawXyLines(canvas);
        drawValueLines(canvas);
    }

    private void calculate() {
        int w = getWidth();
        int h = getHeight();
        lineX = w - padding_X_px - PADDING_X_R;
        lineY = h - padding_Y_px - textSize;
        startX = padding_X_px;
        endX = w - PADDING_X_R;
        startY = h - padding_Y_px;
        endY = textSize;
        dataInterval = (endTime - startTime) / lineX;
        dataInterval = dataInterval > DEFAULT_INTERVAL ? dataInterval : DEFAULT_INTERVAL;
    }

    private void drawValueLines(Canvas canvas) {
        Paint paint = new Paint();
        paint.reset();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        Path vline = new Path();
        long showTime = startTime;
        for (int i = 0; i < timePoints.size(); i++) {
            long pt = timePoints.get(i);
            if (pt >= showTime) {
                double value = valuePoints.get(i);
                if (lineType == 1 && value < 0) {
                    continue;
                }
                float y = (float) ((value - minY) * lineY / (maxY - minY));
                float x = (pt - startTime) * lineX / (endTime - startTime);
                if (vline.isEmpty()) {
                    vline.moveTo(startX + x, startY - y);
                } else {
                    vline.lineTo(startX + x, startY - y);
                }
                showTime += dataInterval;
            }
        }
        canvas.drawPath(vline, paint);
    }

    private void drawXyLines(Canvas canvas) {
        Paint paint = new Paint();
        paint.reset();
        paint.setColor(Color.parseColor("#4876FF"));
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        /*X轴*/
        canvas.drawLine(startX, startY, endX, startY, paint);
        /*Y轴*/
        canvas.drawLine(startX, startY, startX, endY, paint);
        /*Y轴数值*/
        paint.setTextSize(textSize);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#2E2E2E"));
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(minY + "", startX / 2, startY, paint);
        canvas.drawText((minY + maxY) / 2 + "", startX / 2, endY + lineY / 2, paint);
        canvas.drawText(maxY + "", startX / 2, textSize, paint);
        /*X轴数值*/
        if (startX > 0 && endX > 0) {
            int num = xFlags.length;
            int inv = lineX / (num - 1);
            for (int i = 0; i < num; i++) {
                if (xFlags[i] != null) {
                    canvas.drawText(xFlags2[i], startX + i * inv, startY + textSize, paint);
                    canvas.drawText(xFlags[i], startX + i * inv, startY + textSize * 2, paint);
                    canvas.drawLine(startX + i * inv, startY, startX + i * inv, startY - 8, paint);
                }
            }
        }
        /*中间线*/
        paint.setColor(Color.parseColor("#4876FF"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{8, 8}, 0));
        Path middle_line = new Path();
        middle_line.moveTo(startX, endY + lineY / 2);
        middle_line.lineTo(endX, endY + lineY / 2);
        canvas.drawPath(middle_line, paint);
    }
}
