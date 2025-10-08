package com.fisken_astet.fikenastet.ui.selling.statistics

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.databinding.FragmentStatisticsBinding
import com.fisken_astet.fikenastet.ui.dashboard.common_activity.CommonActivity
import com.fisken_astet.fikenastet.utils.CustomYAxisRenderer
import com.fisken_astet.fikenastet.utils.TopRoundedBarChartRenderer
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>() {
    private val viewmodel: StatisticsVM by viewModels()
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_statistics
    }

    override fun getViewModel(): BaseViewModel {
        return viewmodel
    }

    private fun initOnClick() {
        viewmodel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivBack -> {
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }
                R.id.consNewFollowers->{
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere","ListingStatistic")
                    startActivity(intent)
                }
            }
        }
    }

    private fun initView() {
        setupGraph()
    }

    private fun setupGraph() {
        val barChart = binding.barChart
        val customFont = ResourcesCompat.getFont(requireActivity(), R.font.poppins_regular)

        val entries = listOf(
            BarEntry(0f, 10f),
            BarEntry(1f, 5f),
            BarEntry(2f, 0f),
            BarEntry(3f, 0f),
            BarEntry(4f, 15f),
            BarEntry(5f, 0f),
            BarEntry(6f, 0f)
        )


        val barDataSet = BarDataSet(entries, "Sell").apply {
            color = ContextCompat.getColor(requireActivity(), R.color.colorPrimary)
            valueTextSize = 10f
            valueTextColor = ContextCompat.getColor(requireActivity(), R.color.app_text)
            valueTypeface = customFont
            setDrawValues(false)
        }

        val barData = BarData(barDataSet).apply {
            barWidth = 0.7f
        }
        barChart.data = barData

        val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        barChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(days)
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            spaceMin = 0.1f
            labelCount = days.size
            setDrawGridLines(false)
            setDrawLabels(true)
            textSize = 10f
            textColor = ContextCompat.getColor(requireActivity(), R.color.app_text)
            axisLineColor = ContextCompat.getColor(requireActivity(), R.color.app_text)
            typeface = customFont
        }

        barChart.axisLeft.apply {
            axisMinimum = 0f
            axisMaximum = 20f
            granularity = 2f
            setDrawGridLines(false)
            textSize = 12f
            textColor = ContextCompat.getColor(requireActivity(), R.color.app_text)
            axisLineColor = ContextCompat.getColor(requireActivity(), R.color.app_text)
            typeface = customFont
        }

        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setFitBars(true)
        barChart.animateY(1000)
        barChart.setTouchEnabled(false)

        barChart.renderer =
            TopRoundedBarChartRenderer(barChart, barChart.animator, barChart.viewPortHandler)
        // Replace default YAxisRenderer (for LEFT axis)
        barChart.rendererLeftYAxis = CustomYAxisRenderer(
            barChart.viewPortHandler,
            barChart.axisLeft,
            barChart.getTransformer(YAxis.AxisDependency.LEFT)
        )

        barChart.invalidate()
    }
}
