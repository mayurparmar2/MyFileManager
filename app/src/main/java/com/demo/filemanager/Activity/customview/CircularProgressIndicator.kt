package com.demo.filemanager.Activity.customview

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.PropertyValuesHolder
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.SweepGradient
import android.os.Build
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import com.demo.filemanager.R
import com.github.mikephil.charting.utils.Utils


class CircularProgressIndicator : View {
    var interpolator: Interpolator
    private var circleBounds: RectF? = null
    private var direction: Int
    private var dotPaint: Paint? = null
    var isFlag: Boolean
    private var isAnimationEnabled = false
    private var isFillBackgroundEnabled = false
    private var maxProgressValue: Double
    private var minicicrle: Paint? = null
    var onProgressChangeListener: OnProgressChangeListener? = null
    private var progressAnimator: ValueAnimator? = null
    private var progressBackgroundPaint: Paint? = null
    private var progressPaint: Paint? = null
    private var progressText: String? = null
    private var progressTextAdapter: ProgressTextAdapter? = null
    var progress: Double
        private set
    private var radius = 0f
    var isDotEnabled = false
        private set
    private var startAngle: Int
    private var sweepAngle: Int
    private var textPaint: Paint? = null
    private var textX = 0f
    private var textY = 0f

    @Retention(AnnotationRetention.SOURCE)
    annotation class Cap

    @Retention(AnnotationRetention.SOURCE)
    annotation class Direction

    @Retention(AnnotationRetention.SOURCE)
    annotation class GradientType
    interface OnProgressChangeListener {
        fun onProgressChanged(d: Double, d2: Double)
    }

    interface ProgressTextAdapter {
        fun formatText(d: Double): String?
    }

    fun setfont() {}

