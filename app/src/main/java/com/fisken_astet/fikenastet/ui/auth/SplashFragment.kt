package com.fisken_astet.fikenastet.ui.auth

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.location.LocationHandler
import com.fisken_astet.fikenastet.base.location.LocationResultListener
import com.fisken_astet.fikenastet.base.permission.PermissionHandler
import com.fisken_astet.fikenastet.base.permission.Permissions
import com.fisken_astet.fikenastet.base.utils.BindingUtils
import com.fisken_astet.fikenastet.base.utils.Status
import com.fisken_astet.fikenastet.base.utils.showToast
import com.fisken_astet.fikenastet.data.api.Constants
import com.fisken_astet.fikenastet.data.model.OtpSentModel
import com.fisken_astet.fikenastet.databinding.FragmentSplashBinding
import com.fisken_astet.fikenastet.ui.dashboard.DashBoardActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(), LocationResultListener {
    private val viewModel: AuthCommonVM by viewModels()
    private var locationHandler: LocationHandler? = null
    private var mCurrentLocation: Location? = null
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObserver()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_splash
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }


    private fun initView() {
        checkLocation()
        val user=sharedPrefManager.getLoginData()
        val isLoggedIn = sharedPrefManager.getIsLoggedIn()
        if (user!=null){
            binding.consButton.visibility=View.GONE
            if (isLoggedIn==true){
                startActivity(Intent(requireContext(), DashBoardActivity::class.java))
                requireActivity().finish()
            }
            else {
                if (user.is_verified == 1) {
                    if (user.is_2fa_enabled == 1) {
                        val bundle = Bundle()
                        bundle.putString("From", "Login")
                        findNavController().navigate(
                            R.id.fragmentTwoAuth, bundle
                        )
                    } else {
                        startActivity(Intent(requireContext(), DashBoardActivity::class.java))
                        requireActivity().finish()
                    }
                } else {
                    sendOtp(user.email)
                }
            }
        }
        else{
            binding.consButton.visibility=View.VISIBLE
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner, Observer {
            when (it?.id) {
                R.id.consButton -> {
                        findNavController().navigate(R.id.fragmentLogin)
                }
            }
        })

    }

    private fun initObserver(){
        viewModel.observeCommon.observe(viewLifecycleOwner){
            when(it?.status){
                Status.LOADING -> showLoading()
                Status.SUCCESS -> {
                    hideLoading()
                    when(it.message){
                        "RESEND_OTP" -> {
                            try {
                                hideLoading()
                                val myDataModel =
                                    BindingUtils.parseJson<OtpSentModel>(it.data.toString())
                                if (myDataModel?.success == 200) {
                                    showToast(myDataModel.message.toString())
                                    val user =sharedPrefManager.getLoginData()
                                    val bundle = Bundle()
                                    bundle.putString("Form", "Register")
                                    bundle.putString(
                                        "Email", user?.email
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
                    }
                    catch (e:Exception){
                        showToast(e.message.toString())
                        e.printStackTrace()
                    }
                }
                else -> {

                }
            }
        }
    }


    private fun checkLocation() {
        val rationale = "This app needs access to your storage and camera to function properly."
        val options = Permissions.Options()
        Permissions.check(
            requireContext(), BindingUtils.permissions, rationale, options, object : PermissionHandler() {
                override fun onGranted() {
                    createLocationHandler()
                }

            })
    }

    private fun createLocationHandler() {
        locationHandler = LocationHandler(requireActivity(), this)
        locationHandler?.getUserLocation()
        locationHandler?.removeLocationUpdates()
    }

    override fun getLocation(location: Location) {
        this.mCurrentLocation = location
        Constants.latitude = location.latitude.toString()
        Constants.longitude = location.longitude.toString()
        //openNewActivity()
    }

    private fun sendOtp(email:String?) {
        if (email!=null) {
            val request = HashMap<String, Any>()
            request["email"] = email.toString()
            request["type"] = 2
            viewModel.verifyEmailAndResend(Constants.VERIFY_OTP, request, 2)
        }
    }
}