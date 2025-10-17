package com.fisken_astet.fikenastet.ui.settings.device_permissions

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.BR
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.SimpleRecyclerViewAdapter
import com.fisken_astet.fikenastet.data.model.PermissionsModel
import com.fisken_astet.fikenastet.databinding.FragmentDevicePermissionsBinding
import com.fisken_astet.fikenastet.databinding.HolderPermissionsBinding
import com.fisken_astet.fikenastet.ui.dashboard.common_activity.CommonActivity
import com.fisken_astet.fikenastet.ui.settings.SettingsVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DevicePermissionsFragment : BaseFragment<FragmentDevicePermissionsBinding>() {
    private val viewModel: SettingsVM by viewModels()
    private lateinit var permissionsAdapter: SimpleRecyclerViewAdapter<PermissionsModel, HolderPermissionsBinding>
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
        // adapter
        initAdapter()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivBack -> {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }

                R.id.ivNotification -> {
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "Notifications")
                    startActivity(intent)
                }
            }
        }

    }

    private fun initObserver() {

    }

    private fun initAdapter() {
        permissionsAdapter =
            SimpleRecyclerViewAdapter(R.layout.holder_permissions, BR.bean) { v, m, pos ->
                when (v.id) {

                }
            }
        permissionsAdapter.list = getPermissionsList()
        binding.rvPermissions.adapter = permissionsAdapter
    }

    private fun getPermissionsList(): ArrayList<PermissionsModel> {
        val list = ArrayList<PermissionsModel>()
        list.add(PermissionsModel("Camera", false))
        list.add(PermissionsModel("Location Services", false))
        list.add(PermissionsModel("Notifications", false))
        list.add(PermissionsModel("Photos", false))
        return list
    }

}