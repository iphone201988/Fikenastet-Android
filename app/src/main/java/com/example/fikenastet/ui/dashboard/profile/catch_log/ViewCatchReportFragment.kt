package com.example.fikenastet.ui.dashboard.profile.catch_log

import android.view.View
import androidx.fragment.app.viewModels
import com.example.fikenastet.BR
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.SimpleRecyclerViewAdapter
import com.example.fikenastet.data.model.ReplyModel
import com.example.fikenastet.databinding.FragmentViewCatchReportBinding
import com.example.fikenastet.databinding.ItemReplyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewCatchReportFragment : BaseFragment<FragmentViewCatchReportBinding>() {
    private val viewModel: CatchLogVM by viewModels()
    // adapter
    private lateinit var replyAdapter: SimpleRecyclerViewAdapter<ReplyModel, ItemReplyBinding>
    override fun getLayoutResource(): Int {
        return R.layout.fragment_view_catch_report
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObserver()
    }

    private fun initView() {
        initAdapter()
    }

    private fun initObserver() {

    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivBack -> {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }

                R.id.ivNotification -> {

                }
                R.id.tvLoadMore -> {

                }
                R.id.consButton2->{

                }
                R.id.consButton -> {

                }
                R.id.ivSend->{

                }
            }
        }
    }

    // adapter
    private fun initAdapter() {
        replyAdapter = SimpleRecyclerViewAdapter(R.layout.item_reply, BR.bean) { v, m, pos ->
            when (v.id) {
                R.id.ivHeart -> {
                    if (replyAdapter.list[pos].isSelected==false) {
                        replyAdapter.list[pos].isSelected = true
                        replyAdapter.notifyItemChanged(pos,m)
                    }
                    else{
                        replyAdapter.list[pos].isSelected = false
                        replyAdapter.notifyItemChanged(pos,m)
                    }
                }

            }
        }
        replyAdapter.list = getReplyList()
        binding.rvReply.adapter = replyAdapter
    }

    private fun getReplyList(): ArrayList<ReplyModel> {
        val list = ArrayList<ReplyModel>()
        list.add(ReplyModel(""))
        list.add(ReplyModel(""))
        list.add(ReplyModel(""))
        list.add(ReplyModel(""))
        list.add(ReplyModel(""))
        list.add(ReplyModel(""))
        return list
    }

}