package com.fisken_astet.fikenastet.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.utils.BindingUtils
import com.fisken_astet.fikenastet.base.utils.BindingUtils.showOrHidePassword
import com.fisken_astet.fikenastet.base.utils.Status
import com.fisken_astet.fikenastet.base.utils.showToast
import com.fisken_astet.fikenastet.data.api.Constants
import com.fisken_astet.fikenastet.data.model.OtpSentModel
import com.fisken_astet.fikenastet.data.model.VerifiedUserModel
import com.fisken_astet.fikenastet.databinding.FragmentLoginBinding
import com.fisken_astet.fikenastet.ui.dashboard.DashBoardActivity
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val viewmodel: AuthCommonVM by viewModels()
    private var deviceToken: String? = null
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObserver()
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
                    sharedPrefManager.clear()
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.auth_navigation, true)
                        .build()

                    findNavController().navigate(R.id.fragmentSplash, null, navOptions)

                }
                R.id.ivPassword -> {
                    showOrHidePassword(binding.edtPassword, binding.ivPassword)
                }

                R.id.tvForget -> {
                    findNavController().navigate(R.id.fragmentForget)
                }

                R.id.tvRegisterLink -> {
                    findNavController().navigate(R.id.fragmentSignUp)
                }

                R.id.textGetStarted , R.id.consLoginButton-> {
                    BindingUtils.preventMultipleClick(it)
                    if (validate()) {
                        loginApi()
                    }
                }
            }
        })
    }

    private fun initView() {
        getFCMToken()
    }

    private fun initObserver() {
        viewmodel.observeCommon.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> showLoading()
                Status.SUCCESS -> {
                    when (it.message) {
                        "LOGIN" -> {
                            try {
                                val myDataModel =
                                    BindingUtils.parseJson<VerifiedUserModel>(it.data.toString())
                                if (myDataModel != null) {
                                    if (myDataModel.success == 200) {
                                        if (myDataModel.user != null) {
                                            sharedPrefManager.setToken(myDataModel.access_token.toString())
                                            if (myDataModel.user.is_verified == 1) {
                                                hideLoading()
                                                showToast(myDataModel.message.toString())
                                                sharedPrefManager.setLoginData(myDataModel.user)
                                                if (myDataModel.user.is_2fa_enabled == 1){
                                                    val bundle = Bundle()
                                                    bundle.putString("From", "Login")
                                                    findNavController().navigate(
                                                        R.id.fragmentTwoAuth, bundle
                                                    )
                                                }
                                                else{
                                                    val intent = Intent(
                                                        requireContext(), DashBoardActivity::class.java
                                                    )
                                                    startActivity(intent)
                                                    requireActivity().finish()
                                                }
                                            } else {
                                                sendOtp()
                                            }
                                        }
                                    } else {
                                        hideLoading()
                                    }
                                }

                            } catch (e: Exception) {
                                showToast(e.message.toString())
                                e.printStackTrace()
                            }
                        }

                        "RESEND_OTP" -> {
                            try {
                                hideLoading()
                                val myDataModel =
                                    BindingUtils.parseJson<OtpSentModel>(it.data.toString())
                                if (myDataModel?.success == 200) {
                                    showToast(myDataModel.message.toString())
                                    val bundle = Bundle()
                                    bundle.putString("Form", "Register")
                                    bundle.putString(
                                        "Email", binding.edtEmail.text.toString()
                                    )
                                    findNavController().navigate(
                                        R.id.fragmentVerify, bundle
                                    )
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

    private fun validate(): Boolean {
        if (binding.edtEmail.text?.trim().toString().isEmpty()) {
            showToast("Please enter valid email address")
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.text?.trim().toString())
                .matches()
        ) {
            showToast("Please enter valid email address")
            return false
        }
        else if (binding.edtPassword.text?.trim().toString().isEmpty()) {
            showToast("Please enter valid password")
            return false
        } else if (binding.edtPassword.text?.trim().toString().length < 8) {
            showToast("Please enter valid password of length 8")
            return false
        } else if (!binding.edtPassword.text?.trim().toString().any { it.isDigit() }) {
            showToast("Password must contain atleast one digit")
            return false
        } else if (!binding.edtPassword.text?.trim().toString().any { it.isLowerCase() }) {
            showToast("Password must contain atleast one lowercase letter")
            return false
        } else if (!binding.edtPassword.text?.trim().toString().any { it.isUpperCase() }) {
            showToast("Password must contain atleast one uppercase letter")
            return false
        } else if (!binding.edtPassword.text?.trim().toString()
                .any { "!#$%&()*+,-.:;<=>?@[\\]^_|~".contains(it) }
        ) {
            showToast("Password must contain atleast one special character")
            return false
        }
        else {
            return true
        }

    }

    /** Get Firebase Token **/
    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            }
            deviceToken = it.result
            Log.d("deviceToken", it.result)
        }
    }

    /** api call **/
    private fun loginApi() {
        if (deviceToken != null) {
            val request = HashMap<String, Any>()
            request["email"] = binding.edtEmail.text.toString().trim()
            request["password"] = binding.edtPassword.text.toString().trim()
            request["device_token"] = deviceToken.toString()
            request["device_type"] = "1"
            viewmodel.loginApi(Constants.LOGIN, request)
        }
    }

    private fun sendOtp() {
        val request = HashMap<String, Any>()
        request["email"] = binding.edtEmail.text.toString().trim()
        request["type"] = 2
        viewmodel.verifyEmailAndResend(Constants.VERIFY_OTP, request, 2)
    }

}