    constructor(context: Context) : super(context) {
        startAngle = DEFAULT_PROGRESS_START_ANGLE
        sweepAngle = 0
        maxProgressValue = 100.0
        progress = Utils.DOUBLE_EPSILON
        direction = 1
        interpolator = AccelerateDecelerateInterpolator()
        isFlag = false
        init(context, null)
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        startAngle = DEFAULT_PROGRESS_START_ANGLE
        sweepAngle = 0
        maxProgressValue = 100.0
        progress = Utils.DOUBLE_EPSILON
        direction = 1
        interpolator = AccelerateDecelerateInterpolator()
        isFlag = false
        init(context, attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet?, i: Int) : super(
        context,
        attributeSet,
        i
    ) {
        startAngle = DEFAULT_PROGRESS_START_ANGLE
        sweepAngle = 0
        maxProgressValue = 100.0
        progress = Utils.DOUBLE_EPSILON
        direction = 1
        interpolator = AccelerateDecelerateInterpolator()
        isFlag = false
        init(context, attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet?, i: Int, i2: Int) : super(
        context,
        attributeSet,
        i,
        i2
    ) {
        startAngle = DEFAULT_PROGRESS_START_ANGLE
        sweepAngle = 0
        maxProgressValue = 100.0
        progress = Utils.DOUBLE_EPSILON
        direction = 1
        interpolator = AccelerateDecelerateInterpolator()
        isFlag = false
        init(context, attributeSet)
    }

    @SuppressLint("ResourceType")
    private fun init(context: Context, attributeSet: AttributeSet?) {
        val i: Int
        val i2: Int
        val i3: Int
        val cap: Paint.Cap
        val i4: Int
        var parseColor = Color.parseColor(DEFAULT_PROGRESS_COLOR)
        var parseColor2 = Color.parseColor(DEFAULT_PROGRESS_BACKGROUND_COLOR)
        var dp2px = dp2px(8.0f)
        var sp2px = sp2px(24.0f)
        isDotEnabled = true
        val cap2 = Paint.Cap.ROUND
        if (attributeSet != null) {
            val obtainStyledAttributes =
                context.obtainStyledAttributes(attributeSet, R.styleable.CircularProgressIndicator)
            parseColor = obtainStyledAttributes.getColor(15, parseColor)
            parseColor2 = obtainStyledAttributes.getColor(12, parseColor2)
            dp2px = obtainStyledAttributes.getDimensionPixelSize(16, dp2px)
            i2 = obtainStyledAttributes.getDimensionPixelSize(13, dp2px)
            i4 = obtainStyledAttributes.getColor(18, parseColor)
            sp2px = obtainStyledAttributes.getDimensionPixelSize(19, sp2px)
            isDotEnabled = obtainStyledAttributes.getBoolean(3, true)
            i = obtainStyledAttributes.getColor(1, parseColor)
            i3 = obtainStyledAttributes.getDimensionPixelSize(2, dp2px)
            val i5 = obtainStyledAttributes.getInt(17, DEFAULT_PROGRESS_START_ANGLE)
            startAngle = i5
            if (i5 < 0 || i5 > ANGLE_END_PROGRESS_BACKGROUND) {
                startAngle = DEFAULT_PROGRESS_START_ANGLE
            }
            isAnimationEnabled = obtainStyledAttributes.getBoolean(4, true)
            isFillBackgroundEnabled = obtainStyledAttributes.getBoolean(5, false)
            direction = obtainStyledAttributes.getInt(0, 1)
            cap = if (obtainStyledAttributes.getInt(14, 0) == 0) Paint.Cap.ROUND else Paint.Cap.BUTT
            val string = obtainStyledAttributes.getString(6)
            if (string != null) {
                progressTextAdapter = PatternProgresTsextAdapter(string)
            } else {
                progressTextAdapter = DefaultProgressTextAdapter()
            }
            reformatProgressText()
            val color = obtainStyledAttributes.getColor(8, 0)
            if (color != 0) {
                val color2 = obtainStyledAttributes.getColor(7, -1)
                require(color2 != -1) { "did you forget to specify gradientColorEnd?" }
                post { setGradient(color, color2) }
            }
            obtainStyledAttributes.recycle()
        } else {
            i = parseColor
            i2 = dp2px
            i3 = i2
            cap = cap2
            i4 = i
        }
        val paint = Paint()
        progressPaint = paint
        paint.strokeCap = cap
        progressPaint!!.strokeWidth = dp2px.toFloat()
        progressPaint!!.style = Paint.Style.STROKE
        progressPaint!!.color = parseColor
        progressPaint!!.isAntiAlias = true
        val paint2 = Paint()
        minicicrle = paint2
        paint2.strokeWidth = 10.0f
        minicicrle!!.style = Paint.Style.STROKE
        minicicrle!!.color = Color.parseColor("#E86868")
        val style = if (isFillBackgroundEnabled) Paint.Style.FILL_AND_STROKE else Paint.Style.STROKE
        val paint3 = Paint()
        progressBackgroundPaint = paint3
        paint3.style = style
        progressBackgroundPaint!!.strokeWidth = i2.toFloat()
        progressBackgroundPaint!!.color = parseColor2
        progressBackgroundPaint!!.isAntiAlias = true
        val paint4 = Paint()
        dotPaint = paint4
        paint4.strokeCap = Paint.Cap.ROUND
        dotPaint!!.strokeWidth = i3.toFloat()
        dotPaint!!.style = Paint.Style.FILL_AND_STROKE
        dotPaint!!.color = i
        dotPaint!!.isAntiAlias = true
        this.textPaint = TextPaint()
        (this.textPaint as TextPaint).strokeCap = Paint.Cap.ROUND
        (this.textPaint as TextPaint).setColor(i4)
        (this.textPaint as TextPaint).setAntiAlias(true)
        (this.textPaint as TextPaint).setTextSize(sp2px.toFloat())
        if (Build.VERSION.SDK_INT >= 26) {
            (this.textPaint as TextPaint).setTypeface(resources.getFont(R.font.nuntinoblack))
        }
        circleBounds = RectF()
    }

    override fun onMeasure(i: Int, i2: Int) {
        super.onMeasure(i, i2)
        val paddingLeft = paddingLeft
        val paddingRight = paddingRight
        val paddingTop = paddingTop
        val paddingBottom = paddingBottom
        var size = MeasureSpec.getSize(i)
        var size2 = MeasureSpec.getSize(i2)
        val mode = MeasureSpec.getMode(i)
        val mode2 = MeasureSpec.getMode(i2)
        val rect = Rect()
        val paint = textPaint
        val str = progressText
        paint!!.getTextBounds(str, 0, str!!.length, rect)
        val strokeWidth = dotPaint!!.strokeWidth
        val strokeWidth2 = progressPaint!!.strokeWidth
        val strokeWidth3 = progressBackgroundPaint!!.strokeWidth
        val max = ((if (isDotEnabled) Math.max(
            strokeWidth,
            Math.max(strokeWidth2, strokeWidth3)
        ) else Math.max(strokeWidth2, strokeWidth3)).toInt() + dp2px(150.0f) + Math.max(
            paddingBottom + paddingTop,
            paddingLeft + paddingRight
        )).toFloat()
        val max2 = (max + Math.max(rect.width(), rect.height()) + 0.1f * max).toInt()
        if (mode == Int.MIN_VALUE) {
            size = Math.min(max2, size)
        } else if (mode != 1073741824) {
            size = max2
        }
        if (mode2 == Int.MIN_VALUE) {
            size2 = Math.min(max2, size2)
        } else if (mode2 != 1073741824) {
            size2 = max2
        }
        val i3 = size2 - paddingTop - paddingBottom
        w = i3.toFloat()
        val min = Math.min(i3, size - paddingLeft - paddingRight)
        setMeasuredDimension(min, min)
    }

    override fun onSizeChanged(i: Int, i2: Int, i3: Int, i4: Int) {
        calculateBounds(i, i2)
        val shader = progressPaint!!.shader
        if (shader is RadialGradient) {
            val radialGradient = shader
        }
    }

    private fun calculateBounds(i: Int, i2: Int) {
        val f = i.toFloat()
        radius = f / 2.0f
        val strokeWidth = dotPaint!!.strokeWidth
        val strokeWidth2 = progressPaint!!.strokeWidth
        val strokeWidth3 = progressBackgroundPaint!!.strokeWidth
        val max = (if (isDotEnabled) Math.max(
            strokeWidth,
            Math.max(strokeWidth2, strokeWidth3)
        ) else Math.max(strokeWidth2, strokeWidth3)) / 2.0f
        circleBounds!!.left = max
        circleBounds!!.top = max
        circleBounds!!.right = f - max
        circleBounds!!.bottom = i2 - max
        radius = circleBounds!!.width() / 2.0f
        calculateTextBounds()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        val valueAnimator = progressAnimator
        valueAnimator?.cancel()
    }

    override fun onDraw(canvas: Canvas) {
        drawProgressBackground(canvas)
        drawProgress(canvas)
        if (isFlag) {
            drawminicircle(canvas)
        }
        if (isDotEnabled) {
            drawDot(canvas)
        }
        drawText(canvas)
    }

    private fun drawProgressBackground(canvas: Canvas) {
        canvas.drawArc(circleBounds!!, 0.0f, 360.0f, false, progressBackgroundPaint!!)
    }

    private fun drawProgress(canvas: Canvas) {
        canvas.drawArc(
            circleBounds!!, startAngle.toFloat(), sweepAngle.toFloat(), false,
            progressPaint!!
        )
    }

    fun drawminicircle(canvas: Canvas) {
        canvas.drawCircle(
            circleBounds!!.centerX(),
            circleBounds!!.centerY(),
            width / 2 - progressBackgroundPaint!!.strokeWidth - 15.0f,
            minicicrle!!
        )
    }

    private fun drawDot(canvas: Canvas) {
        val radians = Math.toRadians((startAngle + sweepAngle + 180).toDouble())
        canvas.drawPoint(
            circleBounds!!.centerX() - radius * Math.cos(radians).toFloat(),
            circleBounds!!.centerY() - radius * Math.sin(radians).toFloat(),
            dotPaint!!
        )
    }

    private fun drawText(canvas: Canvas) {
        canvas.drawText(progressText!!, textX, textY, textPaint!!)
    }

    fun setCurrentProgress(d: Double) {
        if (d > maxProgressValue) {
            maxProgressValue = d
        }
        setProgress(d, maxProgressValue)
    }

    fun setProgress(d: Double, d2: Double) {
        val d3 = if (direction == 1) -(d / d2 * 360.0) else d / d2 * 360.0
        val d4 = progress
        maxProgressValue = d2
        val min = Math.min(d, d2)
        progress = min
        val onProgressChangeListener = onProgressChangeListener
        onProgressChangeListener?.onProgressChanged(min, maxProgressValue)
        reformatProgressText()
        calculateTextBounds()
        stopProgressAnimation()
        if (isAnimationEnabled) {
            startProgressAnimation(d4, d3)
            return
        }
        sweepAngle = d3.toInt()
        invalidate()
    }

    private fun startProgressAnimation(d: Double, d2: Double) {
        val ofInt = PropertyValuesHolder.ofInt(
            PROPERTY_ANGLE,
            sweepAngle, d2.toInt()
        )
//        val ofObject = ValueAnimator.ofObject(this,22)
        val ofObject = ValueAnimator.ofObject(object : TypeEvaluator<Double?> {
//            override fun evaluate(f: Float, d3: Double, d4: Double): Double {
//                return java.lang.Double.valueOf(d3 + (d4 - d3) * f)
//            }

            override fun evaluate(
                fraction: Float,
                startValue: Double?,
                endValue: Double?
            ): Double? {
                return java.lang.Double.valueOf(startValue!! + (endValue!!  - startValue) * fraction)
            }
        }, java.lang.Double.valueOf(d), java.lang.Double.valueOf(progress))
        progressAnimator = ofObject
        ofObject.setDuration(1000L)
        progressAnimator!!.setValues(ofInt)
        progressAnimator!!.interpolator = interpolator
        progressAnimator!!.addUpdateListener { valueAnimator ->
            sweepAngle =
                valueAnimator.getAnimatedValue(PROPERTY_ANGLE) as Int
            this@CircularProgressIndicator.invalidate()
        }
        progressAnimator!!.addListener(object : AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {

            }

            override fun onAnimationCancel(animation: Animator) {
                sweepAngle = d2.toInt()
                this@CircularProgressIndicator.invalidate()
                progressAnimator = null
            }

            override fun onAnimationRepeat(animation: Animator) {

            }


        })
//        progressAnimator!!.addListener(object : DefaultAnimatorListener() {
//            fun onAnimationCancel(animator: Animator?) {
//                sweepAngle = d2.toInt()
//                this@CircularProgressIndicator.invalidate()
//                progressAnimator = null
//            }
//        })
        progressAnimator!!.start()
    }

    private fun stopProgressAnimation() {
        val valueAnimator = progressAnimator
        valueAnimator?.cancel()
    }

    private fun reformatProgressText() {
        progressText = progressTextAdapter!!.formatText(progress)
    }

    private fun calculateTextBounds(): Rect {
        val rect = Rect()
        val paint = textPaint
        val str = progressText
        paint!!.getTextBounds(str, 0, str!!.length, rect)
        textX = circleBounds!!.centerX() - rect.width() / 2.0f
        textY = circleBounds!!.centerY() + rect.height() / 2.0f
        return rect
    }

    private fun dp2px(f: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, f, resources.displayMetrics)
            .toInt()
    }

