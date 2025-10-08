package com.fisken_astet.fikenastet.ui.dashboard.profile.feeds

import android.view.View
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.BR
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.SimpleRecyclerViewAdapter
import com.fisken_astet.fikenastet.databinding.FragmentAddFeedsBinding
import com.fisken_astet.fikenastet.databinding.ItemAddImagesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFeedsFragment : BaseFragment<FragmentAddFeedsBinding>() {
    private val viewModel: FeedVM by viewModels()
    private lateinit var feedsAdapter: SimpleRecyclerViewAdapter<String, ItemAddImagesBinding>
    private var isClicked =false
    override fun getLayoutResource(): Int {
        return R.layout.fragment_add_feeds
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
        initAdapter()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivBack -> {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }

                R.id.ivNotification -> {

                }

                R.id.tvAddMore -> {
                    isClicked =!isClicked
                    binding.rvFeeds.visibility = if (isClicked) View.VISIBLE else View.GONE
                    binding.consUploadPic.visibility = if (isClicked) View.INVISIBLE else View.VISIBLE
                }

                R.id.tvAddMoreTag -> {

                }

                R.id.consButton -> {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

    private fun initObserver() {

    }

    private fun initAdapter() {
        val feedsList = listOf("", "", "", "", "", "", "", "")
        feedsAdapter = SimpleRecyclerViewAdapter(R.layout.item_add_images, BR.bean) { v, m, pos ->
        }
        feedsAdapter.list = feedsList
        binding.rvFeeds.adapter = feedsAdapter
    }

}