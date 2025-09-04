package com.example.fikenastet.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.databinding.FragmentProfileReadyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileReadyFragment : BaseFragment<FragmentProfileReadyBinding>() {
    private val viewmodel: AuthCommonVM by viewModels()
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_profile_ready
    }

    override fun getViewModel(): BaseViewModel {
        return viewmodel
    }

    private fun initOnClick() {
        viewmodel.onClick.observe(viewLifecycleOwner, {
            when (it?.id) {
                R.id.ivBack -> {
                    findNavController().popBackStack()
                }

                R.id.consSaveAndContinue -> {
                    val bundle = Bundle()
                    bundle.putString("Form", "ProfileReadyFragment")
                    findNavController().navigate(R.id.fragmentAccountCreated, bundle)
                }
            }
        })
    }

    private fun initView() {

    }

}