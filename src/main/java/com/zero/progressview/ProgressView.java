package com.zero.progressview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by xq on 16-10-18.
 */
public class ProgressView extends View {
    private int viewWidth = (int) (getScreenWidth() * 0.6f);
    private int viewHeight = viewWidth;
    private int centerX = viewWidth / 2;
    private int centerY = viewHeight / 2;
    private Paint degreePaint;
    private int degreeLength = viewWidth / 18;//刻度的长度
    private int textSize = viewWidth / 3;
    private int count = 180;
    private int offset = viewWidth / 18;
    private Rect rect = new Rect();
    private RectF rectF = new RectF();
    private String title = "今日步数";
    private String planText = "目标计划";
    private String centerText;
    private String acupointText = "健身小跑";
    private String modifyText = "修改计划";
    private String step = "步";
    private Paint textPaint;
    private Paint currentTextPaint;
    private Paint planPaint;
    private Paint buttonPaint;
    private Paint buttonTextPaint;
    private float currentValues = 0;
    private float currentAngle = 0;
    private float lastAngle;
    private int aniSpeed = 1500;
    private float total;
    private ValueAnimator progressAnimator;
    private String TAG = this.getClass().getSimpleName();

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        degreePaint = new Paint();
        degreePaint.setAntiAlias(true);
        degreePaint.setColor(Color.parseColor("#abcdef"));
        degreePaint.setStyle(Paint.Style.FILL);
        degreePaint.setStrokeWidth(dp2px(1f));

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(viewWidth / 15);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.parseColor("#66000000"));

        currentTextPaint = new Paint();
        currentTextPaint.setAntiAlias(true);
        currentTextPaint.setTextSize(textSize);
        currentTextPaint.setStyle(Paint.Style.FILL);
        currentTextPaint.setFakeBoldText(true);//设置字体为粗体
        currentTextPaint.setTextAlign(Paint.Align.CENTER);
        currentTextPaint.setColor(getResources().getColor(android.R.color.holo_blue_light));

        planPaint = new Paint();
        planPaint.setAntiAlias(true);
        planPaint.setColor(Color.GRAY);
        planPaint.setTextSize(viewWidth / 20);
        planPaint.setTextAlign(Paint.Align.CENTER);

        buttonPaint = new Paint();
        buttonPaint.setAntiAlias(true);
        buttonPaint.setStyle(Paint.Style.STROKE);
        buttonPaint.setStrokeCap(Paint.Cap.ROUND);
        buttonPaint.setStrokeWidth(viewWidth / 9);
        buttonPaint.setColor(getResources().getColor(android.R.color.holo_blue_light));

        buttonTextPaint = new Paint();
        buttonTextPaint.setAntiAlias(true);
        buttonTextPaint.setTextSize(viewWidth / 15);
        buttonTextPaint.setStyle(Paint.Style.FILL);
        buttonTextPaint.setTextAlign(Paint.Align.CENTER);
        buttonTextPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.
        //绘制刻度
        for (int i = 0; i < count; i++) {
            if (i > count * 5 / 8 && i < count * 7 / 8) {
                canvas.rotate(360 / count, centerX, centerY);
                continue;
            }
            degreePaint.setColor(getResources().getColor(android.R.color.holo_blue_light));
            canvas.drawLine(0, centerY, degreeLength, centerY, degreePaint);
            canvas.rotate(360 / count, centerX, centerY);
        }
        //绘制标题
        canvas.drawText(title, centerX, centerY - textSize / 2 - offset, textPaint);
        //绘制中间字体 centerText
        canvas.drawText(((int) currentValues) + "", centerX, centerY + textSize / 3, currentTextPaint);
        float textWith = currentTextPaint.measureText(((int) currentValues) + "");
        //绘制天
        canvas.drawText(step, centerX + textWith/2 + offset, centerY + textSize / 3, textPaint);
        //目标计划
        canvas.drawText(planText, centerX, centerY + textSize * 2 / 3, planPaint);
        canvas.drawText(acupointText, centerX, centerY + textSize * 2 / 3 + 1.5f * offset, planPaint);
        float btnY = viewWidth - textSize / 3;
        canvas.drawLine(viewWidth / 3, btnY, viewWidth * 2 / 3, btnY, buttonPaint);
        rectF.set(viewWidth / 3, btnY - textSize / 6, viewWidth * 2 / 3, btnY + textSize / 6);
        //绘制“修改计划”
        canvas.drawText(modifyText, centerX, btnY + textSize / 12, buttonTextPaint);
        //绘制当前进度
        if (currentAngle > 0) {
            //画布先旋转-46度
            canvas.rotate(-46, centerX, centerY);
            //绘制刻度
            for (int i = 0; i < currentAngle; i++) {
                degreePaint.setColor(getResources().getColor(android.R.color.holo_red_light));
                canvas.drawLine(0, centerY, degreeLength, centerY, degreePaint);
                canvas.rotate(360 / count, centerX, centerY);
            }
            // 绘制最后一格刻度
            if (currentAngle*2 == 360*3/4){
                canvas.drawLine(0,centerY,degreeLength,centerY,degreePaint);
            }
        }
    }

    float x = 0;
    float y = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            x = event.getX();
            y = event.getY();
            Log.d(TAG, "onTouchEvent: x=" + x + "=====y=" + y);
        } else if (action == MotionEvent.ACTION_MOVE) {
        } else if (action == MotionEvent.ACTION_UP) {
            float upX = event.getX();
            float upY = event.getY();
            if (rectF.contains(upX, upY)) {
                Log.d(TAG, "you click the button " + (upX - x) + "-------------" + (upY - y));
                if (Math.abs(upX - x) < 10 || Math.abs(upY - y) < 10) {
                    if (listener != null) {
                        listener.onCommandClick(this);
                    }
                }
            }
        }
        return true;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public void setViewWidth(int width) {
        this.viewWidth = width;
    }

    public void setPlanText(String planText) {
        this.acupointText = planText;
    }

    public void setCenterText(String centerText) {
        this.centerText = centerText;
    }

    /**
     * 设置完属性后需要调用此方法刷新界面
     */
    public void valuesSetCommplete() {
        if (currentValues > 0 && total > 0) {
            setAnimation(lastAngle, currentValues, aniSpeed);
        } else {
            invalidate();
        }
    }

    public void setCurrentCount(float total, float currentValues) {
        this.currentValues = currentValues;
        this.total = total;
        if (currentValues > count) {
//            currentValues = count;
        }
        if (currentValues < 0) {
            currentValues = 0;
        }
        setAnimation(lastAngle, currentValues, aniSpeed);
    }

    /**
     * 为进度设置动画
     *
     * @param last
     * @param current
     */
    private void setAnimation(float last, float current, int length) {
        progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngle);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentValues = (float) animation.getAnimatedValue();
                currentAngle = currentValues / total * (count * 6 / 8);
                Log.i(TAG, "onAnimationUpdate: currentAngle=" + currentAngle);
                invalidate();
            }
        });
        progressAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(viewWidth, viewHeight);
    }

    private int dp2px(float dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5f * (dp >= 0 ? 1 : -1));
    }

    private int getScreenWidth() {
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private CommandClickListener listener;

    public interface CommandClickListener {
        void onCommandClick(View view);
    }

    public void setCommandClickListener(CommandClickListener listener) {
        this.listener = listener;
    }
}

