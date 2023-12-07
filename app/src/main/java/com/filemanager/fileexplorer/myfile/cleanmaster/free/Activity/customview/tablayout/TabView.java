package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.tablayout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;

import androidx.recyclerview.widget.ItemTouchHelper;

import com.demo.example.R;


public class TabView extends View {
    private ValueAnimator mAnimation;
    private int mColorStroke;
    private int mColorStrokeBlank;
    private int mColorText;
    private int mColorTextCur;
    private int mCount;
    private int mCurIndex;
    private int mDownIndex;
    private int mDuration;
    private float mFactor;
    private int mHeight;
    private boolean mIsDragging;
    private int mLastIndex;
    private OnTabSelectedListener mOnTabSelectedListener;
    private float mPadding;
    private Paint mPaintA;
    private Paint mPaintB;
    private Paint mPaintTitle;
    private Paint mPaintTitleCur;
    private Rect mRect;
    private RectF mRectF;
    private float mRectRadius;
    private float mReservedPadding;
    private int mTextSize;
    private String[] mTitles;
    private int mTouchSlop;
    private float mTouchX;
    private float mTouchY;
    private int mWidth;
    private float mWidthB;


    public interface OnTabSelectedListener {
        void onTabSelected(int i);
    }

    public TabView(Context context) {
        this(context, null);
    }

