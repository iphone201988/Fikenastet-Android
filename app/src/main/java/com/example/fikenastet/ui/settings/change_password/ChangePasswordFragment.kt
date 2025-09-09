package com.example.fikenastet.ui.settings.change_password

import android.view.View
import androidx.fragment.app.viewModels
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.utils.BindingUtils.showOrHidePassword
import com.example.fikenastet.databinding.FragmentChangePasswordBinding
import com.example.fikenastet.ui.settings.SettingsVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>() {
    private val viewModel: SettingsVM by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_change_password
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
                R.id.ivNotification->{

                }

                R.id.ivOldPassword->{
                    showOrHidePassword(binding.etOldPassword,binding.ivOldPassword)
                }
                R.id.ivNewPassword->{
                    showOrHidePassword(binding.etNewPassword,binding.ivNewPassword)
                }
                R.id.ivConfirmPassword->{
                    showOrHidePassword(binding.etConfirmPassword,binding.ivConfirmPassword)
                }
                R.id.consButton->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

}