package com.fisken_astet.fikenastet.ui.dashboard.profile.profile_view_pager

import android.view.View
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.BR
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.SimpleRecyclerViewAdapter
import com.fisken_astet.fikenastet.databinding.FragmentCatchFeedBinding
import com.fisken_astet.fikenastet.databinding.ItemFeedBinding
import com.fisken_astet.fikenastet.ui.dashboard.profile.catch_log.CatchLogVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatchFeedFragment : BaseFragment<FragmentCatchFeedBinding>() {
    private val viewModel: CatchLogVM by viewModels()
    private lateinit var catchFeedsAdapter: SimpleRecyclerViewAdapter<String, ItemFeedBinding>
    override fun getLayoutResource(): Int {
        return R.layout.fragment_catch_feed
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
                R.id.tvSeeAll -> {

                }
            }
        }
    }

    private fun initAdapter() {
        val feedsList = listOf("", "", "", "", "", "", "", "")
        catchFeedsAdapter = SimpleRecyclerViewAdapter(R.layout.item_feed, BR.bean) { v, m, pos ->
        }
        catchFeedsAdapter.list = feedsList
        binding.rvFeeds.adapter = catchFeedsAdapter
    }

}