    private fun sp2px(f: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, f, resources.displayMetrics)
            .toInt()
    }

    private fun invalidateEverything() {
        calculateBounds(width, height)
        requestLayout()
        invalidate()
    }

    fun setProgressStrokeWidthDp(i: Int) {
        setProgressStrokeWidthPx(dp2px(i.toFloat()))
    }

    fun setProgressStrokeWidthPx(i: Int) {
        progressPaint!!.strokeWidth = i.toFloat()
        invalidateEverything()
    }

    fun setProgressBackgroundStrokeWidthDp(i: Int) {
        setProgressBackgroundStrokeWidthPx(dp2px(i.toFloat()))
    }

    fun setProgressBackgroundStrokeWidthPx(i: Int) {
        progressBackgroundPaint!!.strokeWidth = i.toFloat()
        invalidateEverything()
    }

    fun setTextSizeSp(i: Int) {
        setTextSizePx(sp2px(i.toFloat()))
    }

    fun setTextSize1(i: Int) {
        textPaint!!.textSize = i.toFloat()
        invalidate()
    }

    fun setTextSizePx(i: Int) {
        textPaint!!.textSize
        textPaint!!.measureText(progressText)
        if (isDotEnabled) {
            Math.max(dotPaint!!.strokeWidth, progressPaint!!.strokeWidth)
        } else {
            progressPaint!!.strokeWidth
        }
        circleBounds!!.width()
        textPaint!!.textSize = i.toFloat()
        invalidate(calculateTextBounds())
    }

    fun setShouldDrawDot(z: Boolean) {
        isDotEnabled = z
        if (dotPaint!!.strokeWidth > progressPaint!!.strokeWidth) {
            requestLayout()
        } else {
            invalidate()
        }
    }

    fun setDotWidthDp(i: Int) {
        setDotWidthPx(dp2px(i.toFloat()))
    }

    fun setDotWidthPx(i: Int) {
        dotPaint!!.strokeWidth = i.toFloat()
        invalidateEverything()
    }

    fun setProgressTextAdapter(progressTextAdapter: ProgressTextAdapter?) {
        if (progressTextAdapter != null) {
            this.progressTextAdapter = progressTextAdapter
        } else {
            this.progressTextAdapter = DefaultProgressTextAdapter()
        }
        reformatProgressText()
        invalidateEverything()
    }

    fun getProgressTextAdapter(): ProgressTextAdapter? {
        return progressTextAdapter
    }

    var progressColor: Int
        get() = progressPaint!!.color
        set(i) {
            progressPaint!!.color = i
            invalidate()
        }
    var progressBackgroundColor: Int
        get() = progressBackgroundPaint!!.color
        set(i) {
            progressBackgroundPaint!!.color = i
            invalidate()
        }
    val progressStrokeWidth: Float
        get() = progressPaint!!.strokeWidth
    val progressBackgroundStrokeWidth: Float
        get() = progressBackgroundPaint!!.strokeWidth
    var textColor: Int
        get() = textPaint!!.color
        set(i) {
            textPaint!!.color = i
            val rect = Rect()
            val paint = textPaint
            val str = progressText
            paint!!.getTextBounds(str, 0, str!!.length, rect)
            invalidate(rect)
        }
    val textSize: Float
        get() = textPaint!!.textSize
    var dotColor: Int
        get() = dotPaint!!.color
        set(i) {
            dotPaint!!.color = i
            invalidate()
        }
    val dotWidth: Float
        get() = dotPaint!!.strokeWidth
    var maxProgress: Double
        get() = maxProgressValue
        set(d) {
            maxProgressValue = d
            if (d < progress) {
                setCurrentProgress(d)
            }
            invalidate()
        }

    fun getStartAngle(): Int {
        return startAngle
    }

    fun setStartAngle(i: Int) {
        startAngle = i
        invalidate()
    }

    fun getDirection(): Int {
        return direction
    }

    fun setDirection(i: Int) {
        direction = i
        invalidate()
    }

    var progressStrokeCap: Int
        get() = if (progressPaint!!.strokeCap == Paint.Cap.ROUND) 0 else 1
        set(i) {
            val cap = if (i == 0) Paint.Cap.ROUND else Paint.Cap.BUTT
            if (progressPaint!!.strokeCap != cap) {
                progressPaint!!.strokeCap = cap
                invalidate()
            }
        }

    fun setAnimationEnabled(z: Boolean) {
        isAnimationEnabled = z
        if (z) {
            return
        }
        stopProgressAnimation()
    }

    fun isAnimationEnabled(): Boolean {
        return isAnimationEnabled
    }

    fun setFillBackgroundEnabled(z: Boolean) {
        if (z == isFillBackgroundEnabled) {
            return
        }
        isFillBackgroundEnabled = z
        progressBackgroundPaint!!.style = if (z) Paint.Style.FILL_AND_STROKE else Paint.Style.STROKE
        invalidate()
    }

    fun isFillBackgroundEnabled(): Boolean {
        return isFillBackgroundEnabled
    }

    fun setGradient(i: Int, i2: Int) {
        var radialGradient: Shader? = null
        val width = width / 2.0f
        val height = height / 2.0f
        val color = progressPaint!!.color
        var shader: Shader? = null
        if (i != 1) {
            if (i == 2) {
                radialGradient =
                    RadialGradient(width, height, width, color, i2, Shader.TileMode.MIRROR)
            } else if (i == 3) {
                radialGradient =
                    SweepGradient(width, height, intArrayOf(color, i2), null as FloatArray?)
            }
            shader = radialGradient
        } else {
            shader = LinearGradient(
                0.0f,
                0.0f,
                getWidth().toFloat(),
                getHeight().toFloat(),
                color,
                i2,
                Shader.TileMode.CLAMP
            )
        }
        if (shader != null) {
            val matrix = Matrix()
            matrix.postRotate(startAngle.toFloat(), width, height)
            shader.setLocalMatrix(matrix)
        }
        progressPaint!!.setShader(shader)
        invalidate()
    }

    val gradientType: Int
        get() {
            val shader = progressPaint!!.shader
            if (shader is LinearGradient) {
                return 1
            }
            if (shader is RadialGradient) {
                return 2
            }
            return if (shader is SweepGradient) 3 else 0
        }

    companion object {
        private const val ANGLE_END_PROGRESS_BACKGROUND = 360
        private const val ANGLE_START_PROGRESS_BACKGROUND = 0
        const val CAP_BUTT = 1
        const val CAP_ROUND = 0
        private const val DEFAULT_ANIMATION_DURATION = 1000
        private const val DEFAULT_PROGRESS_BACKGROUND_COLOR = "#e0e0e0"
        private const val DEFAULT_PROGRESS_COLOR = "#3F51B5"
        private const val DEFAULT_PROGRESS_START_ANGLE = 270
        private const val DEFAULT_STROKE_WIDTH_DP = 8
        private const val DEFAULT_TEXT_SIZE_SP = 24
        private const val DESIRED_WIDTH_DP = 150
        const val DIRECTION_CLOCKWISE = 0
        const val DIRECTION_COUNTERCLOCKWISE = 1
        const val LINEAR_GRADIENT = 1
        const val NO_GRADIENT = 0
        private const val PROPERTY_ANGLE = "angle"
        const val RADIAL_GRADIENT = 2
        const val SWEEP_GRADIENT = 3
        private var w = 0f
    }
}