    public TabView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TabView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDownIndex = 0;
        initTypedArray(context, attributeSet);
        init(context);
    }

    private void initTypedArray(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.TabView);
        String string = obtainStyledAttributes.getString(6);
        this.mTitles = string != null ? string.split(";") : null;
        this.mTextSize = (int) obtainStyledAttributes.getDimension(5, TypedValue.applyDimension(1, 14.0f, getResources().getDisplayMetrics()));
        int color = obtainStyledAttributes.getColor(0, Color.parseColor("#FF4081"));
        this.mColorText = color;
        this.mColorStroke = color;
        int color2 = obtainStyledAttributes.getColor(1, Color.parseColor("#ffffff"));
        this.mColorTextCur = color2;
        this.mColorStrokeBlank = color2;
        this.mPadding = (int) obtainStyledAttributes.getDimension(3, 2.0f);
        this.mReservedPadding = (int) obtainStyledAttributes.getDimension(4, -1.0f);
        this.mDuration = obtainStyledAttributes.getInteger(2, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
        obtainStyledAttributes.recycle();
    }

    private void init(Context context) {
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        String[] strArr = this.mTitles;
        this.mCount = strArr != null ? strArr.length : 0;
        this.mRect = new Rect();
        this.mRectF = new RectF();
        Paint paint = new Paint(1);
        this.mPaintA = paint;
        paint.setColor(this.mColorStroke);
        Paint paint2 = new Paint(1);
        this.mPaintB = paint2;
        paint2.setColor(this.mColorStrokeBlank);
        Paint paint3 = new Paint(1);
        this.mPaintTitle = paint3;
        paint3.setTextSize(this.mTextSize);
        this.mPaintTitle.setTypeface(getResources().getFont(R.font.nutinoremibold));
        this.mPaintTitle.setTextAlign(Paint.Align.CENTER);
        this.mPaintTitle.setColor(getResources().getColor(R.color.black));
        Paint paint4 = new Paint(1);
        this.mPaintTitleCur = paint4;
        paint4.setTextSize(this.mTextSize);
        this.mPaintTitleCur.setTypeface(getResources().getFont(R.font.nunrinoregular));
        this.mPaintTitleCur.setTextAlign(Paint.Align.CENTER);
        this.mPaintTitleCur.setColor(this.mColorTextCur);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mAnimation = ofFloat;
        ofFloat.setDuration(this.mDuration);
        this.mAnimation.setInterpolator(new LinearInterpolator());
        this.mAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                TabView.this.mFactor = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                TabView.this.invalidate();
            }
        });
        this.mAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (TabView.this.mFactor != 1.0f || TabView.this.mOnTabSelectedListener == null) {
                    return;
                }
                TabView.this.mOnTabSelectedListener.onTabSelected(TabView.this.mCurIndex);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mCount <= 0) {
            return;
        }
        this.mRect.set(0, 0, this.mWidth, this.mHeight);
        this.mRectF.set(this.mRect);
        Rect rect = this.mRect;
        float f = this.mPadding;
        rect.set((int) f, (int) f, (int) (this.mWidth - f), (int) (this.mHeight - f));
        this.mRectF.set(this.mRect);
        RectF rectF = this.mRectF;
        float f2 = this.mRectRadius;
        canvas.drawRoundRect(rectF, f2, f2, this.mPaintB);
        float f3 = this.mReservedPadding;
        float f4 = this.mWidthB;
        float f5 = (this.mLastIndex * f4) + f3;
        float f6 = f5 + ((((this.mCurIndex * f4) + f3) - f5) * this.mFactor);
        this.mRect.set((int) (f6 - f3), 0, (int) (f4 + f6 + f3), this.mHeight);
        this.mRectF.set(this.mRect);
        RectF rectF2 = this.mRectF;
        float f7 = this.mRectRadius;
        canvas.drawRoundRect(rectF2, f7, f7, this.mPaintA);
        int textHeight = (this.mHeight + ((int) getTextHeight(this.mPaintTitle))) / 2;
        for (int i = 0; i < this.mCount; i++) {
            float f8 = this.mReservedPadding;
            float f9 = this.mWidthB;
            float f10 = (i * f9) + f8 + (f9 / 2.0f);
            float f11 = ((f9 / 2.0f) + f6) - f8;
            if (f11 < 0.0f) {
                f11 = 0.0f;
            }
            int i2 = (int) (f11 / f9);
            if (i2 == i && (i2 == this.mCurIndex || i2 == this.mLastIndex)) {
                canvas.drawText(this.mTitles[i], f10, textHeight, this.mPaintTitleCur);
            } else {
                canvas.drawText(this.mTitles[i], f10, textHeight, this.mPaintTitle);
            }
        }
    }

    @Override
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.mWidth = View.MeasureSpec.getSize(i);
        this.mHeight = View.MeasureSpec.getSize(i2);
        float dimension = getResources().getDimension(R.dimen.radius_tab);
        this.mRectRadius = dimension;
        float f = this.mReservedPadding;
        if (f == -1.0f) {
            f = (int) (dimension * 0.85f);
        }
        this.mReservedPadding = f;
        int i3 = this.mWidth;
        float f2 = i3 - (f * 2.0f);
        int i4 = this.mCount;
        if (i4 <= 0) {
            i4 = 1;
        }
        this.mWidthB = f2 / i4;
        setMeasuredDimension(i3, this.mHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mCount > 0) {
            float f = this.mFactor;
            if (f == 0.0f || f == 1.0f) {
                motionEvent.getActionMasked();
                motionEvent.getActionIndex();
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                int action = motionEvent.getAction();
                if (action == 0) {
                    this.mTouchX = x;
                    this.mTouchY = y;
                    int i = (int) ((x - this.mReservedPadding) / this.mWidthB);
                    this.mDownIndex = i;
                    int max = Math.max(i, 0);
                    this.mDownIndex = max;
                    int min = Math.min(max, this.mCount - 1);
                    this.mDownIndex = min;
                    this.mIsDragging = false;
                    return min != this.mCurIndex;
                }
                if (action != 1) {
                    if (action == 2) {
                        if (!this.mIsDragging && (Math.abs(x - this.mTouchX) > this.mTouchSlop || Math.abs(y - this.mTouchY) > this.mTouchSlop)) {
                            this.mIsDragging = true;
                        }
                        return !this.mIsDragging;
                    } else if (action != 3) {
                        return super.onTouchEvent(motionEvent);
                    }
                }
                if (!this.mIsDragging && x >= 0.0f && x <= this.mWidth && y >= 0.0f && y <= this.mHeight) {
                    int min2 = Math.min(Math.max((int) ((x - this.mReservedPadding) / this.mWidthB), 0), this.mCount - 1);
                    int i2 = this.mDownIndex;
                    if (min2 == i2) {
                        this.mLastIndex = this.mCurIndex;
                        this.mCurIndex = i2;
                        startAnim();
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private float getTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (float) ((Math.ceil(fontMetrics.descent - fontMetrics.top) + 2.0d) / 2.0d);
    }

    private void startAnim() {
        stopAnim();
        ValueAnimator valueAnimator = this.mAnimation;
        if (valueAnimator != null) {
            valueAnimator.start();
        }
    }

    private void stopAnim() {
        ValueAnimator valueAnimator = this.mAnimation;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    public void setTitle(String[] strArr) {
        if (strArr == null || strArr.length <= 0) {
            return;
        }
        this.mTitles = strArr;
        int length = strArr.length;
        this.mCount = length;
        float f = this.mWidth - (this.mReservedPadding * 2.0f);
        if (length <= 0) {
            length = 1;
        }
        this.mWidthB = f / length;
        postInvalidate();
    }

    public void select(int i, boolean z) {
        int i2 = this.mCurIndex;
        if (i == i2) {
            return;
        }
        this.mLastIndex = i2;
        this.mCurIndex = i;
        if (z) {
            startAnim();
            return;
        }
        this.mFactor = 1.0f;
        invalidate();
    }

    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        this.mOnTabSelectedListener = onTabSelectedListener;
    }
}
