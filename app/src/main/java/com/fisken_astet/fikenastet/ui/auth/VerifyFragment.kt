package com.fisken_astet.fikenastet.ui.auth

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.utils.BindingUtils
import com.fisken_astet.fikenastet.base.utils.Status
import com.fisken_astet.fikenastet.base.utils.showToast
import com.fisken_astet.fikenastet.data.api.Constants
import com.fisken_astet.fikenastet.data.model.ForgetEmailModel
import com.fisken_astet.fikenastet.data.model.ForgetVerifyModel
import com.fisken_astet.fikenastet.data.model.OtpSentModel
import com.fisken_astet.fikenastet.data.model.VerifiedUserModel
import com.fisken_astet.fikenastet.databinding.FragmentVerifyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyFragment : BaseFragment<FragmentVerifyBinding>() {
    private val viewmodel: AuthCommonVM by viewModels()
    private var email: String? = null
    private var data: String? = null
    private var countDownTimer: CountDownTimer? = null
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObserver()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_verify
    }

    override fun getViewModel(): BaseViewModel {
        return viewmodel
    }

    private fun initOnClick() {
        viewmodel.onClick.observe(viewLifecycleOwner, {
            when (it?.id) {
                R.id.ivBack -> {
                    if (data=="Register"){
                        sharedPrefManager.clear()
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.auth_navigation, true)
                            .build()

                        findNavController().navigate(R.id.fragmentSplash, null, navOptions)
                    }
                    else {
                        findNavController().popBackStack()
                    }
                }
                R.id.tvResendCode -> {
                    BindingUtils.preventMultipleClick(it)
                    if (data != null) {
                        if (data == "Register") {
                            if (email != null) {
                                verifyOtpAndResend(2)
                            }
                        } else {
                            verifyOtpAndResendForForget(1)
                        }
                    }
                }
                R.id.consVerifyButton -> {
                    BindingUtils.preventMultipleClick(it)
                    if (data != null) {
                        if (data == "Register") {
                            if (validate()) {
                                verifyOtpAndResend(1)
                            }
                        } else {
                            if (validate()) {
                                verifyOtpAndResendForForget(2)
                            }
                        }
                    }
                }
            }
        })
    }

    private fun initView() {
        email = arguments?.getString("Email")
        data = arguments?.getString("Form")
        startCountDownTimer()
    }

    private fun initObserver() {
        viewmodel.observeCommon.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> showLoading()
                Status.SUCCESS -> {
                    hideLoading()
                    when (it.message) {
                        "VERIFY_OTP" -> {
                            try {
                                val myDataModel =
                                    BindingUtils.parseJson<VerifiedUserModel>(it.data.toString())
                                if (myDataModel?.success == 200) {
                                    countDownTimer = null
                                    if (myDataModel.user != null) {
                                        sharedPrefManager.setLoginData(myDataModel.user)
                                    }
                                    findNavController().navigate(R.id.fragmentProfileReady)
                                }

                            } catch (e: Exception) {
                                showToast(e.message.toString())
                                e.printStackTrace()
                            }
                        }

                        "RESEND_OTP" -> {
                            try {
                                val myDataModel =
                                    BindingUtils.parseJson<OtpSentModel>(it.data.toString())
                                if (myDataModel?.success == 200) {
                                    showToast(myDataModel.message.toString())
                                    resetCountDownTimer()
                                }

                            } catch (e: Exception) {
                                showToast(e.message.toString())
                                e.printStackTrace()
                            }
                        }

                        "VERIFY_OTP_FORGET" -> {
                            try {
                                val myDataModel =
                                    BindingUtils.parseJson<ForgetVerifyModel>(it.data.toString())
                                if (myDataModel != null) {
                                    if (myDataModel.status == 200) {
                                        showToast(myDataModel.message.toString())
                                        val bundle = Bundle()
                                        // change pass token
                                        bundle.putString("Token", myDataModel.data?.token)
                                        bundle.putString("Email", email.toString())
                                        findNavController().navigate(R.id.fragmentPassword, bundle)
                                    }
                                }

                            } catch (e: Exception) {
                                showToast(e.message.toString())
                                e.printStackTrace()
                            }
                        }

                        "RESEND_OTP_FORGET" -> {
                            try {
                                val myDataModel =
                                    BindingUtils.parseJson<ForgetEmailModel>(it.data.toString())
                                if (myDataModel?.status == 200) {
                                    showToast(myDataModel.message.toString())
                                    resetCountDownTimer()
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

    private fun validate(): Boolean {
        if (binding.edtCode.text.isNullOrEmpty()) {
            showToast("Please enter valid otp")
            return false
        }
//        else if (binding.edtCode.text.toString().length < 6) {
//            showToast("Please enter valid otp of six digits")
//            return false
//        }
        else {
            return true
        }
    }

    /** api call **/
    // signup process
    private fun verifyOtpAndResend(type: Int) {
        val request = HashMap<String, Any>()
        if (type == 1) {
            request["otp"] = binding.edtCode.text.toString()
        } else {
            request["email"] = email.toString()
        }
        request["type"] = type
        viewmodel.verifyEmailAndResend(Constants.VERIFY_OTP, request, type)
    }

    // forget flow
    private fun verifyOtpAndResendForForget(type: Int) {
        val request = HashMap<String, Any>()
        if (type == 2) {
            request["otp"] = binding.edtCode.text.toString()
        }
        request["email"] = email.toString()
        request["type"] = type
        viewmodel.verifyEmailAndResendForForget(Constants.FORGOT_PASSWORD, request, type)
    }


    /** countdown timer **/
    private fun startCountDownTimer() {
        // Cancel any existing timer
        countDownTimer?.cancel()

        // Show the countdown UI again
        binding.clContainer.visibility = View.VISIBLE
        binding.tvResendCode.visibility = View.GONE

        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000).toInt()
                binding.tvRegisterLink.text = "$seconds sec"
            }

            override fun onFinish() {
                binding.clContainer.visibility = View.GONE
                binding.tvResendCode.visibility = View.VISIBLE
            }
        }.start()
    }

    private fun resetCountDownTimer() {
        countDownTimer?.cancel()
        startCountDownTimer()
    }

}