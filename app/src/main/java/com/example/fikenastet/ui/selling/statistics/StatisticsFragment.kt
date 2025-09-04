package com.example.fikenastet.ui.selling.statistics

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.databinding.FragmentStatisticsBinding
import com.github.mikephil.charting.components.XAxis
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
            }
        }
    }

    private fun initView() {
        setupGraph()
    }


    private fun setupGraph() {
        val barChart = binding.barChart
        val customFont = ResourcesCompat.getFont(requireActivity(), R.font.inter_medium)

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
            valueTextSize = 12f
            valueTextColor = ContextCompat.getColor(requireActivity(), R.color.app_text)
            valueTypeface = customFont
            setDrawValues(true)
        }

        val barData = BarData(barDataSet).apply {
            barWidth = 0.5f
        }

        barChart.data = barData

        val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        barChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(days)
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            labelCount = days.size
            setDrawGridLines(false)
            setDrawLabels(true)
            textSize = 12f
            textColor = ContextCompat.getColor(requireActivity(), R.color.app_text)
            typeface = customFont
        }

        barChart.axisLeft.apply {
            axisMinimum = 0f
            setDrawGridLines(true)
            textSize = 12f
            textColor = ContextCompat.getColor(requireActivity(), R.color.app_text)
            typeface = customFont
        }

        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setFitBars(true)
        barChart.animateY(1000)

        // Set custom renderer for top-rounded bars
        barChart.renderer =
            TopRoundedBarChartRenderer(barChart, barChart.animator, barChart.viewPortHandler)

        barChart.invalidate()
    }
}
