package com.fisken_astet.fikenastet.ui.auth

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.databinding.FragmentPasswordSuccessBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordSuccessFragment : BaseFragment<FragmentPasswordSuccessBinding>() {
    private val viewmodel: AuthCommonVM by viewModels()
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_password_success
    }

    override fun getViewModel(): BaseViewModel {
        return viewmodel
    }

    private fun initOnClick() {
        viewmodel.onClick.observe(viewLifecycleOwner, {
            when (it?.id) {
                R.id.consDoneButton -> {
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.auth_navigation, true)
                        .build()

                    findNavController().navigate(R.id.fragmentLogin, null, navOptions)
                }
            }
        })
    }

    private fun initView() {

    }

}