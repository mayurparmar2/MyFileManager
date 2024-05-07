package com.demo.filemanager.Activity.customview.tablayout


import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.ItemTouchHelper
import com.demo.filemanager.R


class TabView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    i: Int = 0
) :
    View(context, attributeSet, i) {
    private var mAnimation: ValueAnimator? = null
    private var mColorStroke = 0
    private var mColorStrokeBlank = 0
    private var mColorText = 0
    private var mColorTextCur = 0
    private var mCount = 0
    private var mCurIndex = 0
    private var mDownIndex = 0
    private var mDuration = 0
    private var mFactor = 0f
    private var mHeight = 0
    private var mIsDragging = false
    private var mLastIndex = 0
    private var mOnTabSelectedListener: OnTabSelectedListener? = null
    private var mPadding = 0f
    private var mPaintA: Paint? = null
    private var mPaintB: Paint? = null
    private var mPaintTitle: Paint? = null
    private var mPaintTitleCur: Paint? = null
    private var mRect: Rect? = null
    private var mRectF: RectF? = null
    private var mRectRadius = 0f
    private var mReservedPadding = 0f
    private var mTextSize = 0
    private var mTitles: Array<String>? = null
    private var mTouchSlop = 0
    private var mTouchX = 0f
    private var mTouchY = 0f
    private var mWidth = 0
    private var mWidthB = 0f

    interface OnTabSelectedListener {
        fun onTabSelected(i: Int)
    }

    init {
        initTypedArray(context, attributeSet)
        init(context)
    }

    @SuppressLint("ResourceType")
    private fun initTypedArray(context: Context, attributeSet: AttributeSet?) {
        val obtainStyledAttributes =
            context.obtainStyledAttributes(attributeSet, R.styleable.TabView)
        val string = obtainStyledAttributes.getString(6)
        mTitles = string?.split(";".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
        mTextSize = obtainStyledAttributes.getDimension(
            5,
            TypedValue.applyDimension(1, 14.0f, resources.displayMetrics)
        ).toInt()
        val color = obtainStyledAttributes.getColor(0, Color.parseColor("#FF4081"))
        mColorText = color
        mColorStroke = color
        val color2 = obtainStyledAttributes.getColor(1, Color.parseColor("#ffffff"))
        mColorTextCur = color2
        mColorStrokeBlank = color2
        mPadding = obtainStyledAttributes.getDimension(3, 2.0f).toInt().toFloat()
        mReservedPadding = obtainStyledAttributes.getDimension(4, -1.0f).toInt().toFloat()
        mDuration = obtainStyledAttributes.getInteger(
            2,
            ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION
        )
        obtainStyledAttributes.recycle()
    }

    private fun init(context: Context) {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        val strArr = mTitles
        mCount = strArr?.size ?: 0
        mRect = Rect()
        mRectF = RectF()
        val paint = Paint(1)
        mPaintA = paint
        paint.color = mColorStroke
        val paint2 = Paint(1)
        mPaintB = paint2
        paint2.color = mColorStrokeBlank
        val paint3 = Paint(1)
        mPaintTitle = paint3
        paint3.textSize = mTextSize.toFloat()
        mPaintTitle!!.setTypeface(resources.getFont(R.font.nutinoremibold))
        mPaintTitle!!.textAlign = Paint.Align.CENTER
        mPaintTitle!!.color = resources.getColor(R.color.black)
        val paint4 = Paint(1)
        mPaintTitleCur = paint4
        paint4.textSize = mTextSize.toFloat()
        mPaintTitleCur!!.setTypeface(resources.getFont(R.font.nunrinoregular))
        mPaintTitleCur!!.textAlign = Paint.Align.CENTER
        mPaintTitleCur!!.color = mColorTextCur
        val ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f)
        mAnimation = ofFloat
        ofFloat.setDuration(mDuration.toLong())
        mAnimation!!.interpolator = LinearInterpolator()
        mAnimation!!.addUpdateListener { valueAnimator ->
            mFactor = valueAnimator.animatedValue as Float
            this@TabView.invalidate()
        }
        mAnimation!!.addListener(object : Animator.AnimatorListener {
            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                if (mFactor != 1.0f || mOnTabSelectedListener == null) {
                    return
                }
                mOnTabSelectedListener!!.onTabSelected(mCurIndex)
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mCount <= 0) {
            return
        }
        mRect!![0, 0, mWidth] = mHeight
        mRectF!!.set(mRect!!)
        val rect = mRect
        val f = mPadding
        rect!![f.toInt(), f.toInt(), (mWidth - f).toInt()] = (mHeight - f).toInt()
        mRectF!!.set(mRect!!)
        val rectF = mRectF
        val f2 = mRectRadius
        canvas.drawRoundRect(rectF!!, f2, f2, mPaintB!!)
        val f3 = mReservedPadding
        val f4 = mWidthB
        val f5 = mLastIndex * f4 + f3
        val f6 = f5 + (mCurIndex * f4 + f3 - f5) * mFactor
        mRect!![(f6 - f3).toInt(), 0, (f4 + f6 + f3).toInt()] = mHeight
        mRectF!!.set(mRect!!)
        val rectF2 = mRectF
        val f7 = mRectRadius
        canvas.drawRoundRect(rectF2!!, f7, f7, mPaintA!!)
        val textHeight = (mHeight + getTextHeight(mPaintTitle).toInt()) / 2
        for (i in 0 until mCount) {
            val f8 = mReservedPadding
            val f9 = mWidthB
            val f10 = i * f9 + f8 + f9 / 2.0f
            var f11 = f9 / 2.0f + f6 - f8
            if (f11 < 0.0f) {
                f11 = 0.0f
            }
            val i2 = (f11 / f9).toInt()
            if (i2 == i && (i2 == mCurIndex || i2 == mLastIndex)) {
                canvas.drawText(
                    mTitles!![i], f10, textHeight.toFloat(),
                    mPaintTitleCur!!
                )
            } else {
                canvas.drawText(
                    mTitles!![i], f10, textHeight.toFloat(),
                    mPaintTitle!!
                )
            }
        }
    }

    override fun onMeasure(i: Int, i2: Int) {
        super.onMeasure(i, i2)
        mWidth = MeasureSpec.getSize(i)
        mHeight = MeasureSpec.getSize(i2)
        val dimension = resources.getDimension(R.dimen.radius_tab)
        mRectRadius = dimension
        var f = mReservedPadding
        if (f == -1.0f) {
            f = (dimension * 0.85f).toInt().toFloat()
        }
        mReservedPadding = f
        val i3 = mWidth
        val f2 = i3 - f * 2.0f
        var i4 = mCount
        if (i4 <= 0) {
            i4 = 1
        }
        mWidthB = f2 / i4
        setMeasuredDimension(i3, mHeight)
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        if (mCount > 0) {
            val f = mFactor
            if (f == 0.0f || f == 1.0f) {
                motionEvent.actionMasked
                motionEvent.actionIndex
                val x = motionEvent.x
                val y = motionEvent.y
                val action = motionEvent.action
                if (action == 0) {
                    mTouchX = x
                    mTouchY = y
                    val i = ((x - mReservedPadding) / mWidthB).toInt()
                    mDownIndex = i
                    val max = Math.max(i, 0)
                    mDownIndex = max
                    val min = Math.min(max, mCount - 1)
                    mDownIndex = min
                    mIsDragging = false
                    return min != mCurIndex
                }
                if (action != 1) {
                    if (action == 2) {
                        if (!mIsDragging && (Math.abs(x - mTouchX) > mTouchSlop || Math.abs(y - mTouchY) > mTouchSlop)) {
                            mIsDragging = true
                        }
                        return !mIsDragging
                    } else if (action != 3) {
                        return super.onTouchEvent(motionEvent)
                    }
                }
                if (!mIsDragging && x >= 0.0f && x <= mWidth && y >= 0.0f && y <= mHeight) {
                    val min2 = Math.min(
                        Math.max(((x - mReservedPadding) / mWidthB).toInt(), 0),
                        mCount - 1
                    )
                    val i2 = mDownIndex
                    if (min2 == i2) {
                        mLastIndex = mCurIndex
                        mCurIndex = i2
                        startAnim()
                        return true
                    }
                }
                return false
            }
            return false
        }
        return false
    }

    private fun getTextHeight(paint: Paint?): Float {
        val fontMetrics = paint!!.fontMetrics
        return ((Math.ceil((fontMetrics.descent - fontMetrics.top).toDouble()) + 2.0) / 2.0).toFloat()
    }

    private fun startAnim() {
        stopAnim()
        val valueAnimator = mAnimation
        valueAnimator?.start()
    }

    private fun stopAnim() {
        val valueAnimator = mAnimation
        valueAnimator?.cancel()
    }

    fun setTitle(strArr: Array<String>?) {
        if (strArr == null || strArr.size <= 0) {
            return
        }
        mTitles = strArr
        var length = strArr.size
        mCount = length
        val f = mWidth - mReservedPadding * 2.0f
        if (length <= 0) {
            length = 1
        }
        mWidthB = f / length
        postInvalidate()
    }

    fun select(i: Int, z: Boolean) {
        val i2 = mCurIndex
        if (i == i2) {
            return
        }
        mLastIndex = i2
        mCurIndex = i
        if (z) {
            startAnim()
            return
        }
        mFactor = 1.0f
        invalidate()
    }

    fun setOnTabSelectedListener(onTabSelectedListener: OnTabSelectedListener?) {
        mOnTabSelectedListener = onTabSelectedListener
    }
}

