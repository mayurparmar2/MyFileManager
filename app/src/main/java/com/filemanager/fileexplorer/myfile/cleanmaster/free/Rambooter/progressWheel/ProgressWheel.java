package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.progressWheel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.view.ViewCompat;

import com.demo.example.R;


public class ProgressWheel extends View {
    private int barColor;
    private int barLength;
    private Paint barPaint;
    private int barWidth;
    private RectF circleBounds;
    private int circleColor;
    private RectF circleInnerContour;
    private RectF circleOuterContour;
    private Paint circlePaint;
    private int circleRadius;
    private int contourColor;
    private Paint contourPaint;
    private float contourSize;
    private int delayMillis;
    private int fullRadius;
    boolean isSpinning;
    private int layout_height;
    private int layout_width;
    private int paddingBottom;
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    int progress;
    private RectF rectBounds;
    private int rimColor;
    private Paint rimPaint;
    private int rimWidth;
    private Handler spinHandler;
    private int spinSpeed;
    private String[] splitText;
    private String text;
    private int textColor;
    private Paint textPaint;
    private int textSize;

    public ProgressWheel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.layout_height = 0;
        this.layout_width = 0;
        this.fullRadius = 100;
        this.circleRadius = 80;
        this.barLength = 60;
        this.barWidth = 20;
        this.rimWidth = 20;
        this.textSize = 20;
        this.contourSize = 0.0f;
        this.paddingTop = 5;
        this.paddingBottom = 5;
        this.paddingLeft = 5;
        this.paddingRight = 5;
        this.barColor = -1442840576;
        this.contourColor = -1442840576;
        this.circleColor = 0;
        this.rimColor = -1428300323;
        this.textColor = ViewCompat.MEASURED_STATE_MASK;
        this.barPaint = new Paint();
        this.circlePaint = new Paint();
        this.rimPaint = new Paint();
        this.textPaint = new Paint();
        this.contourPaint = new Paint();
        this.rectBounds = new RectF();
        this.circleBounds = new RectF();
        this.circleOuterContour = new RectF();
        this.circleInnerContour = new RectF();
        this.spinSpeed = 2;
        this.delayMillis = 0;
        this.spinHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                ProgressWheel.this.invalidate();
                if (ProgressWheel.this.isSpinning) {
                    ProgressWheel.this.progress += ProgressWheel.this.spinSpeed;
                    if (ProgressWheel.this.progress > 360) {
                        ProgressWheel.this.progress = 0;
                    }
                    ProgressWheel.this.spinHandler.sendEmptyMessageDelayed(0, ProgressWheel.this.delayMillis);
                }
            }
        };
        this.progress = 0;
        this.isSpinning = false;
        this.text = "";
        this.splitText = new String[0];
