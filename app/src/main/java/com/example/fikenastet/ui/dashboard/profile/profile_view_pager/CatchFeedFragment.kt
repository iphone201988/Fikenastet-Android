package com.example.fikenastet.ui.dashboard.profile.profile_view_pager

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.example.fikenastet.BR
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.SimpleRecyclerViewAdapter
import com.example.fikenastet.databinding.FragmentCatchFeedBinding
import com.example.fikenastet.databinding.ItemFeedBinding
import com.example.fikenastet.ui.dashboard.common_activity.CommonActivity
import com.example.fikenastet.ui.dashboard.profile.catch_log.CatchLogVM
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
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "CatchLog")
                    startActivity(intent)
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