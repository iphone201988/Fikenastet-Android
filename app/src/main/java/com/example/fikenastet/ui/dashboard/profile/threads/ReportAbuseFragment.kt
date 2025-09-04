package com.example.fikenastet.ui.dashboard.profile.threads

import android.view.View
import androidx.fragment.app.viewModels
import com.example.fikenastet.BR
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.SimpleRecyclerViewAdapter
import com.example.fikenastet.databinding.FragmentReportAbuseBinding
import com.example.fikenastet.databinding.ItemReportBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportAbuseFragment : BaseFragment<FragmentReportAbuseBinding>() {
    private val viewModel: ThreadsVM by viewModels()
    private lateinit var reportAdapter: SimpleRecyclerViewAdapter<String, ItemReportBinding>
    override fun getLayoutResource(): Int {
        return R.layout.fragment_report_abuse
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView(view: View) {
        // view
        initView()
        // click
        initOnClick()
        // observer
        initObserver()
    }

    private fun initView() {
        initAdapter()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivBack->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                R.id.ivNotification->{

                }
                R.id.consButton->{

                }
            }
        }
    }

    private fun initObserver() {

    }

    // adapter
    private fun initAdapter() {
        val reportList =
            listOf<String>("non fishing content", "rule violations", "abuse", "spam", "others")
        reportAdapter = SimpleRecyclerViewAdapter(R.layout.item_report, BR.bean) { v, m, pos ->
            when (v.id) {

            }

        }
        reportAdapter.list = reportList
        binding.rvReply.adapter = reportAdapter
    }

}