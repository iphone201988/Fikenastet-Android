package com.example.fikenastet.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignupBinding>() {
    private val viewmodel: AuthCommonVM by viewModels()
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_signup
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


                R.id.tvRegisterLink -> {
                    findNavController().popBackStack()
                }

                R.id.consRegisterButton -> {
                    val bundle = Bundle()
                    bundle.putString("Form", "Register")
                    findNavController().navigate(R.id.fragmentVerify, bundle)
                }
            }
        })
    }

    private fun initView() {

    }

}