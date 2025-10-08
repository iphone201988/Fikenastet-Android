package com.fisken_astet.fikenastet.utils

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.fisken_astet.fikenastet.R

class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progress = 0f
    private var max = 1000f // configurable total value
    private val strokeWidth = 22f
    private val arcGapAngle = 8f

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = this@CircularProgressView.strokeWidth
        color = context.getColor(R.color.graph_color)
        strokeCap = Paint.Cap.ROUND
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = this@CircularProgressView.strokeWidth
        color = context.getColor(R.color.colorPrimary)
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        color = context.getColor(R.color.colorPrimary)
        textSize = 64f
        typeface = Typeface.DEFAULT_BOLD
    }

    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        color = Color.BLACK
        textSize = 40f
    }

    fun setProgress(value: Float, maxValue: Float = this.max) {
        max = maxValue
        progress = value.coerceIn(0f, max)
        invalidate()
    }

    fun animateProgress(value: Float, maxValue: Float = this.max, duration: Long = 1500L) {
        max = maxValue
        ValueAnimator.ofFloat(0f, value.coerceIn(0f, max)).apply {
            this.duration = duration
            addUpdateListener {
                progress = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (minOf(width, height) / 2f) - (strokeWidth / 2f)

        val rectF = RectF(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )

        val totalAngle = 360f - arcGapAngle // gap between arcs
        val startAngle = -90f + (arcGapAngle / 2f)

        // Draw background arc
        canvas.drawArc(rectF, startAngle, totalAngle, false, backgroundPaint)

        // Draw progress arc with same gap alignment
        val sweepAngle = (progress / max) * totalAngle
        canvas.drawArc(rectF, startAngle, sweepAngle, false, progressPaint)

        // Draw labels
        canvas.drawText("Listing views", centerX, centerY - 30, labelPaint)
        canvas.drawText(progress.toInt().toString(), centerX, centerY + 50, textPaint)
    }


    /// end gap
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//
//        val centerX = width / 2f
//        val centerY = height / 2f
//        val radius = (minOf(width, height) / 2f) - (strokeWidth / 2f)
//
//        val rectF = RectF(
//            centerX - radius,
//            centerY - radius,
//            centerX + radius,
//            centerY + radius
//        )
//
//        val startAngle = -90f
//
//        // Total angle for progress
//        val progressAngle = (progress / max) * 360f
//        val gap = arcGapAngle.coerceAtMost(progressAngle)
//
//        // Draw progress arc (leaving a gap at the end)
//        canvas.drawArc(rectF, startAngle, progressAngle - gap, false, progressPaint)
//
//        // Draw remaining background arc (after the progress + gap)
//        if (progress < max) {
//            val remainingStart = startAngle + progressAngle
//            val remainingSweep = 360f - progressAngle
//            canvas.drawArc(rectF, remainingStart, remainingSweep, false, backgroundPaint)
//        }
//
//        // Draw labels
//        canvas.drawText("Listing views", centerX, centerY - 30, labelPaint)
//        canvas.drawText(progress.toInt().toString(), centerX, centerY + 50, textPaint)
//    }
}