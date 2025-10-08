package com.fisken_astet.fikenastet.utils

import android.graphics.Canvas
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.renderer.YAxisRenderer
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler

class CustomYAxisRenderer(
    viewPortHandler: ViewPortHandler,
    yAxis: YAxis,
    trans: Transformer
) : YAxisRenderer(viewPortHandler, yAxis, trans) {

    override fun renderAxisLine(c: Canvas) {
        if (!mYAxis.isEnabled || !mYAxis.isDrawAxisLineEnabled) return

        mAxisLinePaint.color = mYAxis.axisLineColor
        mAxisLinePaint.strokeWidth = mYAxis.axisLineWidth

        val xPos = if (mYAxis.axisDependency == YAxis.AxisDependency.LEFT) {
            mViewPortHandler.contentLeft()
        } else {
            mViewPortHandler.contentRight()
        }

        // Only draw from bottom to the top label instead of the whole height
        val topY = mViewPortHandler.contentTop() + mYAxis.textSize + mYAxis.yOffset
        val bottomY = mViewPortHandler.contentBottom()

        c.drawLine(xPos, bottomY, xPos, topY, mAxisLinePaint)
    }
}