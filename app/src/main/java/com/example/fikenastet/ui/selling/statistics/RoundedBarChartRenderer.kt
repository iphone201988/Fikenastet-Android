package com.example.fikenastet.ui.selling.statistics

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.buffer.BarBuffer
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler

class TopRoundedBarChartRenderer(
    chart: BarDataProvider,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler,
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val radius = 10f  // Corner radius in pixels

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val trans = mChart.getTransformer(dataSet.axisDependency)
        mBarBorderPaint.color = dataSet.barBorderColor
        mBarBorderPaint.strokeWidth = Utils.convertDpToPixel(dataSet.barBorderWidth)

        val drawBorder = dataSet.barBorderWidth > 0f

        val buffer: BarBuffer = mBarBuffers[index]
        buffer.setPhases(mAnimator.phaseX, mAnimator.phaseY)
        buffer.setDataSet(index)
        buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
        buffer.setBarWidth(mChart.barData.barWidth)
        buffer.feed(dataSet)

        trans.pointValuesToPixel(buffer.buffer)

        for (j in buffer.buffer.indices step 4) {
            val left = buffer.buffer[j]
            val top = buffer.buffer[j + 1]
            val right = buffer.buffer[j + 2]
            val bottom = buffer.buffer[j + 3]

            if (top >= bottom) continue

            // SET THE COLOR EXPLICITLY!
            mRenderPaint.color = dataSet.getColor(j / 4)  // index per bar

            val path = Path().apply {
                moveTo(left, bottom)
                lineTo(left, top + radius)
                quadTo(left, top, left + radius, top)
                lineTo(right - radius, top)
                quadTo(right, top, right, top + radius)
                lineTo(right, bottom)
                close()
            }

            c.drawPath(path, mRenderPaint)

            if (drawBorder) {
                c.drawPath(path, mBarBorderPaint)
            }
        }

    }
}
