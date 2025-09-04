package com.example.fikenastet.ui.auth

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.databinding.FragmentLoginBinding
import com.example.fikenastet.ui.dashboard.DashBoardActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val viewmodel: AuthCommonVM by viewModels()
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_login
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

                R.id.tvForget -> {
                    findNavController().navigate(R.id.fragmentForget)
                }

                R.id.tvRegisterLink -> {
                    findNavController().navigate(R.id.fragmentSignUp)
                }

                R.id.textGetStarted , R.id.consLoginButton-> {
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