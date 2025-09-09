package com.example.fikenastet.ui.dashboard.profile.catch_log

import android.view.View
import androidx.fragment.app.viewModels
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.databinding.FragmentAddCatchLogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCatchLogFragment : BaseFragment<FragmentAddCatchLogBinding>() {
    private val viewModel: CatchLogVM by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_add_catch_log
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
                R.id.ivDropArrow->{

                }
                R.id.ivDropArrow2->{

                }
                R.id.ivDropArrow3->{

                }
                R.id.tvAddMore->{

                }
                R.id.consUploadPic->{

                }
                R.id.consButton->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

}