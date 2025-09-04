package com.example.fikenastet.ui.dashboard.profile.threads

import android.view.View
import androidx.fragment.app.viewModels
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.databinding.FragmentNewThreadBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewThreadFragment : BaseFragment<FragmentNewThreadBinding>() {
    private val viewModel: ThreadsVM by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_new_thread
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

    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivBack -> {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }

                R.id.ivNotification -> {

                }

                R.id.consButton -> {

                }
            }
        }
    }

    private fun initObserver() {

    }
}