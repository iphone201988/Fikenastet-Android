package com.fisken_astet.fikenastet.ui.settings.lake_owner_panel

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.databinding.FragmentMyLakeDetailBinding
import com.fisken_astet.fikenastet.ui.dashboard.common_activity.CommonActivity
import com.fisken_astet.fikenastet.utils.TopRoundedBarChartRenderer
import com.fisken_astet.fikenastet.utils.CustomYAxisRenderer
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyLakeDetailFragment : BaseFragment<FragmentMyLakeDetailBinding>() {
    private val viewModel: MyLakesVM by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_my_lake_detail
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView(view: View) {
        initView()
        initObserver()
        initOnClick()
    }

    private fun initView() {

    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner){
            when(it?.id){
                R.id.ivBack->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                R.id.ivNotification->{
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "Notifications")
                    startActivity(intent)
                }
            }
        }
    }

    private fun initObserver() {
        setupGraph()
    }

    private fun setupGraph() {
        val barChart = binding.barChart
        val customFont = ResourcesCompat.getFont(requireActivity(), R.font.poppins_regular)

        val entries = listOf(
            BarEntry(0f, 200f),   // Mon
            BarEntry(1f, 400f),   // Tue
            BarEntry(2f, 0f),     // Wed
            BarEntry(3f, 800f),   // Thu
            BarEntry(4f, 1500f),  // Fri
            BarEntry(5f, 600f),   // Sat
            BarEntry(6f, 2000f)   // Sun
        )


        val barDataSet = BarDataSet(entries, "Sales").apply {
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
            setDrawGridLines(false)
            textSize = 10f
            textColor = ContextCompat.getColor(requireActivity(), R.color.app_text)
            axisLineColor = ContextCompat.getColor(requireActivity(), R.color.app_text)
            typeface = customFont
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return when {
                        value >= 1000f -> "${(value / 1000)}k"
                        else -> value.toInt().toString()
                    }
                }
            }
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