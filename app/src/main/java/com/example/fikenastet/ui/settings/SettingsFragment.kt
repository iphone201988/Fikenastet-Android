package com.example.fikenastet.ui.settings

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.example.fikenastet.BR
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.SimpleRecyclerViewAdapter
import com.example.fikenastet.databinding.FragmentSettingsBinding
import com.example.fikenastet.databinding.ItemSettingsBinding
import com.example.fikenastet.ui.dashboard.common_activity.CommonActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    private val viewModel: SettingsVM by viewModels()
    private lateinit var settingsAdapter: SimpleRecyclerViewAdapter<String, ItemSettingsBinding>
    override fun getLayoutResource(): Int {
        return R.layout.fragment_settings
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
        initAdapter()
    }

    private fun initObserver() {

    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivBack -> {
                    parentFragmentManager.popBackStack()
                }

                R.id.ivNotification -> {

                }

                R.id.tvLakePanel -> {
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "MyLakes")
                    startActivity(intent)
                }
            }
        }

    }

    private fun initAdapter() {
        val settingsList = listOf(
            "Change Password",
            "Two-factor authentication",
            "Notifications",
            "Payment & Orders",
            "Terms of Service",
            "Device permissions",
            "Log Out",
            "Delete Account"
        )
        settingsAdapter = SimpleRecyclerViewAdapter(R.layout.item_settings, BR.bean) { v, m, pos ->
            when (v?.id) {
                R.id.tvSettings -> {
                    when (m) {
                        "Change Password" -> {
                            val intent = Intent(requireActivity(), CommonActivity::class.java)
                            intent.putExtra("fromWhere", "ChangePassword")
                            startActivity(intent)
                        }

                        "Notifications" -> {
                            val intent = Intent(requireActivity(), CommonActivity::class.java)
                            intent.putExtra("fromWhere", "NotificationsEnabled")
                            startActivity(intent)
                        }

                        "Payment & Orders" -> {
                            val intent = Intent(requireActivity(), CommonActivity::class.java)
                            intent.putExtra("fromWhere", "Payment")
                            startActivity(intent)
                        }

                    }
                }
            }
        }
        settingsAdapter.list = settingsList
        binding.rvSettings.adapter = settingsAdapter

    }

}