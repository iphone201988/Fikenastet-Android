package com.fisken_astet.fikenastet.ui.auth

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.utils.BindingUtils
import com.fisken_astet.fikenastet.base.utils.BindingUtils.showOrHidePassword
import com.fisken_astet.fikenastet.base.utils.Status
import com.fisken_astet.fikenastet.base.utils.showToast
import com.fisken_astet.fikenastet.data.api.Constants
import com.fisken_astet.fikenastet.data.model.SimpleModel
import com.fisken_astet.fikenastet.databinding.FragmentPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordFragment : BaseFragment<FragmentPasswordBinding>() {
    private val viewmodel: AuthCommonVM by viewModels()
    private var token: String? = null
    private var email: String? = null
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObserver()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_password
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
                R.id.ivEye -> {
                    showOrHidePassword(binding.edtPassword, binding.ivEye)
                }

                R.id.ivEyeConfirm -> {
                    showOrHidePassword(binding.edtConfirmPassword, binding.ivEyeConfirm)
                }

                R.id.consLoginButton -> {
                    if (validate()) {
                        changePasswordApi()
                    }
                }
            }
        })
    }

    private fun initView() {
        token = arguments?.getString("Token")
        email = arguments?.getString("Email")
    }

    private fun initObserver() {
        viewmodel.observeCommon.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.SUCCESS -> {
                    hideLoading()
                    try {
                        when (it.message) {
                            "NEW_PASSWORD" -> {
                                try {
                                    val myDataModel =
                                        BindingUtils.parseJson<SimpleModel>(it.data.toString())
                                    if (myDataModel != null) {
                                        if (myDataModel.status == 200) {
                                            showToast(myDataModel.message.toString())
                                            findNavController().navigate(R.id.fragmentPasswordSuccess)
                                        }
                                    }

                                } catch (e: Exception) {
                                    showToast(e.message.toString())
                                    e.printStackTrace()
                                }
                            }
                        }
                    } catch (e: Exception) {
                        showToast(e.message.toString())
                        e.printStackTrace()
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

                Status.LOADING -> showLoading()
                else -> {

                }
            }
        }
    }

    private fun changePasswordApi() {
        if (email != null && token != null) {
            val request = HashMap<String, Any>()
            request["email"] = email.toString()
            request["token"] = token.toString()
            request["type"] = 3
            request["password"] = binding.edtConfirmPassword.text.toString()
            request["confirm_password"] = binding.edtConfirmPassword.text.toString()
            viewmodel.verifyEmailAndResendForForget(Constants.FORGOT_PASSWORD, request, 3)
        }
    }

    private fun validate(): Boolean {
        if (binding.edtPassword.text?.trim().toString().isEmpty()) {
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
        } else if (binding.edtConfirmPassword.text?.trim().toString().isEmpty()) {
            showToast("Please enter valid confirm password")
            return false
        } else if (binding.edtConfirmPassword.text?.trim().toString().length < 8) {
            showToast("Please enter valid confirm password of length 8")
            return false
        } else if (!binding.edtConfirmPassword.text?.trim().toString().any { it.isDigit() }) {
            showToast("Confirm Password must contain atleast one digit")
            return false
        } else if (!binding.edtConfirmPassword.text?.trim().toString().any { it.isLowerCase() }) {
            showToast("Confirm Password must contain atleast one lowercase letter")
            return false
        } else if (!binding.edtConfirmPassword.text?.trim().toString().any { it.isUpperCase() }) {
            showToast("Confirm Password must contain atleast one uppercase letter")
            return false
        } else if (!binding.edtConfirmPassword.text?.trim().toString()
                .any { "!#$%&()*+,-.:;<=>?@[\\]^_|~".contains(it) }
        ) {
            showToast("Confirm Password must contain atleast one special character")
            return false
        } else if (binding.edtPassword.text?.trim()
                .toString() != binding.edtConfirmPassword.text?.trim().toString()
        ) {
            showToast("Passwords don't match")
            return false
        } else {
            return true
        }
    }

}