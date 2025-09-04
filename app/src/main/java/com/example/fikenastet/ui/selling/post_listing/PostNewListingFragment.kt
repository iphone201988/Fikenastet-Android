package com.example.fikenastet.ui.selling.post_listing

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.databinding.FragmentPostNewListingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostNewListingFragment : BaseFragment<FragmentPostNewListingBinding>() {
    private val viewModel: PostListingVM by viewModels()
    override fun onCreateView(view: View) {
        initOnClick()
        initView()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_post_new_listing
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {

    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner, Observer {
            when (it?.id) {
                R.id.ivBack -> {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

}