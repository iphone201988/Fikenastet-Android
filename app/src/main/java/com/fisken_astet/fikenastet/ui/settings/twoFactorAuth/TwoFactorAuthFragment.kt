package com.fisken_astet.fikenastet.ui.settings.twoFactorAuth

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.utils.BindingUtils
import com.fisken_astet.fikenastet.base.utils.BindingUtils.setBase64Image
import com.fisken_astet.fikenastet.base.utils.Status
import com.fisken_astet.fikenastet.base.utils.showToast
import com.fisken_astet.fikenastet.data.api.Constants
import com.fisken_astet.fikenastet.data.model.ProfileCompleteModel
import com.fisken_astet.fikenastet.data.model.SimpleModel
import com.fisken_astet.fikenastet.data.model.TwoFAEnableModel
import com.fisken_astet.fikenastet.data.model.TwoFAVerifyViaLogin
import com.fisken_astet.fikenastet.databinding.FragmentTwoFactorAuthBinding
import com.fisken_astet.fikenastet.ui.dashboard.DashBoardActivity
import com.fisken_astet.fikenastet.ui.settings.SettingsVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TwoFactorAuthFragment : BaseFragment<FragmentTwoFactorAuthBinding>() {
    private val viewModel: SettingsVM by viewModels()
    private var from: String?=null
    override fun getLayoutResource(): Int {
        return R.layout.fragment_two_factor_auth
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView(view: View) {
        // view
        initView()
        // click
        initOnClick()
        // observer
        initObserver()
    }

    /** handle view **/
    private fun initView() {
        from =arguments?.getString("From")
        if (from!=null){
            val user = sharedPrefManager.getLoginData()
            if (user!=null){
                binding.tvQrCode.text =
                    user.google2fa_secret.toString()
                setBase64Image(
                    binding.ivQr,
                    user.qr_code.toString()
                )
            }
        }
        else{
            twoFactorEnable()
        }
    }

    /** handle click **/
    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivBack -> {
                    if (from=="Login"){
                        sharedPrefManager.clear()
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.auth_navigation, true)
                            .build()

                        findNavController().navigate(R.id.fragmentSplash, null, navOptions)
                    }
                    else {
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                }

                R.id.consButton -> {
                    BindingUtils.preventMultipleClick(it)
                    if (validate()){
                       if (from=="Login"){
                           twoFactorVerifyLogin()
                       }
                        else{
                           twoFactorVerify()
                        }
                    }
                }
            }
        }

    }

    /** handle observer **/
    private fun initObserver() {
        viewModel.settingsObserver.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> showLoading()
                Status.SUCCESS -> {
                    hideLoading()
                    when (it.message) {
                        "TWO_FA_ENABLE" -> {
                            try {
                                val myDataModel =
                                    BindingUtils.parseJson<TwoFAEnableModel>(it.data.toString())
                                if (myDataModel != null) {
                                    if (myDataModel.status == 200) {
                                        if (myDataModel.data != null) {
                                            binding.tvQrCode.text =
                                                myDataModel.data.secret.toString()
                                            setBase64Image(
                                                binding.ivQr,
                                                myDataModel.data.qr_code.toString()
                                            )
                                        }
                                    }
                                }

                            } catch (e: Exception) {
                                showToast(e.message.toString())
                                e.printStackTrace()
                            }
                        }
                        "TWO_FA_VERIFY"->{
                            try {
                                val myDataModel = BindingUtils.parseJson<SimpleModel>(it.data.toString())
                                if (myDataModel!=null){
                                    if (myDataModel.status==200){
                                        showToast(myDataModel.message.toString())
                                        requireActivity().onBackPressedDispatcher.onBackPressed()
                                    }
                                }
                            }
                            catch (e:Exception){
                                showToast(e.message.toString())
                                e.printStackTrace()
                            }
                        }
                        "TWO_FA_VERIFY_VIA_LOGIN"->{
                            try {
                                val myDataModel = BindingUtils.parseJson<TwoFAVerifyViaLogin>(it.data.toString())
                                if (myDataModel!=null){
                                    if (myDataModel.status==200){
                                        showToast(myDataModel.message.toString())
                                        val intent = Intent(
                                            requireContext(), DashBoardActivity::class.java
                                        )
                                        startActivity(intent)
                                        requireActivity().finish()
                                    }
                                }
                            }
                            catch (e:Exception){
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

    private fun validate(): Boolean{
        if (binding.etCode.text.toString().trim().isNullOrEmpty()){
            showToast("Please enter valid code")
            return false
        }
        else{
            return true
        }
    }
    /** apis **/
    private fun twoFactorEnable() {
        val request = HashMap<String, Any>()
        viewModel.twoFactorAuthApi(Constants.TWO_FA_ENABLE, request, 0)
    }

    private fun twoFactorVerify() {
        val request = HashMap<String, Any>()
        request["otp"] = binding.etCode.text.toString().trim()
        viewModel.twoFactorAuthApi(Constants.TWO_FA_VERIFY, request, 1)
    }

    private fun twoFactorVerifyLogin() {
        val user =sharedPrefManager.getLoginData()
        val request = HashMap<String, Any>()
        request["otp"] = binding.etCode.text.toString().trim()
        request["user_id"] = user?.id.toString()
        viewModel.twoFactorAuthApi(Constants.TWO_FA_VERIFY_LOGIN, request, 2)
    }
}