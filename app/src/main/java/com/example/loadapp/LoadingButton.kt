package com.example.loadapp

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0

    private var valueAnimator = ValueAnimator()


    private var downloadString = context.getString(R.string.button_download)
    private var loadingString = context.getString(R.string.button_loading)

    private var buttonValue = ""

    private var progress = 0


    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { _, _, new ->
        when(new) {
            ButtonState.Loading -> {
                buttonValue = loadingString
                valueAnimator.start()
            }
            ButtonState.Completed -> {
                buttonValue = downloadString
                valueAnimator.cancel()
                progress = 0
            }
            else -> {}
        }
        invalidate()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 50.0f
        typeface = Typeface.create( "", Typeface.NORMAL)
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
        }

        buttonState = ButtonState.Completed

        valueAnimator = ValueAnimator.ofInt(0, 360).apply {
            duration = 2500
            addUpdateListener {
                progress = (it.animatedValue) as Int
                invalidate()
            }
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
        }
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    private val rect = RectF(
        740f,
        50f,
        810f,
        110f
    )

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.color = context.getColor(R.color.colorPrimary)
        canvas?.drawRect(0f,0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        paint.color = context.getColor(R.color.black)
        canvas?.drawRect(0f, 0f, (width * progress/360f), height.toFloat(), paint)
        //both of this just draw because of progress. When it is greater then 0, it will draw
        paint.color = context.getColor(R.color.colorAccent)
        canvas?.drawArc(rect, 0f,progress.toFloat(), true, paint)

        paint.color = context.getColor(R.color.white)
        canvas?.drawText(buttonValue, (widthSize/2.0f), (heightSize/2.0f) + 20.0f, paint)

    }



}