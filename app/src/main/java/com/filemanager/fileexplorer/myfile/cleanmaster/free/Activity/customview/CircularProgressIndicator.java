package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.demo.example.R;
import com.github.mikephil.charting.utils.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class CircularProgressIndicator extends View {
    private static final int ANGLE_END_PROGRESS_BACKGROUND = 360;
    private static final int ANGLE_START_PROGRESS_BACKGROUND = 0;
    public static final int CAP_BUTT = 1;
    public static final int CAP_ROUND = 0;
    private static final int DEFAULT_ANIMATION_DURATION = 1000;
    private static final String DEFAULT_PROGRESS_BACKGROUND_COLOR = "#e0e0e0";
    private static final String DEFAULT_PROGRESS_COLOR = "#3F51B5";
    private static final int DEFAULT_PROGRESS_START_ANGLE = 270;
    private static final int DEFAULT_STROKE_WIDTH_DP = 8;
    private static final int DEFAULT_TEXT_SIZE_SP = 24;
    private static final int DESIRED_WIDTH_DP = 150;
    public static final int DIRECTION_CLOCKWISE = 0;
    public static final int DIRECTION_COUNTERCLOCKWISE = 1;
    public static final int LINEAR_GRADIENT = 1;
    public static final int NO_GRADIENT = 0;
    private static final String PROPERTY_ANGLE = "angle";
    public static final int RADIAL_GRADIENT = 2;
    public static final int SWEEP_GRADIENT = 3;
    private static float w;
    private Interpolator animationInterpolator;
    private RectF circleBounds;
    private int direction;
    private Paint dotPaint;
    boolean flag;
    private boolean isAnimationEnabled;
    private boolean isFillBackgroundEnabled;
    private double maxProgressValue;
    private Paint minicicrle;
    private OnProgressChangeListener onProgressChangeListener;
    private ValueAnimator progressAnimator;
    private Paint progressBackgroundPaint;
    private Paint progressPaint;
    private String progressText;
    private ProgressTextAdapter progressTextAdapter;
    private double progressValue;
    private float radius;
    private boolean shouldDrawDot;
    private int startAngle;
    private int sweepAngle;
    private Paint textPaint;
    private float textX;
    private float textY;

    @Retention(RetentionPolicy.SOURCE)

    public @interface Cap {
    }

    @Retention(RetentionPolicy.SOURCE)

    public @interface Direction {
    }

    @Retention(RetentionPolicy.SOURCE)

    public @interface GradientType {
    }


    public interface OnProgressChangeListener {
        void onProgressChanged(double d, double d2);
    }


    public interface ProgressTextAdapter {
        String formatText(double d);
    }

    public void setfont() {
    }

    public CircularProgressIndicator(Context context) {
        super(context);
        this.startAngle = DEFAULT_PROGRESS_START_ANGLE;
        this.sweepAngle = 0;
        this.maxProgressValue = 100.0d;
        this.progressValue = Utils.DOUBLE_EPSILON;
        this.direction = 1;
        this.animationInterpolator = new AccelerateDecelerateInterpolator();
        this.flag = false;
        init(context, null);
    }

    public CircularProgressIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.startAngle = DEFAULT_PROGRESS_START_ANGLE;
        this.sweepAngle = 0;
        this.maxProgressValue = 100.0d;
        this.progressValue = Utils.DOUBLE_EPSILON;
        this.direction = 1;
        this.animationInterpolator = new AccelerateDecelerateInterpolator();
        this.flag = false;
        init(context, attributeSet);
    }

    public CircularProgressIndicator(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.startAngle = DEFAULT_PROGRESS_START_ANGLE;
        this.sweepAngle = 0;
        this.maxProgressValue = 100.0d;
        this.progressValue = Utils.DOUBLE_EPSILON;
        this.direction = 1;
        this.animationInterpolator = new AccelerateDecelerateInterpolator();
        this.flag = false;
        init(context, attributeSet);
    }

    public CircularProgressIndicator(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.startAngle = DEFAULT_PROGRESS_START_ANGLE;
        this.sweepAngle = 0;
        this.maxProgressValue = 100.0d;
        this.progressValue = Utils.DOUBLE_EPSILON;
        this.direction = 1;
        this.animationInterpolator = new AccelerateDecelerateInterpolator();
        this.flag = false;
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        int i;
        int i2;
        int i3;
        Paint.Cap cap;
        int i4;
        int parseColor = Color.parseColor(DEFAULT_PROGRESS_COLOR);
        int parseColor2 = Color.parseColor(DEFAULT_PROGRESS_BACKGROUND_COLOR);
        int dp2px = dp2px(8.0f);
        int sp2px = sp2px(24.0f);
        this.shouldDrawDot = true;
        Paint.Cap cap2 = Paint.Cap.ROUND;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CircularProgressIndicator);
            parseColor = obtainStyledAttributes.getColor(15, parseColor);
            parseColor2 = obtainStyledAttributes.getColor(12, parseColor2);
            dp2px = obtainStyledAttributes.getDimensionPixelSize(16, dp2px);
            i2 = obtainStyledAttributes.getDimensionPixelSize(13, dp2px);
            i4 = obtainStyledAttributes.getColor(18, parseColor);
            sp2px = obtainStyledAttributes.getDimensionPixelSize(19, sp2px);
            this.shouldDrawDot = obtainStyledAttributes.getBoolean(3, true);
            i = obtainStyledAttributes.getColor(1, parseColor);
            i3 = obtainStyledAttributes.getDimensionPixelSize(2, dp2px);
            int i5 = obtainStyledAttributes.getInt(17, DEFAULT_PROGRESS_START_ANGLE);
            this.startAngle = i5;
            if (i5 < 0 || i5 > ANGLE_END_PROGRESS_BACKGROUND) {
                this.startAngle = DEFAULT_PROGRESS_START_ANGLE;
            }
            this.isAnimationEnabled = obtainStyledAttributes.getBoolean(4, true);
            this.isFillBackgroundEnabled = obtainStyledAttributes.getBoolean(5, false);
            this.direction = obtainStyledAttributes.getInt(0, 1);
            cap = obtainStyledAttributes.getInt(14, 0) == 0 ? Paint.Cap.ROUND : Paint.Cap.BUTT;
            String string = obtainStyledAttributes.getString(6);
            if (string != null) {
                this.progressTextAdapter = new PatternProgressTextAdapter(string);
            } else {
                this.progressTextAdapter = new DefaultProgressTextAdapter();
            }
            reformatProgressText();
            final int color = obtainStyledAttributes.getColor(8, 0);
            if (color != 0) {
                final int color2 = obtainStyledAttributes.getColor(7, -1);
                if (color2 == -1) {
                    throw new IllegalArgumentException("did you forget to specify gradientColorEnd?");
                }
                post(new Runnable() {
                    @Override
                    public void run() {
                        CircularProgressIndicator.this.setGradient(color, color2);
                    }
                });
            }
            obtainStyledAttributes.recycle();
        } else {
            i = parseColor;
            i2 = dp2px;
            i3 = i2;
            cap = cap2;
            i4 = i;
        }
        Paint paint = new Paint();
        this.progressPaint = paint;
        paint.setStrokeCap(cap);
        this.progressPaint.setStrokeWidth(dp2px);
        this.progressPaint.setStyle(Paint.Style.STROKE);
        this.progressPaint.setColor(parseColor);
        this.progressPaint.setAntiAlias(true);
        Paint paint2 = new Paint();
        this.minicicrle = paint2;
        paint2.setStrokeWidth(10.0f);
        this.minicicrle.setStyle(Paint.Style.STROKE);
        this.minicicrle.setColor(Color.parseColor("#E86868"));
        Paint.Style style = this.isFillBackgroundEnabled ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE;
        Paint paint3 = new Paint();
        this.progressBackgroundPaint = paint3;
        paint3.setStyle(style);
        this.progressBackgroundPaint.setStrokeWidth(i2);
        this.progressBackgroundPaint.setColor(parseColor2);
        this.progressBackgroundPaint.setAntiAlias(true);
        Paint paint4 = new Paint();
        this.dotPaint = paint4;
        paint4.setStrokeCap(Paint.Cap.ROUND);
        this.dotPaint.setStrokeWidth(i3);
        this.dotPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.dotPaint.setColor(i);
        this.dotPaint.setAntiAlias(true);
        TextPaint textPaint = new TextPaint();
        this.textPaint = textPaint;
        textPaint.setStrokeCap(Paint.Cap.ROUND);
        this.textPaint.setColor(i4);
        this.textPaint.setAntiAlias(true);
        this.textPaint.setTextSize(sp2px);
        if (Build.VERSION.SDK_INT >= 26) {
            this.textPaint.setTypeface(getResources().getFont(R.font.nuntinoblack));
        }
        this.circleBounds = new RectF();
    }

    @Override
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        Rect rect = new Rect();
        Paint paint = this.textPaint;
        String str = this.progressText;
        paint.getTextBounds(str, 0, str.length(), rect);
        float strokeWidth = this.dotPaint.getStrokeWidth();
        float strokeWidth2 = this.progressPaint.getStrokeWidth();
        float strokeWidth3 = this.progressBackgroundPaint.getStrokeWidth();
        float max = ((int) (this.shouldDrawDot ? Math.max(strokeWidth, Math.max(strokeWidth2, strokeWidth3)) : Math.max(strokeWidth2, strokeWidth3))) + dp2px(150.0f) + Math.max(paddingBottom + paddingTop, paddingLeft + paddingRight);
        int max2 = (int) (max + Math.max(rect.width(), rect.height()) + (0.1f * max));
        if (mode == Integer.MIN_VALUE) {
            size = Math.min(max2, size);
        } else if (mode != 1073741824) {
            size = max2;
        }
        if (mode2 == Integer.MIN_VALUE) {
            size2 = Math.min(max2, size2);
        } else if (mode2 != 1073741824) {
            size2 = max2;
        }
        int i3 = (size2 - paddingTop) - paddingBottom;
        w = i3;
        int min = Math.min(i3, (size - paddingLeft) - paddingRight);
        setMeasuredDimension(min, min);
    }

    @Override
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        calculateBounds(i, i2);
        Shader shader = this.progressPaint.getShader();
        if (shader instanceof RadialGradient) {
            RadialGradient radialGradient = (RadialGradient) shader;
        }
    }

    private void calculateBounds(int i, int i2) {
        float f = i;
        this.radius = f / 2.0f;
        float strokeWidth = this.dotPaint.getStrokeWidth();
        float strokeWidth2 = this.progressPaint.getStrokeWidth();
        float strokeWidth3 = this.progressBackgroundPaint.getStrokeWidth();
        float max = (this.shouldDrawDot ? Math.max(strokeWidth, Math.max(strokeWidth2, strokeWidth3)) : Math.max(strokeWidth2, strokeWidth3)) / 2.0f;
        this.circleBounds.left = max;
        this.circleBounds.top = max;
        this.circleBounds.right = f - max;
        this.circleBounds.bottom = i2 - max;
        this.radius = this.circleBounds.width() / 2.0f;
        calculateTextBounds();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ValueAnimator valueAnimator = this.progressAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    public boolean isFlag() {
        return this.flag;
    }

    public void setFlag(boolean z) {
        this.flag = z;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawProgressBackground(canvas);
        drawProgress(canvas);
        if (isFlag()) {
            drawminicircle(canvas);
        }
        if (this.shouldDrawDot) {
            drawDot(canvas);
        }
        drawText(canvas);
    }

    private void drawProgressBackground(Canvas canvas) {
        canvas.drawArc(this.circleBounds, 0.0f, 360.0f, false, this.progressBackgroundPaint);
    }

    private void drawProgress(Canvas canvas) {
        canvas.drawArc(this.circleBounds, this.startAngle, this.sweepAngle, false, this.progressPaint);
    }

    public void drawminicircle(Canvas canvas) {
        canvas.drawCircle(this.circleBounds.centerX(), this.circleBounds.centerY(), ((getWidth() / 2) - this.progressBackgroundPaint.getStrokeWidth()) - 15.0f, this.minicicrle);
    }

    private void drawDot(Canvas canvas) {
        double radians = Math.toRadians(this.startAngle + this.sweepAngle + 180);
        canvas.drawPoint(this.circleBounds.centerX() - (this.radius * ((float) Math.cos(radians))), this.circleBounds.centerY() - (this.radius * ((float) Math.sin(radians))), this.dotPaint);
    }

    private void drawText(Canvas canvas) {
        canvas.drawText(this.progressText, this.textX, this.textY, this.textPaint);
    }

    public void setMaxProgress(double d) {
        this.maxProgressValue = d;
        if (d < this.progressValue) {
            setCurrentProgress(d);
        }
        invalidate();
    }

    public void setCurrentProgress(double d) {
        if (d > this.maxProgressValue) {
            this.maxProgressValue = d;
        }
        setProgress(d, this.maxProgressValue);
    }

    public void setProgress(double d, double d2) {
        double d3 = this.direction == 1 ? -((d / d2) * 360.0d) : (d / d2) * 360.0d;
        double d4 = this.progressValue;
        this.maxProgressValue = d2;
        double min = Math.min(d, d2);
        this.progressValue = min;
        OnProgressChangeListener onProgressChangeListener = this.onProgressChangeListener;
        if (onProgressChangeListener != null) {
            onProgressChangeListener.onProgressChanged(min, this.maxProgressValue);
        }
        reformatProgressText();
        calculateTextBounds();
        stopProgressAnimation();
        if (this.isAnimationEnabled) {
            startProgressAnimation(d4, d3);
            return;
        }
        this.sweepAngle = (int) d3;
        invalidate();
    }

    private void startProgressAnimation(double d, final double d2) {
        PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt(PROPERTY_ANGLE, this.sweepAngle, (int) d2);
        ValueAnimator ofObject = ValueAnimator.ofObject(new TypeEvaluator<Double>() {
            @Override
            public Double evaluate(float f, Double d3, Double d4) {
                return Double.valueOf(d3.doubleValue() + ((d4.doubleValue() - d3.doubleValue()) * f));
            }
        }, Double.valueOf(d), Double.valueOf(this.progressValue));
        this.progressAnimator = ofObject;
        ofObject.setDuration(1000L);
        this.progressAnimator.setValues(ofInt);
        this.progressAnimator.setInterpolator(this.animationInterpolator);
        this.progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                CircularProgressIndicator.this.sweepAngle = ((Integer) valueAnimator.getAnimatedValue(CircularProgressIndicator.PROPERTY_ANGLE)).intValue();
                CircularProgressIndicator.this.invalidate();
            }
        });
        this.progressAnimator.addListener(new DefaultAnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animator) {
                CircularProgressIndicator.this.sweepAngle = (int) d2;
                CircularProgressIndicator.this.invalidate();
                CircularProgressIndicator.this.progressAnimator = null;
            }
        });
        this.progressAnimator.start();
    }

    private void stopProgressAnimation() {
        ValueAnimator valueAnimator = this.progressAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    private void reformatProgressText() {
        this.progressText = this.progressTextAdapter.formatText(this.progressValue);
    }

    private Rect calculateTextBounds() {
        Rect rect = new Rect();
        Paint paint = this.textPaint;
        String str = this.progressText;
        paint.getTextBounds(str, 0, str.length(), rect);
        this.textX = this.circleBounds.centerX() - (rect.width() / 2.0f);
        this.textY = this.circleBounds.centerY() + (rect.height() / 2.0f);
        return rect;
    }

    private int dp2px(float f) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, f, getResources().getDisplayMetrics());
    }

    private int sp2px(float f) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, f, getResources().getDisplayMetrics());
    }

    private void invalidateEverything() {
        calculateBounds(getWidth(), getHeight());
        requestLayout();
        invalidate();
    }

    public void setProgressColor(int i) {
        this.progressPaint.setColor(i);
        invalidate();
    }

    public void setProgressBackgroundColor(int i) {
        this.progressBackgroundPaint.setColor(i);
        invalidate();
    }

    public void setProgressStrokeWidthDp(int i) {
        setProgressStrokeWidthPx(dp2px(i));
    }

    public void setProgressStrokeWidthPx(int i) {
        this.progressPaint.setStrokeWidth(i);
        invalidateEverything();
    }

    public void setProgressBackgroundStrokeWidthDp(int i) {
        setProgressBackgroundStrokeWidthPx(dp2px(i));
    }

    public void setProgressBackgroundStrokeWidthPx(int i) {
        this.progressBackgroundPaint.setStrokeWidth(i);
        invalidateEverything();
    }

    public void setTextColor(int i) {
        this.textPaint.setColor(i);
        Rect rect = new Rect();
        Paint paint = this.textPaint;
        String str = this.progressText;
        paint.getTextBounds(str, 0, str.length(), rect);
        invalidate(rect);
    }

    public void setTextSizeSp(int i) {
        setTextSizePx(sp2px(i));
    }

    public void setTextSize1(int i) {
        this.textPaint.setTextSize(i);
        invalidate();
    }

    public void setTextSizePx(int i) {
        this.textPaint.getTextSize();
        this.textPaint.measureText(this.progressText);
        if (this.shouldDrawDot) {
            Math.max(this.dotPaint.getStrokeWidth(), this.progressPaint.getStrokeWidth());
        } else {
            this.progressPaint.getStrokeWidth();
        }
        this.circleBounds.width();
        this.textPaint.setTextSize(i);
        invalidate(calculateTextBounds());
    }

    public void setShouldDrawDot(boolean z) {
        this.shouldDrawDot = z;
        if (this.dotPaint.getStrokeWidth() > this.progressPaint.getStrokeWidth()) {
            requestLayout();
        } else {
            invalidate();
        }
    }

    public void setDotColor(int i) {
        this.dotPaint.setColor(i);
        invalidate();
    }

    public void setDotWidthDp(int i) {
        setDotWidthPx(dp2px(i));
    }

    public void setDotWidthPx(int i) {
        this.dotPaint.setStrokeWidth(i);
        invalidateEverything();
    }

    public void setProgressTextAdapter(ProgressTextAdapter progressTextAdapter) {
        if (progressTextAdapter != null) {
            this.progressTextAdapter = progressTextAdapter;
        } else {
            this.progressTextAdapter = new DefaultProgressTextAdapter();
        }
        reformatProgressText();
        invalidateEverything();
    }

    public ProgressTextAdapter getProgressTextAdapter() {
        return this.progressTextAdapter;
    }

    public int getProgressColor() {
        return this.progressPaint.getColor();
    }

    public int getProgressBackgroundColor() {
        return this.progressBackgroundPaint.getColor();
    }

    public float getProgressStrokeWidth() {
        return this.progressPaint.getStrokeWidth();
    }

    public float getProgressBackgroundStrokeWidth() {
        return this.progressBackgroundPaint.getStrokeWidth();
    }

    public int getTextColor() {
        return this.textPaint.getColor();
    }

    public float getTextSize() {
        return this.textPaint.getTextSize();
    }

    public boolean isDotEnabled() {
        return this.shouldDrawDot;
    }

    public int getDotColor() {
        return this.dotPaint.getColor();
    }

    public float getDotWidth() {
        return this.dotPaint.getStrokeWidth();
    }

    public double getProgress() {
        return this.progressValue;
    }

    public double getMaxProgress() {
        return this.maxProgressValue;
    }

    public int getStartAngle() {
        return this.startAngle;
    }

    public void setStartAngle(int i) {
        this.startAngle = i;
        invalidate();
    }

    public int getDirection() {
        return this.direction;
    }

    public void setDirection(int i) {
        this.direction = i;
        invalidate();
    }

    public int getProgressStrokeCap() {
        return this.progressPaint.getStrokeCap() == Paint.Cap.ROUND ? 0 : 1;
    }

    public void setProgressStrokeCap(int i) {
        Paint.Cap cap = i == 0 ? Paint.Cap.ROUND : Paint.Cap.BUTT;
        if (this.progressPaint.getStrokeCap() != cap) {
            this.progressPaint.setStrokeCap(cap);
            invalidate();
        }
    }

    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }

    public OnProgressChangeListener getOnProgressChangeListener() {
        return this.onProgressChangeListener;
    }

    public void setAnimationEnabled(boolean z) {
        this.isAnimationEnabled = z;
        if (z) {
            return;
        }
        stopProgressAnimation();
    }

    public boolean isAnimationEnabled() {
        return this.isAnimationEnabled;
    }

    public void setFillBackgroundEnabled(boolean z) {
        if (z == this.isFillBackgroundEnabled) {
            return;
        }
        this.isFillBackgroundEnabled = z;
        this.progressBackgroundPaint.setStyle(z ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
        invalidate();
    }

    public boolean isFillBackgroundEnabled() {
        return this.isFillBackgroundEnabled;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.animationInterpolator = interpolator;
    }

    public Interpolator getInterpolator() {
        return this.animationInterpolator;
    }

    public void setGradient(int i, int i2) {
        Shader radialGradient = null;
        float width = getWidth() / 2.0f;
        float height = getHeight() / 2.0f;
        int color = this.progressPaint.getColor();
        Shader shader = null;
        if (i != 1) {
            if (i == 2) {
                radialGradient = new RadialGradient(width, height, width, color, i2, Shader.TileMode.MIRROR);
            } else if (i == 3) {
                radialGradient = new SweepGradient(width, height, new int[]{color, i2}, (float[]) null);
            }
            shader = radialGradient;
        } else {
            shader = new LinearGradient(0.0f, 0.0f, getWidth(), getHeight(), color, i2, Shader.TileMode.CLAMP);
        }
        if (shader != null) {
            Matrix matrix = new Matrix();
            matrix.postRotate(this.startAngle, width, height);
            shader.setLocalMatrix(matrix);
        }
        this.progressPaint.setShader(shader);
        invalidate();
    }

    public int getGradientType() {
        Shader shader = this.progressPaint.getShader();
        if (shader instanceof LinearGradient) {
            return 1;
        }
        if (shader instanceof RadialGradient) {
            return 2;
        }
        return shader instanceof SweepGradient ? 3 : 0;
    }
}
