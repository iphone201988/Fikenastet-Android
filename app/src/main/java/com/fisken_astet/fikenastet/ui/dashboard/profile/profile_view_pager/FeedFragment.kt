package com.fisken_astet.fikenastet.ui.dashboard.profile.profile_view_pager

import android.view.View
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.BR
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.SimpleRecyclerViewAdapter
import com.fisken_astet.fikenastet.databinding.FragmentFeedBinding
import com.fisken_astet.fikenastet.databinding.ItemFeedBinding
import com.fisken_astet.fikenastet.ui.dashboard.profile.feeds.FeedVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : BaseFragment<FragmentFeedBinding>() {
    private val viewModel: FeedVM by viewModels()
    private lateinit var feedsAdapter: SimpleRecyclerViewAdapter<String, ItemFeedBinding>
    override fun getLayoutResource(): Int {
        return R.layout.fragment_feed
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

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.tvSeeAll -> {

                }
            }
        }
    }

    private fun initObserver() {

    }

    private fun initAdapter() {
        val feedsList = listOf("", "", "", "", "", "", "", "")
        feedsAdapter = SimpleRecyclerViewAdapter(R.layout.item_feed, BR.bean) { v, m, pos ->
        }
        feedsAdapter.list = feedsList
        binding.rvFeeds.adapter = feedsAdapter
    }

}