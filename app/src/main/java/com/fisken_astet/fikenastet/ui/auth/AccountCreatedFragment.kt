package com.fisken_astet.fikenastet.ui.auth

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.utils.showSuccessToast
import com.fisken_astet.fikenastet.databinding.FragmentAccountCreatedBinding
import com.fisken_astet.fikenastet.ui.dashboard.DashBoardActivity
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
                    val intent = Intent(requireContext(), DashBoardActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        })
    }

    private fun initView() {

    }

}