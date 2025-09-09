package com.example.fikenastet.ui.dashboard.profile.threads

import android.view.View
import androidx.fragment.app.viewModels
import com.example.fikenastet.BR
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.SimpleRecyclerViewAdapter
import com.example.fikenastet.data.model.ReplyModel
import com.example.fikenastet.databinding.FragmentReportAbuseBinding
import com.example.fikenastet.databinding.ItemReportBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportAbuseFragment : BaseFragment<FragmentReportAbuseBinding>() {
    private val viewModel: ThreadsVM by viewModels()
    private lateinit var reportAdapter: SimpleRecyclerViewAdapter<ReplyModel, ItemReportBinding>
    override fun getLayoutResource(): Int {
        return R.layout.fragment_report_abuse
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView(view: View) {
        // view
        binding.tvTitle.text = "Report"
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
                R.id.consButton, R.id.textButton -> {

                }
            }
        }
    }

    private fun initObserver() {

    }

    // adapter
    private fun initAdapter() {
        reportAdapter = SimpleRecyclerViewAdapter(R.layout.item_report, BR.bean) { v, m, pos ->
            when (v.id) {
                R.id.ivTick -> {
                    if (reportAdapter.list[pos].isSelected == false) {
                        reportAdapter.list[pos].isSelected = true
                        reportAdapter.notifyItemChanged(pos, m)
                    } else {
                        reportAdapter.list[pos].isSelected = false
                        reportAdapter.notifyItemChanged(pos, m)
                    }
                }
            }

        }
        reportAdapter.list = getList()
        binding.rvReply.adapter = reportAdapter
    }


    private fun getList(): ArrayList<ReplyModel> {
        val list = ArrayList<ReplyModel>()
        list.add(ReplyModel("non fishing content"))
        list.add(ReplyModel("rule violations"))
        list.add(ReplyModel("abuse"))
        list.add(ReplyModel("spam"))
        list.add(ReplyModel("others"))
        return list
    }

}