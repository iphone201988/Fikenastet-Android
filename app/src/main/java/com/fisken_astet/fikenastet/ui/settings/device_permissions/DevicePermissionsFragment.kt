package com.fisken_astet.fikenastet.ui.settings.device_permissions

import android.view.View
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.databinding.FragmentDevicePermissionsBinding
import com.fisken_astet.fikenastet.ui.settings.SettingsVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DevicePermissionsFragment : BaseFragment<FragmentDevicePermissionsBinding>() {
    private val viewModel: SettingsVM by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_device_permissions
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

    private fun initView() {

    }

    private fun initOnClick() {

    }

    private fun initObserver() {

    }

}