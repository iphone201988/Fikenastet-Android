package com.fisken_astet.fikenastet.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.utils.BindingUtils
import com.fisken_astet.fikenastet.base.utils.Status
import com.fisken_astet.fikenastet.base.utils.showToast
import com.fisken_astet.fikenastet.data.api.Constants
import com.fisken_astet.fikenastet.data.model.ForgetEmailModel
import com.fisken_astet.fikenastet.databinding.FragmentFrogetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetFragment : BaseFragment<FragmentFrogetBinding>() {
    private val viewmodel: AuthCommonVM by viewModels()
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObserver()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_froget
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

                R.id.consContinueButton -> {
                    BindingUtils.preventMultipleClick(it)
                    if (validate()) {
                        forgetPassApi()
                    }
                }
            }
        })
    }

    private fun initView() {

    }

    private fun initObserver() {
        viewmodel.observeCommon.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> showLoading()
                Status.SUCCESS -> {
                    hideLoading()
                    when (it.message) {
                        "RESEND_OTP_FORGET" -> {
                            try {
                                val myDataModel =
                                    BindingUtils.parseJson<ForgetEmailModel>(it.data.toString())
                                if (myDataModel != null) {
                                    if (myDataModel.status == 200) {
                                        showToast(myDataModel.message.toString())
                                        val bundle = Bundle()
                                        bundle.putString("Form", "Forget")
                                        bundle.putString("Email", binding.edtEmail.text.toString())
                                        findNavController().navigate(R.id.fragmentVerify, bundle)
                                    }
                                }

                            } catch (e: Exception) {
                                showToast(e.message.toString())
                                e.printStackTrace()
                            }
                        }
                    }
                }

                Status.ERROR -> {
                    hideLoading()
                    try {
                        showToast(it.message.toString())
                    } catch (e: Exception) {
                        showToast(e.message.toString())
                        e.printStackTrace()
                    }
                }

                else -> {

                }
            }
        }
    }

    /** api call **/
    private fun forgetPassApi() {
        val request = HashMap<String, Any>()
        request["email"] = binding.edtEmail.text.toString()
        request["type"] = 1
        viewmodel.verifyEmailAndResendForForget(Constants.FORGOT_PASSWORD, request, 1)
    }

    private fun validate(): Boolean {
        if (binding.edtEmail.text?.trim().toString().isEmpty()) {
            showToast("Please enter valid email address")
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.text?.trim().toString())
                .matches()
        ) {
            showToast("Please enter valid email address")
            return false
        } else {
            return true
        }
    }
}