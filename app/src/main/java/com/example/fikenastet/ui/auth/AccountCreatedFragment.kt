package com.example.fikenastet.ui.auth

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.utils.showSuccessToast
import com.example.fikenastet.databinding.FragmentAccountCreatedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountCreatedFragment : BaseFragment<FragmentAccountCreatedBinding>() {
    private val viewmodel: AuthCommonVM by viewModels()
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_account_created
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

                R.id.consGotItButton -> {
                    showSuccessToast("Will navigate home page ")
                }
            }
        })
    }

    private fun initView() {

    }

}