//        public static final int[] ProgressWheel = {
//                R.attr.barColor,
//                R.attr.barLength,
//                R.attr.barWidth,
//                R.attr.circleColor,
//                R.attr.contourColor,
//                R.attr.contourSize,
//                R.attr.delayMillis,
//                R.attr.radius,
//                R.attr.rimColor,
//                R.attr.rimWidth,
//                R.attr.spinSpeed,
//                R.attr.text,
//                R.attr.textColor,
//                R.attr.textSize};

        parseAttributes(context.obtainStyledAttributes(attributeSet, R.styleable.ProgressWheel));
    }

    @Override
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int paddingLeft = (measuredWidth - getPaddingLeft()) - getPaddingRight();
        int paddingTop = (measuredHeight - getPaddingTop()) - getPaddingBottom();
        if (paddingLeft > paddingTop) {
            paddingLeft = paddingTop;
        }
        setMeasuredDimension(getPaddingLeft() + paddingLeft + getPaddingRight(), paddingLeft + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.layout_width = i;
        this.layout_height = i2;
        setupBounds();
        setupPaints();
        invalidate();
    }

    private void setupPaints() {
        this.barPaint.setColor(this.barColor);
        this.barPaint.setAntiAlias(true);
        this.barPaint.setStyle(Paint.Style.STROKE);
        this.barPaint.setStrokeWidth(this.barWidth);
        this.rimPaint.setColor(this.rimColor);
        this.rimPaint.setAntiAlias(true);
        this.rimPaint.setStyle(Paint.Style.STROKE);
        this.rimPaint.setStrokeWidth(this.rimWidth);
        this.circlePaint.setColor(this.circleColor);
        this.circlePaint.setAntiAlias(true);
        this.circlePaint.setStyle(Paint.Style.FILL);
        this.textPaint.setColor(this.textColor);
        this.textPaint.setStyle(Paint.Style.FILL);
        this.textPaint.setAntiAlias(true);
        this.textPaint.setTypeface(getResources().getFont(R.font.nuntinoblack));
        this.textPaint.setTextSize(this.textSize);
        this.contourPaint.setColor(this.contourColor);
        this.contourPaint.setAntiAlias(true);
        this.contourPaint.setStyle(Paint.Style.STROKE);
        this.contourPaint.setStrokeWidth(this.contourSize);
    }

    private void setupBounds() {
        int min = Math.min(this.layout_width, this.layout_height);
        int i = this.layout_width - min;
        int i2 = (this.layout_height - min) / 2;
        this.paddingTop = getPaddingTop() + i2;
        this.paddingBottom = getPaddingBottom() + i2;
        int i3 = i / 2;
        this.paddingLeft = getPaddingLeft() + i3;
        this.paddingRight = getPaddingRight() + i3;
        int width = getWidth();
        int height = getHeight();
        this.rectBounds = new RectF(this.paddingLeft, this.paddingTop, width - this.paddingRight, height - this.paddingBottom);
        int i4 = this.paddingLeft;
        int i5 = this.barWidth;
        this.circleBounds = new RectF(i4 + i5, this.paddingTop + i5, (width - this.paddingRight) - i5, (height - this.paddingBottom) - i5);
        this.circleInnerContour = new RectF(this.circleBounds.left + (this.rimWidth / 2.0f) + (this.contourSize / 2.0f), this.circleBounds.top + (this.rimWidth / 2.0f) + (this.contourSize / 2.0f), (this.circleBounds.right - (this.rimWidth / 2.0f)) - (this.contourSize / 2.0f), (this.circleBounds.bottom - (this.rimWidth / 2.0f)) - (this.contourSize / 2.0f));
        this.circleOuterContour = new RectF((this.circleBounds.left - (this.rimWidth / 2.0f)) - (this.contourSize / 2.0f), (this.circleBounds.top - (this.rimWidth / 2.0f)) - (this.contourSize / 2.0f), this.circleBounds.right + (this.rimWidth / 2.0f) + (this.contourSize / 2.0f), this.circleBounds.bottom + (this.rimWidth / 2.0f) + (this.contourSize / 2.0f));
        int i6 = width - this.paddingRight;
        int i7 = this.barWidth;
        int i8 = (i6 - i7) / 2;
        this.fullRadius = i8;
        this.circleRadius = (i8 - i7) + 1;
    }

    @SuppressLint("ResourceType")
    private void parseAttributes(TypedArray typedArray) {
        this.barWidth = (int) typedArray.getDimension(2, this.barWidth);
        this.rimWidth = (int) typedArray.getDimension(9, this.rimWidth);
        this.spinSpeed = (int) typedArray.getDimension(10, this.spinSpeed);
        int integer = typedArray.getInteger(6, this.delayMillis);
        this.delayMillis = integer;
        if (integer < 0) {
            this.delayMillis = 0;
        }
        this.barColor = typedArray.getColor(0, this.barColor);
        this.barLength = (int) typedArray.getDimension(1, this.barLength);
        this.textSize = (int) typedArray.getDimension(13, this.textSize);
        this.textColor = typedArray.getColor(12, this.textColor);
        if (typedArray.hasValue(11)) {
            setText(typedArray.getString(11));
        }
        this.rimColor = typedArray.getColor(8, this.rimColor);
        this.circleColor = typedArray.getColor(3, this.circleColor);
        this.contourColor = typedArray.getColor(4, this.contourColor);
        this.contourSize = typedArray.getDimension(5, this.contourSize);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        String[] strArr;
        super.onDraw(canvas);
        canvas.drawArc(this.circleBounds, 360.0f, 360.0f, false, this.circlePaint);
        canvas.drawArc(this.circleBounds, 360.0f, 360.0f, false, this.rimPaint);
        canvas.drawArc(this.circleOuterContour, 360.0f, 360.0f, false, this.contourPaint);
        canvas.drawArc(this.circleInnerContour, 360.0f, 360.0f, false, this.contourPaint);
        if (this.isSpinning) {
            canvas.drawArc(this.circleBounds, this.progress - 90, this.barLength, false, this.barPaint);
        } else {
            canvas.drawArc(this.circleBounds, -90.0f, this.progress, false, this.barPaint);
        }
        float descent = ((this.textPaint.descent() - this.textPaint.ascent()) / 2.0f) - this.textPaint.descent();
        for (String str : this.splitText) {
            canvas.drawText(str, (getWidth() / 2) - (this.textPaint.measureText(str) / 2.0f), (getHeight() / 2) + descent, this.textPaint);
        }
    }

    public boolean isSpinning() {
        return this.isSpinning;
    }

    public void resetCount() {
        this.progress = 0;
        setText("0%");
        invalidate();
    }

    public void stopSpinning() {
        this.isSpinning = false;
        this.progress = 0;
        this.spinHandler.removeMessages(0);
    }

    public void spin() {
        this.isSpinning = true;
        this.spinHandler.sendEmptyMessage(0);
    }

    public void incrementProgress() {
        this.isSpinning = false;
        int i = this.progress + 1;
        this.progress = i;
        if (i > 360) {
            this.progress = 0;
        }
        this.spinHandler.sendEmptyMessage(0);
    }

    public void setProgress(int i) {
        this.isSpinning = false;
        this.progress = i;
        this.spinHandler.sendEmptyMessage(0);
    }

    public void setText(String str) {
        this.text = str;
        this.splitText = str.split("\n");
    }

    public int getCircleRadius() {
        return this.circleRadius;
    }

    public void setCircleRadius(int i) {
        this.circleRadius = i;
    }

    public int getBarLength() {
        return this.barLength;
    }

    public void setBarLength(int i) {
        this.barLength = i;
    }

    public int getBarWidth() {
        return this.barWidth;
    }

    public void setBarWidth(int i) {
        this.barWidth = i;
    }

    public int getTextSize() {
        return this.textSize;
    }

    public void setTextSize(int i) {
        this.textSize = i;
    }

    @Override
    public int getPaddingTop() {
        return this.paddingTop;
    }

    public void setPaddingTop(int i) {
        this.paddingTop = i;
    }

    @Override
    public int getPaddingBottom() {
        return this.paddingBottom;
    }

    public void setPaddingBottom(int i) {
        this.paddingBottom = i;
    }

    @Override
    public int getPaddingLeft() {
        return this.paddingLeft;
    }

    public void setPaddingLeft(int i) {
        this.paddingLeft = i;
    }

    @Override
    public int getPaddingRight() {
        return this.paddingRight;
    }

    public void setPaddingRight(int i) {
        this.paddingRight = i;
    }

    public int getBarColor() {
        return this.barColor;
    }

    public void setBarColor(int i) {
        this.barColor = i;
    }

    public int getCircleColor() {
        return this.circleColor;
    }

    public void setCircleColor(int i) {
        this.circleColor = i;
    }

    public int getRimColor() {
        return this.rimColor;
    }

    public void setRimColor(int i) {
        this.rimColor = i;
    }

    public Shader getRimShader() {
        return this.rimPaint.getShader();
    }

    public void setRimShader(Shader shader) {
        this.rimPaint.setShader(shader);
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int i) {
        this.textColor = i;
    }

    public int getSpinSpeed() {
        return this.spinSpeed;
    }

    public void setSpinSpeed(int i) {
        this.spinSpeed = i;
    }

    public int getRimWidth() {
        return this.rimWidth;
    }

    public void setRimWidth(int i) {
        this.rimWidth = i;
    }

    public int getDelayMillis() {
        return this.delayMillis;
    }

    public void setDelayMillis(int i) {
        this.delayMillis = i;
    }
}
