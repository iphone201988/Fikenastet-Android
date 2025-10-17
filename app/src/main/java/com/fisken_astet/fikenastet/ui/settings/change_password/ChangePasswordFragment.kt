package com.fisken_astet.fikenastet.ui.settings.change_password

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.utils.BaseCustomDialog
import com.fisken_astet.fikenastet.base.utils.BindingUtils
import com.fisken_astet.fikenastet.base.utils.BindingUtils.showOrHidePassword
import com.fisken_astet.fikenastet.base.utils.Status
import com.fisken_astet.fikenastet.base.utils.showToast
import com.fisken_astet.fikenastet.data.api.Constants
import com.fisken_astet.fikenastet.data.model.ProfileCompleteModel
import com.fisken_astet.fikenastet.databinding.DialogChangePasswordBinding
import com.fisken_astet.fikenastet.databinding.FragmentChangePasswordBinding
import com.fisken_astet.fikenastet.ui.dashboard.common_activity.CommonActivity
import com.fisken_astet.fikenastet.ui.settings.SettingsVM
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>() {
    private val viewModel: SettingsVM by viewModels()
    private lateinit var successDialog: BaseCustomDialog<DialogChangePasswordBinding>
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
        // dialog
        initDialog()
    }

    private fun initObserver() {
        viewModel.settingsObserver.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> showLoading()
                Status.SUCCESS -> {
                    hideLoading()
                    when (it.message) {
                        "UPDATE_PROFILE" -> {
                            try {
                                val myDataModel= BindingUtils.parseJson<ProfileCompleteModel>(it.data.toString())
                                if (myDataModel!=null) {
                                    if (myDataModel.success == 200) {
                                            successDialog.show()
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

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivBack -> {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                R.id.ivNotification->{
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "Notifications")
                    startActivity(intent)
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
                    BindingUtils.preventMultipleClick(it)
                    if (validate()){
                        apiCall()
                    }
                }
            }
        }
    }

    // dialog
    private fun initDialog() {
        successDialog = BaseCustomDialog(requireContext(), R.layout.dialog_change_password) {
            when (it.id) {
                R.id.consButton -> {
                    successDialog.dismiss()
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

    // validations
    private fun validate():Boolean{
        if (binding.etOldPassword.text?.trim().toString().isEmpty()) {
            showToast("Please enter valid old password")
            return false
        } else if (binding.etOldPassword.text?.trim().toString().length < 8) {
            showToast("Please enter valid old password of length 8")
            return false
        } else if (!binding.etOldPassword.text?.trim().toString().any { it.isDigit() }) {
            showToast("Old Password must contain atleast one digit")
            return false
        } else if (!binding.etOldPassword.text?.trim().toString().any { it.isLowerCase() }) {
            showToast("Old Password must contain atleast one lowercase letter")
            return false
        } else if (!binding.etOldPassword.text?.trim().toString().any { it.isUpperCase() }) {
            showToast("Old Password must contain atleast one uppercase letter")
            return false
        } else if (!binding.etOldPassword.text?.trim().toString()
                .any { "!#$%&()*+,-.:;<=>?@[\\]^_|~".contains(it) }
        ) {
            showToast("Old Password must contain atleast one special character")
            return false
        } else if (binding.etNewPassword.text?.trim().toString().isEmpty()) {
            showToast("Please enter valid new password")
            return false
        } else if (binding.etNewPassword.text?.trim().toString().length < 8) {
            showToast("Please enter valid new password of length 8")
            return false
        } else if (!binding.etNewPassword.text?.trim().toString().any { it.isDigit() }) {
            showToast("New Password must contain atleast one digit")
            return false
        } else if (!binding.etNewPassword.text?.trim().toString().any { it.isLowerCase() }) {
            showToast("New Password must contain atleast one lowercase letter")
            return false
        } else if (!binding.etNewPassword.text?.trim().toString().any { it.isUpperCase() }) {
            showToast("New Password must contain atleast one uppercase letter")
            return false
        } else if (!binding.etNewPassword.text?.trim().toString()
                .any { "!#$%&()*+,-.:;<=>?@[\\]^_|~".contains(it) }
        ) {
            showToast("New Password must contain atleast one special character")
            return false
        } else if (binding.etConfirmPassword.text?.trim().toString().isEmpty()) {
            showToast("Please enter valid confirm password")
            return false
        } else if (binding.etConfirmPassword.text?.trim().toString().length < 8) {
            showToast("Please enter valid confirm password of length 8")
            return false
        } else if (!binding.etConfirmPassword.text?.trim().toString().any { it.isDigit() }) {
            showToast("Confirm Password must contain atleast one digit")
            return false
        } else if (!binding.etConfirmPassword.text?.trim().toString().any { it.isLowerCase() }) {
            showToast("Confirm Password must contain atleast one lowercase letter")
            return false
        } else if (!binding.etConfirmPassword.text?.trim().toString().any { it.isUpperCase() }) {
            showToast("Confirm Password must contain atleast one uppercase letter")
            return false
        } else if (!binding.etConfirmPassword.text?.trim().toString()
                .any { "!#$%&()*+,-.:;<=>?@[\\]^_|~".contains(it) }
        ) {
            showToast("Confirm Password must contain atleast one special character")
            return false
        }
        else if (binding.etOldPassword.text?.trim()
                .toString() == binding.etNewPassword.text?.trim().toString() || binding.etOldPassword.text?.trim()
                .toString() == binding.etConfirmPassword.text?.trim().toString()
        ) {
            showToast("New password must be different from old password")
            return false
        }
        else if (binding.etNewPassword.text?.trim()
                .toString() != binding.etConfirmPassword.text?.trim().toString()
        ) {
            showToast("New Password and confirm password don't match")
            return false
        }
        else{
            return true
        }
    }

    /** api call **/
    private fun apiCall(){
        val request = HashMap<String, RequestBody>()
        request["type"]="3".toRequestBody()
        request["old_password"]=binding.etOldPassword.text.toString().trim().toRequestBody()
        request["new_password"]=binding.etConfirmPassword.text.toString().trim().toRequestBody()
        request["confirm_password"]=binding.etConfirmPassword.text.toString().trim().toRequestBody()
        viewModel.updateProfileApi(Constants.COMPLETE_PROFILE,request,null)
    }

}