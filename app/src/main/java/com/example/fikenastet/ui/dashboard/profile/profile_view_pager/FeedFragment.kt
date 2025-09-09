package com.example.fikenastet.ui.dashboard.profile.profile_view_pager

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.example.fikenastet.BR
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.SimpleRecyclerViewAdapter
import com.example.fikenastet.databinding.FragmentFeedBinding
import com.example.fikenastet.databinding.ItemFeedBinding
import com.example.fikenastet.ui.dashboard.common_activity.CommonActivity
import com.example.fikenastet.ui.dashboard.profile.feeds.FeedVM
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
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "AddFeed")
                    startActivity(intent)
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